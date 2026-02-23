package ftn.mrs_team11_gorki.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ftn.mrs_team11_gorki.service.PassengerService;

public class FavouriteRouteViewModelFactory implements ViewModelProvider.Factory {

    private final PassengerService passengerService;

    public FavouriteRouteViewModelFactory(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FavouriteRouteViewModel.class)) {
            return (T) new FavouriteRouteViewModel(passengerService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
