package com.example.ad_team3;

import static org.junit.Assert.assertEquals;

import com.example.ad_team3.PredictionModelApi;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.ResponseBody;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteApiIntegrationTest {

    private PredictionModelApi api;

    @Before
    public void setUp() {
        OkHttpClient okHttpClient = getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://8.222.245.68:8443/") // Change this to your testing base URL
                .client(okHttpClient) // Use the custom OkHttpClient with disabled SSL verification
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(PredictionModelApi.class);
    }

    @Test
    public void testApiIsReachable() throws IOException {
        Call<ResponseBody> call = api.sendRainfallData(1, 12); // Replace with appropriate parameters
        Response<ResponseBody> response = call.execute();

        assertEquals(200, response.code());
    }

    // Disable SSL verification
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
}
