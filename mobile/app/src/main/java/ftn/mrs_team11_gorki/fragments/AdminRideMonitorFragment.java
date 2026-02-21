package ftn.mrs_team11_gorki.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;

import ftn.mrs_team11_gorki.R;

// Retrofit
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import ftn.mrs_team11_gorki.service.AdminService;
import ftn.mrs_team11_gorki.auth.ApiClient;

import ftn.mrs_team11_gorki.dto.GetDriverInfoDTO;
import ftn.mrs_team11_gorki.dto.AdminRideMonitorDTO;


public class AdminRideMonitorFragment extends Fragment {

    private TextInputEditText etQuery;
    private MaterialButton btnFindDriver, btnFindActiveRide;
    private LinearProgressIndicator progress;

    private MaterialCardView cardDriver, cardRide, cardRoute;

    private TextView tvDriverName, tvDriverEmail, tvDriverVehicle, tvDriverActivity;
    private Chip chipStatus;
    private TextView tvTimes, tvPrice, tvPanic;
    private TextView tvStart, tvEnd, tvDistanceEta, tvCurrentLocation;

    private AdminService adminApi;
    private GetDriverInfoDTO foundDriver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_ride_monitor, container, false);

        etQuery = v.findViewById(R.id.etQuery);
        btnFindDriver = v.findViewById(R.id.btnFindDriver);
        btnFindActiveRide = v.findViewById(R.id.btnFindActiveRide);
        progress = v.findViewById(R.id.progress);

        cardDriver = v.findViewById(R.id.cardDriver);
        cardRide = v.findViewById(R.id.cardRide);
        cardRoute = v.findViewById(R.id.cardRoute);

        tvDriverName = v.findViewById(R.id.tvDriverName);
        tvDriverEmail = v.findViewById(R.id.tvDriverEmail);
        tvDriverVehicle = v.findViewById(R.id.tvDriverVehicle);
        tvDriverActivity = v.findViewById(R.id.tvDriverActivity);

        chipStatus = v.findViewById(R.id.chipStatus);
        tvTimes = v.findViewById(R.id.tvTimes);
        tvPrice = v.findViewById(R.id.tvPrice);
        tvPanic = v.findViewById(R.id.tvPanic);

        tvStart = v.findViewById(R.id.tvStart);
        tvEnd = v.findViewById(R.id.tvEnd);
        tvDistanceEta = v.findViewById(R.id.tvDistanceEta);
        tvCurrentLocation = v.findViewById(R.id.tvCurrentLocation);

        btnFindActiveRide.setEnabled(false);

        adminApi = ApiClient.getRetrofit(requireContext()).create(AdminService.class);

        btnFindDriver.setOnClickListener(x -> findDriver());
        btnFindActiveRide.setOnClickListener(x -> findActiveRide());

        return v;
    }

    private void setLoading(boolean loading) {
        progress.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnFindDriver.setEnabled(!loading);
        btnFindActiveRide.setEnabled(!loading && foundDriver != null);
    }

    private void resetRideCards() {
        cardRide.setVisibility(View.GONE);
        cardRoute.setVisibility(View.GONE);
    }

    private void findDriver() {
        String q = etQuery.getText() != null ? etQuery.getText().toString().trim() : "";
        if (q.isEmpty()) {
            etQuery.setError("Enter full name");
            return;
        }

        setLoading(true);
        resetRideCards();
        cardDriver.setVisibility(View.GONE);
        foundDriver = null;

        adminApi.searchDriver(q).enqueue(new Callback<GetDriverInfoDTO>() {
            @Override
            public void onResponse(@NonNull Call<GetDriverInfoDTO> call, @NonNull Response<GetDriverInfoDTO> response) {
                setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    foundDriver = response.body();
                    bindDriver(foundDriver);
                    btnFindActiveRide.setEnabled(true);
                } else if (response.code() == 404) {
                    toast("Driver not found");
                } else {
                    toast("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetDriverInfoDTO> call, @NonNull Throwable t) {
                setLoading(false);
                toast("Network error: " + t.getMessage());
            }
        });
    }

    private void findActiveRide() {
        if (foundDriver == null || foundDriver.user == null) return;

        long driverId = foundDriver.user.getId();

        setLoading(true);
        resetRideCards();

        adminApi.getActiveRide(driverId).enqueue(new Callback<AdminRideMonitorDTO>() {
            @Override
            public void onResponse(@NonNull Call<AdminRideMonitorDTO> call, @NonNull Response<AdminRideMonitorDTO> response) {
                setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    bindMonitor(response.body());
                } else if (response.code() == 404) {
                    toast("Driver has no active ride.");
                } else {
                    toast("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AdminRideMonitorDTO> call, @NonNull Throwable t) {
                setLoading(false);
                toast("Network error: " + t.getMessage());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void bindDriver(GetDriverInfoDTO d) {
        cardDriver.setVisibility(View.VISIBLE);

        String fullName = safe(d.user.getFirstName()) + " " + safe(d.user.getLastName());
        tvDriverName.setText(fullName.trim());
        tvDriverEmail.setText(safe(d.user.getEmail()));

        String vehicleText = "Vehicle: -";
        if (d.vehicle != null) {
            vehicleText = "Vehicle: " + safe(d.vehicle.getType()) + " | " + safe(d.vehicle.getModel());
        }
        tvDriverVehicle.setText(vehicleText);

        double activity = d.activityLast24h;
        tvDriverActivity.setText("Activity (last 24h): " + activity);
    }

    @SuppressLint("SetTextI18n")
    private void bindMonitor(AdminRideMonitorDTO dto) {
        if (dto.driver != null) bindDriver(dto.driver);

        if (dto.ride != null) {
            cardRide.setVisibility(View.VISIBLE);
            chipStatus.setText(safe(dto.ride.getStatus()));

            tvTimes.setText(
                    "Scheduled: " + safe(dto.ride.getScheduledTime().toString()) +
                            "\nStart: " + safe(dto.ride.getStartingTime().toString()) +
                            "\nEnd: " + safe(dto.ride.getEndingTime().toString())
            );

            tvPrice.setText("Price: " + (dto.ride.getPrice() != null ? dto.ride.getPrice() : 0));
            tvPanic.setText("Panic: " + (dto.ride.getPanicActivated() != null && dto.ride.getPanicActivated() ? "YES" : "NO"));

            // route
            if (dto.ride.getRoute() != null) {
                cardRoute.setVisibility(View.VISIBLE);

                String startAddr = "-";
                String endAddr = "-";
                if (dto.ride.getRoute().getLocations() != null && !dto.ride.getRoute().getLocations().isEmpty()) {
                    startAddr = safe(dto.ride.getRoute().getLocations().get(0).getAddress());
                    endAddr = safe(dto.ride.getRoute().getLocations().get(dto.ride.getRoute().getLocations().size() - 1).getAddress());
                }

                tvStart.setText("Start: " + startAddr);
                tvEnd.setText("End: " + endAddr);

                double dist = dto.ride.getRoute().getDistance();
                tvDistanceEta.setText("Distance: " + dist + " km" + "\nETA: ");
            }
        }

        if (dto.currentLocation != null) {
            tvCurrentLocation.setText("Current location: " + safe(dto.currentLocation.getAddress()));
            // ako trebaju markeri na mapi
        } else {
            tvCurrentLocation.setText("Current location: -");
        }
    }

    private String safe(String s) {
        return s == null ? "-" : s;
    }

    private void toast(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
