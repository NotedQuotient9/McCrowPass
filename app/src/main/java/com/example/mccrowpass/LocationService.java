package com.example.mccrowpass;

import com.example.mccrowpass.domain.LatLong;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LocationService {

    LocationClient client = new LocationClient();

    public float getBearing(LatLong deviceLatLong) {

        List<LatLong> latLongs = client.getLocations();
        LatLong nearestLatLong = getNearestLocation(latLongs, deviceLatLong);

        return (float)bearingInDegrees(deviceLatLong, nearestLatLong);

    }

    private LatLong getNearestLocation(List<LatLong> latLongs, LatLong deviceLatLong) {

        Map<Double, LatLong> distances = new HashMap<>();
        latLongs.forEach(latLong -> distances.put(getDistance(latLong, deviceLatLong), latLong));
        Map<Double, LatLong> sortedMap = new TreeMap<>(distances);
        return (LatLong) sortedMap.values().toArray()[0];

    }

    private double getDistance(LatLong mcLatLong, LatLong deviceLatLong) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(deviceLatLong.getLatitude() - mcLatLong.getLatitude());
        double lonDistance = Math.toRadians(deviceLatLong.getLongitude() - mcLatLong.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(mcLatLong.getLatitude())) * Math.cos(Math.toRadians(deviceLatLong.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        distance = Math.pow(distance, 2) + Math.pow(0.0, 2);
        return Math.sqrt(distance);
    }

    private double bearingInRadians(LatLong src, LatLong dst) {
        double srcLat = Math.toRadians(src.getLatitude());
        double dstLat = Math.toRadians(dst.getLatitude());
        double dLng = Math.toRadians(dst.getLongitude() - src.getLongitude());

        return Math.atan2(Math.sin(dLng) * Math.cos(dstLat),
                Math.cos(srcLat) * Math.sin(dstLat) -
                        Math.sin(srcLat) * Math.cos(dstLat) * Math.cos(dLng));
    }

    private double bearingInDegrees(LatLong src, LatLong dst) {
        return (Math.toDegrees(bearingInRadians(src, dst)));
    }
}
