package ftn.mrs_team11_gorki.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.dto.MessageDTO;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.VH> {

    private static final int TYPE_LEFT = 0;
    private static final int TYPE_RIGHT = 1;
    private final List<MessageDTO> items = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void submit(List<MessageDTO> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    public void add(MessageDTO msg) {
        items.add(msg);
        notifyItemInserted(items.size()-1);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;

        if (viewType == TYPE_LEFT) {
            v = inflater.inflate(R.layout.item_chat_message, parent, false);
        } else {
            v = inflater.inflate(R.layout.item_chat_message_right, parent, false);
        }

        return new VH(v);
    }
    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {

        MessageDTO m = items.get(position);

        h.content.setText(m.getContent());
        h.sender.setText(m.getSender());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {

        TextView content;
        TextView sender;

        VH(View v) {
            super(v);
            content = v.findViewById(R.id.tvContent);
            sender = v.findViewById(R.id.tvSender);
        }
    }

    @Override
    public int getItemViewType(int position) {

        MessageDTO msg = items.get(position);

        if (msg.getSender() != null &&
                msg.getSender().startsWith("ADMIN")) {

            return TYPE_LEFT;
        }

        return TYPE_RIGHT;
    }

}

