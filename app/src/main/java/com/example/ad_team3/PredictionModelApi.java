package com.example.ad_team3;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PredictionModelApi {
    @GET("/api/getDefaultModel")
    Call<PredictionModel> getDefaultModel();

    @POST("/user/neuralprophet")
    Call<ResponseBody> sendRainfallData(
            @Query("sDate") String startDate,
            @Query("eDate") String endDate,
            @Query("mId") String modelId,
            @Query("sId") String locationId,
            @Query("periods") int periods,
            @Query("wRMSE") int wRMSE,
            @Query("wMAPE") int wMAPE,
            @Body String rainfallDataList
    );



}
