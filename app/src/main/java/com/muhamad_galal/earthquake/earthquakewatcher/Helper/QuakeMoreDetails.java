package com.muhamad_galal.earthquake.earthquakewatcher.Helper;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.muhamad_galal.earthquake.earthquakewatcher.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuakeMoreDetails {

    private String url;
    private Context context;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    public QuakeMoreDetails(final Context context , String url , RequestQueue queue) {
        this.url = url;
        this.context = context;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                dialogBuilder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.popup , null);

                Button dismissButton = (Button) view.findViewById(R.id.dismissPop);
                Button dismissButtonTop = (Button) view.findViewById(R.id.dismissPopTop);
                TextView popList = (TextView) view.findViewById(R.id.popList);
                WebView htmlPop = (WebView) view.findViewById(R.id.htmlWebView);

                StringBuilder stringBuilder = new StringBuilder();
                //parse data from JSON
                try {
                    JSONArray cities = response.getJSONArray("cities");
                    for (int i = 0 ; i < cities.length() ; i++){

                        JSONObject citiesObj = cities.getJSONObject(i);

                        stringBuilder.append("City: " + citiesObj.getString("name")
                                + "\n" + "Distance: " + citiesObj.getString("distance")
                                + "\n" + "Population: " + citiesObj.getString("population"));

                        stringBuilder.append("\n\n");
                    }
                    popList.setText(stringBuilder);

                    // setup webView data
                    if (response.has("tectonicSummary") && response.getString("tectonicSummary") != null){

                        JSONObject tectonicSummary = response.getJSONObject("tectonicSummary");

                        if (tectonicSummary.has("text") && tectonicSummary.getString("text") != null){
                            String summary = tectonicSummary.getString("text");

                            htmlPop.loadDataWithBaseURL(null , summary , "text/html" , "UTF-8" , null);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // setup Button onClickListener
                dismissButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dismissButtonTop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialogBuilder.setView(view);
                dialog = dialogBuilder.create();
                dialog.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //deal with Volley Error
                new ErrorHandler(context, error);
            }
        });
        queue.add(jsonObjectRequest);
    }
}