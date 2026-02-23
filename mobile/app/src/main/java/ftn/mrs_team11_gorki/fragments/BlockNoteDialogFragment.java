package ftn.mrs_team11_gorki.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ftn.mrs_team11_gorki.R;

public class BlockNoteDialogFragment extends DialogFragment {

    public interface Listener {
        void onConfirm(long userId, String reason);
    }

    private static final String ARG_USER_ID = "user_id";
    private static final String ARG_BLOCKED = "blocked";

    public static BlockNoteDialogFragment newInstance(long userId, boolean blocked) {
        BlockNoteDialogFragment f = new BlockNoteDialogFragment();
        Bundle b = new Bundle();
        b.putLong(ARG_USER_ID, userId);
        b.putBoolean(ARG_BLOCKED, blocked);
        f.setArguments(b);
        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = requireActivity().getLayoutInflater().inflate(R.layout.fragment_block_note_dialog, null);

        long userId = requireArguments().getLong(ARG_USER_ID);
        boolean blocked = requireArguments().getBoolean(ARG_BLOCKED);

        EditText etReason = v.findViewById(R.id.etReason);
        Button btnConfirm = v.findViewById(R.id.btnConfirm);
        Button btnCancel = v.findViewById(R.id.btnCancel);

        btnConfirm.setText(blocked ? "Unblock" : "Block");

        btnCancel.setOnClickListener(view -> dismiss());

        btnConfirm.setOnClickListener(view -> {
            String reason = etReason.getText() != null ? etReason.getText().toString().trim() : "";
            if (!blocked && reason.isEmpty()) {
                etReason.setError("Please enter a reason.");
                return;
            }
            if (getParentFragment() instanceof Listener) {
                ((Listener) getParentFragment()).onConfirm(userId, reason);
            }
            dismiss();
        });

        return new MaterialAlertDialogBuilder(requireContext())
                .setView(v)
                .create();
    }
}
