package com.example.ad_team3;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteApiManager {
    private final String baseUrl = "https://8.222.245.68:8443/";
    private final int periods = 12;

    private final int stationId;

    PredictionModelApi api;


    public RemoteApiManager(String nearestLocation){
        this.stationId = mapLocationToStationId(nearestLocation);

        OkHttpClient okHttpClient = getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(PredictionModelApi.class);
    }

    public void sendRainfallData(RainfallDataCallback callback) {
        Call<ResponseBody> call = api.sendRainfallData(
                stationId,
                periods
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        // Process the successful response and call the appropriate callback method
                        String responseBody = response.body().string();
                        JsonParser jsonParser = new JsonParser();
                        JsonObject jsonObject = jsonParser.parse(responseBody).getAsJsonObject();
                        JsonArray jsonDataArray = jsonObject.getAsJsonArray("data");
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<RainfallData>>() {}.getType();
                        List<RainfallData> rainfallDataList = gson.fromJson(jsonDataArray, listType);
                        callback.onRainfallDataReceived(rainfallDataList); // Call the callback method for successful response
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("Remote API Manager", "Error: " + response.message());
                    callback.onRainfallDataError("Error: " + response.message()); // Call the callback method for network failure
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Remote API Manager", "Network Failure: " + t.getMessage());
                callback.onRainfallDataError("Network Failure: " + t.getMessage()); // Call the callback method for network failure
            }
        });
    }



    // Empty checkServerTrusted methods in custom X509TrustManager implementation, no certificate issued from TCA available
    private OkHttpClient getUnsafeOkHttpClient() {
        try {
            TrustManager[] trustAllCertificates = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCertificates[0]);
            builder.hostnameVerifier((hostname, session) -> true);

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int mapLocationToStationId(String nearestLocation){
        switch (nearestLocation) {
            case "Clementi":
                return 1;
            case "Changi":
                return 2;
            // Handle more locations if adding, integer has to match to the springboot mapping
            default:
                return -1;
        }
    }
}
