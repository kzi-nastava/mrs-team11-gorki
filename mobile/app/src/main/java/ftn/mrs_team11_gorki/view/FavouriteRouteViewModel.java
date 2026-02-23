package ftn.mrs_team11_gorki.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ftn.mrs_team11_gorki.dto.GetRouteDTO;
import ftn.mrs_team11_gorki.service.PassengerService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteRouteViewModel extends ViewModel {

    private final PassengerService passengerService;

    private final MutableLiveData<List<GetRouteDTO>> routes = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);

    public FavouriteRouteViewModel(@NonNull PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    public LiveData<List<GetRouteDTO>> getRoutes() { return routes; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }

    public void loadFavouriteRoutes(long passengerId) {
        loading.setValue(true);
        error.setValue(null);

        passengerService.getFavouriteRoutes(passengerId).enqueue(new Callback<Collection<GetRouteDTO>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<GetRouteDTO>> call,
                                   @NonNull Response<Collection<GetRouteDTO>> response) {
                loading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    routes.setValue(new ArrayList<>(response.body()));
                } else {
                    error.setValue("Failed to load favourite routes.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Collection<GetRouteDTO>> call, @NonNull Throwable t) {
                loading.setValue(false);
                error.setValue(t.getMessage() != null ? t.getMessage() : "Network error.");
            }
        });
    }

    public void deleteFavouriteRoute(long passengerId, long routeId) {
        loading.setValue(true);
        error.setValue(null);

        passengerService.deleteFavouriteRoute(passengerId, routeId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                loading.setValue(false);
                if (response.isSuccessful()) {
                    List<GetRouteDTO> current = routes.getValue();
                    if (current == null) current = new ArrayList<>();

                    List<GetRouteDTO> updated = new ArrayList<>();
                    for (GetRouteDTO r : current) {
                        if (r.getId() == null || r.getId() != routeId) updated.add(r);
                    }
                    routes.setValue(updated);
                } else {
                    error.setValue("Failed to remove route from favourites.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                loading.setValue(false);
                error.setValue(t.getMessage() != null ? t.getMessage() : "Network error.");
            }
        });
    }
}
