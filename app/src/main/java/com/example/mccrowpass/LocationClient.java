package com.example.mccrowpass;

import com.example.mccrowpass.domain.LatLong;

import java.util.Arrays;
import java.util.List;

public class LocationClient {

    // stub implementation, returns hardcoded list of coords
    public List<LatLong> getLocations() {
        return Arrays.asList(new LatLong(54.975902, -1.612614,  "newastle 1"),
        new LatLong(54.977138, -1.575296, "Byker"),
        new LatLong(54.961793, -1.588085, "Felling Bypass"),
        new LatLong(54.971774, -1.614603,"Grainger Street"),
        new LatLong(55.021122, -1.536561,"Asda Longbenton"),
        new LatLong(55.037254, -1.564927,"Killingworth Centre"),
        new LatLong(54.999544, -1.4787,"Tyneside Delivery Kitchen"),
        new LatLong(55.035971, -1.624507, "Gosforth Park"),
        new LatLong(55.01239, -1.666514,"Kingston Park"),
        new LatLong(54.958596, -1.670662,"Metro Centre Yellow")

//                new Location(54.987593107251016, -1.594092740758485, "Rehills Test")
        );
    }

}
