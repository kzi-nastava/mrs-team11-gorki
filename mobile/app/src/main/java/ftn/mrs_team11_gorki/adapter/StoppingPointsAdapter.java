package ftn.mrs_team11_gorki.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ftn.mrs_team11_gorki.R;

public class StoppingPointsAdapter extends ArrayAdapter<String> {

    public StoppingPointsAdapter(@NonNull Context context, @NonNull List<String> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.stopping_point_label, parent, false);
        }

        TextView tv = (TextView) v;
        String item = getItem(position);
        tv.setText(item != null ? item : "");
        return v;
    }
}
