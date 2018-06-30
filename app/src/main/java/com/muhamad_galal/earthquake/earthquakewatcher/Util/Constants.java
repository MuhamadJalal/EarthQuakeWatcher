package com.muhamad_galal.earthquake.earthquakewatcher.Util;

import java.util.Random;

public class Constants {
    // URl to parse data from it
    public static final String URL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.geojson";
    // Number of objects which will parse from API
    public static final int LIMIT = 500;

    public static int randomColor(int max , int min){
        return new Random().nextInt(max - min) + min;
    }
}
