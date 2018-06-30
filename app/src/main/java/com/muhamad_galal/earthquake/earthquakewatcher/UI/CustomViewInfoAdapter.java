package com.muhamad_galal.earthquake.earthquakewatcher.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.muhamad_galal.earthquake.earthquakewatcher.R;

public class CustomViewInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private View view;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomViewInfoAdapter(Context context) {
        this.context = context;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (layoutInflater != null) {
            view = layoutInflater.inflate(R.layout.custom_info_window , null);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        TextView title = (TextView) view.findViewById(R.id.winTitle);
        title.setText(marker.getTitle());

        TextView magnitude = (TextView) view.findViewById(R.id.magnitude);
        magnitude.setText(marker.getSnippet());
        return view;
    }
}