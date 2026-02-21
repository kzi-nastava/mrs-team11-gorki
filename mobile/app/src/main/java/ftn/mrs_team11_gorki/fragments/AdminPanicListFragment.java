package ftn.mrs_team11_gorki.fragments;

import android.graphics.Rect;
import android.os.Bundle;
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
import ftn.mrs_team11_gorki.adapter.DriverRideHistoryRecyclerAdapter;
import ftn.mrs_team11_gorki.auth.TokenStorage;
import ftn.mrs_team11_gorki.view.AdminPanicViewModel;
import ftn.mrs_team11_gorki.view.SimpleItemSelectedListener;
import ftn.mrs_team11_gorki.BuildConfig;
import ftn.mrs_team11_gorki.service.WsManager;

public class AdminPanicListFragment extends Fragment {

    private TextView txtStatus;
    private Spinner spinnerSort;
    private EditText edtFromDate;
    private EditText edtToDate;
    private Button btnRefresh;
    private Button btnClear;

    private DriverRideHistoryRecyclerAdapter adapter;
    private AdminPanicViewModel viewModel;

    public AdminPanicListFragment() {
        super(R.layout.fragment_history);
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

        adapter = new DriverRideHistoryRecyclerAdapter();
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

        viewModel = new ViewModelProvider(this).get(AdminPanicViewModel.class);

        viewModel.getVisibleRides().observe(getViewLifecycleOwner(), rides -> {
            adapter.setData(rides);
            if (rides == null || rides.isEmpty()) {
                String err = viewModel.getErrorText().getValue();
                if (err == null || err.isEmpty()) txtStatus.setText("Nema panic vožnji (posle filtera/sorta).");
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
        viewModel.loadPanicRides(token, viewModel.getFromDate(), viewModel.getToDate());
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

    private WsManager ws;
    private android.media.MediaPlayer player;

    @Override
    public void onStart() {
        if (!isAdded()) return;
        super.onStart();

        TokenStorage ts = new TokenStorage(requireContext());
        String token = ts.getToken();

        ws = new WsManager("ws://" + BuildConfig.API_HOST + ":" + BuildConfig.API_PORT + "/ws-native");

        ws.connect(token, new WsManager.PanicListener() {
            @Override
            public void onPanicEvent(String payloadJson) {
                requireActivity().runOnUiThread(() -> {
                    playSound();
                    showAlert(payloadJson); // ili samo "PANIC!" ako ne parsiraš JSON
                    loadNow(); // refresh lista
                });
            }

            @Override
            public void onError(Throwable t) {
                requireActivity().runOnUiThread(() ->
                        txtStatus.setText("WS error: " + t.getMessage())
                );
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (ws != null) ws.disconnect();
        ws = null;
        stopSound();
    }

    private void playSound() {
        stopSound();
        player = android.media.MediaPlayer.create(requireContext(), R.raw.panic_sound);
        player.start();
    }

    private void stopSound() {
        if (player != null) {
            try { player.stop(); } catch (Exception ignored) {}
            try { player.release(); } catch (Exception ignored) {}
            player = null;
        }
    }

    private void showAlert(String payloadJson) {
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("PANIC")
                .setMessage("Stigao je panic event!\n\n")
                .setPositiveButton("OK", null)
                .show();
    }
}