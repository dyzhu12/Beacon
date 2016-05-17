package com.example.davidzhu.beacon;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by David Zhu on 5/16/2016.
 */
public class ListBeaconActivity extends AppCompatActivity {

    // TODO This should be an arraylist of beacons
    private ArrayList<String> beacons = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_beacon);

        adapter = new ArrayAdapter<String>(this, R.layout.list_i_beacon, R.id.list_beacon_name, beacons);

        // Testing with fake data!
        populateWithFakeData();

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

    public void populateWithFakeData() {
        beacons.add("Beacon 1");
        beacons.add("Beacon 2");
        beacons.add("Beacon 3");
//        adapter.notifyDataSetChanged();
    }

    // Handler for FILTER button in bottom right -- launches Filter Activity
    public void showFiltersForList(View view) {
        Intent intent = new Intent(this, FilterBeaconActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.stay);
    }
}
