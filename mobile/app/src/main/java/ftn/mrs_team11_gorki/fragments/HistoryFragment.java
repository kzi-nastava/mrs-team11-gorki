package ftn.mrs_team11_gorki.fragments;

import android.os.Bundle;
import android.view.View;
import ftn.mrs_team11_gorki.R;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import ftn.mrs_team11_gorki.adapter.DriverRideHistoryRecyclerAdapter;
import ftn.mrs_team11_gorki.view.HistoryViewModel;
import ftn.mrs_team11_gorki.auth.TokenStorage;
import ftn.mrs_team11_gorki.view.SimpleItemSelectedListener;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private TextView txtStatus;
    private Spinner spinnerSort;
    private EditText edtFromDate;
    private EditText edtToDate;
    private Button btnRefresh;

    private Button btnClear;

    private DriverRideHistoryRecyclerAdapter adapter;
    private HistoryViewModel viewModel;

    public HistoryFragment() {
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

        DriverRideHistoryRecyclerAdapter adapter = new DriverRideHistoryRecyclerAdapter();
        rvHistory.setAdapter(adapter);

        rvHistory.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        txtStatus = view.findViewById(R.id.txtStatus);
        spinnerSort = view.findViewById(R.id.spinnerSort);
        edtFromDate = view.findViewById(R.id.edtFromDate);
        edtToDate = view.findViewById(R.id.edtToDate);

        edtFromDate.setOnClickListener(v -> openDatePicker(true));
        edtToDate.setOnClickListener(v -> openDatePicker(false));
        btnRefresh = view.findViewById(R.id.btnRefresh);
        btnClear= view.findViewById(R.id.btnClear);

        // Spinner + sort-array + ArrayAdapter(simple_spinner_item) kao u GUI II :contentReference[oaicite:6]{index=6}
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sort_array,
                android.R.layout.simple_spinner_item
        );
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(sortAdapter);

        viewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        viewModel.getVisibleRides().observe(getViewLifecycleOwner(), rides -> {
            adapter.setData(rides);
            if (rides == null || rides.isEmpty()) {
                // status samo ako nema greške
                if (viewModel.getErrorText().getValue() == null || viewModel.getErrorText().getValue().isEmpty()) {
                    txtStatus.setText("Nema vožnji (posle filtera/sorta).");
                }
            } else {
                txtStatus.setText("");
            }
        });

        viewModel.getErrorText().observe(getViewLifecycleOwner(), err -> {
            if (err != null && !err.isEmpty()) txtStatus.setText(err);
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (Boolean.TRUE.equals(loading)) txtStatus.setText("Učitavam...");
        });

        spinnerSort.setOnItemSelectedListener(new SimpleItemSelectedListener(pos -> {
            viewModel.setSortOption(pos);
        }));


        btnRefresh.setOnClickListener(v -> loadNow());
        btnClear.setOnClickListener(v -> {

            // ocisti UI polja
            edtFromDate.setText("");
            edtToDate.setText("");

            // resetuj spinner (Bez sortiranja = index 0)
            spinnerSort.setSelection(0);

            // resetuj ViewModel state
            viewModel.setFromDate(null);
            viewModel.setToDate(null);
            viewModel.setSortOption(0);

            // reload
            loadNow();
        });

        loadNow();
    }

    private void loadNow() {
        TokenStorage ts = new TokenStorage(requireContext());
        String token = ts.getToken();
        Long driverId = ts.getUserId();

        viewModel.loadHistory(token, driverId, viewModel.getFromDate(), viewModel.getToDate());
    }
}