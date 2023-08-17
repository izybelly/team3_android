package com.example.ad_team3;

import static org.junit.Assert.*;

import com.example.ad_team3.CustomLocation;
import com.example.ad_team3.LocationUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import android.location.Location;

public class LocationUtilsTest {

    @Test
    public void testFindNearestLocation() {
        Location userLocation = new Location("TestProvider");
        userLocation.setLatitude(1.2494);
        userLocation.setLongitude(103.8301);

        List<CustomLocation> locations = new ArrayList<>();
        locations.add(new CustomLocation("Marina Bay Sands", 1.2836, 103.8607));
        locations.add(new CustomLocation("Changi Airport", 1.3644, 103.9915));
        locations.add(new CustomLocation("Sentosa Island", 1.2494, 103.8303));

        CustomLocation nearestLocation = LocationUtils.findNearestLocation(userLocation, locations);
        assertEquals(nearestLocation.getName(), "Sentosa Island");
    }

    @Test
    public void testCalculateDistance() {
        Location userLocation = new Location("TestProvider");
        userLocation.setLatitude(1.2494);
        userLocation.setLongitude(103.8303);

        CustomLocation closestLocation = new CustomLocation("Sentosa Island", 1.2494, 103.8303);

        double distance = LocationUtils.calculateDistance(userLocation, closestLocation);
        assertEquals(distance, 0, 0.001);
    }
}
