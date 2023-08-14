package com.example.ad_team3;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RainfallChartMarkerView extends MarkerView {
    // These member variables hold references to the views inside the custom marker layout
    private final TextView tvContent;
    List<RainfallData> rainfallDataList;
    public RainfallChartMarkerView(Context context, int layoutResource, List<RainfallData> rainfallDataList) {
        super(context, layoutResource);
        this.rainfallDataList = rainfallDataList;
        // Find the views inside the custom marker layout
        tvContent = findViewById(R.id.tvContent);
    }

    // This method will be called every time a marker is drawn on the chart
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int index = (int) e.getX();
        String xLabel="";
        if (index >= 0 && index < rainfallDataList.size()) {
            xLabel=rainfallDataList.get(index).getDate(); // Use the corresponding date string as the label
        }
        // Display the y-value of the selected data point
        int yValue = Math.round(e.getY());
        tvContent.setText(xLabel+":\n"+yValue+ " mm"); // Set the content of the TextView
        tvContent.requestLayout();
    }

    // This method controls the position of the marker view on the chart
    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2f), -getHeight());
    }
}
