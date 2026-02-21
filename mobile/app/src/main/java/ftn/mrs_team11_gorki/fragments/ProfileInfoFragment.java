package ftn.mrs_team11_gorki.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.auth.ApiClient;
import ftn.mrs_team11_gorki.auth.TokenStorage;
import ftn.mrs_team11_gorki.databinding.FragmentProfileInfoBinding;
import ftn.mrs_team11_gorki.dto.GetUserDTO;
import ftn.mrs_team11_gorki.dto.UpdateUserDTO;
import ftn.mrs_team11_gorki.service.UserService;
import ftn.mrs_team11_gorki.view.ProfileInfoViewModel;
import ftn.mrs_team11_gorki.view.ProfileInfoViewModelFactory;

public class ProfileInfoFragment extends Fragment {

    private FragmentProfileInfoBinding binding;
    private ProfileInfoViewModel viewModel;

    private Long userId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileInfoBinding.inflate(inflater, container, false);

        TokenStorage ts = new TokenStorage(requireContext());
        userId = ts.getUserId();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserService userService = ApiClient.getRetrofit(requireContext()).create(UserService.class);

        ProfileInfoViewModelFactory factory = new ProfileInfoViewModelFactory(userService, userId);
        viewModel = new ViewModelProvider(this, factory).get(ProfileInfoViewModel.class);

        observeViewModel();

        viewModel.loadUser();

        binding.profileInfo.saveChangesButton.setOnClickListener(v -> {
            UpdateUserDTO dto = buildUpdateDtoFromForm();
            if (dto == null) return; // invalid phone etc.
            viewModel.updateUser(dto);
        });

        binding.profileInfo.revertChangesButton.setOnClickListener(v -> viewModel.revert());
    }

    private void observeViewModel() {
        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                fillForm(user);
            }
        });

        viewModel.isLoading().observe(getViewLifecycleOwner(), loading -> {
            setLoading(Boolean.TRUE.equals(loading));
        });

        viewModel.getMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                viewModel.clearMessage(); // da se ne ponavlja posle rotacije
            }
        });
    }

    private void fillForm(@NonNull GetUserDTO u) {
        binding.profileInfo.firstNameInput.setText(u.getFirstName());
        binding.profileInfo.lastNameInput.setText(u.getLastName());
        binding.profileInfo.homeAddressInput.setText(u.getAddress());
        binding.profileInfo.phoneInput.setText(String.valueOf(u.getPhoneNumber()));
        binding.profileInfo.emailInput.setText(u.getEmail());

        // ==== Blocked banner (only if blocked AND DRIVER) ====
        boolean isBlocked = Boolean.TRUE.equals(u.getBlocked()); // ili u.isBlocked() ako ti je boolean
        String role = u.getRole(); // očekujem "DRIVER" ili "ROLE_DRIVER"

        boolean isDriver = "DRIVER".equals(role) || "ROLE_DRIVER".equals(role);

        if (isBlocked && isDriver) {
            binding.blockInfoDriver.getRoot().setVisibility(View.VISIBLE);

            // etBlockReason je unutar included layout-a
            EditText etReason = binding.blockInfoDriver.getRoot().findViewById(R.id.etBlockReason);
            String reason = u.getBlockReason() != null ? u.getBlockReason() : "";
            etReason.setText(reason);
        } else {
            binding.blockInfoDriver.getRoot().setVisibility(View.GONE);
        }

    }

    @Nullable
    private UpdateUserDTO buildUpdateDtoFromForm() {
        String firstName = binding.profileInfo.firstNameInput.getText().toString().trim();
        String lastName = binding.profileInfo.lastNameInput.getText().toString().trim();
        String address = binding.profileInfo.homeAddressInput.getText().toString().trim();
        String phoneStr = binding.profileInfo.phoneInput.getText().toString().trim();
        String email = binding.profileInfo.emailInput.getText().toString().trim();

        int phone;
        try {
            phone = Integer.parseInt(phoneStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Neispravan broj telefona", Toast.LENGTH_SHORT).show();
            return null;
        }

        UpdateUserDTO dto = new UpdateUserDTO();
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setAddress(address);
        dto.setPhoneNumber(phone);
        dto.setEmail(email);

        return dto;
    }

    private void setLoading(boolean loading) {
        binding.profileInfo.saveChangesButton.setEnabled(!loading);
        binding.profileInfo.revertChangesButton.setEnabled(!loading);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
