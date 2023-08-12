package com.example.ad_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

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

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        OkHttpClient okHttpClient = getUnsafeOkHttpClient();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8443/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        PredictionModelApi api = retrofit.create(PredictionModelApi.class);

        Call<DataRequest> call = api.receiveDataFromSpringBoot();
        call.enqueue(new Callback<DataRequest>() {

            @Override
            public void onResponse(Call<DataRequest> call, Response<DataRequest> response) {
                if (response.isSuccessful()) {
                    DataRequest data = response.body();
                    String parameter1 = data.getParameter1();
                    int parameter2 = data.getParameter2();
                    // Use the data as needed
                    Log.d("AndroidApp", "Parameter1: " + parameter1);
                    Log.d("AndroidApp", "Parameter2: " + parameter2);
                } else {
                    // Handle the error here
                    Log.e("AndroidApp", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DataRequest> call, Throwable t) {
                // Handle network failure here
                Log.e("AndroidApp", "Network Failure: " + t.getMessage());
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

