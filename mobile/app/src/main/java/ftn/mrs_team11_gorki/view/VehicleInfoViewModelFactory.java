package ftn.mrs_team11_gorki.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ftn.mrs_team11_gorki.service.DriverService;

public class VehicleInfoViewModelFactory implements ViewModelProvider.Factory {

    private final DriverService driverService;
    private final Long driverId;

    public VehicleInfoViewModelFactory(DriverService driverService, Long driverId) {
        this.driverService = driverService;
        this.driverId = driverId;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(VehicleInfoViewModel.class)) {
            return (T) new VehicleInfoViewModel(driverService, driverId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
