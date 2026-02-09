package ftn.mrs_team11_gorki.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ftn.mrs_team11_gorki.auth.ApiClient;
import ftn.mrs_team11_gorki.auth.TokenStorage;
import ftn.mrs_team11_gorki.databinding.FragmentProfileInfoBinding;
import ftn.mrs_team11_gorki.dto.GetUserDTO;
import ftn.mrs_team11_gorki.dto.UpdateUserDTO;
import ftn.mrs_team11_gorki.dto.UpdatedUserDTO;
import ftn.mrs_team11_gorki.service.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileInfoFragment extends Fragment {
    private FragmentProfileInfoBinding binding;
    private UserService userService;
    private Long userId;
    private GetUserDTO originalUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TokenStorage ts = new TokenStorage(requireContext());
        String token = ts.getToken();
        userId = ts.getUserId();
        binding = FragmentProfileInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userService = ApiClient.getRetrofit(getContext()).create(UserService.class);

        loadUser(); // GET na učitavanje

        binding.profileInfo.saveChangesButton.setOnClickListener(v -> updateUser()); // PUT na klik
        binding.profileInfo.revertChangesButton.setOnClickListener(v -> revertChanges());
    }

    private void loadUser() {
        setLoading(true);

        userService.getUser(userId).enqueue(new Callback<GetUserDTO>() {
            @Override
            public void onResponse(@NonNull Call<GetUserDTO> call,
                                   @NonNull Response<GetUserDTO> response) {
                setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    originalUser = response.body();
                    fillForm(originalUser);
                } else {
                    Toast.makeText(requireContext(),
                            "Ne mogu da učitam profil (" + response.code() + ")",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetUserDTO> call, @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(requireContext(),
                        "Greška: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillForm(GetUserDTO u) {
        binding.profileInfo.firstNameInput.setText(u.getFirstName());
        binding.profileInfo.lastNameInput.setText(u.getLastName());
        binding.profileInfo.homeAddressInput.setText(u.getAddress());
        binding.profileInfo.phoneInput.setText(String.valueOf(u.getPhoneNumber()));
        binding.profileInfo.emailInput.setText(u.getEmail());
    }

    private void updateUser() {
        String firstName = binding.profileInfo.firstNameInput.getText().toString().trim();
        String lastName = binding.profileInfo.lastNameInput.getText().toString().trim();
        String address = binding.profileInfo.homeAddressInput.getText().toString().trim();
        String phone = binding.profileInfo.phoneInput.getText().toString().trim();
        String email = binding.profileInfo.emailInput.getText().toString().trim();

        UpdateUserDTO dto = new UpdateUserDTO();
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setAddress(address);
        dto.setPhoneNumber(Integer.parseInt(phone));
        dto.setEmail(email);

        setLoading(true);

        userService.updateUser(userId, dto).enqueue(new Callback<UpdatedUserDTO>() {
            @Override
            public void onResponse(@NonNull Call<UpdatedUserDTO> call,
                                   @NonNull Response<UpdatedUserDTO> response) {
                setLoading(false);

                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(),
                            "Sačuvano",
                            Toast.LENGTH_SHORT).show();
                    loadUser();
                } else {
                    Toast.makeText(requireContext(),
                            "Neuspešan update (" + response.code() + ")",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpdatedUserDTO> call, @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(requireContext(),
                        "Greška: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void revertChanges() {
        if (originalUser != null) {
            fillForm(originalUser);
        }
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
