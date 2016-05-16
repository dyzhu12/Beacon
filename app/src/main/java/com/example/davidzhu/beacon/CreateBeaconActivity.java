package com.example.davidzhu.beacon;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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

    /*
     * Handler for ImageButton for adding photos
     */
    public void addPhoto(final View v) {

        final CharSequence[] items = { "Take Photo", "Choose Photo" };
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setTitle(R.string.change_photo);
        dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    takePhoto(v);
                } else if (items[item].equals("Choose Photo")) {
                    choosePhoto(v);
                }
            }
        });
        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {}
        });

        dialogBuilder.show();
    }

    public void takePhoto(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }
    public void choosePhoto(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        ImageButton imageButton = (ImageButton) findViewById(R.id.add_photo_button);
//        switch(requestCode) {
//            case 0:
//                if (resultCode == RESULT_OK) {
//
//                }
//                break;
//            case 1:
//                if (resultCode == RESULT_OK) {
//
//                }
//                break;
//            default: break;
//        }
        if (resultCode == RESULT_OK) {
            System.out.println("RESULT OK");
            System.out.println(requestCode);
            System.out.println(resultCode);
            System.out.println(imageReturnedIntent);
            System.out.println(imageReturnedIntent.getDataString());

            Uri uri = imageReturnedIntent.getData();
            imageButton.setImageDrawable(null);
            imageButton.setBackgroundColor(Color.TRANSPARENT);

            System.out.println(getImageBitmap(uri.getPath()));
            imageButton.setImageBitmap(getImageBitmap(imageReturnedIntent.getDataString()));

        }
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
        }
        return bm;
    }

}
