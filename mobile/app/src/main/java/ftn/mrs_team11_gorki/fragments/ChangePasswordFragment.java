package ftn.mrs_team11_gorki.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ftn.mrs_team11_gorki.auth.ApiClient;
import ftn.mrs_team11_gorki.auth.TokenStorage;
import ftn.mrs_team11_gorki.databinding.FragmentChangePasswordBinding;
import ftn.mrs_team11_gorki.dto.UpdatePasswordDTO;
import ftn.mrs_team11_gorki.service.UserService;
import ftn.mrs_team11_gorki.view.ChangePasswordViewModel;
import ftn.mrs_team11_gorki.view.ChangePasswordViewModelFactory;

public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordBinding binding;
    private ChangePasswordViewModel viewModel;

    private Long userId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);

        TokenStorage ts = new TokenStorage(requireContext());
        userId = ts.getUserId();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserService userService = ApiClient.getRetrofit(requireContext()).create(UserService.class);

        ChangePasswordViewModelFactory factory = new ChangePasswordViewModelFactory(userService, userId);
        viewModel = new ViewModelProvider(this, factory).get(ChangePasswordViewModel.class);

        observeViewModel();

        binding.changePassword.doneButton.setOnClickListener(v -> onDoneClicked());
    }

    private void observeViewModel() {
        viewModel.isLoading().observe(getViewLifecycleOwner(), loading ->
                setLoading(Boolean.TRUE.equals(loading))
        );

        viewModel.getMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                viewModel.clearMessage();
            }
        });

        viewModel.getSuccess().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                clearInputs();
                viewModel.clearSuccess();
            }
        });
    }

    private void onDoneClicked() {
        String current = binding.changePassword.currentPasswordInput.getText().toString();
        String newPass = binding.changePassword.newPasswordInput.getText().toString();
        String confirm = binding.changePassword.confirmNewPasswordInput.getText().toString();

        if (current.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(requireContext(), "Popuni sva polja", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPass.equals(confirm)) {
            Toast.makeText(requireContext(), "Nova lozinka se ne poklapa", Toast.LENGTH_SHORT).show();
            return;
        }

        UpdatePasswordDTO dto = new UpdatePasswordDTO();
        dto.setCurrentPassword(current);
        dto.setNewPassword(newPass);
        dto.setNewPasswordConfirmed(confirm);

        viewModel.changePassword(dto);
    }

    private void clearInputs() {
        binding.changePassword.currentPasswordInput.setText("");
        binding.changePassword.newPasswordInput.setText("");
        binding.changePassword.confirmNewPasswordInput.setText("");
    }

    private void setLoading(boolean loading) {
        binding.changePassword.doneButton.setEnabled(!loading);
        binding.changePassword.currentPasswordInput.setEnabled(!loading);
        binding.changePassword.newPasswordInput.setEnabled(!loading);
        binding.changePassword.confirmNewPasswordInput.setEnabled(!loading);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
