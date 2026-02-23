package ftn.mrs_team11_gorki.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ftn.mrs_team11_gorki.service.NominatimService;
import ftn.mrs_team11_gorki.service.RideService;
import retrofit2.Retrofit;

public class RideOrderingViewModelFactory implements ViewModelProvider.Factory {

    private final RideService rideService;
    private final NominatimService nominatimService;

    public RideOrderingViewModelFactory(RideService rideService, NominatimService nominatimService) {
        this.rideService = rideService;
        this.nominatimService = nominatimService;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RideOrderingViewModel.class)) {
            return (T) new RideOrderingViewModel(rideService, nominatimService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
