package ftn.mrs_team11_gorki.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.adapter.PassengerRideHistoryRecyclerAdapter;
import ftn.mrs_team11_gorki.auth.TokenStorage;
import ftn.mrs_team11_gorki.dto.LocationDTO;
import ftn.mrs_team11_gorki.service.OsrmService;
import ftn.mrs_team11_gorki.view.PassengerHistoryViewModel;
import ftn.mrs_team11_gorki.view.SimpleItemSelectedListener;

public class PassengerHistoryFragment extends Fragment {

    private TextView txtStatus;
    private Spinner spinnerSort;
    private EditText edtFromDate;
    private EditText edtToDate;
    private Button btnRefresh;
    private Button btnClear;

    private PassengerHistoryViewModel viewModel;

    public PassengerHistoryFragment() {
        super(R.layout.fragment_history_passenger);
    }
    private OsrmService osrmService;

    private void openDatePicker(boolean isFrom) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int y = cal.get(java.util.Calendar.YEAR);
        int m = cal.get(java.util.Calendar.MONTH);
        int d = cal.get(java.util.Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog dlg = new android.app.DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    String mm = String.format(java.util.Locale.US, "%02d", month + 1);
                    String dd = String.format(java.util.Locale.US, "%02d", dayOfMonth);
                    String iso = year + "-" + mm + "-" + dd;

                    if (isFrom) {
                        edtFromDate.setText(iso);
                        viewModel.setFromDate(iso);
                    } else {
                        edtToDate.setText(iso);
                        viewModel.setToDate(iso);
                    }
                },
                y, m, d
        );
        dlg.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvHistory = view.findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(requireContext()));

        PassengerRideHistoryRecyclerAdapter adapter = new PassengerRideHistoryRecyclerAdapter();
        rvHistory.setAdapter(adapter);
        adapter.setOnMapClickListener(this::openRideMapDialog);

        txtStatus = view.findViewById(R.id.txtStatus);
        spinnerSort = view.findViewById(R.id.spinnerSort);
        edtFromDate = view.findViewById(R.id.edtFromDate);
        edtToDate = view.findViewById(R.id.edtToDate);
        btnRefresh = view.findViewById(R.id.btnRefresh);
        btnClear = view.findViewById(R.id.btnClear);

        edtFromDate.setOnClickListener(v -> openDatePicker(true));
        edtToDate.setOnClickListener(v -> openDatePicker(false));

        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sort_array,
                android.R.layout.simple_spinner_item
        );
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(sortAdapter);

        viewModel = new ViewModelProvider(this).get(PassengerHistoryViewModel.class);

        viewModel.getVisibleRides().observe(getViewLifecycleOwner(), rides -> {
            adapter.setData(rides);
            if (rides == null || rides.isEmpty()) {
                String err = viewModel.getErrorText().getValue();
                if (err == null || err.isEmpty()) txtStatus.setText("Nema vožnji (posle filtera/sorta).");
            } else txtStatus.setText("");
        });

        viewModel.getErrorText().observe(getViewLifecycleOwner(), err -> {
            if (err != null && !err.isEmpty()) txtStatus.setText(err);
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (Boolean.TRUE.equals(loading)) txtStatus.setText("Učitavam...");
        });

        spinnerSort.setOnItemSelectedListener(new SimpleItemSelectedListener(viewModel::setSortOption));

        btnRefresh.setOnClickListener(v -> loadNow());

        btnClear.setOnClickListener(v -> {
            edtFromDate.setText("");
            edtToDate.setText("");
            spinnerSort.setSelection(0);

            viewModel.setFromDate(null);
            viewModel.setToDate(null);
            viewModel.setSortOption(0);

            loadNow();
        });

        loadNow();
    }

    private void loadNow() {
        TokenStorage ts = new TokenStorage(requireContext());
        String token = ts.getToken();
        Long passengerId = ts.getUserId(); // PASSENGER id

        viewModel.loadHistory(token, passengerId, viewModel.getFromDate(), viewModel.getToDate());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        org.osmdroid.config.Configuration.getInstance().load(
                requireContext().getApplicationContext(),
                requireContext().getSharedPreferences("osmdroid", 0)
        );

        // osmdroid user agent (ako već radi globalno, ok je i ovde)
        org.osmdroid.config.Configuration.getInstance()
                .setUserAgentValue(requireContext().getPackageName());

        okhttp3.OkHttpClient http = new okhttp3.OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    okhttp3.Request r = chain.request().newBuilder()
                            .header("User-Agent", requireContext().getPackageName() + " (student-project)")
                            .build();
                    return chain.proceed(r);
                })
                .build();

        retrofit2.Retrofit osrmRetrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("https://router.project-osrm.org/")
                .client(http)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build();

        osrmService = osrmRetrofit.create(OsrmService.class);
    }

    private void openRideMapDialog(ftn.mrs_team11_gorki.dto.DriverRideHistoryDTO ride) {
        if (ride == null || ride.getRoute() == null || ride.getRoute().getLocations() == null
                || ride.getRoute().getLocations().size() < 2
                || ride.getRoute().getLocations().get(0) == null
                || ride.getRoute().getLocations().get(1) == null) {
            txtStatus.setText("Route data missing for this ride.");
            return;
        }
        View dv = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_ride_map, null);

        org.osmdroid.views.MapView m = dv.findViewById(R.id.mapRide);
        TextView txt = dv.findViewById(R.id.txtRouteInfo);
        Button btnClose = dv.findViewById(R.id.btnCloseMap);

        android.app.AlertDialog dlg = new android.app.AlertDialog.Builder(requireContext())
                .setView(dv)
                .create();

        btnClose.setOnClickListener(v -> dlg.dismiss());

        dlg.setOnShowListener(x -> {
            // map init
            m.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
            m.setMultiTouchControls(true);
            m.setBuiltInZoomControls(false);
            m.getController().setZoom(15.0);
            m.onResume();

            // uzmi start/dest iz DTO (mora da imaju lat/lon)
            List<LocationDTO> locs = ride.getRoute().getLocations();
            LocationDTO s = locs.get(0);
            LocationDTO d = locs.get(1);

            org.osmdroid.util.GeoPoint start = new org.osmdroid.util.GeoPoint(s.getLatitude(), s.getLongitude());
            org.osmdroid.util.GeoPoint dest  = new org.osmdroid.util.GeoPoint(d.getLatitude(), d.getLongitude());

            // markeri
            org.osmdroid.views.overlay.Marker ms = new org.osmdroid.views.overlay.Marker(m);
            ms.setPosition(start);
            ms.setAnchor(org.osmdroid.views.overlay.Marker.ANCHOR_CENTER, org.osmdroid.views.overlay.Marker.ANCHOR_BOTTOM);
            ms.setIcon(getScaledDrawable(R.drawable.ic_pickup, 38, 38));
            ms.setTitle("Start");

            org.osmdroid.views.overlay.Marker md = new org.osmdroid.views.overlay.Marker(m);
            md.setPosition(dest);
            md.setAnchor(org.osmdroid.views.overlay.Marker.ANCHOR_CENTER, org.osmdroid.views.overlay.Marker.ANCHOR_BOTTOM);
            md.setIcon(getScaledDrawable(R.drawable.ic_dropoff, 38, 38));
            md.setTitle("Destination");

            m.getOverlays().add(ms);
            m.getOverlays().add(md);

            // OSRM polyline (kao kod estimate)
            String coords = start.getLongitude() + "," + start.getLatitude()
                    + ";" + dest.getLongitude() + "," + dest.getLatitude();

            txt.setText("Loading route...");

            zoomBetween(m, start, dest);
            m.invalidate();

            osrmService.route(coords, "full", "polyline6").enqueue(new retrofit2.Callback<ftn.mrs_team11_gorki.dto.OsrmRouteResponse>() {
                @Override
                public void onResponse(@NonNull retrofit2.Call<ftn.mrs_team11_gorki.dto.OsrmRouteResponse> call,
                                       @NonNull retrofit2.Response<ftn.mrs_team11_gorki.dto.OsrmRouteResponse> resp) {
                    if (!resp.isSuccessful() || resp.body() == null || resp.body().routes == null || resp.body().routes.isEmpty()) {
                        txt.setText("Route not available.");
                        zoomBetween(m, start, dest);
                        return;
                    }

                    var r0 = resp.body().routes.get(0);
                    List<GeoPoint> pts = decodePolyline6(r0.geometry);

                    org.osmdroid.views.overlay.Polyline pl = new org.osmdroid.views.overlay.Polyline();
                    pl.setPoints(pts);
                    pl.setGeodesic(true);
                    pl.setWidth(18f);
                    pl.setColor(android.graphics.Color.parseColor("#362D88"));

                    // ispod markera
                    m.getOverlays().add(0, pl);

                    double km = r0.distance / 1000.0;
                    String eta = formatDuration((long) r0.duration);
                    txt.setText(String.format(java.util.Locale.US, "Distance: %.2f km | ETA: %s", km, eta));

                    zoomToRoute(m, pts); // “malo bolji zoom-out”
                    m.invalidate();
                }

                @Override
                public void onFailure(@NonNull retrofit2.Call<ftn.mrs_team11_gorki.dto.OsrmRouteResponse> call,
                                      @NonNull Throwable t) {
                    txt.setText("Route error: " + t.getMessage());
                    zoomBetween(m, start, dest);
                }
            });
        });

        dlg.setOnDismissListener(x -> {
            try {
                m.onPause();
                m.onDetach();   // bitno za osmdroid cleanup
            } catch (Exception ignored) {}
        });

        dlg.show();
    }

    private static List<org.osmdroid.util.GeoPoint> decodePolyline6(String encoded) {
        final double factor = 1e6;
        List<org.osmdroid.util.GeoPoint> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        long lat = 0, lon = 0;

        while (index < len) {
            long result = 0; int shift = 0; int b;
            do { b = encoded.charAt(index++) - 63; result |= (long) (b & 0x1f) << shift; shift += 5; } while (b >= 0x20);
            long dlat = ((result & 1) != 0) ? ~(result >> 1) : (result >> 1);
            lat += dlat;

            result = 0; shift = 0;
            do { b = encoded.charAt(index++) - 63; result |= (long) (b & 0x1f) << shift; shift += 5; } while (b >= 0x20);
            long dlon = ((result & 1) != 0) ? ~(result >> 1) : (result >> 1);
            lon += dlon;

            poly.add(new org.osmdroid.util.GeoPoint(lat / factor, lon / factor));
        }
        return poly;
    }

    private String formatDuration(long seconds) {
        long mins = seconds / 60;
        long h = mins / 60;
        long m = mins % 60;
        return (h > 0) ? (h + "h " + m + "m") : (m + "m");
    }

    // “bolji zoom out”: povećaj bounding box ~15% + padding
    private void zoomToRoute(org.osmdroid.views.MapView map, List<org.osmdroid.util.GeoPoint> points) {
        if (points == null || points.isEmpty()) return;
        org.osmdroid.util.BoundingBox bb = org.osmdroid.util.BoundingBox.fromGeoPoints(points);

        double latSpan = bb.getLatNorth() - bb.getLatSouth();
        double lonSpan = bb.getLonEast() - bb.getLonWest();

        double latPad = latSpan * 0.15;  // 15% zoom-out
        double lonPad = lonSpan * 0.15;

        org.osmdroid.util.BoundingBox bigger = new org.osmdroid.util.BoundingBox(
                bb.getLatNorth() + latPad,
                bb.getLonEast() + lonPad,
                bb.getLatSouth() - latPad,
                bb.getLonWest() - lonPad
        );

        map.zoomToBoundingBox(bigger, true, 120);
    }

    private void zoomBetween(org.osmdroid.views.MapView map, org.osmdroid.util.GeoPoint a, org.osmdroid.util.GeoPoint b) {
        List<org.osmdroid.util.GeoPoint> pts = new ArrayList<>();
        pts.add(a); pts.add(b);
        zoomToRoute(map, pts);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Drawable getScaledDrawable(int drawableResId, int widthDp, int heightDp) {
        Drawable d = requireContext().getDrawable(drawableResId);
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
}