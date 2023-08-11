package com.example.ad_team3;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class RainfallData implements Serializable {
    @SerializedName("Date")
    private String date;

    @SerializedName("Actual Rainfall (mm)")
    private double actualRainfall;

    @SerializedName("Predicted Rainfall (mm)")
    private double predictedRainfall;

    public String getDate() {
        return date;
    }

    public double getActualRainfall() {
        return actualRainfall;
    }

    public double getPredictedRainfall() {
        return predictedRainfall;
    }

}

