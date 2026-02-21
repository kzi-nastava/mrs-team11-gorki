package ftn.mrs_team11_gorki.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import ftn.mrs_team11_gorki.dto.CreateRideDTO;
import ftn.mrs_team11_gorki.dto.CreateRouteDTO;
import ftn.mrs_team11_gorki.dto.CreatedRideDTO;
import ftn.mrs_team11_gorki.dto.LocationDTO;
import ftn.mrs_team11_gorki.dto.NominatimPlaceDTO;
import ftn.mrs_team11_gorki.service.NominatimService;
import ftn.mrs_team11_gorki.service.RideService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideOrderingViewModel extends ViewModel {

    private final RideService rideService;
    private final NominatimService nominatimService;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> message = new MutableLiveData<>();
    private final MutableLiveData<CreatedRideDTO> createdRide = new MutableLiveData<>();

    public RideOrderingViewModel(RideService rideService, NominatimService nominatimService) {
        this.rideService = rideService;
        this.nominatimService = nominatimService;
    }

    public LiveData<Boolean> isLoading() { return loading; }
    public LiveData<String> getMessage() { return message; }
    public LiveData<CreatedRideDTO> getCreatedRide() { return createdRide; }

    public void clearMessage() { message.setValue(null); }

    public void orderRide(
            @NonNull Long creatorId,
            @NonNull String startAddress,
            @NonNull String endAddress,
            @NonNull List<String> stoppingPoints,
            @NonNull List<String> linkedPassengersEmails,
            @NonNull String scheduledTimeText, // "2026-02-28T18:15"
            @NonNull String vehicleType,
            boolean babyTransport,
            boolean petFriendly
    ) {
        // ---- basic validation ----
        final String start = startAddress.trim();
        final String end = endAddress.trim();
        final String scheduledText = scheduledTimeText.trim();
        final String vehicle = vehicleType.trim();

        if (start.isEmpty()) { message.setValue("Unesi početnu adresu."); return; }
        if (end.isEmpty()) { message.setValue("Unesi krajnju adresu."); return; }
        if (vehicle.isEmpty()) { message.setValue("Izaberi tip vozila."); return; }

        final String scheduledTime;
        if(scheduledText.isEmpty()){
            scheduledTime = null;
        } else {
            try {
                scheduledTime = LocalDateTime.parse(scheduledText, DateTimeFormatter.ISO_LOCAL_DATE_TIME).toString();
            } catch (DateTimeParseException e) {
                message.setValue("Pogrešan format vremena. Primer: 2026-02-28T18:15");
                return;
            }
        }

        // ---- build ordered address list: start + stops + end ----
        List<String> allAddresses = new ArrayList<>();
        allAddresses.add(start);

        for (String sp : stoppingPoints) {
            if (sp != null && !sp.trim().isEmpty()) allAddresses.add(sp.trim());
        }

        allAddresses.add(end);

        // ---- clean linked passengers ----
        List<String> emails = new ArrayList<>();
        for (String em : linkedPassengersEmails) {
            if (em != null && !em.trim().isEmpty()) emails.add(em.trim());
        }

        loading.setValue(true);

        // ---- geocode sequentially, then POST ride ----
        geocodeSequential(allAddresses, 0, new ArrayList<>(), new LocationsCallback() {
            @Override
            public void onSuccess(List<LocationDTO> locations) {
                CreateRouteDTO routeDTO = new CreateRouteDTO(locations);

                CreateRideDTO rideDTO = new CreateRideDTO(
                        scheduledTime,
                        routeDTO,
                        emails,
                        creatorId,
                        babyTransport,
                        petFriendly,
                        vehicle
                );

                rideService.createRide(rideDTO).enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<CreatedRideDTO> call,
                                           @NonNull Response<CreatedRideDTO> response) {
                        loading.setValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            createdRide.setValue(response.body());
                            message.setValue("Voznja kreirana.");
                        } else {
                            message.setValue("Neuspešno kreiranje vožnje (" + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CreatedRideDTO> call, @NonNull Throwable t) {
                        loading.setValue(false);
                        message.setValue("Greška: " + t.getMessage());
                        t.printStackTrace();
                    }
                });
            }

            @Override
            public void onError(String err) {
                loading.setValue(false);
                message.setValue(err);
            }
        });
    }

    // ----------------- geocoding helpers -----------------

    private void geocodeSequential(
            List<String> addresses,
            int idx,
            List<LocationDTO> out,
            LocationsCallback cb
    ) {
        if (idx >= addresses.size()) {
            cb.onSuccess(out);
            return;
        }

        String address = addresses.get(idx);

        nominatimService.search(address, "json", 1, 0, "rs")
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<NominatimPlaceDTO>> call,
                                           @NonNull Response<List<NominatimPlaceDTO>> response) {

                        if (!response.isSuccessful() || response.body() == null || response.body().isEmpty()) {
                            cb.onError("Ne mogu da pronađem koordinate za: " + address);
                            return;
                        }

                        NominatimPlaceDTO place = response.body().get(0);

                        double lat, lon;
                        try {
                            lat = Double.parseDouble(place.getLat());
                            lon = Double.parseDouble(place.getLon());
                        } catch (Exception e) {
                            cb.onError("Nominatim vratio loše koordinate za: " + address);
                            return;
                        }

                        out.add(new LocationDTO(lat, lon, address));
                        geocodeSequential(addresses, idx + 1, out, cb);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<NominatimPlaceDTO>> call, @NonNull Throwable t) {
                        cb.onError("Greška pri geokodiranju: " + t.getMessage());
                    }
                });
    }

    private interface LocationsCallback {
        void onSuccess(List<LocationDTO> locations);
        void onError(String err);
    }
}
