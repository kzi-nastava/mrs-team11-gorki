package ftn.mrs_team11_gorki.fragments;

import android.graphics.Rect;
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

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.adapter.ScheduledRideRecyclerAdapter;
import ftn.mrs_team11_gorki.auth.TokenStorage;
import ftn.mrs_team11_gorki.dto.RideCancelRequestDTO;
import ftn.mrs_team11_gorki.dto.RideCancelResponseDTO;
import ftn.mrs_team11_gorki.service.ClientUtils;
import ftn.mrs_team11_gorki.view.ScheduledRidesViewModel;
import ftn.mrs_team11_gorki.view.SimpleItemSelectedListener;

import android.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;

public class ScheduledRidesFragment extends Fragment {

    private TextView txtStatus;
    private Spinner spinnerSort;
    private EditText edtFromDate;
    private EditText edtToDate;
    private Button btnRefresh;
    private Button btnClear;

    private ScheduledRideRecyclerAdapter adapter;
    private ScheduledRidesViewModel viewModel;

    public ScheduledRidesFragment() {
        super(R.layout.fragment_scheduled_rides);
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

        adapter = new ScheduledRideRecyclerAdapter();
        adapter.setOnCancelClickListener(ride -> openCancelDialog(ride.getRideId()));
        rvHistory.setAdapter(adapter);
        rvHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvHistory.setClipToPadding(false);
        rvHistory.addItemDecoration(new SpaceItemDecoration(dpToPx(10), dpToPx(12)));

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

        viewModel = new ViewModelProvider(this).get(ScheduledRidesViewModel.class);

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
        Long userId = ts.getUserId(); // driver/passenger/admin (ulogovan)

        viewModel.loadScheduled(token, userId, viewModel.getFromDate(), viewModel.getToDate());
    }

    private void openCancelDialog(Long rideId) {
        View dv = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_cancel_reason, null);
        EditText edt = dv.findViewById(R.id.edtCancelReason);

        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Cancel ride")
                .setView(dv)
                .setNegativeButton("Close", (d, w) -> d.dismiss())
                .setPositiveButton("Confirm", null) // override kasnije
                .create();

        dialog.setOnShowListener(d -> {
            Button btn = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
            btn.setOnClickListener(v -> {
                String reason = edt.getText().toString().trim();
                if (reason.isEmpty()) {
                    edt.setError("Reason is required");
                    return;
                }
                dialog.dismiss();
                callCancelRide(rideId, reason);
            });
        });

        dialog.show();
    }

    private void callCancelRide(Long rideId, String reason) {
        TokenStorage ts = new TokenStorage(requireContext());
        String token = ts.getToken();
        Long actorId = ts.getUserId();

        String cancelledBy = cancelledByFromJwt(token);
        RideCancelRequestDTO body = new RideCancelRequestDTO(reason, cancelledBy, actorId);

        ClientUtils.getRideService()
                .cancelRide("Bearer " + token, rideId, body)
                .enqueue(new retrofit2.Callback<RideCancelResponseDTO>() {
                    @Override
                    public void onResponse(@NonNull retrofit2.Call<RideCancelResponseDTO> call,
                                           @NonNull retrofit2.Response<RideCancelResponseDTO> resp) {
                        if (!resp.isSuccessful()) {
                            txtStatus.setText("Cancel failed: HTTP " + resp.code());
                            return;
                        }
                        // refresh lista
                        loadNow();
                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<RideCancelResponseDTO> call,
                                          @NonNull Throwable t) {
                        txtStatus.setText("Cancel failed: " + t.getMessage());
                    }
                });
    }

    private String cancelledByFromJwt(String jwt) {
        try {
            String[] parts = jwt.split("\\.");
            String payloadJson = new String(
                    Base64.decode(parts[1], Base64.URL_SAFE),
                    StandardCharsets.UTF_8
            );

            JSONObject payload = new JSONObject(payloadJson);

            // varijante koje se često koriste:
            if (payload.has("role")) {
                String role = payload.getString("role");
                return role.contains("DRIVER") ? "DRIVER" : "PASSENGER";
            }
            if (payload.has("authorities")) {
                Object a = payload.get("authorities");
                if (a instanceof JSONArray) {
                    for (int i = 0; i < ((JSONArray) a).length(); i++) {
                        String auth = ((JSONArray) a).getString(i);
                        if (auth.contains("DRIVER")) return "DRIVER";
                    }
                } else {
                    String auth = String.valueOf(a);
                    if (auth.contains("DRIVER")) return "DRIVER";
                }
            }
        } catch (Exception ignored) {}
        return "PASSENGER";
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

    private int dpToPx(int dp) {
        float density = requireContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}