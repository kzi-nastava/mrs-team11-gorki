package ftn.mrs_team11_gorki.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.dto.ChatDTO;
import ftn.mrs_team11_gorki.dto.MessageDTO;

public class SupportInboxAdapter extends RecyclerView.Adapter<SupportInboxAdapter.VH> {

    public interface OnChatClick { void onClick(ChatDTO chat); }

    private final List<ChatDTO> items = new ArrayList<>();
    private final OnChatClick onChatClick;
    private Long selectedChatId = null;

    public SupportInboxAdapter(OnChatClick onChatClick) {
        this.onChatClick = onChatClick;
    }

    public void submit(List<ChatDTO> chats) {
        items.clear();
        if (chats != null) items.addAll(chats);
        notifyDataSetChanged();
    }

    public void setSelectedChatId(Long chatId) {
        selectedChatId = chatId;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_support_inbox_chat, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        ChatDTO chat = items.get(position);

        h.tvChatName.setText("Chat #" + chat.getId());
        int msgCount = (chat.getMessages() == null) ? 0 : chat.getMessages().size();
        h.tvMeta.setText("User ID: " + chat.getUserId() + "   Msgs: " + msgCount);

        String preview = "No messages yet";
        if (chat.getMessages() != null && !chat.getMessages().isEmpty()) {
            MessageDTO last = chat.getMessages().get(chat.getMessages().size() - 1);
            preview = last.getContent();
        }
        h.tvPreview.setText(preview);

        boolean selected = selectedChatId != null && selectedChatId.equals(chat.getId());
        h.itemView.setAlpha(selected ? 1f : 0.92f);

        h.itemView.setOnClickListener(v -> onChatClick.onClick(chat));
    }

    @Override public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvChatName, tvMeta, tvPreview;
        VH(View v) {
            super(v);
            tvChatName = v.findViewById(R.id.tvChatName);
            tvMeta = v.findViewById(R.id.tvMeta);
            tvPreview = v.findViewById(R.id.tvPreview);
        }
    }
}
