package com.example.davidzhu.beacon;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

public class MyCreatedBeaconsActivity extends AppCompatActivity {
    private ArrayAdapter<String> adapter;

    final ArrayList<String> beaconDisplayNames = new ArrayList<String>();
    final ArrayList<String> beaconIds = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_created_beacons);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //getBeaconDisplayNames();
        adapter = new ArrayAdapter<String>(this, R.layout.beacon_list_item, beaconDisplayNames);
        System.out.println("LINE 34");

        final ListView listView = (ListView) findViewById(R.id.list_view_my_created_beacons);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String deleted_beacon_id = beaconIds.get(position);

                ParseQuery<Beacon> query = ParseQuery.getQuery("Beacon");
                query.whereEqualTo("objectId", deleted_beacon_id);
                query.findInBackground(new FindCallback<Beacon>() {
                    @Override
                    public void done(List<Beacon> results, ParseException e) {
                        if (e == null) {
                            for (Beacon beacon : results) { //mostly fixed, but strange behavior when you add beacons (names) in the order:
                                Intent intent = new Intent(MyCreatedBeaconsActivity.this, ViewBeaconActivity.class);
                                intent.putExtra("beaconId", beacon.getObjectId());
                                startActivity(intent);
                                /*beacon.deleteInBackground();
                                beaconIds.clear();
                                beaconDisplayNames.clear();
                                adapter.clear();
                                queryMyCreatedBeacons();
                                adapter.notifyDataSetChanged();*/

                                //listView.invalidate();
                                //queryMyCreatedBeacons();
                            }
                        }
                    }
                });
            }
        });

        queryMyCreatedBeacons();
    }

//    //myCreatedDataName is in the format beaconid.beacondisplayname
//    private void getBeaconIds(){
//        beaconIds.clear();
//        for(String myCreatedBeacon : mCreatedBeaconsArray){
//            String[] array = myCreatedBeacon.split("."); //split myCreatedBeacons
//            beaconIds.add(array[0]);
//            //beaconDisplayNames.add(array[1]);
//        }
//    }
//
//    //myCreatedDataBeacon is in the format beaconid.beacondisplayname
//    private void getBeaconDisplayNames(){
//        beaconDisplayNames.clear();
//        for(String myCreatedBeacon : mCreatedBeaconsArray){
//            String[] array = myCreatedBeacon.split("."); //split myCreatedBeacons
//            beaconDisplayNames.add(array[1]);
//        }
//    }

    private void queryMyCreatedBeacons() {
        ParseUser user = ParseUser.getCurrentUser();
        String userId = user.getObjectId();

        ParseQuery<Beacon> myCreatedBeaconsQuery = ParseQuery.getQuery("Beacon");
        myCreatedBeaconsQuery.whereEqualTo("user", user);

        myCreatedBeaconsQuery.findInBackground(new FindCallback<Beacon>() {
            @Override
            public void done(List<Beacon> results, ParseException e) {
                if (e == null) {
                    //for each beacon in the objects List
                    for (Beacon beacon : results) {
                        String beaconId = beacon.getObjectId();
                        String beaconDisplayName = beacon.getString("name");
                        beaconIds.add(beaconId);
                        beaconDisplayNames.add(beaconDisplayName);
                        adapter.notifyDataSetChanged();
                    }
                 }
            }
        });

    }
}


