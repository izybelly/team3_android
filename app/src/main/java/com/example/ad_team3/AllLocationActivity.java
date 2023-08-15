package com.example.ad_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllLocationActivity extends AppCompatActivity {
    private ArrayList<String> checkedItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_location);
        ImageView pinChangi = findViewById(R.id.pin_Changi);
        ImageView pinClementi = findViewById(R.id.pin_Clementi);
        CheckBox changi = findViewById(R.id.cbChangi);
        CheckBox clementi = findViewById(R.id.cbClementi);
        CheckBox sentosa = findViewById(R.id.cbSentosa);
        CheckBox kranji = findViewById(R.id.cbKranji);

        changi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Change the color filter based on the checkbox state
                int color = isChecked ? Color.RED : Color.BLACK;
                pinChangi.setColorFilter(color);
            }
        });
        clementi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Change the color filter based on the checkbox state
                int color = isChecked ? Color.RED : Color.BLACK;
                pinClementi.setColorFilter(color);
            }
        });



        Button submit = findViewById(R.id.submit_allloc);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkedItems.clear();

                if (changi.isChecked()) {
                    checkedItems.add("Changi");
                }
                if (clementi.isChecked()) {
                    checkedItems.add("Clementi");
                }
                if (sentosa.isChecked()) {
                    checkedItems.add("Sentosa");
                }
                if (kranji.isChecked()) {
                    checkedItems.add("Kranji");
                }

                // Start the next activity and pass the list via Intent
                Intent intent = new Intent(AllLocationActivity.this, SelectedLocationPredictionActivity.class);
                intent.putStringArrayListExtra("checkedItems", checkedItems);
                startActivity(intent);
            }
        });
    }

}