package com.example.davidzhu.beacon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.example.davidzhu.beacon.R.color.colorPrimary;


/**
 * Created by David Zhu on 5/17/2016.
 */
public class ViewBeaconActivity extends AppCompatActivity {

    private BeaconTest sampleBeacon = new BeaconTest("Beacon Name", 20);
    private ArrayList<Object> beaconItems;
    private BeaconRecyclerViewAdapter beaconRecyclerAdapter;

    private Beacon beacon;

    private RecyclerView beaconContent;
    private CollapsingToolbarLayout collapsingToolbar;

    private ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_beacon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        beaconContent = (RecyclerView) findViewById(R.id.beacon_content);


        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(getApplicationContext(), colorPrimary));

        user = ParseUser.getCurrentUser();

        ParseQuery<Beacon> query = ParseQuery.getQuery("Beacon");
        query.whereEqualTo("objectId", getIntent().getExtras().get("beaconId"));
        doQuery(query);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void doQuery(ParseQuery query) {
        final ViewBeaconActivity self = this;

        query.findInBackground(new FindCallback<Beacon>() {
            @Override
            public void done(List<Beacon> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    beacon = objects.get(0);
                    collapsingToolbar.setTitle(beacon.getString("name"));
                    beaconItems = beacon.getItems();

                    ImageView headerImage = (ImageView) findViewById(R.id.header);

                    if (beacon.getImage()!= null) {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(
                                    beacon.getImage().getData(),
                                    0,
                                    beacon.getImage().getData().length);
                            headerImage.setImageBitmap(bitmap);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }

                    beaconRecyclerAdapter = new BeaconRecyclerViewAdapter(beaconItems, getApplicationContext());
                    beaconContent.setAdapter(beaconRecyclerAdapter);
                    beaconContent.setLayoutManager(new LinearLayoutManager(self));

                    // Only show the edit fab if the user is the owner
                    ArrayList<String> userBeacons = (ArrayList) user.get("created");
                    if (!beacon.getCreator().getObjectId().equals(user.getObjectId())) {
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
            }
        });
    }

    // Waiting for My Saved Beacons to go through
    public void toggleRating(View view) {
    }

    // Launch Edit Beacon here
    public void editBeacon(View view) {
        Intent intent = new Intent(this, CreateBeaconActivity.class);
        intent.putExtra("beaconId", beacon.getObjectId());
        intent.putExtra("createBeacon", false);

        startActivityForResult(intent, 0);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ParseQuery<Beacon> query = ParseQuery.getQuery("Beacon");
            query.whereEqualTo("objectId", data.getExtras().get("beaconId"));
            doQuery(query);

        }
    }
}
