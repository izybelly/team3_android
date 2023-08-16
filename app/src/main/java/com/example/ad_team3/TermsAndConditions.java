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

    private static final String[] LOCATION_PERMISSIONS = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        Button terms_conditions = findViewById(R.id.agreeButton);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean termsAccepted = prefs.getBoolean(TERMS_ACCEPTED_KEY, false);

        if (termsAccepted) {
            Intent intent = new Intent(TermsAndConditions.this, DefaultLocationActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        terms_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(TermsAndConditions.this, LOCATION_PERMISSIONS[0])
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(TermsAndConditions.this, LOCATION_PERMISSIONS[1])
                                != PackageManager.PERMISSION_GRANTED) {

                    // Permissions are not granted, request them
                    ActivityCompat.requestPermissions(TermsAndConditions.this,
                            LOCATION_PERMISSIONS,
                            LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                    // Permissions are already granted, proceed with location access
                    prefs.edit().putBoolean(TERMS_ACCEPTED_KEY, true).apply();
                    Intent intent = new Intent(TermsAndConditions.this, DefaultLocationActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(TermsAndConditions.this, DefaultLocationActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(this, "Kindly review and accept the Terms & Conditions", Toast.LENGTH_SHORT).show();
            }
        }
    }

}