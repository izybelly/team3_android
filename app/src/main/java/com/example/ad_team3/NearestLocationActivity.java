package com.example.ad_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.Console;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
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

                String jsonBody = "{\n" +
                        "  \"data\": [\n" +
                        "    {\n" +
                        "      \"ds\": \"2022-06\",\n" +
                        "      \"y\": 211.8\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"ds\": \"2022-07\",\n" +
                        "      \"y\": 145.2\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"ds\": \"2022-08\",\n" +
                        "      \"y\": 141.4\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"ds\": \"2022-09\",\n" +
                        "      \"y\": 121.0\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"ds\": \"2022-10\",\n" +
                        "      \"y\": 279.6\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"ds\": \"2022-11\",\n" +
                        "      \"y\": 313.8\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"ds\": \"2022-12\",\n" +
                        "      \"y\": 215.4\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"ds\": \"2023-01\",\n" +
                        "      \"y\": 302.6\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"ds\": \"2023-02\",\n" +
                        "      \"y\": 324.40000000000003\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"ds\": \"2023-03\",\n" +
                        "      \"y\": 243.4\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"ds\": \"2023-04\",\n" +
                        "      \"y\": 222.8\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"ds\": \"2023-05\",\n" +
                        "      \"y\": 112.8\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"ds\": \"2023-06\",\n" +
                        "      \"y\": 228.4\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}";
                Log.d("JsonBody",jsonBody);
                // Parse the JSON string into a JsonObject
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObjectbody = jsonParser.parse(jsonBody).getAsJsonObject();


//                List<RainfallData> rainfallDataEntries = new ArrayList<>();
//                RainfallData ent_2022_06 = new RainfallData();
//                ent_2022_06.setY(211.8);
//                ent_2022_06.setDs("2022-06");
//
//                RainfallData ent_2022_07 = new RainfallData();
//                ent_2022_07.setY(145.2);
//                ent_2022_07.setDs("2022-07");
//
//                RainfallData ent_2022_08 = new RainfallData();
//                ent_2022_08.setY(141.4);
//                ent_2022_08.setDs("2022-08");
//
//                RainfallData ent_2022_09 = new RainfallData();
//                ent_2022_09.setY(121.0);
//                ent_2022_09.setDs("2022-09");
//
//                RainfallData ent_2022_10 = new RainfallData();
//                ent_2022_10.setY(279.6);
//                ent_2022_10.setDs("2022-10");
//
//                RainfallData ent_2022_11 = new RainfallData();
//                ent_2022_11.setY(313.8);
//                ent_2022_11.setDs("2022-11");
//
//                RainfallData ent_2022_12 = new RainfallData();
//                ent_2022_12.setY(215.4);
//                ent_2022_12.setDs("2022-12");
//
//                RainfallData ent_2023_01 = new RainfallData();
//                ent_2023_01.setY(302.6);
//                ent_2023_01.setDs("2023-01");
//
//                RainfallData ent_2023_02 = new RainfallData();
//                ent_2023_02.setY(324.40000000000003);
//                ent_2023_02.setDs("2023-02");
//
//                RainfallData ent_2023_03 = new RainfallData();
//                ent_2023_03.setY(243.4);
//                ent_2023_03.setDs("2023-03");
//
//                RainfallData ent_2023_04 = new RainfallData();
//                ent_2023_04.setY(222.8);
//                ent_2023_04.setDs("2023-04");
//
//                RainfallData ent_2023_05 = new RainfallData();
//                ent_2023_05.setY(112.8);
//                ent_2023_05.setDs("2023-05");
//
//                RainfallData ent_2023_06 = new RainfallData();
//                ent_2023_06.setY(228.4);
//                ent_2023_06.setDs("2023-06");
//
//                RainfallDataList rainfallDataList = new RainfallDataList();
//                rainfallDataList.setData(rainfallDataEntries);

                String baseUrl = "http://8.222.245.68:8080/";

                // Create a custom OkHttpClient with the TrustManager that trusts all certificates (only for testing, not recommended for production)
                OkHttpClient okHttpClient = getUnsafeOkHttpClient();

                // Create a Retrofit instance with your Spring Boot server URL and the custom OkHttpClient
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                // Create an instance of your PredictionModelApi interface
                PredictionModelApi api = retrofit.create(PredictionModelApi.class);

                String startDate = "2022-06";
                String endDate = "2023-06";
                String modelId = "0600";
                String nearestLocation = "changi";
                int periods = selectedValue;
                int wRMSE = 12;
                int wMAPE = 12;

                // Make the API call
                Call<ResponseBody> call = api.sendRainfallData(
                        startDate,
                        endDate,
                        modelId,
                        nearestLocation,
                        periods,
                        wRMSE,
                        wMAPE,
                        jsonObjectbody
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
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonObject = jsonParser.parse(responseBody).getAsJsonObject();
                                JsonArray jsonDataArray = jsonObject.getAsJsonArray("data"); // Extract the "data" array

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<RainfallData>>() {}.getType();
                                List<RainfallData> rainfallDataList = gson.fromJson(jsonDataArray, listType);
                                visualize_return(rainfallDataList);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // Handle the error here
                            Log.e("Nearest Location Activity", "Error: " + response.message());
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
        });}
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    protected void visualize_return(List<RainfallData> rainfallDataList) {
        Intent intent = new Intent(NearestLocationActivity.this, RainfallChart.class);

        // Pass the list of RainfallData as an extra in the intent
        intent.putExtra("rainfallDataList", (Serializable) rainfallDataList);
        intent.putExtra("station",nearestLocation);
        // Start the next activity
        startActivity(intent);
    }

}
