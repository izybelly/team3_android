package com.example.ad_team3;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RainfallDataList {
    @SerializedName("data")
    private List<RainfallData> data;

    public List<RainfallData> getData() {
        return data;
    }

    public void setData(List<RainfallData> data) {
        this.data = data;
    }
}

