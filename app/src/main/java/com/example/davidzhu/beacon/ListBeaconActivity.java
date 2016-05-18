package com.example.davidzhu.beacon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Zhu on 5/16/2016.
 */
public class ListBeaconActivity extends AppCompatActivity {

    private ArrayList<Beacon> beaconList;
    private BeaconAdapter adapter;
    private ParseUser user;
    private String sort;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_beacon);


        user = ParseUser.getCurrentUser();
        sort = user.getString("sortFilter");

        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String userId = user.getString("username");

        int distance = user.getInt("distanceFilter");
        int popularity = user.getInt("ratingFilter");
        String sort = user.getString("sortFilter");

        List<String> savedTags = user.getList("savedTags");
        ParseQuery<Beacon> query = ParseQuery.getQuery("Beacon");

        if(savedTags != null && savedTags.size() > 0){
            query.whereContainedIn("tags",savedTags);
        }

        if(distance != -1){
            query.whereWithinMiles("location", user.getParseGeoPoint("location"), distance);
        }

        if(popularity != -1){
            query.whereGreaterThanOrEqualTo("popularity",popularity);
        }

        if (sort.equals("popularity")) {
            doPopularityQuery(query);
        }
        if (sort.equals("distance")) {
            doDistanceQuery(query);
        }
        if (sort.equals("name")) {
            doNameQuery(query);
        }

    }

    private void doDistanceQuery(ParseQuery query) {

        query.orderByDescending("name").whereExists("location");
        query.findInBackground(new FindCallback<Beacon>() {
            @Override
            public void done(List<Beacon> objects, ParseException e) {
                if (e == null) {
                    ParseUser user = ParseUser.getCurrentUser();
                    ArrayList<Beacon> sortedList = new ArrayList<Beacon>();
                    for (int i = 0; i < objects.size(); i++) {
                        double distance = objects.get(i).getLocation().distanceInMilesTo(user.getParseGeoPoint("location"));

                        if (sortedList.size() == 0) {
                            sortedList.add(objects.get(i));
                        } else {
                            for (int j = 0; j < sortedList.size(); j++) {
                                double distance2 = sortedList.get(j).getLocation().distanceInMilesTo(user.getParseGeoPoint("location"));
                                if (distance < distance2) {
                                    sortedList.add(j, objects.get(i));
                                    break;
                                }
                                if (j == sortedList.size() - 1) {
                                    sortedList.add(objects.get(i));
                                    break;
                                }
                            }
                        }
                    }
                    setAdapter(sortedList);
                }
            }
        });
    }

    private void doPopularityQuery(ParseQuery query) {
        query.orderByDescending("popularity").whereExists("location");
        query.findInBackground(new FindCallback<Beacon>() {
            @Override
            public void done(List<Beacon> objects, ParseException e) {
                if (e == null) {
                    setAdapter(objects);
                }
            }
        });
    }

    private void setAdapter(List<Beacon> objects) {
        adapter = new BeaconAdapter(this, R.layout.list_i_beacon, R.id.list_beacon_name, (ArrayList<Beacon>) objects);

        ListView beaconList = (ListView) findViewById(R.id.beacon_list);
        beaconList.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return(super.onOptionsItemSelected(item));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                user.fetch();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String sort = (String) data.getExtras().get("sort");
            int distance = user.getInt("distanceFilter");
            int popularity = user.getInt("ratingFilter");

            List<String> savedTags = user.getList("savedTags");
            ParseQuery<Beacon> query = ParseQuery.getQuery("Beacon");

            if(savedTags != null){
                query.whereContainedIn("tags",savedTags);
            }

            if(distance != -1){
                query.whereWithinMiles("location", user.getParseGeoPoint("location"), distance);
            }

            if(popularity != -1){
                query.whereGreaterThanOrEqualTo("popularity",popularity);
            }


            if (sort.equals("distance")) {
                doDistanceQuery(query);
            }
            if (sort.equals("popularity")) {
                doPopularityQuery(query);
            }
            if (sort.equals("name")) {
                doNameQuery(query);
            }
        }
    }

    private void doNameQuery(ParseQuery query) {
        query.orderByAscending("name").whereExists("location");
        query.findInBackground(new FindCallback<Beacon>() {
            @Override
            public void done(List<Beacon> objects, ParseException e) {
                if (e == null) {
                    setAdapter(objects);
                }
            }
        });
    }

    // Handler for FILTER button in bottom right -- launches Filter Activity
    public void showFiltersForList(View view) {
        Intent intent = new Intent(this, FilterBeaconActivity.class);
        startActivityForResult(intent, 0);
        overridePendingTransition(R.anim.slide_in, R.anim.stay);
    }
}
