package ftn.mrs_team11_gorki.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.auth.ApiClient;
import ftn.mrs_team11_gorki.databinding.FragmentPassengerHomeBinding;
import ftn.mrs_team11_gorki.dto.CreateRatingDTO;
import ftn.mrs_team11_gorki.dto.CreatedRatingDTO;
import ftn.mrs_team11_gorki.dto.GetVehicleHomeDTO;
import ftn.mrs_team11_gorki.service.VehicleService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import  ftn.mrs_team11_gorki.service.RatingService;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.RatingBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AlertDialog;

import android.widget.EditText;
import ftn.mrs_team11_gorki.auth.TokenStorage;
import ftn.mrs_team11_gorki.dto.GetUserDTO;
import ftn.mrs_team11_gorki.service.UserService;


public class PassengerHomeFragment extends Fragment {
    private UserService userService;
    private Long userId;
    private boolean isBlocked = false;

    private MapView map;
    private VehicleService vehicleService;
    private final List<Marker> vehicleMarkers = new ArrayList<>();
    private FragmentPassengerHomeBinding binding;


    private static final String SP_NAME = "rating";
    private static final String SP_LAST_PROMPT_RIDE_ID = "last_prompt_ride_id";

    private RatingService ratingService;


    public PassengerHomeFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        TokenStorage ts = new TokenStorage(requireContext());
        userId = ts.getUserId();

        userService = ApiClient
                .getRetrofit(requireContext())
                .create(UserService.class);

        ratingService = ApiClient
                .getRetrofit(requireContext())
                .create(RatingService.class);

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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPassengerHomeBinding.inflate(inflater, container, false);

        // MAP init
        map = binding.osmMap;
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(false);
        map.setMaxZoomLevel(20.0);
        map.setMinZoomLevel(10.0);
        MapController controller = (MapController) map.getController();
        controller.setZoom(16.0);
        controller.setCenter(new GeoPoint(45.2671, 19.8335)); // Novi Sad default

        loadVehiclesAndShowMarkers();

        return binding.getRoot();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadPassengerBlockedInfo();
        checkPendingRatingAndShowModal();
        binding.rideOrderingView.orderYourRide.setOnClickListener(v -> {
            if (isBlocked) {
                Toast.makeText(requireContext(), "Your account is blocked.", Toast.LENGTH_SHORT).show();
                return;
            }
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.rideOrderingFragment);
        });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    //

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

    private void loadPassengerBlockedInfo() {
        if (userService == null || userId == null) return;

        userService.getUser(userId).enqueue(new Callback<GetUserDTO>() {
            @Override
            public void onResponse(@NonNull Call<GetUserDTO> call,
                                   @NonNull Response<GetUserDTO> response) {
                if (!response.isSuccessful() || response.body() == null) return;

                GetUserDTO u = response.body();

                // prilagodi ako ti je boolean getter drugačiji
                boolean blocked = Boolean.TRUE.equals(u.getBlocked());
                String role = u.getRole();

                boolean isPassenger = "PASSENGER".equals(role) || "ROLE_PASSENGER".equals(role);

                if (blocked && isPassenger) {
                    isBlocked = true;

                    // pokaži banner
                    binding.blockInfoPassenger.getRoot().setVisibility(View.VISIBLE);

                    // upiši razlog
                    EditText et = binding.blockInfoPassenger.getRoot().findViewById(R.id.etBlockReason);
                    String reason = u.getBlockReason() != null ? u.getBlockReason() : "";
                    et.setText(reason);

                    // (opciono) zabrani ordering dok je blokiran
                    binding.rideOrderingView.getRoot().setAlpha(0.6f);
                    binding.rideOrderingView.getRoot().setEnabled(false);
                    // bonus: da i klikovi na decu ne prolaze
                    binding.rideOrderingView.getRoot().setClickable(false);

                } else {
                    isBlocked = false;
                    binding.blockInfoPassenger.getRoot().setVisibility(View.GONE);

                    binding.rideOrderingView.getRoot().setAlpha(1f);
                    binding.rideOrderingView.getRoot().setEnabled(true);
                    binding.rideOrderingView.getRoot().setClickable(true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetUserDTO> call, @NonNull Throwable t) {
                // ignoriši ili toast po želji
            }
        });
    }


}
