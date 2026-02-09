package ftn.mrs_team11_gorki.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.navigation.NavOptions;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.databinding.FragmentLoginBinding;
import ftn.mrs_team11_gorki.databinding.LoginFormBinding;


import ftn.mrs_team11_gorki.auth.ApiClient;
import ftn.mrs_team11_gorki.auth.TokenStorage;
import ftn.mrs_team11_gorki.dto.LoginRequest;
import ftn.mrs_team11_gorki.dto.LoginResponse;
import ftn.mrs_team11_gorki.service.AuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends DialogFragment {
    private FragmentLoginBinding dialogBinding;
    private LoginFormBinding formBinding;
    private AuthService authApi;
    private Call<LoginResponse> loginCall;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialogBinding = FragmentLoginBinding.inflate(requireActivity().getLayoutInflater());
        formBinding = LoginFormBinding.bind(dialogBinding.getRoot().findViewById(R.id.loginForm));

        authApi = ApiClient
                .getRetrofit(requireContext())
                .create(AuthService.class);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogBinding.getRoot())
                .create();

        formBinding.loginButton.setOnClickListener(v -> {
            String email = formBinding.emailInput.getText().toString().trim();
            String password = formBinding.passwordInput.getText().toString();

            // basic validacija
            if (TextUtils.isEmpty(email)) {
                formBinding.emailInput.setError("Email is required");
                formBinding.emailInput.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                formBinding.passwordInput.setError("Password is required");
                formBinding.passwordInput.requestFocus();
                return;
            }

            formBinding.loginButton.setEnabled(false);

            LoginRequest request = new LoginRequest(email, password);

            loginCall = authApi.login(request);
            loginCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (!isAdded()) return;

                    formBinding.loginButton.setEnabled(true);

                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse res = response.body();

                        if (res.getToken() != null && !res.getToken().isEmpty()) {
                            new TokenStorage(requireContext()).save(res);

                            Toast.makeText(requireContext(),
                                    "Login success: " + res.getRole(),
                                    Toast.LENGTH_SHORT).show();

                            dismiss();

                            // navigacija tek nakon dismiss (da ne puca zbog dialog lifecycle)

                            NavController navController =
                                    Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);

                            NavOptions navOptions = new NavOptions.Builder()
                                    .setPopUpTo(R.id.unuserHomeFragment, true) // izbriši guest iz back stack-a
                                    .build();

                            navController.navigate(R.id.homeFragment, null, navOptions);
                        } else {
                            Toast.makeText(requireContext(),
                                    "Invalid credentials!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                    // HTTP greske
                    int code = response.code();
                    if (code == 401) {
                        Toast.makeText(requireContext(), "Pogrešan email/password", Toast.LENGTH_SHORT).show();
                    } else if (code == 403) {
                        Toast.makeText(requireContext(), "Account blocked/inactive", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "HTTP error: " + code, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    if (!isAdded()) return;

                    formBinding.loginButton.setEnabled(true);

                    // ako je cancelovano u onDestroyView, ignorisi
                    if (call.isCanceled()) return;

                    Toast.makeText(requireContext(),
                            "Network error: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        });

        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (loginCall != null) {
            loginCall.cancel();
            loginCall = null;
        }

        dialogBinding = null;
        formBinding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }
}