package ftn.mrs_team11_gorki.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import ftn.mrs_team11_gorki.R;
import androidx.appcompat.app.AlertDialog;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import ftn.mrs_team11_gorki.auth.ApiClient;
import ftn.mrs_team11_gorki.dto.GetVehicleHomeDTO;
import ftn.mrs_team11_gorki.service.NominatimService;
import ftn.mrs_team11_gorki.service.OsrmService;
import ftn.mrs_team11_gorki.service.RatingService;
import ftn.mrs_team11_gorki.dto.CreateRatingDTO;
import ftn.mrs_team11_gorki.dto.CreatedRatingDTO;

import ftn.mrs_team11_gorki.service.VehicleService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private MapView map;
    private NominatimService nominatimService;
    private OsrmService osrmService;
    private VehicleService vehicleService;
    private final List<Marker> vehicleMarkers = new ArrayList<>();

    private interface Done { void run(); }

    /*
    //================== OVO ===================
    private static final String SP_NAME = "rating";
    private static final String SP_LAST_PROMPT_RIDE_ID = "last_prompt_ride_id";

    private RatingService ratingService;
    //================== OVO ===================*/



    public HomeFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        //================== OVO ===================
        ratingService = ApiClient
                .getRetrofit(requireContext())
                .create(RatingService.class);
        //================== OVO ===================*/

        Configuration.getInstance().load(
                requireContext().getApplicationContext(),
                requireContext().getSharedPreferences("osmdroid", 0)
        );
        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());


        vehicleService = ApiClient
                .getPublicRetrofit()
                .create(VehicleService.class);

        OkHttpClient http = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request r = chain.request().newBuilder()
                            // bitno za Nominatim usage policy :contentReference[oaicite:4]{index=4}
                            .header("User-Agent", requireContext().getPackageName() + " (student-project)")
                            .build();
                    return chain.proceed(r);
                })
                .build();

        Retrofit nominatimRetrofit = new Retrofit.Builder()
                .baseUrl("https://nominatim.openstreetmap.org/")
                .client(http)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        nominatimService = nominatimRetrofit.create(NominatimService.class);

        Retrofit osrmRetrofit = new Retrofit.Builder()
                .baseUrl("https://router.project-osrm.org/")
                .client(http)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        osrmService = osrmRetrofit.create(OsrmService.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_home, container, false);
        map = v.findViewById(R.id.osmMap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(false);
        map.setMaxZoomLevel(20.0);
        map.setMinZoomLevel(10.0);
        MapController controller = (MapController) map.getController();
        controller.setZoom(16.0);
        controller.setCenter(new GeoPoint(45.2671, 19.8335));
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



    /*ODAVDE DO KRAJA
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //================== OVO ===================
        checkPendingRatingAndShowModal();
        //================== OVO ===================
    }



    //================== OVO ===================
    private void checkPendingRatingAndShowModal() {
        ratingService.getPendingLatestRideId().enqueue(new Callback<Long>() {
            @Override
            public void onResponse(@NonNull Call<Long> call, @NonNull Response<Long> response) {

                Toast.makeText(requireContext(),
                        "pending-latest code=" + response.code(),
                        Toast.LENGTH_SHORT).show();

                if (!response.isSuccessful()) {
                    // 401/403 -> token / auth problem
                    return;
                }

                Long rideId = response.body();

                Toast.makeText(requireContext(),
                        "pending-latest rideId=" + rideId,
                        Toast.LENGTH_SHORT).show();

                if (rideId == null) return;

                if (alreadyShownForRide(rideId)) {
                    Toast.makeText(requireContext(),
                            "Already shown for rideId=" + rideId,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                markShownForRide(rideId);
                showRatePromptDialog(rideId);
            }

            @Override
            public void onFailure(@NonNull Call<Long> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(),
                        "pending-latest FAIL: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showRatePromptDialog(long rideId) {
        if (!isAdded()) return;

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Ride finished")
                .setMessage("Your ride has ended. Would you like to rate the driver and vehicle?")
                .setNegativeButton("Later", (d, w) -> d.dismiss())
                .setPositiveButton("Rate", (d, w) -> openRateDialog(rideId))
                .show();
    }

    private void openRateDialog(long rideId) {
        if (!isAdded()) return;

        View v = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_rate_ride, null);

        RatingBar rbDriver = v.findViewById(R.id.rbDriver);
        RatingBar rbVehicle = v.findViewById(R.id.rbVehicle);
        TextInputEditText etComment = v.findViewById(R.id.etComment);

        rbDriver.setRating(5f);
        rbVehicle.setRating(5f);

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Rate ride")
                .setView(v)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Send", null)
                .create();

        dialog.setOnShowListener(di -> {
            Button btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btn.setOnClickListener(x -> {
                String comment = (etComment.getText() != null) ? etComment.getText().toString().trim() : "";
                double driver = rbDriver.getRating();
                double vehicle = rbVehicle.getRating();

                if (comment.isEmpty()) {
                    etComment.setError("Comment is required");
                    return;
                }
                if (driver < 1 || vehicle < 1) {
                    toast("Rating must be from 1 to 5");
                    return;
                }

                CreateRatingDTO dto = new CreateRatingDTO(driver, vehicle, comment);

                ratingService.rateRide(rideId, dto).enqueue(new Callback<CreatedRatingDTO>() {
                    @Override
                    public void onResponse(@NonNull Call<CreatedRatingDTO> call,
                                           @NonNull Response<CreatedRatingDTO> response) {
                        if (response.isSuccessful()) {
                            dialog.dismiss();
                            toast("Thanks! Rating submitted.");
                        } else {
                            toast("Could not submit rating (" + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CreatedRatingDTO> call, @NonNull Throwable t) {
                        toast("Network error");
                    }
                });
            });
        });

        dialog.show();
    }


    private boolean alreadyShownForRide(long rideId) {
        SharedPreferences sp = requireContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getLong(SP_LAST_PROMPT_RIDE_ID, -1L) == rideId;
    }

    private void markShownForRide(long rideId) {
        SharedPreferences sp = requireContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putLong(SP_LAST_PROMPT_RIDE_ID, rideId).apply();
    }

    private void toast(String msg) {
        if (!isAdded()) return;
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }
    //================== OVO ===================*/
}