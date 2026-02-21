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
import ftn.mrs_team11_gorki.dto.RideStopRequestDTO;
import ftn.mrs_team11_gorki.dto.RideStopResponseDTO;
import ftn.mrs_team11_gorki.dto.LocationDTO;
import ftn.mrs_team11_gorki.dto.OsrmRouteResponse;
import ftn.mrs_team11_gorki.service.OsrmService;
import ftn.mrs_team11_gorki.service.DriverService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RideInProgressDriverFragment extends Fragment {

    // ====== UI ======
    private MapView map;
    private View bottomPanel;

    private TextView tvStart, tvEnd, tvEta;
    private MaterialButton btnInconsistency, btnPanic;

    // ====== PANIC ANIMATION (CAR MARKER) ======
    private final Handler panicUi = new Handler(Looper.getMainLooper());
    private Runnable panicAnim;
    private boolean panicAnimating = false;

    private org.osmdroid.views.overlay.Polygon panicRing; // opcionalno "glow" oko auta

    // ====== API ======
    private DriverService driverApi;
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

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private java.util.concurrent.ExecutorService geoExec =
            java.util.concurrent.Executors.newSingleThreadExecutor();
    private OkHttpClient httpClient; // reuse za nominatim

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(
                requireContext().getApplicationContext(),
                requireContext().getSharedPreferences("osmdroid", 0)
        );
        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());

        httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request r = chain.request().newBuilder()
                            .header("User-Agent", requireContext().getPackageName() + " (student-project)")
                            .build();
                    return chain.proceed(r);
                })
                .build();

        Retrofit osrmRetrofit = new Retrofit.Builder()
                .baseUrl("https://router.project-osrm.org/")
                .client(httpClient)
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
        driverApi = ApiClient.getRetrofit(requireContext()).create(DriverService.class);

        setupMapPadding();
        setupMap();

        btnInconsistency.setText("STOP RIDE"); // ako želiš bez menjanja XML-a
        btnInconsistency.setOnClickListener(v2 -> stopRideDirect());

        btnPanic.setOnClickListener(view -> {
            animateCarMarkerOnPanic(); // vizuelni efekat na autu
            sendPanic();               // backend poziv
        });

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

    private long getDriverId() {
        TokenStorage ts = new TokenStorage(requireContext());
        return ts.getUserId();
    }

    private void loadActiveRide() {
        long driverId = getDriverId();

        driverApi.getActiveRideForEnd(driverId).enqueue(new Callback<DriverRideHistoryDTO>() {
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

    // ====== STOP RIDE ======

    private void openStopRideDialog() {
        if (!isAdded()) return;
        if (currentRideId == null) {
            toast("RideId missing");
            return;
        }

        GeoPoint p = getCurrentCarPointFallback();
        String msg = "Stop ride at:\n" +
                String.format(java.util.Locale.US, "%.6f, %.6f", p.getLatitude(), p.getLongitude());

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Stop ride")
                .setMessage(msg)
                .setPositiveButton("STOP", (d, which) -> stopRideNow(p))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private GeoPoint getCurrentCarPointFallback() {
        if (carMarker != null && carMarker.getPosition() != null) {
            return carMarker.getPosition();
        }
        if (navigationPoints != null && !navigationPoints.isEmpty()) {
            int idx = Math.max(0, Math.min(simIndex, navigationPoints.size() - 1));
            return navigationPoints.get(idx);
        }
        if (routePoints != null && !routePoints.isEmpty()) {
            return routePoints.get(0);
        }
        return new GeoPoint(45.2671, 19.8335);
    }

    private void stopRideNow(GeoPoint p) {
        if (!isAdded()) return;
        if (currentRideId == null) {
            toast("RideId missing");
            return;
        }

        String resolved = reverseGeocode(p);
        final String addrFinal = (resolved != null)
                ? resolved
                : String.format(java.util.Locale.US, "%.6f, %.6f", p.getLatitude(), p.getLongitude());

        RideStopRequestDTO req = new RideStopRequestDTO(
                p.getLatitude(),
                p.getLongitude(),
                addrFinal
        );

        btnInconsistency.setEnabled(false);

        driverApi.stopRide(currentRideId, req).enqueue(new Callback<RideStopResponseDTO>() {
            @Override
            public void onResponse(@NonNull Call<RideStopResponseDTO> call,
                                   @NonNull Response<RideStopResponseDTO> res) {
                if (!isAdded() || getView() == null) return;

                btnInconsistency.setEnabled(true);

                if (!res.isSuccessful()) {
                    toast("Stop error: " + res.code());
                    return;
                }

                stopSimulatedDrive();

                RideStopResponseDTO body = res.body();
                if (body != null) {
                    tvEta.setText("FINISHED | Price: " + body.getPrice());
                    tvEnd.setText("Stopped at: " +
                            (body.getStopAddress() != null ? safe(body.getStopAddress().getAddress()) : addrFinal));
                } else {
                    tvEta.setText("FINISHED");
                    tvEnd.setText("Stopped at: " + addrFinal);
                }

                btnInconsistency.setEnabled(false);
                toast("Ride stopped");
            }

            @Override
            public void onFailure(@NonNull Call<RideStopResponseDTO> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                btnInconsistency.setEnabled(true);
                if (call.isCanceled()) return;
                toast("Network: " + t.getMessage());
                android.util.Log.e("TRACK", "stopRide failure", t);
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

        if (geoExec != null) {
            geoExec.shutdownNow();
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

    @Nullable
    private String reverseGeocode(GeoPoint p) {
        try {
            android.location.Geocoder geocoder =
                    new android.location.Geocoder(requireContext(), java.util.Locale.getDefault());

            java.util.List<android.location.Address> list =
                    geocoder.getFromLocation(p.getLatitude(), p.getLongitude(), 1);

            if (list != null && !list.isEmpty()) {
                android.location.Address a = list.get(0);

                // Najčešće je najbolji "full" line:
                String line = a.getAddressLine(0);
                if (line != null && !line.trim().isEmpty()) return line;

                // fallback (ručno sklapanje)
                String street = a.getThoroughfare();
                String num = a.getSubThoroughfare();
                String city = a.getLocality();
                String country = a.getCountryName();

                StringBuilder sb = new StringBuilder();
                if (street != null) sb.append(street);
                if (num != null) sb.append(" ").append(num);
                if (city != null) sb.append(sb.length() > 0 ? ", " : "").append(city);
                if (country != null) sb.append(sb.length() > 0 ? ", " : "").append(country);

                String s = sb.toString().trim();
                if (!s.isEmpty()) return s;
            }
        } catch (Exception ignored) {}
        return null;
    }

    private void animateCarMarkerOnPanic() {
        if (!isAdded() || map == null) return;

        // Ako PANIC klikne pre nego što se auto marker kreira
        if (carMarker == null || carMarker.getPosition() == null) {
            toast("Car position not ready yet");
            return;
        }

        stopCarMarkerPanicAnim(); // u slučaju da je već u toku

        panicAnimating = true;

        // Par "frame"-ova (nemoj 60 frame-ova jer ćeš stalno praviti bitmap)
        int[] sizesDp = new int[] { CAR_ICON_W_DP, 52, 64, 52, CAR_ICON_W_DP };

        final List<Drawable> frames = new ArrayList<>();
        for (int s : sizesDp) {
            Drawable d = getScaledDrawable(R.drawable.ic_car_marker, s, s);
            frames.add(d);
        }

        createOrResetPanicRing(); // ako nećeš krug, obriši ovaj poziv

        final long TICK = 90L;   // brzina animacije
        final int LOOPS = 5;     // koliko puta da “pulsira”

        panicAnim = new Runnable() {
            int i = 0;
            int total = frames.size() * LOOPS;

            @Override
            public void run() {
                if (!panicAnimating || !isAdded() || map == null || carMarker == null) {
                    stopCarMarkerPanicAnim();
                    return;
                }

                int frameIdx = i % frames.size();
                Drawable icon = frames.get(frameIdx);
                if (icon != null) carMarker.setIcon(icon);

                // mali "shake" / rotacija
                float rot = (frameIdx % 2 == 0) ? 10f : -10f;
                try { carMarker.setRotation(rot); } catch (Exception ignore) {}

                // update krug (opciono)
                updatePanicRing(i, total);

                map.invalidate();

                i++;
                if (i <= total) {
                    panicUi.postDelayed(this, TICK);
                } else {
                    stopCarMarkerPanicAnim();
                }
            }
        };

        panicUi.post(panicAnim);
    }

    private void stopCarMarkerPanicAnim() {
        panicAnimating = false;
        if (panicAnim != null) panicUi.removeCallbacks(panicAnim);
        panicAnim = null;

        // vrati ikonu na normalnu veličinu
        if (carMarker != null) {
            Drawable normal = getScaledDrawable(R.drawable.ic_car_marker, 60, 60);
            if (normal != null) carMarker.setIcon(normal);
            try { carMarker.setRotation(0f); } catch (Exception ignore) {}
        }

        // skloni krug
        if (map != null && panicRing != null) {
            map.getOverlays().remove(panicRing);
            panicRing = null;
            map.invalidate();
        }
    }

// ====== PANIC RING (opciono) ======

    private void createOrResetPanicRing() {
        if (map == null || carMarker == null || carMarker.getPosition() == null) return;

        if (panicRing != null) {
            map.getOverlays().remove(panicRing);
        }

        panicRing = new org.osmdroid.views.overlay.Polygon(map);
        panicRing.setStrokeWidth(6f);

        // boje: crveni stroke + poluprovidan fill
        panicRing.setStrokeColor(android.graphics.Color.argb(220, 255, 0, 0));
        panicRing.setFillColor(android.graphics.Color.argb(60, 255, 0, 0));

        map.getOverlays().add(panicRing);
    }

    private void updatePanicRing(int step, int totalSteps) {
        if (panicRing == null || carMarker == null || carMarker.getPosition() == null) return;

        GeoPoint c = carMarker.getPosition();

        // radius ide od 20m do 140m
        double t = (double) step / Math.max(1, totalSteps);
        double radius = 20.0 + 120.0 * t;

        // fade out (stroke i fill)
        int strokeA = (int) (220 * (1.0 - t));
        int fillA = (int) (60 * (1.0 - t));
        strokeA = Math.max(0, Math.min(255, strokeA));
        fillA = Math.max(0, Math.min(255, fillA));

        panicRing.setStrokeColor(android.graphics.Color.argb(strokeA, 255, 0, 0));
        panicRing.setFillColor(android.graphics.Color.argb(fillA, 255, 0, 0));

        panicRing.setPoints(buildCirclePoints(c, radius, 36));
    }

    private List<GeoPoint> buildCirclePoints(GeoPoint center, double radiusMeters, int points) {
        // aproksimacija: dovoljno dobra za “glow”
        final double R = 6378137.0; // Earth radius in meters
        double lat = Math.toRadians(center.getLatitude());
        double lon = Math.toRadians(center.getLongitude());
        double d = radiusMeters / R;

        List<GeoPoint> out = new ArrayList<>(points + 1);
        for (int i = 0; i <= points; i++) {
            double brng = 2.0 * Math.PI * i / points;
            double lat2 = Math.asin(Math.sin(lat) * Math.cos(d) + Math.cos(lat) * Math.sin(d) * Math.cos(brng));
            double lon2 = lon + Math.atan2(Math.sin(brng) * Math.sin(d) * Math.cos(lat),
                    Math.cos(d) - Math.sin(lat) * Math.sin(lat2));

            out.add(new GeoPoint(Math.toDegrees(lat2), Math.toDegrees(lon2)));
        }
        return out;
    }

    // ====== PANIC ======

    private void sendPanic() {
        if (!isAdded()) return;
        if (currentRideId == null) {
            toast("RideId missing");
            return;
        }

        btnPanic.setEnabled(false);

        driverApi.panicRide(currentRideId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> res) {
                if (!isAdded()) return;

                if (res.isSuccessful()) {
                    toast("Panic sent");
                    // opcionalno: ostavi disabled da ne spamuje
                    btnPanic.setEnabled(false);
                } else {
                    btnPanic.setEnabled(true);
                    toast("Panic error: " + res.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                btnPanic.setEnabled(true);
                if (call.isCanceled()) return;
                toast("Network: " + t.getMessage());
                android.util.Log.e("TRACK", "panic failure", t);
            }
        });
    }

    private void stopRideDirect() {
        if (!isAdded()) return;
        if (currentRideId == null) {
            toast("RideId missing");
            return;
        }

        btnInconsistency.setEnabled(false);
        toast("Stopping ride...");

        final GeoPoint p = getCurrentCarPointFallback();

        geoExec.execute(() -> {
            String address = resolveAddressBlocking(p.getLatitude(), p.getLongitude());
            if (address == null || address.trim().isEmpty()) {
                address = "Unknown address"; // umesto koordinata
            }
            final String addrFinal = address;

            mainHandler.post(() -> stopRideNow(p, addrFinal));
        });
    }

    private void stopRideNow(GeoPoint p, String addrFinal) {
        if (!isAdded()) return;
        if (currentRideId == null) {
            btnInconsistency.setEnabled(true);
            toast("RideId missing");
            return;
        }

        RideStopRequestDTO req = new RideStopRequestDTO(
                p.getLatitude(),
                p.getLongitude(),
                addrFinal
        );

        driverApi.stopRide(currentRideId, req).enqueue(new Callback<RideStopResponseDTO>() {
            @Override
            public void onResponse(@NonNull Call<RideStopResponseDTO> call,
                                   @NonNull Response<RideStopResponseDTO> res) {
                if (!isAdded() || getView() == null) return;

                if (!res.isSuccessful()) {
                    btnInconsistency.setEnabled(true);
                    toast("Stop error: " + res.code());
                    return;
                }

                stopSimulatedDrive();

                RideStopResponseDTO body = res.body();
                tvEta.setText(body != null ? ("FINISHED | Price: " + body.getPrice()) : "FINISHED");
                tvEnd.setText("Stopped at: " +
                        (body != null && body.getStopAddress() != null
                                ? safe(body.getStopAddress().getAddress())
                                : addrFinal));

                toast("Ride stopped");
                btnInconsistency.setEnabled(false);
            }

            @Override
            public void onFailure(@NonNull Call<RideStopResponseDTO> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                btnInconsistency.setEnabled(true);
                if (call.isCanceled()) return;
                toast("Network: " + t.getMessage());
                android.util.Log.e("TRACK", "stopRide failure", t);
            }
        });
    }

    /** 1) prvo probaj Android Geocoder  2) ako fail -> Nominatim */
    private String resolveAddressBlocking(double lat, double lon) {
        // --- Android Geocoder (može fail na emulatoru)
        try {
            if (android.location.Geocoder.isPresent()) {
                android.location.Geocoder g =
                        new android.location.Geocoder(requireContext(), java.util.Locale.getDefault());
                java.util.List<android.location.Address> list = g.getFromLocation(lat, lon, 1);
                if (list != null && !list.isEmpty()) {
                    android.location.Address a = list.get(0);
                    String line = a.getAddressLine(0);
                    if (line != null && !line.trim().isEmpty()) return line;
                }
            }
        } catch (Exception ignore) { }

        // --- Fallback: Nominatim (OpenStreetMap)
        return reverseGeocodeNominatim(lat, lon);
    }

    private String reverseGeocodeNominatim(double lat, double lon) {
        try {
            okhttp3.HttpUrl url = okhttp3.HttpUrl.parse("https://nominatim.openstreetmap.org/reverse")
                    .newBuilder()
                    .addQueryParameter("format", "jsonv2")
                    .addQueryParameter("lat", String.valueOf(lat))
                    .addQueryParameter("lon", String.valueOf(lon))
                    .build();

            okhttp3.Request req = new okhttp3.Request.Builder()
                    .url(url)
                    .header("User-Agent", requireContext().getPackageName() + " (student-project)")
                    .build();

            try (okhttp3.Response resp = httpClient.newCall(req).execute()) {
                if (!resp.isSuccessful() || resp.body() == null) return null;
                String json = resp.body().string();
                org.json.JSONObject o = new org.json.JSONObject(json);
                return o.optString("display_name", null); // npr "Bulevar..., Novi Sad, ..."
            }
        } catch (Exception e) {
            return null;
        }
    }
}
