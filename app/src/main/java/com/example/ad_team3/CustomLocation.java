package com.example.ad_team3;

public class CustomLocation {
    private String name;
    private double latitude;
    private double longitude;

    public CustomLocation(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


}

