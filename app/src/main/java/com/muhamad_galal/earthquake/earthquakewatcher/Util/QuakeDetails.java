package com.muhamad_galal.earthquake.earthquakewatcher.Util;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuakeDetails {

    private String url;
    private Context context;

    public QuakeDetails(final Context context , String url , final RequestQueue queue ) {
        this.url = url;
        this.context = context;

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String detailsURL = "";

                        try {
                            JSONObject properties = response.getJSONObject("properties");
                            JSONObject products = properties.getJSONObject("products");
                            JSONArray geoserve = products.getJSONArray("geoserve");
                            for (int i = 0 ; i < geoserve.length() ; i++){
                                JSONObject geoserveJSONObject = geoserve.getJSONObject(0);
                                JSONObject contentObj = geoserveJSONObject.getJSONObject("contents");
                                JSONObject geoURLJsonObj = contentObj.getJSONObject("geoserve.json");

                                detailsURL = geoURLJsonObj.getString("url");
                            }

                            new QuakeMoreDetails(context , detailsURL , queue);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //TODO ERROR LOGIC
                Toast.makeText(context, "getQuakeDetails Error \n " + error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        queue.add(objectRequest);
    }
}
