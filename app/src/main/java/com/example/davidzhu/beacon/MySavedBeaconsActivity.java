package com.example.davidzhu.beacon;

import android.os.Bundle;
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

public class MySavedBeaconsActivity extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    final ArrayList<String> beaconDisplayNames = new ArrayList<String>();
    final ArrayList<String> beaconIds = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_saved_beacons);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        adapter = new ArrayAdapter<String>(this, R.layout.beacon_list_item, beaconDisplayNames);

        final ListView listView = (ListView) findViewById(R.id.list_view_my_saved_beacons);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String deleted_beacon_id = beaconIds.get(position);

                ParseQuery<Beacon> query = ParseQuery.getQuery("Beacon");
                query.whereEqualTo("objectId", deleted_beacon_id);
                query.findInBackground(new FindCallback<Beacon>() {
                    @Override
                    public void done(List<Beacon> results, ParseException e) {
                        if (e == null) {
                            for (Beacon beacon : results) {
                                //beacon.deleteInBackground(); //need to do beacon.remove();
                                beaconIds.clear();
                                beaconDisplayNames.clear();
                                adapter.clear();
                                queryMyCreatedBeacons();
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
            }
        });

        queryMyCreatedBeacons();
    }

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
