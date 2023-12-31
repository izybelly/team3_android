package com.example.ad_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class AllLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_location);

        SeekBar seekBar = findViewById(R.id.numericSeekBar_all);
        TextView messageTextView = findViewById(R.id.messageTextView_all);

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

        Button submit = findViewById(R.id.submit_allloc);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox changi = findViewById(R.id.checkbox1);
                CheckBox clementi = findViewById(R.id.checkbox2);
                CheckBox sentosa = findViewById(R.id.checkbox3);
                CheckBox kranji = findViewById(R.id.checkbox4);
                int months = seekBar.getProgress() + 1;

                boolean changiIsChecked = changi.isChecked();
                boolean clementiIsChecked = clementi.isChecked();
                boolean sentosaIsChecked = sentosa.isChecked();
                boolean kranjiIsChecked = kranji.isChecked();

                JSONObject requestPrediction = new JSONObject();
                try{
                    requestPrediction.put("months", months);
                    if (changiIsChecked){
                        requestPrediction.put("changi", true);
                    }
                    if (clementiIsChecked){
                        requestPrediction.put("clementi", true);
                    }
                    if (sentosaIsChecked){
                        requestPrediction.put("sentosa", true);
                    }
                    if (kranjiIsChecked){
                        requestPrediction.put("kranji", true);
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}