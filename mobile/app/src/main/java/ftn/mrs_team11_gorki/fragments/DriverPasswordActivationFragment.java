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
import ftn.mrs_team11_gorki.dto.ActivateDriverDTO;
import ftn.mrs_team11_gorki.service.AuthService;
import ftn.mrs_team11_gorki.service.ClientUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverPasswordActivationFragment extends Fragment {

    private EditText edtPassword;
    private Button btnActivate;
    private TextView txtStatus;

    private String token;

    private final AuthService authService =
            ClientUtils.getRetrofit().create(AuthService.class);

    public DriverPasswordActivationFragment() {
        super(R.layout.fragment_driver_password_activation);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtPassword = view.findViewById(R.id.edtPassword);
        btnActivate = view.findViewById(R.id.btnActivate);
        txtStatus = view.findViewById(R.id.txtStatus);

        token = (getArguments() != null) ? getArguments().getString("activationToken") : null;

        if (token == null || token.trim().isEmpty()) {
            setLoading(false, "Missing activation token. Open the email link again.");
            btnActivate.setEnabled(false);
            return;
        }

        btnActivate.setOnClickListener(v -> activate());
    }

    private void activate() {
        String p1 = safe(text(edtPassword));

        if (p1.isEmpty()) {
            txtStatus.setText("Password is required.");
            return;
        }

        setLoading(true, "Activating...");

        ActivateDriverDTO body = new ActivateDriverDTO(token, p1);
        authService.activateDriver(body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!isAdded()) return;

                if (response.isSuccessful()) {
                    setLoading(false, "Activated successfully. You can log in now.");

                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.unuserHomeFragment, true)
                            .build();
                    NavHostFragment.findNavController(DriverPasswordActivationFragment.this)
                            .navigate(R.id.unuserHomeFragment, null, navOptions);

                } else {
                    String msg = "Activation failed: HTTP " + response.code();
                    try {
                        if (response.errorBody() != null) msg += "\n" + response.errorBody().string();
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

    private void setLoading(boolean loading, String message) {
        txtStatus.setText(message == null ? "" : message);
        btnActivate.setEnabled(!loading);
        edtPassword.setEnabled(!loading);
    }

    private static String text(EditText e) {
        return (e == null || e.getText() == null) ? "" : e.getText().toString();
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }
}