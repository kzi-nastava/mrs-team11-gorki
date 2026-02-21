package ftn.mrs_team11_gorki.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.auth.ApiClient;
import ftn.mrs_team11_gorki.auth.TokenStorage;
import ftn.mrs_team11_gorki.dto.GetRideDTO;
import ftn.mrs_team11_gorki.dto.GetRouteDTO;
import ftn.mrs_team11_gorki.dto.LocationDTO;
import ftn.mrs_team11_gorki.service.RideService;
import ftn.mrs_team11_gorki.view.DriverHomeViewModel;
import ftn.mrs_team11_gorki.view.DriverHomeViewModelFactory;

public class DriverHomeFragment extends Fragment {
    private MapView map;
    private Long driverId;
    private DriverHomeViewModel vm;

    private View nextScheduledRideView;
    private View noNextScheduledRideView;

    private TextView tvStartingPoint;
    private TextView tvDestination;
    private TextView tvScheduledTime;
    private TextView tvRidePrice;

    private Button btnStartRide;

    private Long currentRideId = null;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(
                requireContext().getApplicationContext(),
                requireContext().getSharedPreferences("osmdroid", 0)
        );
        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_driver_home, container, false);

        // MAP init
        map = root.findViewById(R.id.osmMap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(false);
        map.setMaxZoomLevel(20.0);
        map.setMinZoomLevel(10.0);
        MapController controller = (MapController) map.getController();
        controller.setZoom(16.0);
        controller.setCenter(new GeoPoint(45.2671, 19.8335)); // Novi Sad default


        nextScheduledRideView = root.findViewById(R.id.nextScheduledRideView);
        noNextScheduledRideView = root.findViewById(R.id.noNextScheduledRideView);

        tvStartingPoint = root.findViewById(R.id.startingPointAddress);
        tvDestination = root.findViewById(R.id.destinationAddress);
        tvScheduledTime = root.findViewById(R.id.scheduledTime);
        tvRidePrice = root.findViewById(R.id.ridePrice);

        btnStartRide = root.findViewById(R.id.btnStartRide);

        RideService rideService = ApiClient.getRetrofit(requireContext()).create(RideService.class);
        DriverHomeViewModelFactory factory = new DriverHomeViewModelFactory(rideService);
        vm = new ViewModelProvider(this, factory).get(DriverHomeViewModel.class);

        bindObservers();

        TokenStorage ts = new TokenStorage(requireContext());
        driverId = ts.getUserId();
        vm.fetchNextScheduledRide(driverId);

        btnStartRide.setOnClickListener(v -> {
            if (currentRideId == null) {
                Toast.makeText(requireContext(), "No ride to start.", Toast.LENGTH_SHORT).show();
                return;
            }
            vm.startRide(currentRideId, driverId);
            Toast.makeText(requireContext(), "Ride started successfully!", Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map != null) map.onResume();
    }

    @Override
    public void onPause() {
        if (map != null) map.onPause();
        super.onPause();
    }

    private void bindObservers() {
        vm.getNextRide().observe(getViewLifecycleOwner(), ride -> {
            currentRideId = (ride != null) ? ride.getId() : null;

            if (ride == null) showNoRide();
            else showRide(ride);
        });

        vm.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null && !err.isEmpty()) {
                Toast.makeText(requireContext(), err, Toast.LENGTH_SHORT).show();
            }
        });

        vm.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (btnStartRide != null) btnStartRide.setEnabled(isLoading == null || !isLoading);
        });
    }

    private void showNoRide() {
        if (nextScheduledRideView != null) nextScheduledRideView.setVisibility(View.INVISIBLE);
        if (noNextScheduledRideView != null) noNextScheduledRideView.setVisibility(View.VISIBLE);
    }

    private void showRide(@NonNull GetRideDTO ride) {
        if (noNextScheduledRideView != null) noNextScheduledRideView.setVisibility(View.INVISIBLE);
        if (nextScheduledRideView != null) nextScheduledRideView.setVisibility(View.VISIBLE);

        tvStartingPoint.setText(extractStartAddress(ride));
        tvDestination.setText(extractEndAddress(ride));

        LocalDateTime scheduled = ride.getScheduledTime();
        tvScheduledTime.setText(scheduled != null ? scheduled.format(dtf) : "-");

        Double price = ride.getPrice();
        tvRidePrice.setText(price != null ? String.format("%.2f", price) : "-");

    }

    private String extractStartAddress(GetRideDTO ride) {
        GetRouteDTO route = ride.getRoute();
        if (route == null) return "-";

        List<LocationDTO> locs = route.getLocations();
        if (locs == null || locs.isEmpty()) return "-";

        LocationDTO first = locs.get(0);
        if (first == null) return "-";

        String addr = first.getAddress();
        return (addr == null || addr.isEmpty()) ? "-" : addr;
    }

    private String extractEndAddress(GetRideDTO ride) {
        GetRouteDTO route = ride.getRoute();
        if (route == null) return "-";

        List<LocationDTO> locs = route.getLocations();
        if (locs == null || locs.isEmpty()) return "-";

        LocationDTO last = locs.get(locs.size() - 1);
        if (last == null) return "-";

        String addr = last.getAddress();
        return (addr == null || addr.isEmpty()) ? "-" : addr;
    }
}
