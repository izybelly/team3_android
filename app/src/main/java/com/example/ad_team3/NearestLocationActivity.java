package com.example.ad_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NearestLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_location);

        TextView nearestLocationTextView = findViewById(R.id.nearestLocationTextView);
        String nearestLocation = getIntent().getStringExtra("nearestLocation");
        nearestLocationTextView.setText("Your nearest location is: " + nearestLocation);

        Button submit = findViewById(R.id.submit_nearestloc);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}