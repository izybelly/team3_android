package com.example.ad_team3;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PredictionModelApi {
    @GET("/api/getDefaultModel")
    Call<PredictionModel> getDefaultModel();
}
