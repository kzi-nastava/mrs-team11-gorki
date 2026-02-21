package ftn.mrs_team11_gorki.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.auth.ApiClient;
import ftn.mrs_team11_gorki.dto.CreateDriverDTO;
import ftn.mrs_team11_gorki.dto.CreateUserDTO;
import ftn.mrs_team11_gorki.dto.CreateVehicleDTO;
import ftn.mrs_team11_gorki.service.ClientUtils;
import ftn.mrs_team11_gorki.service.DriverService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverRegistrationFragment extends Fragment {
    private EditText firstNameInput, lastNameInput, homeAddressInput, phoneInput, emailInput;
    private EditText modelInput, licensePlateInput, seatsInput;
    private AutoCompleteTextView vehicleTypeDropdown;
    private RadioGroup babyTransportGroup, petFriendlyGroup;
    private Button registerButton;

    private DriverService driverService;

    public DriverRegistrationFragment() {
        super(R.layout.fragment_driver_registration);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View form = view.findViewById(R.id.driverRegistrationForm);
        driverService = ApiClient.getRetrofit(requireContext()).create(DriverService.class);
        firstNameInput = form.findViewById(R.id.firstNameInput);
        lastNameInput = form.findViewById(R.id.lastNameInput);
        homeAddressInput = form.findViewById(R.id.homeAddressInput);
        phoneInput = form.findViewById(R.id.phoneInput);
        emailInput = form.findViewById(R.id.emailInput);

        modelInput = form.findViewById(R.id.modelInput);
        licensePlateInput = form.findViewById(R.id.licensePlateInput);
        seatsInput = form.findViewById(R.id.seatsInput);
        vehicleTypeDropdown = form.findViewById(R.id.vehicleTypeDropdown);
        String[] vehicleTypes = new String[]{"STANDARD", "LUXURY", "VAN"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, vehicleTypes);
        vehicleTypeDropdown.setAdapter(adapter);
        babyTransportGroup = form.findViewById(R.id.babyTransport);
        petFriendlyGroup = form.findViewById(R.id.petFriendly);

        registerButton = form.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> registerDriver());
    }

    private void registerDriver() {
        String firstName = safe(text(firstNameInput));
        String lastName = safe(text(lastNameInput));
        String address = safe(text(homeAddressInput));
        String phoneStr = safe(text(phoneInput));
        String email = safe(text(emailInput));

        String model = safe(text(modelInput));
        String plateNumber = safe(text(licensePlateInput));
        String seatsStr = safe(text(seatsInput));
        String vehicleType = safe(vehicleTypeDropdown != null ? vehicleTypeDropdown.getText().toString() : "");

        if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() ||
                phoneStr.isEmpty() || email.isEmpty() ||
                model.isEmpty() || plateNumber.isEmpty() || seatsStr.isEmpty() || vehicleType.isEmpty()) {
            toast("Please fill in all fields.");
            return;
        }

        int phoneNumber;
        int seats;
        try {
            phoneNumber = Integer.parseInt(phoneStr);
        } catch (NumberFormatException e) {
            toast("Phone number must be a number.");
            return;
        }
        try {
            seats = Integer.parseInt(seatsStr);
        } catch (NumberFormatException e) {
            toast("Seats must be a number.");
            return;
        }

        boolean babyTransport = (babyTransportGroup != null && babyTransportGroup.getCheckedRadioButtonId() == R.id.yesBabyTransport);
        boolean petFriendly = (petFriendlyGroup != null && petFriendlyGroup.getCheckedRadioButtonId() == R.id.yesPetFriendly);

        setLoading(true);


        CreateUserDTO user = new CreateUserDTO(email, null, firstName, lastName, phoneNumber, address, null);

        String backendVehicleType = mapVehicleTypeToEnum(vehicleType);

        CreateVehicleDTO vehicle = new CreateVehicleDTO(model, backendVehicleType, plateNumber, seats, babyTransport, petFriendly);

        CreateDriverDTO body = new CreateDriverDTO(user, vehicle);

        driverService.register(body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!isAdded()) return;
                setLoading(false);

                if (response.isSuccessful() || response.code() == 201) {
                    toast("Driver created. Check email for activation link.");
                } else {
                    String msg = "Registration failed: HTTP " + response.code();
                    toast(msg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                setLoading(false);
                toast("Network error: " + t.getMessage());
            }
        });
    }

    private String mapVehicleTypeToEnum(String selected) {
        if (selected == null) return "";
        String s = selected.trim();

        switch (s.toLowerCase()) {
            case "standard": return "STANDARD";
            case "luxury": return "LUXURY";
            case "van": return "VAN";
            default: return s; // fallback
        }
    }
    private void setLoading(boolean loading) {
        if (registerButton != null) registerButton.setEnabled(!loading);

        if (firstNameInput != null) firstNameInput.setEnabled(!loading);
        if (lastNameInput != null) lastNameInput.setEnabled(!loading);
        if (homeAddressInput != null) homeAddressInput.setEnabled(!loading);
        if (phoneInput != null) phoneInput.setEnabled(!loading);
        if (emailInput != null) emailInput.setEnabled(!loading);

        if (modelInput != null) modelInput.setEnabled(!loading);
        if (licensePlateInput != null) licensePlateInput.setEnabled(!loading);
        if (seatsInput != null) seatsInput.setEnabled(!loading);
        if (vehicleTypeDropdown != null) vehicleTypeDropdown.setEnabled(!loading);

        if (babyTransportGroup != null) babyTransportGroup.setEnabled(!loading);
        if (petFriendlyGroup != null) petFriendlyGroup.setEnabled(!loading);
    }

    private void toast(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show();
    }

    private static String text(EditText e) {
        return (e == null || e.getText() == null) ? "" : e.getText().toString();
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }
}