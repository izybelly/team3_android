package com.example.ad_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
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

public class AllLocationActivity extends AppCompatActivity {
    List<Integer> stationIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_location);

        SeekBar seekBar = findViewById(R.id.numericSeekBar_all);
        TextView messageTextView = findViewById(R.id.messageTextView_all);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int selectedValue = i + 1;

                String message = "Selecting prediction for " + selectedValue + " months";
                messageTextView.setText(message);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button submit = findViewById(R.id.submit_allloc);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox changi = findViewById(R.id.checkbox1);
                CheckBox clementi = findViewById(R.id.checkbox2);
//                CheckBox sentosa = findViewById(R.id.checkbox3);
//                CheckBox kranji = findViewById(R.id.checkbox4);

                int selectedValue = seekBar.getProgress() + 1;
                String baseUrl = "https://10.0.2.2:8443/";

                boolean changiIsChecked = changi.isChecked();
                boolean clementiIsChecked = clementi.isChecked();
//                boolean sentosaIsChecked = sentosa.isChecked();
//                boolean kranjiIsChecked = kranji.isChecked();

                OkHttpClient okHttpClient = getUnsafeOkHttpClient();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .client(okHttpClient)
//                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                PredictionModelApi api = retrofit.create(PredictionModelApi.class);


                if (changiIsChecked) {
                    stationIds.add(2);
                }
                if (clementiIsChecked) {
                    stationIds.add(1);
                }

                int periods = selectedValue;

                Call<ResponseBody> call;
                for (int stationId : stationIds) {
                    call = api.retrieveRainfallData(
                            stationId,
                            periods
                    );

                    String fullUrl = call.request().url().toString();
                    Log.d("Full URL", fullUrl);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    // Handle the successful response here
                                    String responseBody = response.body().string(); // Convert response body to string
                                    Log.d("All Location Activity", "Response JSON for stationId: " + stationId + " " + responseBody);

                                    // Now you can parse the JSON string as needed using a JSON library like Gson
                                    // For example, if you have a PredictionModel class, you can deserialize the JSON:
                                    Gson gson = new Gson();
                                    RainfallDataList rainfallDataList = gson.fromJson(responseBody, RainfallDataList.class);

                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat yearMonthFormat = new SimpleDateFormat("yyyy-MM");
                                    String currentYearMonth = yearMonthFormat.format(calendar.getTime());

                                    RainfallData currentData = null;

                                    for (RainfallData data: rainfallDataList.getData()){
                                        if (data.getDate().equals(currentYearMonth)){
                                            currentData = data;
                                            break;
                                        }
                                    }
                                    if (currentData != null){
                                        double actualRainfall = currentData.getActualRainfall();
                                        double predictedRainfall = currentData.getPredictedRainfall();

                                        Log.d("All Location Activitity", "Actual Rainfall for " + currentYearMonth + ": " + actualRainfall);
                                        Log.d("All Location Activitity", "Predicted Rainfall for " + currentYearMonth + ": " + predictedRainfall);
                                        String serializedData = gson.toJson(rainfallDataList);
                                        visualize_return(serializedData, stationId);

                                    } else {
                                        Log.e("All Location Activity", "Data not found for" + currentYearMonth);
                                    }



                                    // Handle the parsed predictionModel data
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                // Handle the error here
                                try {
                                    Log.e("All Location Activity", "Error: " + response.message() + response.errorBody().string());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            // Handle network failure here
                            Log.e("All Location Activity", "Network Failure: " + t.getMessage());
                        }
                    });
                }






}

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

    protected void visualize_return(String serializedData, int stationId) {
        Intent intent = new Intent(AllLocationActivity.this, RainfallChart.class);

        String stationIdStr;
        if (stationId == 1){
            stationIdStr = "Clementi";
        } else {
            stationIdStr = "Changi";
        }



        // Pass the list of RainfallData as an extra in the intent
        intent.putExtra("rainfallDataListJson", serializedData);
        intent.putExtra("station", stationIdStr);
        // Start the next activity
        startActivity(intent);
    }


}