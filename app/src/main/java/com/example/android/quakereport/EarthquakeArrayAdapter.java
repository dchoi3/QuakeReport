package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Daniel on 5/28/2017.
 */

public class EarthquakeArrayAdapter extends ArrayAdapter<Earthquake>{

    private static final String LOG_TAG = EarthquakeArrayAdapter.class.getSimpleName();
    private Context context;
    private int resource;
    private ArrayList<Earthquake> earthquakes;


    public EarthquakeArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Earthquake> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.earthquakes = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView magnitude = (TextView) listItemView.findViewById(R.id.magnitude_textView);
        TextView oLocation = (TextView) listItemView.findViewById(R.id.oLocation_textView);
        TextView pLocation = (TextView) listItemView.findViewById(R.id.pLocation_textView);
        TextView date = (TextView) listItemView.findViewById(R.id.date_textView);
        TextView time = (TextView) listItemView.findViewById(R.id.time_textView);

        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();

        Earthquake earthquake = earthquakes.get(position);
        magnitude.setText(earthquake.getMagnitude());
        magnitudeCircle.setColor(setColor(earthquake.getMagnitude()));
        oLocation.setText(earthquake.getOffSetLocation());
        pLocation.setText(earthquake.getPrimaryLocation());
        date.setText(earthquake.getDate());
        time.setText(earthquake.getTime());

        return listItemView;
    }

    private int setColor(String s){
        double magnitude = Double.parseDouble(s);
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);

        switch(magnitudeFloor){
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;

        }

        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
