package ftn.mrs_team11_gorki.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.dto.MessageDTO;

public class AdminMessageAdapter extends RecyclerView.Adapter<AdminMessageAdapter.VH> {

    private final List<MessageDTO> items = new ArrayList<>();

    public void submit(List<MessageDTO> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    public void add(MessageDTO msg) {
        items.add(msg);
        notifyItemInserted(items.size() - 1);
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_message, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        MessageDTO m = items.get(position);

        h.tvContent.setText(m.getContent());
        h.tvSender.setText(m.getSender());

        boolean isAdmin = m.getSender() != null && m.getSender().toUpperCase().startsWith("ADMIN");

        LinearLayout.LayoutParams bubbleParams = (LinearLayout.LayoutParams) h.bubble.getLayoutParams();
        bubbleParams.gravity = isAdmin ? Gravity.END : Gravity.START;
        h.bubble.setLayoutParams(bubbleParams);

        ((LinearLayout) h.itemView).setGravity(isAdmin ? Gravity.END : Gravity.START);

        h.bubble.setBackgroundResource(isAdmin ? R.drawable.chat_bubble_right : R.drawable.chat_bubble_left);
    }

    @Override public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        View bubble;
        TextView tvContent, tvSender;
        VH(View v) {
            super(v);
            bubble = v.findViewById(R.id.bubble);
            tvContent = v.findViewById(R.id.tvContent);
            tvSender = v.findViewById(R.id.tvSender);
        }
    }
}
