package ftn.mrs_team11_gorki.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.TextView;

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
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.overlay.Overlay;

import java.util.ArrayList;
import java.util.List;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.dto.GetVehicleHomeDTO;
import ftn.mrs_team11_gorki.dto.NominatimPlaceDTO;
import ftn.mrs_team11_gorki.dto.OsrmRouteResponse;
import ftn.mrs_team11_gorki.service.NominatimService;
import ftn.mrs_team11_gorki.service.OsrmService;
import ftn.mrs_team11_gorki.service.VehicleService;
import ftn.mrs_team11_gorki.auth.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import okhttp3.OkHttpClient;
import okhttp3.Interceptor;
import okhttp3.Request;

public class UnuserHomeFragment extends Fragment {

    private MapView map;

    private TextView txtEstimateResult;

    private NominatimService nominatimService;
    private OsrmService osrmService;

    private Polyline routeOverlay;
    private Marker startMarker;
    private Marker destMarker;

    private VehicleService vehicleService;
    private final List<Marker> vehicleMarkers = new ArrayList<>();

    private interface Done { void run(); }

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

        View v = inflater.inflate(R.layout.fragment_unuser_home, container, false);

        // MAP init
        map = v.findViewById(R.id.osmMap);
        LinearLayout panel = v.findViewById(R.id.estimatePanel);
        panel.post(() -> map.setPadding(0, 0, 0, panel.getHeight()));
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
        txtEstimateResult = v.findViewById(R.id.txtEstimateResult);

        estimateBtn.setOnClickListener(view -> {
            String start = startEt.getText().toString().trim();
            String dest = destEt.getText().toString().trim();

            if (start.isEmpty() || dest.isEmpty()) {
                Toast.makeText(requireContext(), "Enter both addresses.", Toast.LENGTH_SHORT).show();
                return;
            }

            estimateBtn.setEnabled(false);
            txtEstimateResult.setText("Estimating...");

            geocodeThenRoute(start, dest, () -> estimateBtn.setEnabled(true));
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

    private static List<GeoPoint> decodePolyline6(String encoded) {
        // polyline6 => precision 1e6
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

    private void geocodeThenRoute(String startAddr, String destAddr, Done done) {

        // 1) Start geocode
        nominatimService.search(startAddr, "json", 1, 0, "rs")
                .enqueue(new Callback<List<NominatimPlaceDTO>>() {
                    @Override
                    public void onResponse(Call<List<NominatimPlaceDTO>> call, Response<List<NominatimPlaceDTO>> resp) {
                        if (!resp.isSuccessful() || resp.body() == null || resp.body().isEmpty()) {
                            txtEstimateResult.setText("Start address not found.");
                            done.run();
                            return;
                        }
                        GeoPoint start = toGeoPoint(resp.body().get(0));

                        // 2) Dest geocode
                        nominatimService.search(destAddr, "json", 1, 0, "rs")
                                .enqueue(new Callback<List<NominatimPlaceDTO>>() {
                                    @Override
                                    public void onResponse(Call<List<NominatimPlaceDTO>> call2, Response<List<NominatimPlaceDTO>> resp2) {
                                        if (!resp2.isSuccessful() || resp2.body() == null || resp2.body().isEmpty()) {
                                            txtEstimateResult.setText("Destination not found.");
                                            done.run();
                                            return;
                                        }
                                        GeoPoint dest = toGeoPoint(resp2.body().get(0));

                                        // 3) OSRM route (PAZI: lon,lat;lon,lat) :contentReference[oaicite:5]{index=5}
                                        String coords = start.getLongitude() + "," + start.getLatitude()
                                                + ";" + dest.getLongitude() + "," + dest.getLatitude();

                                        osrmService.route(coords, "full", "polyline6")
                                                .enqueue(new Callback<OsrmRouteResponse>() {
                                                    @Override
                                                    public void onResponse(Call<OsrmRouteResponse> call3, Response<OsrmRouteResponse> r3) {
                                                        if (!r3.isSuccessful() || r3.body() == null
                                                                || r3.body().routes == null || r3.body().routes.isEmpty()) {
                                                            txtEstimateResult.setText("Route not found.");
                                                            done.run();
                                                            return;
                                                        }

                                                        OsrmRouteResponse.Route route = r3.body().routes.get(0);

                                                        List<GeoPoint> points = decodePolyline6(route.geometry);
                                                        drawRoute(start, dest, points);

                                                        double km = route.distance / 1000.0;
                                                        String eta = formatDuration((long) route.duration);
                                                        txtEstimateResult.setText(String.format("Distance: %.2f km | ETA: %s", km, eta));

                                                        done.run();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<OsrmRouteResponse> call3, Throwable t) {
                                                        txtEstimateResult.setText("Network error (route): " + t.getMessage());
                                                        done.run();
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onFailure(Call<List<NominatimPlaceDTO>> call2, Throwable t) {
                                        txtEstimateResult.setText("Network error (dest): " + t.getMessage());
                                        done.run();
                                    }
                                });
                    }

                    @Override
                    public void onFailure(Call<List<NominatimPlaceDTO>> call, Throwable t) {
                        txtEstimateResult.setText("Network error (start): " + t.getMessage());
                        done.run();
                    }
                });
    }

    private GeoPoint toGeoPoint(NominatimPlaceDTO p) {
        double lat = Double.parseDouble(p.getLat());
        double lon = Double.parseDouble(p.getLon());
        return new GeoPoint(lat, lon);
    }

    private void drawRoute(GeoPoint start, GeoPoint dest, List<GeoPoint> points) {
        if (map == null) return;

        // ukloni staro
        if (routeOverlay != null) map.getOverlays().remove(routeOverlay);
        if (startMarker != null) map.getOverlays().remove(startMarker);
        if (destMarker != null) map.getOverlays().remove(destMarker);

        // polyline
        routeOverlay = new Polyline();
        routeOverlay.setPoints(points);
        routeOverlay.setGeodesic(true);
        routeOverlay.setColor(Color.parseColor("#362D88"));
        routeOverlay.setWidth(20f);


        // markeri
        startMarker = new Marker(map);
        startMarker.setPosition(start);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle("Start");
        startMarker.setIcon(getScaledDrawable(R.drawable.ic_pickup, 45, 45));

        destMarker = new Marker(map);
        destMarker.setPosition(dest);
        destMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        destMarker.setTitle("Destination");
        destMarker.setIcon(getScaledDrawable(R.drawable.ic_dropoff, 45, 45));

        map.getOverlays().add(0, routeOverlay);
        map.getOverlays().add(startMarker);
        map.getOverlays().add(destMarker);

        // zoom da obuhvati celu rutu
        if (points != null && !points.isEmpty()) {
            BoundingBox bb = BoundingBox.fromGeoPoints(points);

            double latSpan = bb.getLatNorth() - bb.getLatSouth();
            double lonSpan = bb.getLonEast() - bb.getLonWest();

            double extra = 0.35; // 20% margine (probaj 0.15 - 0.35)

            BoundingBox padded = new BoundingBox(
                    bb.getLatNorth() + latSpan * extra,
                    bb.getLonEast()  + lonSpan * extra,
                    bb.getLatSouth() - latSpan * extra,
                    bb.getLonWest()  - lonSpan * extra
            );

            map.zoomToBoundingBox(padded, true, 250);
        } else {
            map.getController().animateTo(start);
        }

        map.invalidate();
    }

    private String formatDuration(long seconds) {
        long mins = seconds / 60;
        long h = mins / 60;
        long m = mins % 60;
        if (h > 0) return h + "h " + m + "m";
        return m + "m";
    }
}
