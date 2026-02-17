package ftn.mrs_team11_gorki.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.adapter.ChatAdapter;
import ftn.mrs_team11_gorki.auth.ApiClient;
import ftn.mrs_team11_gorki.auth.TokenStorage;
import ftn.mrs_team11_gorki.dto.ChatDTO;
import ftn.mrs_team11_gorki.dto.MessageDTO;
import ftn.mrs_team11_gorki.dto.SendMessageRequest;
import ftn.mrs_team11_gorki.service.SupportChatService;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;
import ua.naiksoftware.stomp.dto.StompHeader;

public class SupportChatDialogFragment extends DialogFragment {

    private static final String SUBSCRIBE_DEST = "/user/queue/support";
    private static final String SEND_DEST = "/app/support.send";
    private static final String WS_URL = "ws://10.0.2.2:8080/ws/websocket";

    private StompClient stompClient;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final Gson gson = new Gson();

    private ChatAdapter adapter;
    private RecyclerView rv;
    private EditText et;
    private ImageButton btnSend;

    private SupportChatService supportChatService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_support_chat_dialog, container, false);

        rv = v.findViewById(R.id.rvMessages);
        et = v.findViewById(R.id.etMessage);
        btnSend = v.findViewById(R.id.btnSend);

        adapter = new ChatAdapter();
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        supportChatService = ApiClient
                .getRetrofit(requireContext())
                .create(SupportChatService.class);

        loadHistory();

        btnSend.setOnClickListener(view -> sendMessage());

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null && getDialog().getWindow() != null) {
            int w = ViewGroup.LayoutParams.MATCH_PARENT;
            int h = (int) (requireContext().getResources().getDisplayMetrics().heightPixels * 0.6);
            getDialog().getWindow().setLayout(w, h);
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        connectStomp();
    }

    @Override
    public void onStop() {
        super.onStop();
        disconnectStomp();
    }

    // -------------------------
    // REST: istorija
    // -------------------------

    private void loadHistory() {
        if (supportChatService == null) return;

        supportChatService.myChat().enqueue(new Callback<ChatDTO>() {
            @Override
            public void onResponse(Call<ChatDTO> call, Response<ChatDTO> response) {
                if (!isAdded()) return;

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(requireContext(), "History load failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    adapter.submit(Collections.emptyList());
                    return;
                }

                ChatDTO chat = response.body();
                List<MessageDTO> msgs = chat.getMessages();

                if (msgs == null || msgs.isEmpty()) {
                    adapter.submit(Collections.emptyList());
                    return;
                }

                adapter.submit(msgs);
                rv.scrollToPosition(Math.max(0, adapter.getItemCount() - 1));
            }

            @Override
            public void onFailure(Call<ChatDTO> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "History error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                adapter.submit(Collections.emptyList());
            }
        });
    }

    // -------------------------
    // WS: connect + subscribe
    // -------------------------

    private void connectStomp() {
        TokenStorage ts = new TokenStorage(requireContext());
        String token = ts.getToken();

        if (token == null || token.isEmpty()) {
            Toast.makeText(requireContext(), "You must be logged in to use support chat.", Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WS_URL);
        StompHeader authHeader = new StompHeader("Authorization", "Bearer " + token);

        // lifecycle
        Disposable life = stompClient.lifecycle().subscribe(event -> {
            if (!isAdded()) return;

            if (event.getType() == LifecycleEvent.Type.OPENED) {
                requireActivity().runOnUiThread(this::subscribeSupport);

            } else if (event.getType() == LifecycleEvent.Type.ERROR) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(),
                                "WS error: " + (event.getException() != null ? event.getException().getMessage() : "unknown"),
                                Toast.LENGTH_LONG).show()
                );

            } else if (event.getType() == LifecycleEvent.Type.CLOSED) {
                // opcionalno: notify
            }
        });
        disposables.add(life);

        stompClient.connect(Collections.singletonList(authHeader));
    }

    private void subscribeSupport() {
        if (stompClient == null) return;

        Disposable sub = stompClient.topic(SUBSCRIBE_DEST).subscribe(topicMessage -> {
            if (!isAdded()) return;

            try {
                MessageDTO msg = gson.fromJson(topicMessage.getPayload(), MessageDTO.class);

                requireActivity().runOnUiThread(() -> {
                    adapter.add(msg);
                    rv.scrollToPosition(adapter.getItemCount() - 1);
                });

            } catch (Exception e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Parse error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }

        }, throwable -> {
            if (!isAdded()) return;
            requireActivity().runOnUiThread(() ->
                    Toast.makeText(requireContext(), "Subscribe failed: " + throwable.getMessage(), Toast.LENGTH_LONG).show()
            );
        });

        disposables.add(sub);
    }

    private void disconnectStomp() {
        disposables.clear();
        if (stompClient != null) {
            stompClient.disconnect();
            stompClient = null;
        }
    }

    // -------------------------
    // Send: WS, fallback REST
    // -------------------------

    private void sendMessage() {
        String text = et.getText().toString().trim();
        if (text.isEmpty()) return;

        // 1) WS send
        if (stompClient != null && stompClient.isConnected()) {
            SendMessageRequest req = new SendMessageRequest(text);
            String json = gson.toJson(req);

            disposables.add(
                    stompClient.send(SEND_DEST, json).subscribe(
                            () -> {
                                if (!isAdded()) return;
                                requireActivity().runOnUiThread(() -> et.setText(""));
                            },
                            err -> {
                                if (!isAdded()) return;
                                requireActivity().runOnUiThread(() ->
                                        Toast.makeText(requireContext(), "WS send failed: " + err.getMessage(), Toast.LENGTH_LONG).show()
                                );
                            }
                    )
            );
            return;
        }

        // 2) REST fallback
        if (supportChatService != null) {
            supportChatService.sendFallback(new SendMessageRequest(text)).enqueue(new Callback<MessageDTO>() {
                @Override
                public void onResponse(Call<MessageDTO> call, Response<MessageDTO> response) {
                    if (!isAdded()) return;

                    if (response.isSuccessful() && response.body() != null) {
                        adapter.add(response.body());
                        rv.scrollToPosition(adapter.getItemCount() - 1);
                        et.setText("");
                    } else {
                        Toast.makeText(requireContext(), "Send failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MessageDTO> call, Throwable t) {
                    if (!isAdded()) return;
                    Toast.makeText(requireContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

