package com.example.android.quakereport;

/**
 * Created by Daniel on 5/28/2017.
 * This is the class for each earthquake
 */

public class Earthquake {
    private String magnitude;
    private String oLocation;
    private String pLocation;
    private String date;
    private String time;
    private String url;

    public Earthquake(String m, String oL, String pL, String d, String t, String u){
        magnitude = m;
        oLocation = oL;
        pLocation = pL;
        date = d;
        time = t;
        url = u;

    }


    public String getMagnitude() {
        return magnitude;
    }

    public String getOffSetLocation() {
        return oLocation;
    }

    public String getPrimaryLocation() {
        return pLocation;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }
}
