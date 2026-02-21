package ftn.mrs_team11_gorki.adapter;

import android.annotation.SuppressLint;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.dto.NotificationResponseDTO;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Holder> {

    public interface OnClick {
        void onClick(NotificationResponseDTO n);
    }

    private final List<NotificationResponseDTO> items = new ArrayList<>();
    private final OnClick onClick;

    public NotificationAdapter(OnClick onClick) {
        this.onClick = onClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void submit(List<NotificationResponseDTO> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    public void updateItem(NotificationResponseDTO updated) {
        if (updated == null || updated.getId() == null) return;
        for (int i = 0; i < items.size(); i++) {
            NotificationResponseDTO x = items.get(i);
            if (x.getId() != null && x.getId().equals(updated.getId())) {
                items.set(i, updated);
                notifyItemChanged(i);
                return;
            }
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {
        NotificationResponseDTO n = items.get(pos);

        h.tvType.setText(safe(n.getPurpose()));
        h.tvDate.setText(shortDate(safe(n.getCreatedAt())));
        h.tvPreview.setText(preview(safe(n.getContent())));

        boolean unread = (n.getRead() == null) || !n.getRead();
        h.tvUnread.setVisibility(unread ? View.VISIBLE : View.GONE);

        h.card.setOnClickListener(v -> {
            if (onClick != null) onClick.onClick(n);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        MaterialCardView card;
        TextView tvType, tvDate, tvPreview, tvUnread;

        Holder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            tvType = itemView.findViewById(R.id.tvType);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvPreview = itemView.findViewById(R.id.tvPreview);
            tvUnread = itemView.findViewById(R.id.tvUnread);
        }
    }

    private static String safe(String s) { return s == null ? "" : s; }

    private static String preview(String s) {
        if (s == null) return "";
        s = s.trim();
        return s.length() > 70 ? s.substring(0, 70) + "..." : s;
    }

    private static String shortDate(String iso) {
        if (iso == null) return "-";
        String x = iso.replace("T", " ");
        if (x.length() >= 16) return x.substring(0, 16);
        return x;
    }
}