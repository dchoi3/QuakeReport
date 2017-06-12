 /*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

 public class EarthquakeActivity extends AppCompatActivity {
    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    public EarthquakeJsonParser jsonParser;
    public BackgroundDownloader downloader;

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=4&limit=100";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

    }

    @Override
    protected void onResume() {
        super.onResume();

        downloader = new BackgroundDownloader(this);
        downloader.execute(USGS_REQUEST_URL);
        checkThreadStatus();
    }

    private void checkThreadStatus(){
        boolean complete = false;
        String jsonString;
        ArrayList<Earthquake> earthquakes;

        while(!complete){
            if(downloader.getDownloadStatus()){
                complete = true;
                jsonString = downloader.getJsonResults();
                closeDownloader();
                if(jsonString != null){
                    jsonParser = new EarthquakeJsonParser();
                    jsonParser.execute(jsonString);
                }
            }

        }
        complete = false;

        while(!complete && jsonParser!= null) {
            if (jsonParser.checkStatus()) {
                complete = true;
                earthquakes = jsonParser.getEarthquakeArray();
                closeParser();

                final EarthquakeArrayAdapter earthquakeArrayAdapter = new EarthquakeArrayAdapter(this, R.layout.earthquake_customlistview, earthquakes);
                // Set the adapter on the {@link ListView}
                // so the list can be populated in the user interface
                ListView earthquakeListView = (ListView) findViewById(R.id.list);

                earthquakeListView.setAdapter(earthquakeArrayAdapter);
                earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Earthquake currentEarthquake = earthquakeArrayAdapter.getItem(position);
                        Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
                        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                        startActivity(websiteIntent);


                    }
                });


            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        closeDownloader();
        closeParser();
    }

    private void closeDownloader(){
        if(downloader != null){
            downloader.cancel(true);
            downloader = null;
        }
    }

    private void closeParser(){
        if(jsonParser != null){
            jsonParser .cancel(true);
            jsonParser  = null;
        }
    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>>{


        @Override
        protected List<Earthquake> doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(List<Earthquake> earthquakes) {
            super.onPostExecute(earthquakes);
        }
    }

}
