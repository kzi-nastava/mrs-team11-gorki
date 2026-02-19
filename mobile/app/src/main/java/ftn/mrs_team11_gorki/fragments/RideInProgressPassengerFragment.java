package ftn.mrs_team11_gorki.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.auth.ApiClient;
import ftn.mrs_team11_gorki.auth.TokenStorage;
import ftn.mrs_team11_gorki.dto.DriverRideHistoryDTO;
import ftn.mrs_team11_gorki.dto.InconsistencyRequestDTO;
import ftn.mrs_team11_gorki.dto.LocationDTO;
import ftn.mrs_team11_gorki.dto.OsrmRouteResponse;
import ftn.mrs_team11_gorki.service.OsrmService;
import ftn.mrs_team11_gorki.service.PassengerService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RideInProgressPassengerFragment extends Fragment {

    // ====== UI ======
    private MapView map;
    private View bottomPanel;

    private TextView tvStart, tvEnd, tvEta;
    private MaterialButton btnInconsistency, btnPanic;

    // ====== API ======
    private PassengerService passengerApi;
    private OsrmService osrmService;

    // ====== MAP OVERLAYS ======
    private Polyline routeLine;
    private Marker carMarker;
    private Marker startMarker;
    private Marker endMarker;

    // ====== DATA ======
    private Long currentRideId = null;

    private final List<GeoPoint> routePoints = new ArrayList<>();

    private List<GeoPoint> navigationPoints = new ArrayList<>();

    private final Handler simHandler = new Handler(Looper.getMainLooper());
    private Runnable simRunnable;
    private boolean simRunning = false;
    private int simIndex = 0;

    private static final long SIM_TICK_MS = 1100;
    private static final int CAR_ICON_W_DP = 40;
    private static final int CAR_ICON_H_DP = 40;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(
                requireContext().getApplicationContext(),
                requireContext().getSharedPreferences("osmdroid", 0)
        );
        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());

        OkHttpClient http = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request r = chain.request().newBuilder()
                            .header("User-Agent", requireContext().getPackageName() + " (student-project)")
                            .build();
                    return chain.proceed(r);
                })
                .build();

        Retrofit osrmRetrofit = new Retrofit.Builder()
                .baseUrl("https://router.project-osrm.org/")
                .client(http)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        osrmService = osrmRetrofit.create(OsrmService.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ride_in_progress_passenger, container, false);

        map = v.findViewById(R.id.map);
        bottomPanel = v.findViewById(R.id.bottomPanel);

        tvStart = v.findViewById(R.id.tvStart);
        tvEnd = v.findViewById(R.id.tvEnd);
        tvEta = v.findViewById(R.id.tvEta);

        btnInconsistency = v.findViewById(R.id.btnInconsistency);
        btnPanic = v.findViewById(R.id.btnPanic);

        // Auth retrofit
        passengerApi = ApiClient.getRetrofit(requireContext()).create(PassengerService.class);

        setupMapPadding();
        setupMap();

        btnInconsistency.setOnClickListener(x -> openInconsistencyDialog());
        btnPanic.setOnClickListener(x -> toast("Panic not implemented."));

        loadActiveRide();

        return v;
    }

    private void setupMapPadding() {
        if (map == null || bottomPanel == null) return;
        bottomPanel.post(() -> {
            if (map != null) map.setPadding(0, 0, 0, bottomPanel.getHeight());
        });
    }

    private void setupMap() {
        if (map == null) return;

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(false);
        map.setMaxZoomLevel(20.0);
        map.setMinZoomLevel(10.0);

        map.getController().setZoom(16.0);
        map.getController().setCenter(new GeoPoint(45.2671, 19.8335)); // Novi Sad default
        map.invalidate();
    }

    private long getPassengerId() {
        TokenStorage ts = new TokenStorage(requireContext());
        return ts.getUserId();
    }

    private void loadActiveRide() {
        long passengerId = getPassengerId();

        passengerApi.getActiveRide(passengerId).enqueue(new Callback<DriverRideHistoryDTO>() {
            @Override
            public void onResponse(@NonNull Call<DriverRideHistoryDTO> call,
                                   @NonNull Response<DriverRideHistoryDTO> res) {
                if (!isAdded() || getView() == null) return;

                if (!res.isSuccessful() || res.body() == null) {
                    toast("No active ride to show.");
                    return;
                }

                bindRide(res.body());
            }

            @Override
            public void onFailure(@NonNull Call<DriverRideHistoryDTO> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                android.util.Log.e("TRACK", "Failure", t);
                toast("Network error: " + t.getMessage());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void bindRide(DriverRideHistoryDTO dto) {
        if (!isAdded() || map == null) return;

        currentRideId = dto.getRideId();

        routePoints.clear();
        navigationPoints = new ArrayList<>();

        List<LocationDTO> locs = (dto.getRoute() != null) ? dto.getRoute().getLocations() : null;
        if (locs != null && !locs.isEmpty()) {
            for (LocationDTO l : locs) {
                routePoints.add(new GeoPoint(l.getLatitude(), l.getLongitude()));
            }
        }

        String startAddr = (locs != null && !locs.isEmpty()) ? safe(locs.get(0).getAddress()) : "-";
        String endAddr = (locs != null && !locs.isEmpty()) ? safe(locs.get(locs.size() - 1).getAddress()) : "-";

        tvStart.setText("Start: " + startAddr);
        tvEnd.setText("End: " + endAddr);
        tvEta.setText("ETA: ...");

        if (routePoints.size() >= 2) {
            GeoPoint start = routePoints.get(0);
            GeoPoint end = routePoints.get(routePoints.size() - 1);
            fetchStreetRouteAndDraw(start, end);
        } else {
            drawRoute(routePoints);
            GeoPoint carPos = routePoints.isEmpty()
                    ? new GeoPoint(45.2671, 19.8335)
                    : routePoints.get(0);
            placeOrMoveCar(carPos, true);
            startSimulatedDrive();
        }
    }

    private void fetchStreetRouteAndDraw(GeoPoint start, GeoPoint end) {
        if (!isAdded() || map == null) return;

        if (osrmService == null) {
            navigationPoints = new ArrayList<>(routePoints);
            drawRoute(navigationPoints);
            placeOrMoveCar(start, true);
            startSimulatedDrive();
            return;
        }

        String coords = start.getLongitude() + "," + start.getLatitude()
                + ";" + end.getLongitude() + "," + end.getLatitude();

        osrmService.route(coords, "full", "polyline6")
                .enqueue(new Callback<OsrmRouteResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<OsrmRouteResponse> call,
                                           @NonNull Response<OsrmRouteResponse> res) {
                        if (!isAdded() || map == null) return;

                        if (!res.isSuccessful() || res.body() == null
                                || res.body().routes == null || res.body().routes.isEmpty()
                                || res.body().routes.get(0) == null
                                || res.body().routes.get(0).geometry == null) {

                            // fallback
                            navigationPoints = new ArrayList<>(routePoints);
                            drawRoute(navigationPoints);
                            placeOrMoveCar(start, true);
                            tvEta.setText("ETA: -");
                            startSimulatedDrive();
                            return;
                        }

                        String geom = res.body().routes.get(0).geometry;
                        navigationPoints = decodePolyline6(geom);

                        if (navigationPoints == null || navigationPoints.size() < 2) {
                            navigationPoints = new ArrayList<>(routePoints);
                        }

                        drawRoute(navigationPoints);

                        GeoPoint carPos = navigationPoints.isEmpty() ? start : navigationPoints.get(0);
                        placeOrMoveCar(carPos, true);

                        int remaining = Math.max(0, navigationPoints.size() - 1);
                        tvEta.setText(remaining == 0 ? "ETA: arrived" : "ETA: ~" + remaining + " steps");

                        startSimulatedDrive();
                    }

                    @Override
                    public void onFailure(@NonNull Call<OsrmRouteResponse> call, @NonNull Throwable t) {
                        if (!isAdded() || map == null) return;

                        navigationPoints = new ArrayList<>(routePoints);
                        drawRoute(navigationPoints);
                        placeOrMoveCar(start, true);
                        tvEta.setText("ETA: -");
                        startSimulatedDrive();
                    }
                });
    }

    private void drawRoute(List<GeoPoint> pts) {

        if (!isAdded() || map == null) return;

        // remove old overlays
        if (routeLine != null) map.getOverlays().remove(routeLine);
        if (startMarker != null) map.getOverlays().remove(startMarker);
        if (endMarker != null) map.getOverlays().remove(endMarker);

        if (pts == null || pts.isEmpty()) return;

        // ===== ROUTE LINE =====
        routeLine = new Polyline();
        routeLine.setGeodesic(true);
        routeLine.setWidth(18f);
        routeLine.setPoints(pts);

        map.getOverlays().add(0, routeLine);

        // ===== START MARKER =====
        GeoPoint start = pts.get(0);

        startMarker = new Marker(map);
        startMarker.setPosition(start);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle("Start");

        Drawable startIcon = getScaledDrawable(
                R.drawable.ic_pickup,
                38,
                38
        );

        if (startIcon != null) startMarker.setIcon(startIcon);

        map.getOverlays().add(startMarker);

        // ===== END MARKER =====
        GeoPoint end = pts.get(pts.size() - 1);

        endMarker = new Marker(map);
        endMarker.setPosition(end);
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        endMarker.setTitle("Destination");

        Drawable endIcon = getScaledDrawable(
                R.drawable.ic_dropoff,
                38,
                38
        );

        if (endIcon != null) endMarker.setIcon(endIcon);

        map.getOverlays().add(endMarker);

        // ===== CENTER CAMERA =====
        map.getController().setCenter(start);
        map.invalidate();
    }


    private void placeOrMoveCar(GeoPoint p, boolean firstTime) {
        if (!isAdded() || map == null) return;

        if (carMarker == null) {
            carMarker = new Marker(map);
            carMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

            Drawable d = getScaledDrawable(R.drawable.ic_car_marker, CAR_ICON_W_DP, CAR_ICON_H_DP);
            if (d != null) carMarker.setIcon(d);

            map.getOverlays().add(carMarker);
        }

        carMarker.setPosition(p);

        if (firstTime) map.getController().animateTo(p);
        map.invalidate();
    }


    private void startSimulatedDrive() {
        stopSimulatedDrive();

        if (!isAdded() || map == null) return;

        final List<GeoPoint> pts = (navigationPoints != null && navigationPoints.size() >= 2)
                ? navigationPoints
                : routePoints;

        if (pts.size() < 2) return;

        simRunning = true;
        simIndex = 0;

        simRunnable = new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                if (!simRunning) return;
                if (!isAdded() || map == null) {
                    stopSimulatedDrive();
                    return;
                }
                if (pts.isEmpty()) {
                    stopSimulatedDrive();
                    return;
                }

                // clamp
                if (simIndex < 0) simIndex = 0;
                if (simIndex >= pts.size()) simIndex = pts.size() - 1;

                GeoPoint p = pts.get(simIndex);
                placeOrMoveCar(p, false);

                int remaining = Math.max(0, pts.size() - 1 - simIndex);
                if (tvEta != null) {
                    tvEta.setText(remaining == 0 ? "ETA: arrived" : "ETA: ~" + remaining + " steps");
                }

                simIndex++;

                if (simIndex >= pts.size()) {
                    stopSimulatedDrive();
                    return;
                }

                simHandler.postDelayed(this, SIM_TICK_MS);
            }
        };

        simHandler.post(simRunnable);
    }

    private void stopSimulatedDrive() {
        simRunning = false;
        if (simRunnable != null) simHandler.removeCallbacks(simRunnable);
        simRunnable = null;
    }

    // ====== INCONSISTENCY ======

    private void openInconsistencyDialog() {
        if (!isAdded()) return;
        if (currentRideId == null) {
            toast("RideId missing");
            return;
        }

        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_inconsistency, null);
        TextInputEditText et = view.findViewById(R.id.etDesc);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Report inconsistency")
                .setView(view)
                .setPositiveButton("Send", (d, which) -> {
                    String txt = (et.getText() != null) ? et.getText().toString().trim() : "";
                    if (txt.isEmpty()) {
                        toast("Unesi opis");
                        return;
                    }
                    sendInconsistency(txt);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void sendInconsistency(String text) {
        if (!isAdded()) return;
        if (currentRideId == null) {
            toast("RideId missing");
            return;
        }

        passengerApi.reportInconsistency(currentRideId, new InconsistencyRequestDTO(text))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> res) {
                        if (!isAdded() || getView() == null) return;
                        toast(res.isSuccessful() ? "Sent" : "Error: " + res.code());
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        if (!isAdded()) return;
                        toast("Network: " + t.getMessage());
                        android.util.Log.e("TRACK", "Failure", t);
                    }
                });
    }

    // ====== LIFECYCLE ======

    @Override
    public void onPause() {
        stopSimulatedDrive();
        if (map != null) map.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map != null) map.onResume();
    }

    @Override
    public void onDestroyView() {
        stopSimulatedDrive();

        if (map != null) {
            if (routeLine != null) map.getOverlays().remove(routeLine);
            if (carMarker != null) map.getOverlays().remove(carMarker);
            if (startMarker != null) map.getOverlays().remove(startMarker);
            if (endMarker != null) map.getOverlays().remove(endMarker);

            startMarker = null;
            endMarker = null;
            routeLine = null;
            carMarker = null;
            map = null;


        }

        super.onDestroyView();
    }

    // ====== HELPERS ======

    private String safe(String s) {
        return s == null ? "-" : s;
    }

    private void toast(String m) {
        if (!isAdded() || getContext() == null) return;
        Toast.makeText(getContext(), m, Toast.LENGTH_SHORT).show();
    }

    private Drawable getScaledDrawable(int drawableResId, int widthDp, int heightDp) {
        Drawable d = ResourcesCompat.getDrawable(getResources(), drawableResId, null);
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

    private static List<GeoPoint> decodePolyline6(String encoded) {
        final double factor = 1e6;

        List<GeoPoint> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        long lat = 0, lon = 0;

        while (index < len) {
            long result = 0;
            int shift = 0;
            int b;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (long) (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            long dlat = ((result & 1) != 0) ? ~(result >> 1) : (result >> 1);
            lat += dlat;

            result = 0;
            shift = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (long) (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            long dlon = ((result & 1) != 0) ? ~(result >> 1) : (result >> 1);
            lon += dlon;

            poly.add(new GeoPoint(lat / factor, lon / factor));
        }
        return poly;
    }
}
