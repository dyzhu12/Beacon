package com.example.davidzhu.beacon;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MyCreatedBeaconsActivity extends AppCompatActivity {
    //for testing purposes only. Need to connect to database
    private TextView itemTextView;
    private String[] testArray = new String[]{
            "Puppies on the Mall",
            "Coding Party",
            "Ultimate Frisbee",
            "Wow It's My 21st Birthday Party and We're Going to Get Turnt",//test long event titles
            "Turtle Racing",
            "Squirrel Watching",
            "Free Hugs",
            "Free Pizza",
            "No Pizza for you",
            "Google Tech Talk!!",
            "Ride a camel",
            "ugh",
            "test scroll",
            "are we there yet?"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_created_beacons);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar_my_created_beacons);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        itemTextView = (TextView) findViewById(R.id.beacon_list_item_textview);

        ListView lv = new ListView(this);
//        lv.setTextFilterEnabled(true);
        lv.setAdapter(new ArrayAdapter<String>(this, R.layout.beacon_list_item, testArray));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Clicked on MySavedBeacon Tag");
                //make a removeItem method and call it
            }
        });
        setContentView(lv);

    }

    public void onDeleteItemClick(){
        //delete item from database

    }
}
