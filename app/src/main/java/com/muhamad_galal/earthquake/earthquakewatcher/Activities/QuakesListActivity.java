package com.muhamad_galal.earthquake.earthquakewatcher.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.muhamad_galal.earthquake.earthquakewatcher.Model.EarthQuake;
import com.muhamad_galal.earthquake.earthquakewatcher.R;
import com.muhamad_galal.earthquake.earthquakewatcher.Util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuakesListActivity extends AppCompatActivity {

    private ArrayList<String> arrayList;
    private ListView listView;
    private RequestQueue queue;
    private ArrayAdapter arrayAdapter;
    private List<EarthQuake> earthQuakeList;
    private EarthQuake earthQuake;
    private BitmapDescriptor[] colorDescriptor;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quakes_list);


        earthQuake = new EarthQuake();
        earthQuakeList = new ArrayList<>();
        arrayList = new ArrayList<>();

        listView = (ListView) findViewById(R.id.quakeList);

        queue = Volley.newRequestQueue(this);

        getAllQuakes(Constants.URL);
    }

    private void getAllQuakes(String url) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray features = response.getJSONArray("features");

                            for (int i = 0; i < Constants.LIMIT ; i++){
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

                                arrayList.add(earthQuake.getPlace());

                                arrayAdapter = new ArrayAdapter<>(QuakesListActivity.this , android.R.layout.simple_list_item_1
                                        , android.R.id.text1 , arrayList);

                                listView.setAdapter(arrayAdapter);
                                arrayAdapter.notifyDataSetChanged();
                            }

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(QuakesListActivity.this , ItemOnMapActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("earthQuake" , earthQuake);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //TODO remove the unwanted
                Toast.makeText(QuakesListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                recreate();
            }
        });

        queue.add(jsonObjectRequest);
    }
}
