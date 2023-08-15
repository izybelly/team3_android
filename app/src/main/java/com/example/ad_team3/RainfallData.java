package com.example.ad_team3;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class RainfallData implements Serializable {
    @SerializedName("date")
    private String date;

    @SerializedName("actualRainfall")
    private double actualRainfall;

    @SerializedName("predictedRainfall")
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

