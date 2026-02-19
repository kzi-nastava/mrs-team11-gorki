package ftn.mrs_team11_gorki.fragments;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.adapter.NotificationAdapter;
import ftn.mrs_team11_gorki.auth.ApiClient;
import ftn.mrs_team11_gorki.auth.TokenStorage;
import ftn.mrs_team11_gorki.dto.NotificationResponseDTO;
import ftn.mrs_team11_gorki.service.NotificationService;
import okhttp3.ResponseBody;
import retrofit2.*;

public class NotificationInboxDialogFragment extends DialogFragment {

    private RecyclerView rv;
    private NotificationAdapter adapter;

    private TextView tvDetailType, tvDetailDate, tvDetailContent;
    private MaterialButton btnMarkRead, btnClose;

    private NotificationService api;
    private long userId;

    private NotificationResponseDTO selected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_notification_inbox, container, false);

        rv = v.findViewById(R.id.rvNotifications);

        tvDetailType = v.findViewById(R.id.tvDetailType);
        tvDetailDate = v.findViewById(R.id.tvDetailDate);
        tvDetailContent = v.findViewById(R.id.tvDetailContent);

        btnMarkRead = v.findViewById(R.id.btnMarkRead);
        btnClose = v.findViewById(R.id.btnClose);

        userId = new TokenStorage(requireContext()).getUserId();
        api = ApiClient.getRetrofit(requireContext()).create(NotificationService.class);

        adapter = new NotificationAdapter(this::onSelect);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        btnClose.setOnClickListener(x -> dismiss());

        btnMarkRead.setOnClickListener(x -> {
            if (selected == null || selected.getId() == null) return;
            markAsRead(selected);
        });

        loadNotifications();

        return v;
    }

    private void loadNotifications() {
        api.getAllForUser(userId).enqueue(new Callback<List<NotificationResponseDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<NotificationResponseDTO>> call,
                                   @NonNull Response<List<NotificationResponseDTO>> res) {
                if (!isAdded()) return;

                if (!res.isSuccessful() || res.body() == null) {
                    toast("Failed to load: " + res.code());
                    return;
                }

                adapter.submit(res.body());

                // auto-select first if exists
                if (!res.body().isEmpty()) {
                    onSelect(res.body().get(0));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<NotificationResponseDTO>> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                toast("Network: " + t.getMessage());
            }
        });
    }

    private void onSelect(NotificationResponseDTO n) {
        selected = n;

        tvDetailType.setText(safe(n.getPurpose(), "Notification"));
        tvDetailDate.setText(shortDate(safe(n.getCreatedAt(), "-")));
        tvDetailContent.setText(safe(n.getContent(), "-"));

        boolean unread = (n.getRead() == null) || !n.getRead();
        btnMarkRead.setEnabled(unread);
    }

    private void markAsRead(NotificationResponseDTO n) {
        api.markAsRead(n.getId(), userId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> res) {
                if (!isAdded()) return;

                if (res.isSuccessful()) {
                    n.setRead(true);
                    adapter.updateItem(n);
                    btnMarkRead.setEnabled(false);
                } else {
                    toast("Failed: " + res.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                toast("Network: " + t.getMessage());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // Popup sizing
        if (getDialog() != null && getDialog().getWindow() != null) {
            int w = (int) (getResources().getDisplayMetrics().widthPixels * 0.94);
            int h = (int) (getResources().getDisplayMetrics().heightPixels * 0.85);

            getDialog().getWindow().setLayout(w, h);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    private void toast(String m) {
        Toast.makeText(requireContext(), m, Toast.LENGTH_SHORT).show();
    }

    private static String safe(String s, String def) {
        if (s == null) return def;
        s = s.trim();
        return s.isEmpty() ? def : s;
    }

    private static String shortDate(String iso) {
        if (iso == null) return "-";
        String x = iso.replace("T", " ");
        if (x.length() >= 16) return x.substring(0, 16);
        return x;
    }
}