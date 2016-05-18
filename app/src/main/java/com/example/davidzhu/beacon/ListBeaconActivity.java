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

//        System.out.println(user.get("sortFilter"));
        String userId = user.getString("username");
        if (sort.equals("popularity")) {
            doPopularityQuery();
        }


    }

    private void doPopularityQuery() {

        ParseQuery<Beacon> query = ParseQuery.getQuery("Beacon");
        query.orderByDescending("popularity");
        query.findInBackground(new FindCallback<Beacon>() {
            @Override
            public void done(List<Beacon> objects, ParseException e) {
                if (e != null) {
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

    // Handler for FILTER button in bottom right -- launches Filter Activity
    public void showFiltersForList(View view) {
        Intent intent = new Intent(this, FilterBeaconActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.stay);
    }
}
