package com.example.ad_team3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RainfallChart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rainfall_chart);
        Gson gson = new Gson();

        // Retrieve the list of RainfallData from the intent
        String serializedData = getIntent().getStringExtra("rainfallDataListJson");
        RainfallDataList rainfallDataList = gson.fromJson(serializedData, RainfallDataList.class);
        List<RainfallData> dataList = rainfallDataList.getData();
        String stationNameStr = getIntent().getStringExtra("station");
        TextView chartTitle=findViewById(R.id.chartTitle);
        chartTitle.setText("Recorded Rainfall and Prediction\n"+ stationNameStr);
        Log.d("RainfallChart", "Rainfall Chart Test");
        chartData(dataList);
    }

    protected void chartData(List<RainfallData> rainfallDataList){
        LineChart chart = findViewById(R.id.chart);
        chart.setTouchEnabled(true);
        chart.setScaleYEnabled(false); // Disable vertical zoom
        chart.getDescription().setEnabled(false);
        RainfallChartMarkerView markerView = new RainfallChartMarkerView(this, R.layout.rainfall_chart_marker_view,rainfallDataList);
        chart.setMarker(markerView);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // Display the custom marker view with y-value
                chart.highlightValue(h); // Highlight the selected value

                // The custom marker view automatically gets updated with y-value
            }

            @Override
            public void onNothingSelected() {
                // Hide the custom marker view when no data point is selected
                chart.highlightValue(null); // Hide the marker
            }
        });


        List<Entry> actualRainfallEntries = new ArrayList<>();
        List<Entry> predictedRainfallEntries = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        Entry lastActualEntry = null;
        // Modify the loop where you populate the Entry lists
        int index=0;
        for (RainfallData data : rainfallDataList) {
            if (!Double.isNaN(data.getActualRainfall())) {
                // Turn your data into Entry objects
                actualRainfallEntries.add(new Entry(index, (float) data.getActualRainfall()));
                lastActualEntry = new Entry(index, (float) data.getActualRainfall());
            } else if (!Double.isNaN(data.getPredictedRainfall())) {
                if (lastActualEntry != null) {
                    // Add a connecting entry between last actual and first predicted
                    predictedRainfallEntries.add(lastActualEntry);
                    lastActualEntry = null; // Reset the last actual entry
                }
                predictedRainfallEntries.add(new Entry(index, (float) data.getPredictedRainfall()));
            }
            index++;
        }

        Log.d("RainfallChart", "Actual Rainfall Entries: " + actualRainfallEntries.size());
        for (Entry entry : actualRainfallEntries) {
            Log.d("RainfallChart", "Actual Entry: " + entry.toString());
        }

        Log.d("RainfallChart", "Predicted Rainfall Entries: " + predictedRainfallEntries.size());
        for (Entry entry : predictedRainfallEntries) {
            Log.d("RainfallChart", "Predicted Entry: " + entry.toString());
        }


        // Configure dataSet
        LineDataSet actualRainfallDataSet = new LineDataSet(actualRainfallEntries, "Actual Rainfall");
        actualRainfallDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        actualRainfallDataSet.setColor(Color.BLUE); // Set the color of the line
        actualRainfallDataSet.setCircleColor(Color.BLUE); // Set the color of the data points
        actualRainfallDataSet.setLineWidth(2f); // Set the width of the line
        actualRainfallDataSet.setDrawCircles(true); // Set to true to draw data points as circles
        actualRainfallDataSet.setCircleRadius(4f); // Set the radius of the data point circles
        actualRainfallDataSet.setDrawValues(false); // Set to true to draw values on the data points
        actualRainfallDataSet.setValueTextColor(Color.BLACK); // Set the text color of the values
        actualRainfallDataSet.setValueTextSize(12f); // Set the text size of the values
        actualRainfallDataSet.setMode(LineDataSet.Mode.LINEAR); // Set to draw lines
        actualRainfallDataSet.setHighlightEnabled(true); // Disable highlighting of data points
        actualRainfallDataSet.setDrawFilled(true);

        LineDataSet predictedRainfallDataSet = new LineDataSet(predictedRainfallEntries, "Predicted Rainfall");
        predictedRainfallDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        predictedRainfallDataSet.setColor(ContextCompat.getColor(this, R.color.lighter_blue)); // Set the color of the line
        predictedRainfallDataSet.setCircleColor(ContextCompat.getColor(this, R.color.lighter_blue)); // Set the color of the data points
        predictedRainfallDataSet.setLineWidth(2f); // Set the width of the line
        predictedRainfallDataSet.setDrawCircles(true); // Set to true to draw data points as circles
        predictedRainfallDataSet.setCircleRadius(4f); // Set the radius of the data point circles
        predictedRainfallDataSet.setDrawValues(false); // Set to true to draw values on the data points
        predictedRainfallDataSet.setValueTextColor(Color.BLACK); // Set the text color of the values
        predictedRainfallDataSet.setValueTextSize(12f); // Set the text size of the values
        predictedRainfallDataSet.setMode(LineDataSet.Mode.LINEAR); // Set to draw lines
        predictedRainfallDataSet.setHighlightEnabled(true); // Disable highlighting of data points
//        predictedRainfallDataSet.enableDashedLine(10f, 5f, 0f); // Enable dashed line effect
//        predictedRainfallDataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f)); // Set form line dash effect to make legend's line dashed as well

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        dataSets.add(predictedRainfallDataSet);
        dataSets.add(actualRainfallDataSet);
        LineData data = new LineData(dataSets);
        chart.setData(data);

        // Customize the x-axis labels using a ValueFormatter
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set position to BOTTOM
        xAxis.setLabelRotationAngle(-45);
        xAxis.setGranularity(1f); // Set the granularity for the x-axis
        xAxis.setGranularityEnabled(true);// Enable granularity to show all labels
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < rainfallDataList.size()) {
                    return rainfallDataList.get(index).getDate(); // Use the corresponding date string as the label
                }
                return ""; // Return an empty string for invalid indices
            }
        });


        // Get the left y-axis
        YAxis leftAxis = chart.getAxisLeft();
        // Set the minimum value of the y-axis to 0
        leftAxis.setAxisMinimum(0f);
        // Disable the right Y-axis
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false); // Disable drawing labels
        rightAxis.setDrawAxisLine(false); // Disable drawing axis line

        // Customize legend
        Legend legend = chart.getLegend(); // Get the legend of the chart
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP); // Set the vertical alignment to top
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); // Set the horizontal alignment to right
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL); // Set the orientation to horizontal
        legend.setDrawInside(true); // Set to false to draw the legend outside the chart
        legend.setTextSize(12f); // Set the text size of the legend labels
        legend.setTextColor(Color.BLACK); // Set the text color of the legend labels


        chart.invalidate(); // refresh
    }

}
