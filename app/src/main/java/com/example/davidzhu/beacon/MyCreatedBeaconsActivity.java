package com.example.davidzhu.beacon;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

public class MyCreatedBeaconsActivity extends AppCompatActivity {
    //private TextView itemTextView;
    //private ArrayAdapter<String> adapter;

    //fake data for testing purposes
    //private ArrayList<String> testArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_created_beacons);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        final ArrayList<String> mCreatedBeaconsArray = new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.beacon_list_item, mCreatedBeaconsArray);

        ListView listView = (ListView) findViewById(R.id.list_view_my_created_beacons);
        listView.setAdapter(adapter);

        //deletes user's beacon upon clicking the 'x'
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
//                System.out.println("in onclicklistener");
//
//                String beacon_display_name = mCreatedBeaconsArray.get(position);
//                System.out.println("selected: " + beacon_display_name);
//
//                ParseQuery<Beacon> query = ParseQuery.getQuery("Beacon");
//                query.whereEqualTo("name", beacon_display_name); //runs risk of deleting beacons w/ same name
//                query.findInBackground(new FindCallback<Beacon>() {
//                    @Override
//                    public void done(List<Beacon> results, ParseException e) {
//                        if (e == null) {
//                            for (Beacon beacon : results) { //may cause problems if two beacons have same name, will fix later
//                                System.out.println("deleting: " + beacon.getDisplayName());
//
//                                beacon.deleteInBackground();
//                                adapter.notifyDataSetChanged();
//                            }
//                        }
//                    }
//                });
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                System.out.println("in onclicklistener");

                String beacon_display_name = mCreatedBeaconsArray.get(position);
                System.out.println("selected: " + beacon_display_name);

                ParseQuery<Beacon> query = ParseQuery.getQuery("Beacon");
                query.whereEqualTo("name", beacon_display_name); //runs risk of deleting beacons w/ same name
                query.findInBackground(new FindCallback<Beacon>() {
                    @Override
                    public void done(List<Beacon> results, ParseException e) {
                        if (e == null) {
                            for (Beacon beacon : results) { //may cause problems if two beacons have same name, will fix later
                                System.out.println("deleting: " + beacon.getDisplayName());

                                beacon.deleteInBackground();
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
            }
        });

        ParseUser user = ParseUser.getCurrentUser();
        String userId = user.getObjectId();

        ParseQuery<Beacon> myCreatedBeaconsQuery = ParseQuery.getQuery("Beacon");
        myCreatedBeaconsQuery.whereEqualTo("user", user);

        myCreatedBeaconsQuery.findInBackground(new FindCallback<Beacon>() {
            @Override
            public void done(List<Beacon> results, ParseException e) {
                if (e == null) {
                    //for each beacon in the objects List
                    int size = results.size(); //should be number of beacons created by current user
                    System.out.println("size of queried <Beacons> list = " + size);

                    for (Beacon beacon : results) {
                        String myCreatedBeaconName = beacon.getString("name");
                        mCreatedBeaconsArray.add(myCreatedBeaconName);
                        adapter.notifyDataSetChanged();
                        System.out.println("added myCreatedBeaconName: " + myCreatedBeaconName + ", yay!");

                    }
                }
            }
        });
    }
}
