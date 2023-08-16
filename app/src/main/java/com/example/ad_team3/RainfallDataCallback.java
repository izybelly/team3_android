package com.example.ad_team3;

import com.example.ad_team3.RainfallData;

import java.util.List;

public interface RainfallDataCallback {
    void onRainfallDataReceived(List<RainfallData> rainfallDataList);
    void onRainfallDataError(String errorMessage);
}