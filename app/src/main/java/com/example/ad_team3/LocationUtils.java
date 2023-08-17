package com.example.ad_team3;

import android.location.Location;
import android.util.Log;

import java.util.List;

public class LocationUtils {
    public static CustomLocation findNearestLocation(Location userLocation, List<CustomLocation> locations) {
        CustomLocation closestLocation = null;
        float smallestDistance = Float.MAX_VALUE;

        double userLat = userLocation.getLatitude();
        double userLong = userLocation.getLongitude();

        for (CustomLocation loc : locations) {
            float[] results = new float[1];
            Location.distanceBetween(userLat, userLong, loc.getLatitude(), loc.getLongitude(), results);
            float distance = results[0];

            if (distance < smallestDistance) {
                smallestDistance = distance;
                closestLocation = loc;
            }
        }
        return closestLocation;
    }

    public static double calculateDistance(Location userLocation, CustomLocation closestLocation){
        Location locationA = new Location("point A");
        locationA.setLatitude(userLocation.getLatitude());
        locationA.setLongitude(userLocation.getLongitude());

        Location locationB = new Location("point B");
        locationB.setLatitude(closestLocation.getLatitude());
        locationB.setLongitude(closestLocation.getLongitude());

        return locationA.distanceTo(locationB) / 1000.0;
    }
}


