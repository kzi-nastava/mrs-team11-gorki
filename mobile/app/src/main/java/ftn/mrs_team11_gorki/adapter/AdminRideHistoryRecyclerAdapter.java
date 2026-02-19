package ftn.mrs_team11_gorki.adapter;

import android.app.Dialog;
import android.view.*;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.dto.DriverRideHistoryDTO;
import ftn.mrs_team11_gorki.dto.GetRouteDTO;
import ftn.mrs_team11_gorki.dto.LocationDTO;
import ftn.mrs_team11_gorki.dto.PassengerInRideDTO;

public class AdminRideHistoryRecyclerAdapter
        extends RecyclerView.Adapter<AdminRideHistoryRecyclerAdapter.VH> {

    private final List<DriverRideHistoryDTO> data = new ArrayList<>();

    public void setData(List<DriverRideHistoryDTO> newData) {
        data.clear();
        if (newData != null) data.addAll(newData);
        notifyDataSetChanged();
    }

    public interface OnMapClickListener { void onMap(DriverRideHistoryDTO ride); }
    private OnMapClickListener mapListener;
    public void setOnMapClickListener(OnMapClickListener l) { this.mapListener = l; }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_with_go_to_map, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        DriverRideHistoryDTO r = data.get(position);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        h.txtTitle.setText("Ride No. " + safe(r.getRideId()));
        h.txtStartingTime.setText("Starting time: " + (r.getStartingTime() != null ? r.getStartingTime().format(formatter) : "-"));
        h.txtEndingTime.setText("Ending time: " + (r.getEndingTime() != null ? r.getEndingTime().format(formatter) : "-"));
        h.txtPrice.setText("Price: " + r.getPrice());
        h.txtCancelled.setText("Cancelled: " + r.isCanceled());
        h.txtPanic.setText("Panic activated: " + r.isPanicActivated());

        String sp = "-", dp = "-";
        GetRouteDTO route = r.getRoute();
        if (route != null && route.getLocations() != null) {
            List<LocationDTO> locs = route.getLocations();
            if (locs.size() > 0 && locs.get(0) != null) sp = safe(locs.get(0).getAddress());
            if (locs.size() > 1 && locs.get(1) != null) dp = safe(locs.get(1).getAddress());
        }
        h.txtStartingPoint.setText("Starting point: " + sp);
        h.txtDestination.setText("Destination: " + dp);

        // GO TO MAP dugme
        boolean has2 = route != null && route.getLocations() != null && route.getLocations().size() >= 2
                && validLoc(route.getLocations().get(0))
                && validLoc(route.getLocations().get(1));

        h.btnGoToMap.setVisibility(has2 ? View.VISIBLE : View.GONE);
        h.btnGoToMap.setOnClickListener(v -> {
            if (mapListener != null) mapListener.onMap(r);
        });

        h.btnMoreInfo.setOnClickListener(v -> {

            Dialog dialog = new Dialog(v.getContext());
            dialog.setContentView(R.layout.dialog_passengers);

            TableLayout table = dialog.findViewById(R.id.tablePassengers);
            Button btnClose = dialog.findViewById(R.id.btnClose);

            // obriši prethodne redove osim headera (header je index 0)
            while (table.getChildCount() > 1) {
                table.removeViewAt(1);
            }

            if (r.getPassengers() != null) {
                for (PassengerInRideDTO p : r.getPassengers()) {

                    TableRow row = new TableRow(v.getContext());
                    row.setPadding(8, 8, 8, 8);

                    TextView cEmail = makeCell(v, p.getEmail(), 2f);
                    TextView cFirst = makeCell(v, p.getFirstName(), 1.2f);
                    TextView cLast  = makeCell(v, p.getLastName(), 1.2f);
                    TextView cPhone = makeCell(v, p.getPhoneNumber(), 1.4f);

                    row.addView(cEmail);
                    row.addView(cFirst);
                    row.addView(cLast);
                    row.addView(cPhone);

                    table.addView(row);
                }
            }

            btnClose.setOnClickListener(x -> dialog.dismiss());

            dialog.show();
            if (dialog.getWindow() != null) {
                dialog.getWindow().setDimAmount(0.9f); // 0.0 - 1.0 (koliko zatamnjenje)
            }

        });

    }

    private TextView makeCell(View v, String text, float weight) {
        TextView tv = new TextView(v.getContext());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT, weight);
        tv.setLayoutParams(lp);
        tv.setText(text == null ? "" : text);
        tv.setTextSize(12);
        tv.setPadding(6, 6, 6, 6);
        return tv;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView txtTitle, txtStartingTime, txtEndingTime, txtStartingPoint, txtDestination, txtPrice, txtCancelled, txtPanic;
        Button btnMoreInfo, btnGoToMap;

        VH(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtStartingTime = itemView.findViewById(R.id.txtStartingTime);
            txtEndingTime = itemView.findViewById(R.id.txtEndingTime);
            txtStartingPoint = itemView.findViewById(R.id.txtStartingPoint);
            txtDestination = itemView.findViewById(R.id.txtDestination);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtCancelled = itemView.findViewById(R.id.txtCancelled);
            txtPanic = itemView.findViewById(R.id.txtPanic);
            btnMoreInfo = itemView.findViewById(R.id.btnMoreInfo);
            btnGoToMap = itemView.findViewById(R.id.btnGoToMap);
        }
    }
    private static String safe(Object o) { return o == null ? "-" : String.valueOf(o); }

    private static boolean validLoc(LocationDTO l) {
        if (l == null) return false;
        double lat = l.getLatitude();
        double lon = l.getLongitude();
        if ((lat == 0.0 && lon == 0.0)) return false;
        return lat >= -90 && lat <= 90 && lon >= -180 && lon <= 180;
    }
}