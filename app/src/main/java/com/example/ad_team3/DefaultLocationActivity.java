package com.example.ad_team3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import java.util.ArrayList;
import java.util.List;



public class DefaultLocationActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_location);
        String nearestLocation = accessLocation();
        RemoteApiManager apiManager = new RemoteApiManager(nearestLocation);
        apiManager.sendRainfallData(new RainfallDataCallback() {
            @Override
            public void onRainfallDataReceived(List<RainfallData> rainfallDataList) {
                chartData(rainfallDataList);
            }

            @Override
            public void onRainfallDataError(String errorMessage) {
                Log.e("Default Location Activity", "Error" + errorMessage);
            }
        });

        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshLocation(v);
            }
        });

        Button viewOtherStationsButton = findViewById(R.id.viewOthers);
        viewOtherStationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define what should happen when the button is clicked
                // For example, start a new activity
                Intent intent = new Intent(DefaultLocationActivity.this, AllLocationActivity.class);
                startActivity(intent);
            }
        });
    }



    protected void chartData(List<RainfallData> rainfallDataList){
        LineChart chart = findViewById(R.id.chartPlot);
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
        // Populate the Entry lists
        int index=0;
        for (RainfallData data : rainfallDataList) {
            if (index<11) {
                // Turn data into Entry objects
                actualRainfallEntries.add(new Entry(index, (float) data.getActualRainfall()));
            } else if(index==11){
                actualRainfallEntries.add(new Entry(index, (float) data.getActualRainfall()));
                predictedRainfallEntries.add(new Entry(index, (float) data.getActualRainfall()));
            } else {
                predictedRainfallEntries.add(new Entry(index, (float) data.getPredictedRainfall()));
            }
            index++;
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
        actualRainfallDataSet.setDrawFilled(true); // Enable shading below line

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
        predictedRainfallDataSet.enableDashedLine(10f, 5f, 0f); // Enable dashed line effect
        predictedRainfallDataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f)); // Set form line dash effect to make legend's line dashed as well

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        dataSets.add(predictedRainfallDataSet);
        dataSets.add(actualRainfallDataSet);
        LineData data = new LineData(dataSets);
        chart.setData(data);

        // Customize the x-axis labels using a ValueFormatter
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
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
        List<LegendEntry> reversedLegendEntries = new ArrayList<>();
        reversedLegendEntries.add(new LegendEntry("Actual Rainfall", Legend.LegendForm.LINE, Float.NaN, Float.NaN, null, Color.BLUE));
        reversedLegendEntries.add(new LegendEntry("Predicted Rainfall", Legend.LegendForm.LINE, Float.NaN, Float.NaN, null, ContextCompat.getColor(this, R.color.lighter_blue)));
        Legend legend = chart.getLegend(); // Get the legend of the chart
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP); // Set the vertical alignment to top
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); // Set the horizontal alignment to right
        legend.setOrientation(Legend.LegendOrientation.VERTICAL); // Set the orientation to horizontal
        legend.setDrawInside(true); // Set to false to draw the legend outside the chart
        legend.setTextSize(12f); // Set the text size of the legend labels
        legend.setTextColor(Color.BLACK); // Set the text color of the legend labels
        legend.setCustom(reversedLegendEntries);


        chart.invalidate(); // Refresh
    }

    private String accessLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

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

                    TextView nearestLocationTextView = findViewById(R.id.nearestLocationTextView);
                    nearestLocationTextView.setText("Your nearest weather station is: " + nearestLocation);

                    TextView distanceTextView = findViewById(R.id.distance);
                    distanceTextView.setText("You are " + String.format("%.1f", distanceToNearestLocation) + " km away from this station.");

                    return nearestLocation;
                }
                return null;
            }
        } else {
            // Request the ACCESS_FINE_LOCATION permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
        return null;
    }

    public void refreshLocation(View view) {
        // Clear any existing location data or UI
        TextView nearestLocationTextView = findViewById(R.id.nearestLocationTextView);
        nearestLocationTextView.setText("");
        TextView distanceTextView = findViewById(R.id.distance);
        distanceTextView.setText("");
        LineChart chart = findViewById(R.id.chartPlot);
        chart.clear(); // Clear chart data
        chart.invalidate();

        // Re-fetch the location and data
        String nearestLocation = accessLocation();
        if (nearestLocation != null) {
            RemoteApiManager apiManager = new RemoteApiManager(nearestLocation);
            apiManager.sendRainfallData(new RainfallDataCallback() {
                @Override
                public void onRainfallDataReceived(List<RainfallData> rainfallDataList) {
                    chartData(rainfallDataList);
                }

                @Override
                public void onRainfallDataError(String errorMessage) {
                    Log.e("Default Location Activity", "Error" + errorMessage);
                }
            });
        }
    }

}