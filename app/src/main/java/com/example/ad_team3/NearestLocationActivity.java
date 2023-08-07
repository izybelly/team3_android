package com.example.ad_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class NearestLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_location);

        TextView nearestLocationTextView = findViewById(R.id.nearestLocationTextView);
        String nearestLocation = getIntent().getStringExtra("nearestLocation");
        nearestLocationTextView.setText("Your nearest location is: " + nearestLocation);
    }
}