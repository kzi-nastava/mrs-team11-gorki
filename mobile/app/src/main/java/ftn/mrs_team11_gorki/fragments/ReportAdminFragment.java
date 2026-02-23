package ftn.mrs_team11_gorki.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.auth.ApiClient;
import ftn.mrs_team11_gorki.dto.ReportDTO;
import ftn.mrs_team11_gorki.dto.ReportPointDTO;
import ftn.mrs_team11_gorki.service.ReportService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportAdminFragment extends Fragment {

    private TextInputEditText etFrom, etTo, etUserEmail;
    private LocalDate fromDate = null;
    private LocalDate toDate = null;

    private Button btnGenerateAggregate, btnGenerateForUser;
    private ProgressBar progress;
    private TextView txtError;

    private TextView txtTotalRides, txtAvgRides;
    private TextView txtTotalKm, txtAvgKm;
    private TextView txtTotalMoney, txtAvgMoney;

    private BarChart chartDailyRides;
    private LineChart chartDailyKm, chartDailyMoney;
    private LineChart chartCumRides, chartCumKm, chartCumMoney;

    private ReportService reportService;

    private final DecimalFormat df2 = new DecimalFormat("#0.00");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report_admin, container, false);

        reportService = ApiClient.getRetrofit(requireContext()).create(ReportService.class);

        etFrom = v.findViewById(R.id.etFrom);
        etTo = v.findViewById(R.id.etTo);
        etUserEmail = v.findViewById(R.id.etUserEmail);

        btnGenerateAggregate = v.findViewById(R.id.btnGenerateAggregate);
        btnGenerateForUser = v.findViewById(R.id.btnGenerateForUser);

        progress = v.findViewById(R.id.progress);
        txtError = v.findViewById(R.id.txtError);

        txtTotalRides = v.findViewById(R.id.txtTotalRides);
        txtAvgRides = v.findViewById(R.id.txtAvgRides);

        txtTotalKm = v.findViewById(R.id.txtTotalKm);
        txtAvgKm = v.findViewById(R.id.txtAvgKm);

        txtTotalMoney = v.findViewById(R.id.txtTotalMoney);
        txtAvgMoney = v.findViewById(R.id.txtAvgMoney);

        chartDailyRides = v.findViewById(R.id.chartDailyRides);
        chartDailyKm = v.findViewById(R.id.chartDailyKm);
        chartDailyMoney = v.findViewById(R.id.chartDailyMoney);

        chartCumRides = v.findViewById(R.id.chartCumRides);
        chartCumKm = v.findViewById(R.id.chartCumKm);
        chartCumMoney = v.findViewById(R.id.chartCumMoney);

        setupChartsBase();

        // Date pickers
        etFrom.setOnClickListener(view -> pickDate(true));
        etTo.setOnClickListener(view -> pickDate(false));

        // Long click = clear date
        etFrom.setOnLongClickListener(vw -> { clearFrom(); return true; });
        etTo.setOnLongClickListener(vw -> { clearTo(); return true; });

        btnGenerateAggregate.setOnClickListener(view -> generateAggregate());
        btnGenerateForUser.setOnClickListener(view -> generateForUser());

        return v;
    }

    private void generateAggregate() {
        if (!validateDateRange()) return;

        setLoading(true);
        setError(null);

        String from = fromDate != null ? fromDate.toString() : null;
        String to = toDate != null ? toDate.toString() : null;

        reportService.generateAdminAggregateReport(from, to)
                .enqueue(new Callback<ReportDTO>() {
                    @Override
                    public void onResponse(@NonNull Call<ReportDTO> call, @NonNull Response<ReportDTO> response) {
                        setLoading(false);

                        if (!response.isSuccessful() || response.body() == null) {
                            setError("Failed to load report. Code: " + response.code());
                            return;
                        }

                        bindSummary(response.body());
                        bindCharts(response.body());
                        Toast.makeText(requireContext(), "Aggregate report loaded.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ReportDTO> call, @NonNull Throwable t) {
                        setLoading(false);
                        setError(t.getMessage() != null ? t.getMessage() : "Failed to load report.");
                    }
                });
    }

    private void generateForUser() {
        String email = etUserEmail.getText() != null ? etUserEmail.getText().toString().trim() : "";
        if (email.isEmpty()) {
            setError("Please enter a user email.");
            return;
        }
        if (!validateDateRange()) return;

        setLoading(true);
        setError(null);

        String from = fromDate != null ? fromDate.toString() : null;
        String to = toDate != null ? toDate.toString() : null;

        reportService.generateAdminUserReport(email, from, to)
                .enqueue(new Callback<ReportDTO>() {
                    @Override
                    public void onResponse(@NonNull Call<ReportDTO> call, @NonNull Response<ReportDTO> response) {
                        setLoading(false);

                        if (!response.isSuccessful() || response.body() == null) {
                            setError("Failed to load report for user. Code: " + response.code());
                            return;
                        }

                        bindSummary(response.body());
                        bindCharts(response.body());
                        Toast.makeText(requireContext(), "User report loaded.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ReportDTO> call, @NonNull Throwable t) {
                        setLoading(false);
                        setError(t.getMessage() != null ? t.getMessage() : "Failed to load report for user.");
                    }
                });
    }

    private boolean validateDateRange() {
        if (fromDate != null && toDate != null && toDate.isBefore(fromDate)) {
            setError("Invalid date range: \"To\" must be after \"From\".");
            return false;
        }
        return true;
    }

    private void pickDate(boolean isFrom) {
        final Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlg = new DatePickerDialog(requireContext(), (dp, year, month, day) -> {
            LocalDate picked = LocalDate.of(year, month + 1, day);
            if (isFrom) {
                fromDate = picked;
                etFrom.setText(picked.toString());
            } else {
                toDate = picked;
                etTo.setText(picked.toString());
            }
        }, y, m, d);

        dlg.show();
    }

    private void clearFrom() {
        fromDate = null;
        etFrom.setText("");
    }

    private void clearTo() {
        toDate = null;
        etTo.setText("");
    }

    private void bindSummary(ReportDTO dto) {
        if (dto.getSummary() == null) return;

        txtTotalRides.setText(String.valueOf(dto.getSummary().getTotalRides()));
        txtAvgRides.setText("Avg/day: " + df2.format(dto.getSummary().getAvgRidesPerDay()));

        txtTotalKm.setText(df2.format(dto.getSummary().getTotalKilometers()));
        txtAvgKm.setText("Avg/day: " + df2.format(dto.getSummary().getAvgKilometersPerDay()));

        txtTotalMoney.setText(df2.format(dto.getSummary().getTotalMoney()));
        txtAvgMoney.setText("Avg/day: " + df2.format(dto.getSummary().getAvgMoneyPerDay()));
    }

    private void bindCharts(ReportDTO dto) {
        List<ReportPointDTO> points = dto.getPoints();
        if (points == null) points = new ArrayList<>();

        List<String> labels = new ArrayList<>();
        for (ReportPointDTO p : points) {
            labels.add(p.getDate() != null ? p.getDate().toString() : "");
        }

        // Daily rides (bar)
        List<BarEntry> dailyRideEntries = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            dailyRideEntries.add(new BarEntry(i, points.get(i).getRideCount()));
        }
        chartDailyRides.setData(new BarData(new BarDataSet(dailyRideEntries, "Rides per day")));
        applyLabels(chartDailyRides.getXAxis(), labels);
        chartDailyRides.invalidate();

        // Daily km (line)
        List<Entry> dailyKmEntries = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            dailyKmEntries.add(new Entry(i, (float) points.get(i).getKilometers()));
        }
        chartDailyKm.setData(new LineData(new LineDataSet(dailyKmEntries, "Km per day")));
        applyLabels(chartDailyKm.getXAxis(), labels);
        chartDailyKm.invalidate();

        // Daily money (line)
        List<Entry> dailyMoneyEntries = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            dailyMoneyEntries.add(new Entry(i, (float) points.get(i).getMoney()));
        }
        chartDailyMoney.setData(new LineData(new LineDataSet(dailyMoneyEntries, "Money per day")));
        applyLabels(chartDailyMoney.getXAxis(), labels);
        chartDailyMoney.invalidate();

        // Cumulative rides (line)
        List<Entry> cumRideEntries = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            cumRideEntries.add(new Entry(i, points.get(i).getCumulativeRideCount()));
        }
        chartCumRides.setData(new LineData(new LineDataSet(cumRideEntries, "Cumulative rides")));
        applyLabels(chartCumRides.getXAxis(), labels);
        chartCumRides.invalidate();

        // Cumulative km (line)
        List<Entry> cumKmEntries = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            cumKmEntries.add(new Entry(i, (float) points.get(i).getCumulativeKilometers()));
        }
        chartCumKm.setData(new LineData(new LineDataSet(cumKmEntries, "Cumulative km")));
        applyLabels(chartCumKm.getXAxis(), labels);
        chartCumKm.invalidate();

        // Cumulative money (line)
        List<Entry> cumMoneyEntries = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            cumMoneyEntries.add(new Entry(i, (float) points.get(i).getCumulativeMoney()));
        }
        chartCumMoney.setData(new LineData(new LineDataSet(cumMoneyEntries, "Cumulative money")));
        applyLabels(chartCumMoney.getXAxis(), labels);
        chartCumMoney.invalidate();
    }

    private void setupChartsBase() {
        setupBarChart(chartDailyRides);

        setupLineChart(chartDailyKm);
        setupLineChart(chartDailyMoney);

        setupLineChart(chartCumRides);
        setupLineChart(chartCumKm);
        setupLineChart(chartCumMoney);
    }

    private void setupLineChart(LineChart c) {
        c.getDescription().setEnabled(false);
        c.setNoDataText("No data");
        c.setTouchEnabled(true);
        c.setPinchZoom(true);

        XAxis x = c.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setGranularity(1f);
        x.setDrawGridLines(false);

        c.getAxisRight().setEnabled(false);
    }

    private void setupBarChart(BarChart c) {
        c.getDescription().setEnabled(false);
        c.setNoDataText("No data");
        c.setTouchEnabled(true);
        c.setPinchZoom(true);

        XAxis x = c.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setGranularity(1f);
        x.setDrawGridLines(false);

        c.getAxisRight().setEnabled(false);
    }

    private void applyLabels(XAxis xAxis, List<String> labels) {
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setLabelRotationAngle(45f);
        xAxis.setLabelCount(Math.min(labels.size(), 6), false);
    }

    private void setLoading(boolean loading) {
        progress.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnGenerateAggregate.setEnabled(!loading);
        btnGenerateForUser.setEnabled(!loading);
    }

    private void setError(@Nullable String message) {
        if (message == null || message.trim().isEmpty()) {
            txtError.setVisibility(View.GONE);
            txtError.setText("");
        } else {
            txtError.setVisibility(View.VISIBLE);
            txtError.setText(message);
        }
    }
}
