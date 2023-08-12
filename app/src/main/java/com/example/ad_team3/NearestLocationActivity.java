package com.example.ad_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Console;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NearestLocationActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private String nearestLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_location);

        TextView nearestLocationTextView = findViewById(R.id.nearestLocationTextView);
        nearestLocation = getIntent().getStringExtra("nearestLocation");
        nearestLocationTextView.setText("Your nearest location is: " + nearestLocation);

        seekBar = findViewById(R.id.numericSeekBar_near);
        TextView messageTextView = findViewById(R.id.messageTextView_near);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int selectedValue = i + 1;

                String message = "Selecting prediction for " + selectedValue + " months";
                messageTextView.setText(message);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        Button submit = findViewById(R.id.submit_nearestloc);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedValue = seekBar.getProgress() + 1;

                String baseUrl = "https://10.0.2.2:8443/";

                // Create a custom OkHttpClient with the TrustManager that trusts all certificates (only for testing, not recommended for production)
                OkHttpClient okHttpClient = getUnsafeOkHttpClient();

                // Create a Retrofit instance with your Spring Boot server URL and the custom OkHttpClient
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .client(okHttpClient)
//                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                // Create an instance of your PredictionModelApi interface
                PredictionModelApi api = retrofit.create(PredictionModelApi.class);

//                String startDate = "2022-06";
//                String endDate = "2023-06";
//                String modelId = "0600";
//                String nearestLocation = "changi";
                int periods = selectedValue + 12;
                int stationId = 0;
                if (nearestLocation == "Clementi"){
                    stationId = 1;
                } else {
                    stationId = 2;
                }
//                int wRMSE = 12;
//                int wMAPE = 12;

                // Make the API call
                Call<ResponseBody> call = api.retrieveRainfallData(
                        stationId,
                        periods
                );

                String fullUrl = call.request().url().toString();
                Log.d("Full URL", fullUrl);

                RequestBody requestBody = call.request().body();
                String requestBodyString = null;
                if (requestBody instanceof FormBody) {
                    FormBody formBody = (FormBody) requestBody;
                    StringBuilder bodyBuilder = new StringBuilder();
                    for (int i = 0; i < formBody.size(); i++) {
                        bodyBuilder.append(formBody.encodedName(i))
                                .append(": ")
                                .append(formBody.encodedValue(i))
                                .append(", ");
                    }
                    if (bodyBuilder.length() > 0) {
                        bodyBuilder.setLength(bodyBuilder.length() - 2);
                    }
                    requestBodyString = bodyBuilder.toString();
                }

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                // Handle the successful response here
                                String responseBody = response.body().string(); // Convert response body to string
                                Log.d("Nearest Location Activity", "Response JSON: " + responseBody);

                                // Now you can parse the JSON string as needed using a JSON library like Gson
                                // For example, if you have a PredictionModel class, you can deserialize the JSON:
                                Gson gson = new Gson();
                                PredictionModel predictionModel = gson.fromJson(responseBody, PredictionModel.class);


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // Handle the error here
                            try {
                                Log.e("Nearest Location Activity", "Error: " + response.message() + response.errorBody().string());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Handle network failure here
                        Log.e("Nearest Location Activity", "Network Failure: " + t.getMessage());
                    }
                });
            }

            // Method to create a custom OkHttpClient that trusts all certificates (only for testing, not recommended for production)
            private OkHttpClient getUnsafeOkHttpClient() {
                try {
                    // Create a TrustManager that trusts all certificates
                    TrustManager[] trustAllCertificates = new TrustManager[]{
                            new X509TrustManager() {
                                public X509Certificate[] getAcceptedIssuers() {
                                    return new X509Certificate[0];
                                }

                                public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                                }

                                public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                                }
                            }
                    };

                    // Install the custom TrustManager
                    SSLContext sslContext = SSLContext.getInstance("TLS");
                    sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

                    // Create an OkHttpClient that uses the custom TrustManager
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCertificates[0]);
                    builder.hostnameVerifier((hostname, session) -> true);

                    return builder.build();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });}}