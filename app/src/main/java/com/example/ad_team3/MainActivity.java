package com.example.ad_team3;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a custom OkHttpClient with the TrustManager that trusts all certificates (only for testing, not recommended for production)
        OkHttpClient okHttpClient = getUnsafeOkHttpClient();

        // Create a Retrofit instance with your Spring Boot server URL and the custom OkHttpClient
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2:8443/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of your PredictionModelApi interface
        PredictionModelApi api = retrofit.create(PredictionModelApi.class);

        // Make the API call
        Call<PredictionModel> call = api.getDefaultModel();
        call.enqueue(new Callback<PredictionModel>() {
            @Override
            public void onResponse(Call<PredictionModel> call, Response<PredictionModel> response) {
                if (response.isSuccessful()) {
                    // Handle the successful response here
                    PredictionModel predictionModel = response.body();
                    // Use the predictionModel data as needed
                    // For example, you can log the model details to the console
                    String message = "Default Model ID: " + predictionModel.getModelId();
                    Log.d("MainActivity", message);
                } else {
                    // Handle the error here
                    Log.e("MainActivity", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PredictionModel> call, Throwable t) {
                // Handle network failure here
                Log.e("MainActivity", "Network Failure: " + t.getMessage());
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
}
