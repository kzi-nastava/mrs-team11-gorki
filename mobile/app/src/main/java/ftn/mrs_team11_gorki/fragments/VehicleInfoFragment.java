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
import androidx.lifecycle.ViewModelProvider;

import java.util.Arrays;
import java.util.List;

import ftn.mrs_team11_gorki.auth.ApiClient;
import ftn.mrs_team11_gorki.auth.TokenStorage;
import ftn.mrs_team11_gorki.databinding.FragmentVehicleInfoBinding;
import ftn.mrs_team11_gorki.dto.GetVehicleDTO;
import ftn.mrs_team11_gorki.dto.UpdateVehicleDTO;
import ftn.mrs_team11_gorki.service.DriverService;
import ftn.mrs_team11_gorki.view.VehicleInfoViewModel;
import ftn.mrs_team11_gorki.view.VehicleInfoViewModelFactory;

public class VehicleInfoFragment extends Fragment {

    private FragmentVehicleInfoBinding binding;
    private VehicleInfoViewModel viewModel;

    private Long driverId;

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

        DriverService driverService = ApiClient.getRetrofit(requireContext()).create(DriverService.class);

        VehicleInfoViewModelFactory factory = new VehicleInfoViewModelFactory(driverService, driverId);
        viewModel = new ViewModelProvider(this, factory).get(VehicleInfoViewModel.class);

        setupDropdown();
        observeViewModel();

        viewModel.loadVehicle();

        binding.vehicleInfo.saveChangesButton.setOnClickListener(v -> {
            UpdateVehicleDTO dto = buildUpdateVehicleDtoFromForm();
            if (dto == null) return;
            viewModel.updateVehicle(dto);
        });

        binding.vehicleInfo.revertChangesButton.setOnClickListener(v -> viewModel.revert());
    }

    private void observeViewModel() {
        viewModel.getVehicle().observe(getViewLifecycleOwner(), vehicle -> {
            if (vehicle != null) {
                fillForm(vehicle);
            }
        });

        viewModel.isLoading().observe(getViewLifecycleOwner(), loading -> {
            setLoading(Boolean.TRUE.equals(loading));
        });

        viewModel.getMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                viewModel.clearMessage();
            }
        });
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

    private void fillForm(@NonNull GetVehicleDTO v) {
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

    @Nullable
    private UpdateVehicleDTO buildUpdateVehicleDtoFromForm() {
        String model = binding.vehicleInfo.modelInput.getText().toString().trim();
        String plate = binding.vehicleInfo.licensePlateInput.getText().toString().trim();
        String seatsStr = binding.vehicleInfo.seatsInput.getText().toString().trim();
        String type = binding.vehicleInfo.vehicleTypeDropdown.getText().toString().trim();

        boolean babyTransport = binding.vehicleInfo.yesBabyTransport.isChecked();
        boolean petFriendly = binding.vehicleInfo.yesPetFriendly.isChecked();

        int seats;
        try {
            seats = Integer.parseInt(seatsStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Neispravan broj sedišta", Toast.LENGTH_SHORT).show();
            return null;
        }

        UpdateVehicleDTO dto = new UpdateVehicleDTO();
        dto.setModel(model);
        dto.setPlateNumber(plate);
        dto.setSeats(seats);
        dto.setType(type);
        dto.setBabyTransport(babyTransport);
        dto.setPetFriendly(petFriendly);

        return dto;
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
