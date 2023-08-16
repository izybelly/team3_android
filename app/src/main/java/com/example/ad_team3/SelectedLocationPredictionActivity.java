package com.example.ad_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class SelectedLocationPredictionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_location_prediction);
        Intent intent = getIntent();
        ArrayList<String> selectedLocations = intent.getStringArrayListExtra("checkedItems");

        ListView listView = findViewById(R.id.chartListView);
        SelectedStationsListAdapter adapter = new SelectedStationsListAdapter(this,selectedLocations);
        listView.setAdapter(adapter);
    }
}