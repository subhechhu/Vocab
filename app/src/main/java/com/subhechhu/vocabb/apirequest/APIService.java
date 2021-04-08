package com.subhechhu.vocabb.apirequest;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET
    Call<String> makeRequest();
}