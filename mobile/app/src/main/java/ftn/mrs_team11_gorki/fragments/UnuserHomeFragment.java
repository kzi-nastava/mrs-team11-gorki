package ftn.mrs_team11_gorki.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.dto.GetVehicleHomeDTO;
import ftn.mrs_team11_gorki.service.VehicleService;
import ftn.mrs_team11_gorki.auth.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnuserHomeFragment extends Fragment {

    private MapView map;

    private VehicleService vehicleService;
    private final List<Marker> vehicleMarkers = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(
                requireContext().getApplicationContext(),
                requireContext().getSharedPreferences("osmdroid", 0)
        );
        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());


        vehicleService = ApiClient
                .getPublicRetrofit()
                .create(VehicleService.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_unuser_home, container, false);

        // MAP init
        map = v.findViewById(R.id.osmMap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(false);
        map.setMaxZoomLevel(20.0);
        map.setMinZoomLevel(10.0);
        MapController controller = (MapController) map.getController();
        controller.setZoom(16.0);
        controller.setCenter(new GeoPoint(45.2671, 19.8335)); // Novi Sad default

        // FORM
        EditText startEt = v.findViewById(R.id.startingAddress);
        EditText destEt = v.findViewById(R.id.destinationAddress);
        Button estimateBtn = v.findViewById(R.id.btnEstimate);

        estimateBtn.setOnClickListener(view -> {
            String start = startEt.getText().toString().trim();
            String dest = destEt.getText().toString().trim();

            if (start.isEmpty() || dest.isEmpty()) {
                Toast.makeText(requireContext(), "Enter both addresses.", Toast.LENGTH_SHORT).show();
                return;
            }

            // kasnije: geocode + route + polyline
            Toast.makeText(requireContext(), "Estimating route...", Toast.LENGTH_SHORT).show();
        });

        // UCITAVANJE SVIH VOZILA
        loadVehiclesAndShowMarkers();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map != null) map.onResume();
        loadVehiclesAndShowMarkers();
    }

    @Override
    public void onPause() {
        if (map != null) map.onPause();
        super.onPause();
    }

    // -----------------------------
    // VEHICLES -> MARKERS
    // -----------------------------

    private void loadVehiclesAndShowMarkers() {
        if (vehicleService == null || map == null) return;

        vehicleService.getHomeVehicles().enqueue(new Callback<List<GetVehicleHomeDTO>>() {
            @Override
            public void onResponse(Call<List<GetVehicleHomeDTO>> call, Response<List<GetVehicleHomeDTO>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(requireContext(), "Failed to load vehicles: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                showVehicleMarkers(response.body());
            }

            @Override
            public void onFailure(Call<List<GetVehicleHomeDTO>> call, Throwable t) {
                Toast.makeText(requireContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private Drawable getScaledDrawable(int drawableResId, int widthDp, int heightDp) {

        @SuppressLint("UseCompatLoadingForDrawables") Drawable d = requireContext().getDrawable(drawableResId);
        if (d == null) return null;

        int widthPx = dpToPx(widthDp);
        int heightPx = dpToPx(heightDp);

        Bitmap bitmap = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        d.setBounds(0, 0, widthPx, heightPx);
        d.draw(canvas);

        return new BitmapDrawable(getResources(), bitmap);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showVehicleMarkers(List<GetVehicleHomeDTO> vehicles) {
        // ukloni stare markere da se ne dupliraju
        for (Marker m : vehicleMarkers) {
            map.getOverlays().remove(m);
        }
        vehicleMarkers.clear();

        for (GetVehicleHomeDTO v : vehicles) {

            double lat = v.getCurrentLocation().getLatitude();
            double lon = v.getCurrentLocation().getLongitude();
            String status = safeUpper(v.getVehicleAvailability().toString());

            if ((lat == 0.0 && lon == 0.0) || lat < -90 || lat > 90 || lon < -180 || lon > 180) {
                continue;
            }

            Marker marker = new Marker(map);
            marker.setPosition(new GeoPoint(lat, lon));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

            // status -> ikonica
            if ("BUSY".equals(status)) {
                marker.setIcon(getScaledDrawable(R.drawable.unavailable, 38, 38));
                marker.setTitle("Busy vehicle");
            } else {
                marker.setIcon(getScaledDrawable(R.drawable.available, 38, 38));
                marker.setTitle("Available vehicle");
            }

            // marker.setSnippet("Lat: " + lat + ", Lon: " + lon);

            map.getOverlays().add(marker);
            vehicleMarkers.add(marker);
        }

        map.invalidate();
    }

    private String safeUpper(String s) {
        return s == null ? "" : s.trim().toUpperCase();
    }
}
