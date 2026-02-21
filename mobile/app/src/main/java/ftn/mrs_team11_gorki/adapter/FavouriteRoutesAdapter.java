package ftn.mrs_team11_gorki.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.dto.GetRouteDTO;
import ftn.mrs_team11_gorki.dto.LocationDTO;

public class FavouriteRoutesAdapter extends BaseAdapter {

    public interface Listener {
        void onRemoveClicked(@NonNull GetRouteDTO route);
        void onChooseClicked(@NonNull GetRouteDTO route);
    }

    private final Context context;
    private final Listener listener;
    private final List<GetRouteDTO> routes = new ArrayList<>();

    public FavouriteRoutesAdapter(@NonNull Context context, @NonNull Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void submitList(@NonNull List<GetRouteDTO> newItems) {
        routes.clear();
        routes.addAll(newItems);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return routes.size();
    }

    @Override
    public GetRouteDTO getItem(int position) {
        return routes.get(position);
    }

    @Override
    public long getItemId(int position) {
        GetRouteDTO dto = getItem(position);
        return dto.getId() != null ? dto.getId() : position;
    }

    static class VH {
        TextView routeNo;
        TextView startingPointLabel;
        TextView destinationLabel;
        ListView stoppingPointsList;
        Button btnRemoveFromFav;
        Button btnChoose;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH h;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.route_card, parent, false);
            h = new VH();
            h.routeNo = convertView.findViewById(R.id.routeNo);
            h.startingPointLabel = convertView.findViewById(R.id.startingPointLabel);
            h.destinationLabel = convertView.findViewById(R.id.destinationLabel);
            h.stoppingPointsList = convertView.findViewById(R.id.stoppingPointsList);
            h.btnRemoveFromFav = convertView.findViewById(R.id.btnRemoveFromFav);
            h.btnChoose = convertView.findViewById(R.id.btnChoose);
            convertView.setTag(h);
        } else {
            h = (VH) convertView.getTag();
        }

        GetRouteDTO route = getItem(position);

        // Title
        h.routeNo.setText("Route #" + (position + 1));

        // Locations -> start / end / stops
        List<LocationDTO> locs = route.getLocations();
        String start = "";
        String end = "";
        List<String> stops = new ArrayList<>();

        if (locs != null && !locs.isEmpty()) {
            start = safeAddress(locs.get(0));
            end = safeAddress(locs.get(locs.size() - 1));

            // between start and end
            for (int i = 1; i < locs.size() - 1; i++) {
                String addr = safeAddress(locs.get(i));
                if (addr != null) {
                    addr = addr.trim();
                    if (!addr.isEmpty()) {
                        stops.add(addr);
                    }
                }
            }
        }

        h.startingPointLabel.setText(context.getString(R.string.startingPoint) + ": " + start);
        h.destinationLabel.setText(context.getString(R.string.destination) + ": " + end);

        // Stops list inside card
        StoppingPointsAdapter spAdapter = new StoppingPointsAdapter(context, stops);
        h.stoppingPointsList.setAdapter(spAdapter);

        // (Optional) If nested list height becomes 1 row only, uncomment this:
        h.stoppingPointsList.post(() -> setListViewHeightBasedOnChildren(h.stoppingPointsList));

        h.btnRemoveFromFav.setOnClickListener(v -> listener.onRemoveClicked(route));
        h.btnChoose.setOnClickListener(v -> listener.onChooseClicked(route));

        return convertView;
    }

    private String safeAddress(LocationDTO loc) {
        if (loc == null) return "";
        // PROMENI ako ti nije getAddress()
        String a = loc.getAddress();
        return a != null ? a : "";
    }

    // Ako ti nested ListView ne prikazuje sve stavke, koristi ovu helper metodu:
    @SuppressWarnings("unused")
    private void setListViewHeightBasedOnChildren(ListView listView) {
        android.widget.ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;

        int totalHeight = 0;
        int items = 0;

        int width = listView.getWidth();
        if (width <= 0) {
            // fallback: pola ekrana je dovoljno jer je u horizontal layout-u
            width = listView.getResources().getDisplayMetrics().widthPixels / 2;
        }
        int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            Object item = listAdapter.getItem(i);
            if (item instanceof String && ((String) item).trim().isEmpty()) continue;

            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(widthSpec, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
            items++;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (items == 0) ? 0 : totalHeight; // bez divider-a
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
