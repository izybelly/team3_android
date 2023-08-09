package com.example.ad_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class NearestLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_location);

        TextView nearestLocationTextView = findViewById(R.id.nearestLocationTextView);
        String nearestLocation = getIntent().getStringExtra("nearestLocation");
        nearestLocationTextView.setText("Your nearest location is: " + nearestLocation);

        SeekBar seekBar = findViewById(R.id.numericSeekBar_near);
        TextView messageTextView = findViewById(R.id.messageTextView_near);

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

        Button submit = findViewById(R.id.submit_nearestloc);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}