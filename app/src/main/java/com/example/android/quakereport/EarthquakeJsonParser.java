package com.example.android.quakereport;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Daniel on 5/29/2017.
 */

public class EarthquakeJsonParser extends AsyncTask<String, Void, Void> {

    private ArrayList<Earthquake> earthquakeArrayList;
    private String oLocation, pLocation;
    public boolean complete = false;




    @Override
    protected Void doInBackground(String[] params) {
        earthquakeArrayList = new ArrayList<>();
        String jsonRoot, date, time, url, mag;
        jsonRoot = params[0];

        if(jsonRoot == null){
            complete = true;
            return null;
        }
        try {
            JSONObject root = new JSONObject(jsonRoot);

            JSONArray arrayOfQuakes = new JSONArray(root.getString("features"));
            for(int x = 0; x < arrayOfQuakes.length(); x++) {

                JSONObject earthquake = new JSONObject(arrayOfQuakes.getJSONObject(x).getString("properties"));
                mag = formatLong(earthquake.getDouble("mag"));
                locationSplitter(earthquake.get("place").toString());
                date = convertMilliToDate(earthquake.getLong("time"));
                time = convertMilliToTime(earthquake.getLong("time"));
                url = earthquake.get("url").toString();

                earthquakeArrayList.add(new Earthquake(mag, oLocation, pLocation, date, time, url));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            complete = true;
        }
        return null;
    }



    private String convertMilliToDate(Long milliSeconds){
        Date dateObject = new Date(milliSeconds);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormatter.format(dateObject);

    }

    private String convertMilliToTime(Long milliSeconds) {
        Date dateObject = new Date(milliSeconds);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private void locationSplitter(String originalString){

        String splitter = " of ";
        if(originalString.contains(splitter)) {
            String[] newString = originalString.split(splitter);
            oLocation = newString[0] + splitter;
            pLocation = newString[1];
        }else{
            oLocation = "Near the ";
            pLocation = originalString;
        }

    }

    private String formatLong(Double num){

        DecimalFormat formatter = new DecimalFormat("0.0");
        return formatter.format(num);
    }

    public boolean checkStatus(){return complete;}
    public ArrayList<Earthquake> getEarthquakeArray(){return earthquakeArrayList;}

}
