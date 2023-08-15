package com.example.ad_team3;
import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PredictionModelApi {

    @POST("/api/getPredictionModel")
    Call<ResponseBody> sendRainfallData(
            @Query("stationId") int stationId,
            @Query("predictionPeriod") int periods
    );



}
