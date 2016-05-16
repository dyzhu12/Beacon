package com.example.davidzhu.beacon;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by David Zhu on 5/16/2016.
 */
public class FilterBeaconActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar seekbar;
    private TextView distanceText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_beacon);

        seekbar = (SeekBar) findViewById(R.id.seekBar);
        distanceText = (TextView) findViewById(R.id.distance_text);

        distanceText.setText(seekbar.getProgress() + " mi");
        seekbar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        distanceText.setText(seekbar.getProgress() + " mi");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
