package ftn.mrs_team11_gorki.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.databinding.FragmentLoginBinding;
import ftn.mrs_team11_gorki.databinding.LoginFormBinding;

public class LoginFragment extends DialogFragment {
    private FragmentLoginBinding dialogBinding;
    private LoginFormBinding formBinding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialogBinding = FragmentLoginBinding.inflate(requireActivity().getLayoutInflater());
        formBinding = LoginFormBinding.bind(dialogBinding.getRoot().findViewById(R.id.loginForm));

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogBinding.getRoot())
                .create();

        formBinding.loginButton.setOnClickListener(v -> {
            dismiss();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
            navController.navigate(R.id.homeFragment);
        });
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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