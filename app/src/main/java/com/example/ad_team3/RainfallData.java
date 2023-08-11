package com.example.ad_team3;

import com.google.gson.annotations.SerializedName;

public class RainfallData {
    @SerializedName("ds")
    private String ds;

    @SerializedName("y")
    private double y;

    public String getDs(){
        return ds;
    }

    public void setDs(String ds){
        this.ds = ds;
    }

    public double getY(){
        return y;
    }

    public void setY(double y){
        this.y = y;
    }
}

