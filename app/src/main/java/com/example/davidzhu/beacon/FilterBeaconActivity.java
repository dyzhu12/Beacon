package com.example.davidzhu.beacon;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.parse.ParseUser;

/**
 * Created by David Zhu on 5/16/2016.
 */
public class FilterBeaconActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar seekbar;
    private TextView distanceText;
    private TextView applyText;
    private TextView cancelText;
    private RadioGroup rTime;
    private  RadioGroup rRating;
    private  RadioGroup rSort;
    ParseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_beacon);
        user = ParseUser.getCurrentUser();

        seekbar = (SeekBar) findViewById(R.id.seekBar);
        distanceText = (TextView) findViewById(R.id.distance_text);
        rTime = (RadioGroup) findViewById(R.id.time_radio);
        rRating = (RadioGroup) findViewById(R.id.rating_radio);
        rSort = (RadioGroup) findViewById(R.id.sort_radio);
        applyText = (TextView) findViewById(R.id.applyFilter);
        cancelText = (TextView) findViewById(R.id.cancelFilter);

        int time = user.getInt("timeFilter");
        int rating = user.getInt("ratingFilter");
        int distance = user.getInt("distanceFilter");
        String sort = user.getString("sortFilter");

        switch (time){
            case -1: rTime.check(R.id.time_any);
                break;
            case 1: rTime.check(R.id.time_1hr);
                break;
            case 3: rTime.check(R.id.time_3hr);
                break;
            case 6: rTime.check(R.id.time_6hr);
                break;
            case 12: rTime.check(R.id.time_12hr);
                break;
        }

        switch (rating){
            case -1: rRating.check(R.id.rating_any);
                break;
            case 25: rRating.check(R.id.rating_25);
                break;
            case 100: rRating.check(R.id.rating_100);
                break;
            case 500: rRating.check(R.id.rating_500);
                break;
        }

        switch (sort){
            case "distance": rSort.check(R.id.sort_distance);
                break;
            case "popularity": rSort.check(R.id.sort_popularity);
                break;
            case "name": rSort.check(R.id.sort_name);
                break;
        }

        seekbar.setProgress(distance);

        distanceText.setText(seekbar.getProgress() + " mi");
        seekbar.setOnSeekBarChangeListener(this);
        rTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.time_any: user.put("timeFilter",-1);
                        break;
                    case R.id.time_1hr: user.put("timeFilter", 1);
                        break;
                    case R.id.time_3hr: user.put("timeFilter", 3);
                        break;
                    case R.id.time_6hr: user.put("timeFilter", 6);
                        break;
                    case R.id.time_12hr: user.put("timeFilter", 12);
                        break;
                }
            }
        });

        rRating.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rating_any: user.put("ratingFilter",-1);
                        break;
                    case R.id.rating_25: user.put("ratingFilter", 25);
                        break;
                    case R.id.rating_100: user.put("ratingFilter", 100);
                        break;
                    case R.id.rating_500: user.put("ratingFilter", 500);
                        break;
                }
            }
        });

        rSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.sort_distance: user.put("sortFilter","distance");
                        break;
                    case R.id.sort_popularity: user.put("sortFilter", "popularity");
                        break;
                    case R.id.sort_name: user.put("sortFilter", "name");
                        break;
                }
            }
        });

        applyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.saveInBackground();
                finish();
            }
        });

        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.revert();
                finish();
            }
        });



    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        distanceText.setText(seekbar.getProgress() + " mi");
        user.put("distanceFilter", seekBar.getProgress());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }
}
