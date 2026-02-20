package ftn.mrs_team11_gorki.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
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
import android.widget.AutoCompleteTextView;

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
import ftn.mrs_team11_gorki.adapter.AdminRideHistoryRecyclerAdapter;
import ftn.mrs_team11_gorki.auth.TokenStorage;
import ftn.mrs_team11_gorki.dto.LocationDTO;
import ftn.mrs_team11_gorki.dto.UserOptionDTO;
import ftn.mrs_team11_gorki.service.OsrmService;
import ftn.mrs_team11_gorki.view.AdminHistoryViewModel;
import ftn.mrs_team11_gorki.view.SimpleItemSelectedListener;

public class AdminHistoryFragment extends Fragment {

    private TextView txtStatus;
    private Spinner spinnerSort;
    private EditText edtFromDate;
    private EditText edtToDate;
    private Button btnRefresh;
    private Button btnClear;
    private AdminHistoryViewModel viewModel;
    private OsrmService osrmService;

    public AdminHistoryFragment() {
        super(R.layout.fragment_history_admin);
    }

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
        rvHistory.setClipToPadding(false);
        rvHistory.addItemDecoration(new SpaceItemDecoration(dpToPx(10), dpToPx(12)));

        AdminRideHistoryRecyclerAdapter adapter = new AdminRideHistoryRecyclerAdapter();
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

        viewModel = new ViewModelProvider(this).get(AdminHistoryViewModel.class);

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

        AutoCompleteTextView actUser = view.findViewById(R.id.actUser);

        ArrayAdapter<String> userAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                new ArrayList<>()
        );
        actUser.setAdapter(userAdapter);

        final List<UserOptionDTO>[] last = new List[]{ new ArrayList<>() };

        viewModel.getUserSuggestions().observe(getViewLifecycleOwner(), users -> {
            last[0] = (users == null) ? new ArrayList<>() : users;

            List<String> labels = new ArrayList<>();
            for (UserOptionDTO u : last[0]) {
                labels.add(u.firstName() + " " + u.lastName() + " (" + u.email() + ")");
            }

            userAdapter.clear();
            userAdapter.addAll(labels);
            userAdapter.notifyDataSetChanged();

            if (!labels.isEmpty()) actUser.showDropDown();
        });

        android.os.Handler h = new android.os.Handler(android.os.Looper.getMainLooper());
        final Runnable[] pending = new Runnable[1];

        actUser.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(android.text.Editable s) {
                String q = s.toString();

                if (q.trim().isEmpty()) {
                    viewModel.setSelectedUser(null); // tek kad obriše sve
                }

                if (pending[0] != null) h.removeCallbacks(pending[0]);

                pending[0] = () -> viewModel.setUserQuery(q);
                h.postDelayed(pending[0], 250);
            }
        });

        actUser.setOnItemClickListener((parent, v, pos, id) -> {
            if (pos >= 0 && pos < last[0].size()) {
                UserOptionDTO u = last[0].get(pos);
                viewModel.setSelectedUser(u);
                actUser.setText(u.firstName() + " " + u.lastName() + " (" + u.email() + ")", false);
            }
        });

        TokenStorage ts = new TokenStorage(requireContext());
        String token = ts.getToken();
        viewModel.loadAllUsers(token);

        spinnerSort.setOnItemSelectedListener(new SimpleItemSelectedListener(viewModel::setSortOption));

        btnRefresh.setOnClickListener(v -> {
            String typed = actUser.getText() == null ? "" : actUser.getText().toString().trim();

            // 1) pokušaj prvo iz last suggestions (dropdown lista)
            UserOptionDTO chosen = null;
            String typedEmail = extractEmail(typed).toLowerCase();

            if (!typedEmail.isEmpty()) {
                for (UserOptionDTO u : last[0]) {
                    if (u != null && u.email() != null && u.email().equalsIgnoreCase(typedEmail)) {
                        chosen = u;
                        break;
                    }
                }
            }

            // 2) ako nije u suggestions, traži u svim userima koje si učitao
            if (chosen == null && !typedEmail.isEmpty()) {
                for (UserOptionDTO u : viewModel.getAllUsersValue()) {
                    if (u != null && u.email() != null && u.email().equalsIgnoreCase(typedEmail)) {
                        chosen = u;
                        break;
                    }
                }
            }

            // 3) setuj selekciju (ili očisti ako je prazno)
            if (chosen != null) {
                viewModel.setSelectedUser(chosen); // setuje selectedUserEmail i refiltrira
            } else if (typed.isEmpty()) {
                viewModel.setSelectedUser(null);
            } else {
                // nije pronađen match -> nema filtriranja po useru
                viewModel.setSelectedUser(null);
            }

            // 4) sad tek primeni date filter sa servera
            loadNow();
        });

        btnClear.setOnClickListener(v -> {
            edtFromDate.setText("");
            edtToDate.setText("");
            spinnerSort.setSelection(0);

            viewModel.setFromDate(null);
            viewModel.setToDate(null);
            viewModel.setSortOption(0);
            actUser.setText("", false);
            viewModel.setSelectedUser(null);
            viewModel.setUserQuery("");

            loadNow();
        });

        loadNow();
    }

    private void loadNow() {
        TokenStorage ts = new TokenStorage(requireContext());
        String token = ts.getToken();

        viewModel.loadHistory(token, viewModel.getFromDate(), viewModel.getToDate());
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

    private static class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private final int vSpace;
        private final int hSpace;

        SpaceItemDecoration(int verticalSpacePx, int horizontalSpacePx) {
            this.vSpace = verticalSpacePx;
            this.hSpace = horizontalSpacePx;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int pos = parent.getChildAdapterPosition(view);

            outRect.left = hSpace;
            outRect.right = hSpace;

            // spacing izmedju itema
            outRect.top = (pos == 0) ? vSpace : vSpace / 2;
            outRect.bottom = vSpace / 2;
        }
    }

    private static String extractEmail(String s) {
        if (s == null) return "";
        int l = s.indexOf('(');
        int r = s.indexOf(')');
        if (l >= 0 && r > l) return s.substring(l + 1, r).trim();
        return s.trim();
    }
}