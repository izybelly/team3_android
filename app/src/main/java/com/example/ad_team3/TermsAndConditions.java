package com.example.ad_team3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TermsAndConditions extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String TERMS_ACCEPTED_KEY = "termsAccepted";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        Button terms_conditions = findViewById(R.id.agreeButton);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean termsAccepted = prefs.getBoolean(TERMS_ACCEPTED_KEY, false);

        if (termsAccepted) {
            accessLocation();
            return;
        }

        terms_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(TermsAndConditions.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(TermsAndConditions.this,
                            new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                    // Permission is already granted, proceed with location access
                    prefs.edit().putBoolean(TERMS_ACCEPTED_KEY, true).apply();
                    accessLocation();
                }
            }
        });


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
                Toast.makeText(this, "Kindly review and accept the Terms & Conditions", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void accessLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (lastKnownLocation != null) {
                double userLat = lastKnownLocation.getLatitude();
                double userLong = lastKnownLocation.getLongitude();

                // Log the latitude and longitude
                Log.d("Location", "Latitude: " + userLat + ", Longitude: " + userLong);

                List<CustomLocation> locations = new ArrayList<>();
                locations.add(new CustomLocation("Changi", 1.3678, 103.9826));
                locations.add(new CustomLocation("Clementi", 1.3337, 103.7768));

                CustomLocation closestLoc = LocationUtils.findNearestLocation(lastKnownLocation, locations);
                String nearestLocation = closestLoc.getName();

                if (nearestLocation != null) {
                    double distanceToNearestLocation = LocationUtils.calculateDistance(lastKnownLocation, closestLoc);
                    Intent intent = new Intent(TermsAndConditions.this, DefaultLocationActivity.class);
                    intent.putExtra("nearestLocation", nearestLocation);
                    intent.putExtra("distance", distanceToNearestLocation);
                    startActivity(intent);
                }
            }
        }
    }

}