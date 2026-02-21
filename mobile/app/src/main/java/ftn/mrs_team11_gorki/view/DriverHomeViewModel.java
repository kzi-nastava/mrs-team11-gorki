package ftn.mrs_team11_gorki.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ftn.mrs_team11_gorki.dto.GetRideDTO;
import ftn.mrs_team11_gorki.service.RideService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverHomeViewModel extends ViewModel {

    private final RideService rideService;

    private final MutableLiveData<GetRideDTO> nextRide = new MutableLiveData<>(null);
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);

    public DriverHomeViewModel(RideService rideService) {
        this.rideService = rideService;
    }

    public LiveData<GetRideDTO> getNextRide() {
        return nextRide;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void fetchNextScheduledRide(Long driverId) {
        if (driverId == null) {
            error.setValue("Missing driver id.");
            nextRide.setValue(null);
            return;
        }

        loading.setValue(true);
        error.setValue(null);

        rideService.getNextScheduledRide(driverId).enqueue(new Callback<GetRideDTO>() {
            @Override
            public void onResponse(Call<GetRideDTO> call, Response<GetRideDTO> response) {
                loading.setValue(false);

                if (response.isSuccessful()) {
                    nextRide.setValue(response.body()); // može biti null body
                    return;
                }

                if (response.code() == 404) {
                    nextRide.setValue(null);
                    return;
                }

                error.setValue("Failed to load next ride. Code: " + response.code());
                nextRide.setValue(null);
            }

            @Override
            public void onFailure(Call<GetRideDTO> call, Throwable t) {
                loading.setValue(false);
                error.setValue(t.getMessage() != null ? t.getMessage() : "Network error");
                nextRide.setValue(null);
            }
        });
    }

    public void startRide(Long rideId, Long driverId) {
        if (rideId == null || driverId == null) {
            error.setValue("Missing ride or driver id.");
            return;
        }

        loading.setValue(true);
        error.setValue(null);

        rideService.startRide(rideId).enqueue(new Callback<GetRideDTO>() {
            @Override
            public void onResponse(Call<GetRideDTO> call, Response<GetRideDTO> response) {
                loading.setValue(false);

                if (response.isSuccessful()) {
                    fetchNextScheduledRide(driverId);
                    return;
                }

                error.setValue("Failed to start ride. Code: " + response.code());
            }

            @Override
            public void onFailure(Call<GetRideDTO> call, Throwable t) {
                loading.setValue(false);
                error.setValue(t.getMessage() != null ? t.getMessage() : "Network error");
            }
        });
    }
}
