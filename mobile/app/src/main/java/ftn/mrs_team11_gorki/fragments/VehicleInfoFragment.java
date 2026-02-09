package ftn.mrs_team11_gorki.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.List;


import ftn.mrs_team11_gorki.auth.ApiClient;
import ftn.mrs_team11_gorki.auth.TokenStorage;
import ftn.mrs_team11_gorki.databinding.FragmentVehicleInfoBinding;
import ftn.mrs_team11_gorki.dto.GetVehicleDTO;
import ftn.mrs_team11_gorki.dto.UpdateVehicleDTO;
import ftn.mrs_team11_gorki.dto.UpdatedVehicleDTO;
import ftn.mrs_team11_gorki.service.DriverService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleInfoFragment extends Fragment {

    private FragmentVehicleInfoBinding binding; // binding za fragment_vehicle_info.xml
    private DriverService driverService;

    private Long driverId;
    private GetVehicleDTO originalVehicle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentVehicleInfoBinding.inflate(inflater, container, false);

        TokenStorage ts = new TokenStorage(requireContext());
        driverId = ts.getUserId();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        driverService = ApiClient.getRetrofit(requireContext()).create(DriverService.class);

        setupDropdown();
        loadVehicle();

        binding.vehicleInfo.saveChangesButton.setOnClickListener(v -> updateVehicle());
        binding.vehicleInfo.revertChangesButton.setOnClickListener(v -> revertChanges());
    }

    private void setupDropdown() {
        List<String> vehicleTypes = Arrays.asList("STANDARD", "LUXURY", "VAN");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                vehicleTypes
        );

        binding.vehicleInfo.vehicleTypeDropdown.setAdapter(adapter);
        binding.vehicleInfo.vehicleTypeDropdown.setOnClickListener(v ->
                binding.vehicleInfo.vehicleTypeDropdown.showDropDown()
        );
    }

    private void loadVehicle() {
        setLoading(true);

        driverService.getVehicle(driverId).enqueue(new Callback<GetVehicleDTO>() {
            @Override
            public void onResponse(@NonNull Call<GetVehicleDTO> call,
                                   @NonNull Response<GetVehicleDTO> response) {
                setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    originalVehicle = response.body();
                    fillForm(originalVehicle);
                } else {
                    Toast.makeText(requireContext(),
                            "Ne mogu da učitam vozilo (" + response.code() + ")",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetVehicleDTO> call, @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(requireContext(),
                        "Greška: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillForm(GetVehicleDTO v) {
        binding.vehicleInfo.modelInput.setText(safe(v.getModel()));
        binding.vehicleInfo.licensePlateInput.setText(safe(v.getPlateNumber()));
        binding.vehicleInfo.seatsInput.setText(String.valueOf(v.getSeats()));
        binding.vehicleInfo.vehicleTypeDropdown.setText(safe(v.getType()), false);
        Boolean baby = v.getBabyTransport();
        if (baby != null && baby) binding.vehicleInfo.yesBabyTransport.setChecked(true);
        else binding.vehicleInfo.noBabyTransport.setChecked(true);
        Boolean pet = v.getPetFriendly();
        if (pet != null && pet) binding.vehicleInfo.yesPetFriendly.setChecked(true);
        else binding.vehicleInfo.noPetFriendly.setChecked(true);
    }

    private void updateVehicle() {
        String model = binding.vehicleInfo.modelInput.getText().toString().trim();
        String plate = binding.vehicleInfo.licensePlateInput.getText().toString().trim();
        String seatsStr = binding.vehicleInfo.seatsInput.getText().toString().trim();
        String type = binding.vehicleInfo.vehicleTypeDropdown.getText().toString().trim();

        boolean babyTransport = binding.vehicleInfo.yesBabyTransport.isChecked();
        boolean petFriendly = binding.vehicleInfo.yesPetFriendly.isChecked();

        UpdateVehicleDTO dto = new UpdateVehicleDTO();
        dto.setModel(model);
        dto.setPlateNumber(plate);
        dto.setSeats(Integer.parseInt(seatsStr));
        dto.setType(type);
        dto.setBabyTransport(babyTransport);
        dto.setPetFriendly(petFriendly);

        setLoading(true);

        driverService.updateVehicle(driverId, dto).enqueue(new Callback<UpdatedVehicleDTO>() {
            @Override
            public void onResponse(@NonNull Call<UpdatedVehicleDTO> call,
                                   @NonNull Response<UpdatedVehicleDTO> response) {
                setLoading(false);

                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Sačuvano", Toast.LENGTH_SHORT).show();
                    loadVehicle();
                } else {
                    Toast.makeText(requireContext(),
                            "Neuspešan update (" + response.code() + ")",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpdatedVehicleDTO> call, @NonNull Throwable t) {
                setLoading(false);
                Toast.makeText(requireContext(),
                        "Greška: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void revertChanges() {
        if (originalVehicle != null) fillForm(originalVehicle);
    }

    private void setLoading(boolean loading) {
        binding.vehicleInfo.saveChangesButton.setEnabled(!loading);
        binding.vehicleInfo.revertChangesButton.setEnabled(!loading);
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
