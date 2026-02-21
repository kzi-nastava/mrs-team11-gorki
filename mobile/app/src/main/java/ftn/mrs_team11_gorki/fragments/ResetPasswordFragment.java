package ftn.mrs_team11_gorki.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.dto.ResetPasswordDTO;
import ftn.mrs_team11_gorki.service.AuthService;
import ftn.mrs_team11_gorki.service.ClientUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordFragment extends Fragment {

    private EditText edtNew;
    private EditText edtConfirm;
    private Button btnReset;
    private TextView txtStatus;

    private String token;

    private final AuthService authService =
            ClientUtils.getRetrofit().create(AuthService.class);

    public ResetPasswordFragment() {
        super(R.layout.fragment_reset_password);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtNew = view.findViewById(R.id.edtNewPassword);
        edtConfirm = view.findViewById(R.id.edtConfirmPassword);
        btnReset = view.findViewById(R.id.btnReset);
        txtStatus = view.findViewById(R.id.txtStatus);

        token = (getArguments() != null) ? getArguments().getString("resetToken") : null;

        if (token == null || token.isEmpty()) {
            txtStatus.setText("Missing reset token. Open the link from email again.");
            btnReset.setEnabled(false);
            return;
        }

        btnReset.setOnClickListener(v -> doReset());
    }

    private void doReset() {
        String p1 = safe(edtNew.getText() != null ? edtNew.getText().toString() : null);
        String p2 = safe(edtConfirm.getText() != null ? edtConfirm.getText().toString() : null);

        if (p1.isEmpty()) {
            txtStatus.setText("New password is required.");
            return;
        }

        if (!p1.equals(p2)) {
            txtStatus.setText("Passwords do not match.");
            return;
        }

        setLoading(true, "Resetting password...");

        ResetPasswordDTO body = new ResetPasswordDTO(token, p1, p2);

        authService.resetPassword(body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!isAdded()) return;

                if (response.isSuccessful()) {
                    setLoading(false, "Password changed successfully.");
                    navigateAfterSuccess();
                } else {
                    String msg = "Reset failed: HTTP " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            msg += "\n" + response.errorBody().string();
                        }
                    } catch (Exception ignored) {}
                    setLoading(false, msg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                setLoading(false, "Network error: " + t.getMessage());
            }
        });
    }

    private void navigateAfterSuccess() {
        // Opcija A: vrati na guest/home (promeni po potrebi)
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.unuserHomeFragment, true)
                .build();
        NavHostFragment.findNavController(this)
                .navigate(R.id.unuserHomeFragment, null, navOptions);

        // Ako želiš umesto toga da otvori Login dialog:
        // new LoginFragment().show(requireActivity().getSupportFragmentManager(), "loginDialog");
    }

    private void setLoading(boolean loading, String message) {
        txtStatus.setText(message == null ? "" : message);
        btnReset.setEnabled(!loading);
        edtNew.setEnabled(!loading);
        edtConfirm.setEnabled(!loading);
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }
}