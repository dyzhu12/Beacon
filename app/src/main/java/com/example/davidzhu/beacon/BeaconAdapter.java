package com.example.davidzhu.beacon;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Zhu on 5/17/2016.
 */
public class BeaconAdapter extends ArrayAdapter<Beacon> {

    private Activity activity;
    private ArrayList<Beacon> beacons;
    private static LayoutInflater inflater = null;

    private int resourceId;

    public BeaconAdapter(Activity activity, int resource, int textViewResourceId, List<Beacon> objects) {
        super(activity, resource, textViewResourceId, objects);

        try {
            this.activity = activity;
            this.beacons = (ArrayList<Beacon>) objects;
            this.resourceId = resource;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getLength() {
        return beacons.size();
    }

    public static class ViewHolder {
        public TextView beaconName;
        public TextView beaconRating;
        public TextView beaconDistance;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewHolder;

        if (convertView == null) {
            view = inflater.inflate(resourceId, null);
            viewHolder = new ViewHolder();

            viewHolder.beaconName = (TextView) view.findViewById(R.id.list_beacon_name);
            viewHolder.beaconRating = (TextView) view.findViewById(R.id.list_beacon_rating);
            viewHolder.beaconDistance = (TextView) view.findViewById(R.id.list_beacon_distance);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Beacon currentBeacon = beacons.get(position);

        viewHolder.beaconName.setText(currentBeacon.getName());
        viewHolder.beaconRating.setText(Integer.toString(currentBeacon.getRating()));
        viewHolder.beaconDistance.setText(Double.toString(currentBeacon.getDistance()) + " mi");

        return view;
    }
}
