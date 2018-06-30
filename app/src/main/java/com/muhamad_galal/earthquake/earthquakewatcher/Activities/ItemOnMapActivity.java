package com.muhamad_galal.earthquake.earthquakewatcher.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.muhamad_galal.earthquake.earthquakewatcher.Helper.QuakeDetails;
import com.muhamad_galal.earthquake.earthquakewatcher.Model.EarthQuake;
import com.muhamad_galal.earthquake.earthquakewatcher.R;
import com.muhamad_galal.earthquake.earthquakewatcher.UI.CustomViewInfoAdapter;

import java.text.DateFormat;
import java.util.Date;

public class ItemOnMapActivity extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private EarthQuake earthQuake;
    private String title;
    private Double lat;
    private Double lon;
    private Double mag;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_on_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        earthQuake = (EarthQuake) getIntent().getSerializableExtra("earthQuake");

        queue = Volley.newRequestQueue(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        title = earthQuake.getPlace();
        mag = earthQuake.getMagnitude();
        lat = earthQuake.getLat();
        lon = earthQuake.getLon();

        // formatting time
        DateFormat dateFormat = DateFormat.getDateInstance();
        String formated = dateFormat.format(new Date(earthQuake.getTime()).getTime());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(earthQuake.getPlace());
        markerOptions.position(new LatLng(earthQuake.getLat() ,earthQuake.getLon()));
        markerOptions.snippet("Magnitude: " + earthQuake.getMagnitude() + "\n"+"Date: " + formated);

        Marker marker = mMap.addMarker(markerOptions);
        marker.setTag(earthQuake.getDetailLink());

        if (earthQuake.getMagnitude() > 2.0 ){

            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(new LatLng(earthQuake.getLat() , earthQuake.getLon()));
            circleOptions.radius(30000);
            circleOptions.strokeWidth(2.5f);
            circleOptions.strokeColor(Color.MAGENTA);
            circleOptions.fillColor(Color.RED);
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            mMap.addCircle(circleOptions);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(earthQuake.getLat() , earthQuake.getLon())));
        mMap.setInfoWindowAdapter(new CustomViewInfoAdapter(this));
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        new QuakeDetails(this , marker.getTag().toString() , queue);
    }
}