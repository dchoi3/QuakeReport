package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Daniel on 6/13/2017.
 */

public class EarthquakeLoader  extends AsyncTaskLoader<List<Earthquake>> {
    private String url;

    public EarthquakeLoader(Context context, String url){
        super(context);
        this.url = url;

    }

    @Override
    protected void onStartLoading() {
        Log.i("INSIDE OF :", "onStartLoading");
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {

        Log.i("INSIDE OF :", "loadInBackground");
        if(url == null){
            return null;
        }
        List<Earthquake> result = QueryUtils.fetchEarthquakeData(url);
        return result;
    }



}
