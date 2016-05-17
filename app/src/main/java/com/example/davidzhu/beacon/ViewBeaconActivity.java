package com.example.davidzhu.beacon;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import static com.example.davidzhu.beacon.R.color.colorPrimary;


/**
 * Created by David Zhu on 5/17/2016.
 */
public class ViewBeaconActivity extends AppCompatActivity {

    private Beacon sampleBeacon = new Beacon("Beacon Name", 20);
    private ArrayList<Object> beaconItems;
    private BeaconRecyclerViewAdapter beaconRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_beacon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView beaconContent = (RecyclerView) findViewById(R.id.beacon_content);


        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Beacon Name");
        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(getApplicationContext(), colorPrimary));


        sampleBeacon.setDate("Sample Date");
        sampleBeacon.setPhoneNumber("Sample Number");
        sampleBeacon.setAddress("Sample Address");
        sampleBeacon.setWebsite("Sample Website");
        beaconItems = sampleBeacon.getItems();


        beaconRecyclerAdapter = new BeaconRecyclerViewAdapter(beaconItems, getApplicationContext());
        beaconContent.setAdapter(beaconRecyclerAdapter);
        beaconContent.setLayoutManager(new LinearLayoutManager(this));

        // Only show the edit fab if the user is the owner
        boolean userIsOwner = true; //CHANGE ME -- testing purposes only
        if (!userIsOwner) {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.edit_fab);
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            params.setAnchorId(View.NO_ID);
            params.setBehavior(null);

            fab.setLayoutParams(params);
            fab.setVisibility(View.GONE);
        }

        boolean userHasSaved = false;
        if (userHasSaved) {
            ImageView saveButton = (ImageView) findViewById(R.id.save_button);
            saveButton.setOnClickListener(null);
        }

    }

    public void incrementRating(View view) {
        beaconItems.set(0, (int) beaconItems.get(0) + 1);
        ImageView saveButton = (ImageView) findViewById(R.id.save_button);
        saveButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_white_24dp));
        saveButton.setOnClickListener(null);
        beaconRecyclerAdapter.notifyDataSetChanged();
    }

    // Launch Edit Beacon here
    public void editBeacon(View view) {

    }
}
