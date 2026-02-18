package ftn.mrs_team11_gorki.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.adapter.AdminMessageAdapter;
import ftn.mrs_team11_gorki.adapter.SupportInboxAdapter;
import ftn.mrs_team11_gorki.auth.ApiClient;
import ftn.mrs_team11_gorki.auth.TokenStorage;
import ftn.mrs_team11_gorki.dto.AdminSendMessageRequest;
import ftn.mrs_team11_gorki.dto.ChatDTO;
import ftn.mrs_team11_gorki.dto.MessageDTO;
import ftn.mrs_team11_gorki.dto.SupportEventDTO;
import ftn.mrs_team11_gorki.service.AdminSupportService;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;
import ua.naiksoftware.stomp.dto.StompHeader;

import java.util.Collections;

public class AdminSupportFragment extends Fragment {

    // WS: admin listens here (backend must broadcast)
    private static final String SUBSCRIBE_DEST = "/topic/support";
    private static final String SEND_DEST = "/app/support.adminSend";
    private static final String WS_URL = "ws://10.0.2.2:8080/ws/websocket"; // ili /ws-native ako dodaš

    private AdminSupportService adminSupportService;

    private SupportInboxAdapter inboxAdapter;
    private AdminMessageAdapter messageAdapter;

    private RecyclerView rvChats, rvMessages;
    private TextView tvWsStatus, tvChatTitle, tvChatSubtitle;
    private Button btnRefresh;
    private EditText etMessage;
    private ImageButton btnSend;

    private Long selectedChatId = null;

    private StompClient stompClient;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final Gson gson = new Gson();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_admin_support, container, false);

        adminSupportService = ApiClient.getRetrofit(requireContext()).create(AdminSupportService.class);

        tvWsStatus = v.findViewById(R.id.tvWsStatus);
        tvChatTitle = v.findViewById(R.id.tvChatTitle);
        tvChatSubtitle = v.findViewById(R.id.tvChatSubtitle);

        btnRefresh = v.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(view -> loadChats());

        rvChats = v.findViewById(R.id.rvChats);
        rvChats.setLayoutManager(new LinearLayoutManager(requireContext()));

        inboxAdapter = new SupportInboxAdapter(chat -> openChat(chat));
        rvChats.setAdapter(inboxAdapter);

        rvMessages = v.findViewById(R.id.rvMessages);
        rvMessages.setLayoutManager(new LinearLayoutManager(requireContext()));

        messageAdapter = new AdminMessageAdapter();
        rvMessages.setAdapter(messageAdapter);

        etMessage = v.findViewById(R.id.etMessage);
        btnSend = v.findViewById(R.id.btnSend);

        btnSend.setOnClickListener(view -> sendAdminMessage());

        loadChats();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        connectStomp();
    }

    @Override
    public void onStop() {
        super.onStop();
        disconnectStomp();
    }

    private void loadChats() {
        adminSupportService.listChats().enqueue(new Callback<List<ChatDTO>>() {
            @Override
            public void onResponse(Call<List<ChatDTO>> call, Response<List<ChatDTO>> response) {
                if (!isAdded()) return;

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(requireContext(), "Load chats failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                inboxAdapter.submit(response.body());
                inboxAdapter.setSelectedChatId(selectedChatId);
            }

            @Override
            public void onFailure(Call<List<ChatDTO>> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openChat(ChatDTO chat) {
        selectedChatId = chat.getId();
        inboxAdapter.setSelectedChatId(selectedChatId);

        tvChatTitle.setText("Chat #" + chat.getId());
        tvChatSubtitle.setText("User ID: " + chat.getUserId());

        messageAdapter.submit(chat.getMessages());
        if (messageAdapter.getItemCount() > 0) {
            rvMessages.scrollToPosition(messageAdapter.getItemCount() - 1);
        }
    }

    private void connectStomp() {
        TokenStorage ts = new TokenStorage(requireContext());
        String token = ts.getToken();
        if (token == null || token.isEmpty()) {
            tvWsStatus.setText("● Disconnected");
            return;
        }

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WS_URL);
        StompHeader authHeader = new StompHeader("Authorization", "Bearer " + token);

        Disposable life = stompClient.lifecycle().subscribe(ev -> {
            if (!isAdded()) return;

            if (ev.getType() == LifecycleEvent.Type.OPENED) {
                requireActivity().runOnUiThread(() -> tvWsStatus.setText("● Connected"));
                requireActivity().runOnUiThread(this::subscribeAdminFeed);
            } else if (ev.getType() == LifecycleEvent.Type.ERROR) {
                requireActivity().runOnUiThread(() -> tvWsStatus.setText("● Error"));
            } else if (ev.getType() == LifecycleEvent.Type.CLOSED) {
                requireActivity().runOnUiThread(() -> tvWsStatus.setText("● Disconnected"));
            }
        });

        disposables.add(life);
        stompClient.connect(Collections.singletonList(authHeader));
    }

    private void subscribeAdminFeed() {
        Disposable sub = stompClient.topic(SUBSCRIBE_DEST).subscribe(msg -> {
            SupportEventDTO ev = gson.fromJson(msg.getPayload(), SupportEventDTO.class);
            if (ev == null || ev.getChatId() == null || ev.getMessage() == null) return;


            if (selectedChatId != null && ev.getChatId().equals(selectedChatId)) {
                requireActivity().runOnUiThread(() -> {
                    messageAdapter.add(ev.getMessage());
                    rvMessages.scrollToPosition(messageAdapter.getItemCount() - 1);
                });
            }

        }, err -> {
            if (!isAdded()) return;
            requireActivity().runOnUiThread(() ->
                    Toast.makeText(requireContext(), "Subscribe failed: " + err.getMessage(), Toast.LENGTH_LONG).show()
            );
        });

        disposables.add(sub);
    }

    private void sendAdminMessage() {
        if (selectedChatId == null) {
            Toast.makeText(requireContext(), "Select a chat first.", Toast.LENGTH_SHORT).show();
            return;
        }

        String text = etMessage.getText().toString().trim();
        if (text.isEmpty()) return;

        if (stompClient == null || !stompClient.isConnected()) {
            Toast.makeText(requireContext(), "WS not connected.", Toast.LENGTH_SHORT).show();
            return;
        }

        AdminSendMessageRequest req = new AdminSendMessageRequest(selectedChatId, text);
        String json = gson.toJson(req);

        disposables.add(
                stompClient.send(SEND_DEST, json).subscribe(
                        () -> requireActivity().runOnUiThread(() -> {
                            etMessage.setText("");

                            TokenStorage ts = new TokenStorage(requireContext());
                            String email = ts.getEmailFromToken();
                            String sender = (email == null || email.isEmpty()) ? "ADMIN" : email;

                            MessageDTO local = new MessageDTO();
                            local.setSender(sender);
                            local.setContent(text);
                            local.setTimeStamp("");

                            messageAdapter.add(local);
                            rvMessages.scrollToPosition(messageAdapter.getItemCount() - 1);
                        }),
                        err -> requireActivity().runOnUiThread(() ->
                                Toast.makeText(requireContext(), "Send failed: " + err.getMessage(), Toast.LENGTH_LONG).show()
                        )
                )
        );
    }

    private void disconnectStomp() {
        disposables.clear();
        if (stompClient != null) {
            stompClient.disconnect();
            stompClient = null;
        }
    }
}
