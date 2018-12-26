package com.muhamad_galal.earthquake.earthquakewatcher.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.muhamad_galal.earthquake.earthquakewatcher.Helper.ErrorHandler;
import com.muhamad_galal.earthquake.earthquakewatcher.Helper.QuakeDetails;
import com.muhamad_galal.earthquake.earthquakewatcher.Model.EarthQuake;
import com.muhamad_galal.earthquake.earthquakewatcher.R;
import com.muhamad_galal.earthquake.earthquakewatcher.UI.CustomViewInfoAdapter;
import com.muhamad_galal.earthquake.earthquakewatcher.Util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,GoogleMap.OnInfoWindowClickListener ,
        GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private RequestQueue queue;
    private BitmapDescriptor[] colorDescriptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button showListBtn = findViewById(R.id.showListBtn);
        showListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this , QuakesListActivity.class));
            }
        });

        queue = Volley.newRequestQueue(this);

        // create an array of color to differentiate between markers
        colorDescriptor = new BitmapDescriptor[]{
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
        };
        getEarthQuakes();
    }

    /**
     * get all earthquakes
     */
    private void getEarthQuakes() {

        final EarthQuake earthQuake = new EarthQuake();

        // Establish a connection to the API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray features = response.getJSONArray("features");

                    for (int i = 0 ; i < 200 ; i++){
                        // invoke data fom the given URL
                        // get properties object
                        JSONObject properties = features.getJSONObject(i).getJSONObject("properties");
                        // get coordinates object
                        JSONObject geometry = features.getJSONObject(i).getJSONObject("geometry");
                        // get geometry array
                        JSONArray coordinates = geometry.getJSONArray("coordinates");

                        // get latitude and longitude
                        double lon = coordinates.getDouble(0);
                        double lat = coordinates.getDouble(1);
                        earthQuake.setLat(lat);
                        earthQuake.setLon(lon);

                        earthQuake.setPlace(properties.getString("place"));
                        earthQuake.setType(properties.getString("type"));
                        earthQuake.setMagnitude(properties.getDouble("mag"));
                        earthQuake.setDetailLink(properties.getString("detail"));
                        earthQuake.setTime(properties.getLong("time"));
                        // formatting time
                        DateFormat dateFormat = DateFormat.getDateInstance();
                        String formatted = dateFormat.format(new Date(Long.valueOf(properties.getString("time"))).getTime());

                        // setting up marker
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.icon(colorDescriptor[Constants.randomColor(colorDescriptor.length , 0)]);
                        markerOptions.title(earthQuake.getPlace());
                        markerOptions.position(new LatLng(earthQuake.getLat() ,earthQuake.getLon()));
                        markerOptions.snippet("Magnitude: " + earthQuake.getMagnitude() + "\n"+"Date: " + formatted);

                        Marker marker = mMap.addMarker(markerOptions);
                        // set onClick focus on detailed link
                        marker.setTag(earthQuake.getDetailLink());
                        // put zooming factory on map
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat , lon ) , 1));

                        // Adding circle to marker that magnitude > X
                        if (earthQuake.getMagnitude() > 2.0 ){

                            CircleOptions circleOptions = new CircleOptions();
                            circleOptions.center(new LatLng(lat ,lon));
                            circleOptions.radius(30000);
                            circleOptions.strokeWidth(2.5f);
                            circleOptions.strokeColor(Color.MAGENTA);
                            circleOptions.fillColor(Color.RED);
                            // adding circle to the Marker
                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            // adding circle to the Map
                            mMap.addCircle(circleOptions);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //deal with Volley Error
                new ErrorHandler(MapsActivity.this , error);
            }
        });
        queue.add(jsonObjectRequest);
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
        // bind Location with map
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        // check user android device version to get a location permission
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                // ask for permission
                ActivityCompat.requestPermissions(this , new String[] {Manifest.permission.ACCESS_FINE_LOCATION} , 1);
            }else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        mMap.setInfoWindowAdapter(new CustomViewInfoAdapter(this));
        mMap.setOnInfoWindowClickListener(this);
    }

    // handle user response for Location permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }else {
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        new QuakeDetails(this , Objects.requireNonNull(marker.getTag()).toString(), queue);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}