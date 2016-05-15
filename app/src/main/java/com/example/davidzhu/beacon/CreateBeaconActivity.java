package com.example.davidzhu.beacon;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by David Zhu on 4/26/2016.
 */
public class CreateBeaconActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnFocusChangeListener {

    private GoogleMap mMap;
    private EditText dateText;

    private Date beaconDate;
    private String startTime;
    private String endTime;

    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_beacon);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.create_map);
        mapFragment.getMapAsync(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.create_beacon_toolbar);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.create_beacon);

        //Date picker
        dateText = (EditText) findViewById(R.id.create_beacon_date_text);
        dateText.setOnFocusChangeListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Disables map toolbar from appearing when marker is clicked
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setMapToolbarEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_beacon_toolbar, menu);
        return true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            selectBeaconDate(v);
        }
    }

    /*
     * Create a new Date Picker Dialog specifically for the beacon day
     */
    public void selectBeaconDate(View view) {

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                beaconDate = calendar.getTime();
                selectBeaconHours(true);
            }
        };

        new DatePickerDialog(CreateBeaconActivity.this,
                date, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /*
     * Use the same method for selecting both the start and end time of a beacon
     */
    public void selectBeaconHours(boolean isStartTime) {
        TimePickerDialog timePicker;
        final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        if (isStartTime) {
            timePicker = new TimePickerDialog(CreateBeaconActivity.this, new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    startTime = timeFormat.format(calendar.getTime());
                    selectBeaconHours(false);
                }
            }, 0, 0, false);
            timePicker.setTitle(R.string.start_time);
        } else {
            timePicker = new TimePickerDialog(CreateBeaconActivity.this, new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    endTime = timeFormat.format(calendar.getTime());
                    updateLabel();
                }
            }, 0, 0, false);
            timePicker.setTitle(R.string.end_time);
        }

        timePicker.show();
    }

    // Update Hours label once time picking is finished
    public void updateLabel() {
        String format = "MMM d";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);

        String timeText = dateFormat.format(beaconDate) + " from  " + startTime + " to " + endTime;
        dateText.setText(timeText);
    }

}
