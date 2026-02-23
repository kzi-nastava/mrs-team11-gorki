package ftn.mrs_team11_gorki.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ftn.mrs_team11_gorki.service.RideService;

public class DriverHomeViewModelFactory implements ViewModelProvider.Factory {

    private final RideService rideService;

    public DriverHomeViewModelFactory(RideService rideService) {
        this.rideService = rideService;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DriverHomeViewModel.class)) {
            return (T) new DriverHomeViewModel(rideService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
