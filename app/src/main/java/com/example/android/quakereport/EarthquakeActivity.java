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

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

 public class EarthquakeActivity extends AppCompatActivity
         implements LoaderManager.LoaderCallbacks<List<Earthquake>>{

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    EarthquakeArrayAdapter earthquakeArrayAdapter;
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private TextView mEmptyStateTextView;
    private ProgressBar progressBar;
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        final Vibrator vb = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeArrayAdapter = new EarthquakeArrayAdapter(this, R.layout.earthquake_customlistview, new ArrayList<Earthquake>());
        earthquakeListView.setAdapter(earthquakeArrayAdapter);
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vb.vibrate(100);
                Earthquake currentEarthquake = earthquakeArrayAdapter.getItem(position);
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websiteIntent);


            }
        });

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);
        progressBar = (ProgressBar) findViewById(R.id.loading_spinner);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }else{
            progressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

     @Override
     public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {

         SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
         String minMagnitiude = sharedPreferences.getString(
                 getString(R.string.settings_min_magnitude_key),
                 getString(R.string.settings_min_magnitude_default));

         String orderBy = sharedPreferences.getString(
                 getString(R.string.settings_order_by_key),
                 getString(R.string.settings_order_by_default));

         Uri baseUri = Uri.parse(USGS_REQUEST_URL);
         Uri.Builder uriBuilder = baseUri.buildUpon();

         //?format=geojson&eventtype=earthquake&orderby=time&minmag=3&limit=100"
         uriBuilder.appendQueryParameter("format","geojson");
         uriBuilder.appendQueryParameter("limit", "25");
         uriBuilder.appendQueryParameter("minmag", minMagnitiude);
         uriBuilder.appendQueryParameter("orderby", orderBy);


         Log.i("INSIDE OF :", "onCreateLoader");
         return new EarthquakeLoader(this, uriBuilder.toString());
     }

     @Override
     public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data) {
         Log.i("INSIDE OF :", "onLoadFinished");
         earthquakeArrayAdapter.clear();
         if (data != null && !data.isEmpty()) {
             earthquakeArrayAdapter.addAll(data);
         }
         progressBar.setVisibility(View.GONE);
         mEmptyStateTextView.setText(R.string.no_earthquakes);
     }

     @Override
     public void onLoaderReset(Loader<List<Earthquake>> loader) {
         Log.i("INSIDE OF :", "onLoaderReset");
         earthquakeArrayAdapter.clear();
     }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.main, menu);
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();

         if(id == R.id.action_settings){
             Intent settingsIntent = new Intent(this, SettingsActivity.class);
             startActivity(settingsIntent);
             return true;
         }

         return super.onOptionsItemSelected(item);
     }
 }
