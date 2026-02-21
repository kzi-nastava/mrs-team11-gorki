package ftn.mrs_team11_gorki.view; // ili viewmodel

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ftn.mrs_team11_gorki.dto.GetVehicleDTO;
import ftn.mrs_team11_gorki.dto.UpdateVehicleDTO;
import ftn.mrs_team11_gorki.dto.UpdatedVehicleDTO;
import ftn.mrs_team11_gorki.service.DriverService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleInfoViewModel extends ViewModel {

    private final DriverService driverService;
    private final Long driverId;

    private final MutableLiveData<GetVehicleDTO> vehicle = new MutableLiveData<>();
    private final MutableLiveData<GetVehicleDTO> originalVehicle = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> message = new MutableLiveData<>();

    public VehicleInfoViewModel(DriverService driverService, Long driverId) {
        this.driverService = driverService;
        this.driverId = driverId;
    }

    public LiveData<GetVehicleDTO> getVehicle() { return vehicle; }
    public LiveData<Boolean> isLoading() { return loading; }
    public LiveData<String> getMessage() { return message; }

    public void loadVehicle() {
        loading.setValue(true);

        driverService.getVehicle(driverId).enqueue(new Callback<GetVehicleDTO>() {
            @Override
            public void onResponse(@NonNull Call<GetVehicleDTO> call,
                                   @NonNull Response<GetVehicleDTO> response) {
                loading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    vehicle.setValue(response.body());
                    originalVehicle.setValue(response.body());
                } else {
                    message.setValue("Ne mogu da učitam vozilo (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetVehicleDTO> call, @NonNull Throwable t) {
                loading.setValue(false);
                message.setValue("Greška: " + t.getMessage());
            }
        });
    }

    public void updateVehicle(UpdateVehicleDTO dto) {
        loading.setValue(true);

        driverService.updateVehicle(driverId, dto).enqueue(new Callback<UpdatedVehicleDTO>() {
            @Override
            public void onResponse(@NonNull Call<UpdatedVehicleDTO> call,
                                   @NonNull Response<UpdatedVehicleDTO> response) {
                loading.setValue(false);

                if (response.isSuccessful()) {
                    message.setValue("Sačuvano");
                    loadVehicle(); // refresh + update snapshot
                } else {
                    message.setValue("Neuspešan update (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpdatedVehicleDTO> call, @NonNull Throwable t) {
                loading.setValue(false);
                message.setValue("Greška: " + t.getMessage());
            }
        });
    }

    public void revert() {
        GetVehicleDTO orig = originalVehicle.getValue();
        if (orig != null) {
            vehicle.setValue(orig);
        }
    }

    public void clearMessage() {
        message.setValue(null);
    }
}
