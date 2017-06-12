package com.example.android.quakereport;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;

/**
 * Created by Daniel on 5/29/2017.
 */

public class BackgroundDownloader extends AsyncTask<String, Void, Void> {

    private boolean complete = false;
    private String results;
    private Context context;

    public BackgroundDownloader(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... urls) {

            if (urls.length < 1 || urls[0] == null) {return null;}

            URL url;
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;

            try {

                url = new URL(urls[0]);

                if(url == null) return null;
                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.connect();


                if(httpURLConnection.getResponseCode() == 200) {
                    inputStream = httpURLConnection.getInputStream();
                    results = readFromStream(inputStream);
                    complete = true;
                }else{
                    Log.e(LOG_TAG, "ERROR response code: "+httpURLConnection.getResponseCode());
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem Retrieving the earthquake JSON results. ",e);
                publishProgress();

            }finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        return null;

    }


    private String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder builder = new StringBuilder();
        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
        }
        return builder.toString();

    }
    public boolean getDownloadStatus(){return complete;}
    public String getJsonResults(){return results;}
}
