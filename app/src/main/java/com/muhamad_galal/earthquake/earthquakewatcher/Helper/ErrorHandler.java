package com.muhamad_galal.earthquake.earthquakewatcher.Helper;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * To handle all possible error could be happen!
 */
public class ErrorHandler {

    private VolleyError error;
    private Context context;

    public ErrorHandler(Context context, VolleyError error) {
        this.error = error;
        this.context = context;

        //TODO More detailed logic will be make!

        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Toast.makeText(context, "TimeoutError or NoConnectionError" , Toast.LENGTH_LONG).show();
        }else if (error instanceof AuthFailureError) {
            Toast.makeText(context, "AuthFailureError" , Toast.LENGTH_LONG).show();
        }else if (error instanceof ServerError) {
            Toast.makeText(context, "ServerError" , Toast.LENGTH_LONG).show();
        }else if (error instanceof NetworkError) {
            Toast.makeText(context, "NetworkError" , Toast.LENGTH_LONG).show();
        }else if (error instanceof ParseError) {
            Toast.makeText(context, "ParseError" , Toast.LENGTH_LONG).show();
        }
    }
}