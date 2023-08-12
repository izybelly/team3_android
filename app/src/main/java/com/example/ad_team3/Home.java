package com.example.ad_team3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private HandlerThread locationThread;
    private Handler locationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);

        locationThread = new HandlerThread("LocationThread");
        locationThread.start();
        locationHandler = new Handler(locationThread.getLooper());

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Home.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(Home.this,
                            new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                    // Permission is already granted, proceed with location access
                    locationHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            accessLocation();
                        }
                    });
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, AllLocationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Quit the HandlerThread when the activity is destroyed
        if (locationThread != null) {
            locationThread.quit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with location access
                accessLocation();
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method to access user's location
    private void accessLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    double userLat = location.getLatitude();
                    double userLong = location.getLongitude();

                    // Coordinates of Changi station (double check)
                    double changiLat = 1.3678;
                    double changiLong = 103.9826;

                    // Coordinates of Clementi Road station (double check)
                    double clementiLat = 1.3337;
                    double clementiLong = 103.7768;

//                    // Coordinates of Sentosa station (double check)
//                    double sentosaLat = 1.27472;
//                    double sentosaLong = 103.80389;
//
//                    // Coordinates of Kranji Way station (double check)
//                    double kranjiLat = 1.4387;
//                    double kranjiLong = 103.7363;

                    float[] results = new float[1];
                    Location.distanceBetween(userLat, userLong, changiLat, changiLong, results);
                    float distanceToChangi = results[0];

                    Location.distanceBetween(userLat, userLong, clementiLat, clementiLong, results);
                    float distanceToClementi = results[0];

//                    Location.distanceBetween(userLat, userLong, sentosaLat, sentosaLong, results);
//                    float distanceToSentosa = results[0];
//
//                    Location.distanceBetween(userLat, userLong, kranjiLat, kranjiLong, results);
//                    float distanceToKranji = results[0];

                    String nearestLocation;
                    if (distanceToChangi < distanceToClementi) {
                        nearestLocation = "Changi";
//                    } else if (distanceToClementi < distanceToSentosa && distanceToClementi < distanceToKranji) {
//                        nearestLocation = "Clementi";
//                    } else if (distanceToSentosa < distanceToKranji) {
//                        nearestLocation = "Sentosa";
                    } else {
                        nearestLocation = "Changi";
                    }

                    Intent intent = new Intent(Home.this, NearestLocationActivity.class);
                    intent.putExtra("nearestLocation", nearestLocation);
                    startActivity(intent);
                }
            });
        }
    }
}