package ftn.mrs_team11_gorki.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.adapter.ImageUtil;
import ftn.mrs_team11_gorki.dto.GetUserDTO;

public class UserCardListAdapter extends BaseAdapter {

    public interface OnBlockClickListener {
        void onBlockClick(GetUserDTO user);
    }

    private final Context context;
    private final LayoutInflater inflater;
    private final List<GetUserDTO> users = new ArrayList<>();
    private final OnBlockClickListener listener;

    public UserCardListAdapter(Context context, OnBlockClickListener listener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    public void setUsers(List<GetUserDTO> newUsers) {
        users.clear();
        if (newUsers != null) users.addAll(newUsers);
        notifyDataSetChanged();
    }

    public void updateUser(GetUserDTO updated) {
        if (updated == null || updated.getId() == null) return;
        for (int i = 0; i < users.size(); i++) {
            if (updated.getId().equals(users.get(i).getId())) {
                users.set(i, updated);
                notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public GetUserDTO getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        Long id = users.get(position).getId();
        return id != null ? id : position;
    }

    static class VH {
        TextView txtTitle, txtEmail, txtName, txtAddress, txtPhone, txtRole;
        View dotActive, dotBlocked;
        ImageView imgProfile;
        Button btnBlock;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH h;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_user_card, parent, false);
            h = new VH();
            h.txtTitle = convertView.findViewById(R.id.txtTitle);
            h.txtEmail = convertView.findViewById(R.id.txtEmail);
            h.txtName = convertView.findViewById(R.id.txtName);
            h.txtAddress = convertView.findViewById(R.id.txtAddress);
            h.txtPhone = convertView.findViewById(R.id.txtPhone);
            h.txtRole = convertView.findViewById(R.id.txtRole);
            h.dotActive = convertView.findViewById(R.id.dotActive);
            h.dotBlocked = convertView.findViewById(R.id.dotBlocked);
            h.imgProfile = convertView.findViewById(R.id.imgProfile);
            h.btnBlock = convertView.findViewById(R.id.btnBlock);
            convertView.setTag(h);
        } else {
            h = (VH) convertView.getTag();
        }

        GetUserDTO u = getItem(position);

        h.txtTitle.setText("User - id " + safe(u.getId()));
        h.txtEmail.setText("Email - " + safe(u.getEmail()));
        h.txtName.setText(safe(u.getFirstName()) + " " + safe(u.getLastName()));
        h.txtAddress.setText("Home address - " + safe(u.getAddress()));
        h.txtPhone.setText("Phone - " + safe(u.getPhoneNumber()));
        h.txtRole.setText("Role - " + safe(u.getRole()));

        boolean active = getActiveValue(u);
        boolean blocked = getBlockedValue(u);

        h.dotActive.setBackgroundColor(active ? Color.GREEN : Color.LTGRAY);
        h.dotBlocked.setBackgroundColor(blocked ? Color.RED : Color.LTGRAY);

        h.btnBlock.setText(blocked ? "UNBLOCK" : "BLOCK");
        h.btnBlock.setOnClickListener(v -> {
            if (listener != null) listener.onBlockClick(u);
        });

        // ===== Base64 image logic =====
        String img = getProfileImage(u); // base64/data url string
        Bitmap bm = ImageUtil.decodeBase64ToBitmap(img);
        if (bm != null) {
            h.imgProfile.setImageBitmap(bm);
        } else {
            h.imgProfile.setImageResource(R.drawable.user_pic);
        }

        return convertView;
    }

    // --- Helpers: prilagodi po tvom GetUserDTO ako treba ---
    private boolean getBlockedValue(GetUserDTO u) {
        try {
            return (boolean) GetUserDTO.class.getMethod("isBlocked").invoke(u);
        } catch (Exception ignore) {
            try {
                Object v = GetUserDTO.class.getMethod("getBlocked").invoke(u);
                return Boolean.TRUE.equals(v);
            } catch (Exception ignored2) {
                return false;
            }
        }
    }

    private boolean getActiveValue(GetUserDTO u) {
        try {
            return (boolean) GetUserDTO.class.getMethod("isActive").invoke(u);
        } catch (Exception ignore) {
            try {
                Object v = GetUserDTO.class.getMethod("getActive").invoke(u);
                return Boolean.TRUE.equals(v);
            } catch (Exception ignored2) {
                return false;
            }
        }
    }

    private String getProfileImage(GetUserDTO u) {
        try {
            Object v = GetUserDTO.class.getMethod("getProfileImage").invoke(u);
            return v != null ? v.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String safe(Object o) {
        return o == null ? "-" : String.valueOf(o);
    }
}
