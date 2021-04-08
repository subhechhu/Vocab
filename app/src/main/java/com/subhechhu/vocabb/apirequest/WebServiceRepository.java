package com.subhechhu.vocabb.apirequest;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class WebServiceRepository {
    RequestQueue queue;
    MutableLiveData<String> responseData;

    public WebServiceRepository(Application application) {
        queue = Volley.newRequestQueue(application);
        responseData = new MutableLiveData<>();
    }

    public void getDetailsFromWeb(String url) {
        Log.e("TAG","owlbot url: "+url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            responseData.setValue(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        responseData.setValue(error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "token 22f202b5361e234f44893150c0a10f102b180f7b");
                params.put("Accept", "application/json");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public LiveData<String> setDetailsFromWeb() {
        return responseData;
    }
}

















