package com.example.davidzhu.beacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;

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

    public BeaconAdapter(Activity activity, int resource, int textViewResourceId, ArrayList<Beacon> objects) {
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
        public ImageView beaconImage;
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
            viewHolder.beaconImage = (ImageView) view.findViewById(R.id.list_beacon_image);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Beacon currentBeacon = beacons.get(position);

        ((LinearLayout) view.findViewById(R.id.filler_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ViewBeaconActivity.class);
                intent.putExtra("beaconId", currentBeacon.getObjectId());
                activity.startActivity(intent);
            }
        });

        viewHolder.beaconName.setText(currentBeacon.getDisplayName());
        if (currentBeacon.getImage()!= null) {
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(
                        currentBeacon.getImage().getData(), 0,
                        currentBeacon.getImage().getData().length);
                viewHolder.beaconImage.setImageBitmap(bitmap);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        viewHolder.beaconRating.setText(Integer.toString(currentBeacon.getPopularity()));

        // Filler Value
        ParseUser user = ParseUser.getCurrentUser();

        if (ParseUser.getCurrentUser().getParseGeoPoint("location") != null)
            viewHolder.beaconDistance.setText(Double.toString(
                currentBeacon.getLocation().distanceInMilesTo(ParseUser.getCurrentUser().getParseGeoPoint("location"))).substring(0, 4) + " miles");

        return view;
    }
}
