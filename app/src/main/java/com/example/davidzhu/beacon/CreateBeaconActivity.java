package com.example.davidzhu.beacon;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

    static final int CAMERA_REQUEST = 0;
    static final int GALLERY_REQUEST = 1;
    private Beacon beacon;


    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_beacon);
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.create_map);
//        mapFragment.getMapAsync(this);
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

        beacon = new Beacon();
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

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST);
        }

    }
    public void choosePhoto(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        ImageButton imageButton = (ImageButton) findViewById(R.id.add_photo_button);
        Bitmap image = null;
        switch(requestCode) {
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    imageButton.setImageDrawable(null);
                    imageButton.setBackgroundColor(Color.TRANSPARENT);

                    image = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    imageButton.setImageBitmap(image);

                }
                break;
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    imageButton.setImageDrawable(null);
                    imageButton.setBackgroundColor(Color.TRANSPARENT);

                    Uri uri = imageReturnedIntent.getData();

                    try {

                        image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                        //PARSE IMAGE UPLOAD TEST START****************

                        /*beacon.setImage(image);
                        beacon.saveInBackground();*/
                        /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        // Compress image to lower quality scale 1 - 100
                        image.compress(Bitmap.CompressFormat.JPEG, 1, stream);
                        byte[] imageb = stream.toByteArray();

                        // Create the ParseFile
                        ParseFile file = new ParseFile("image.png", imageb);
                        file.saveInBackground();

                        ParseObject imgupload = new ParseObject("ImageUpload");

                        // Create a column named "ImageName" and set the string
                        imgupload.put("ImageName", "AndroidBegin Logo");

                        // Create a column named "ImageFile" and insert the image
                        imgupload.put("ImageFile", file);

                        // Create the class and the columns
                        imgupload.saveInBackground();*/

                        //PARSE IMAGE UPLOAD TEST END******************

                        int thumbFactor = 4; // choose a power of 2
                        Bitmap thumb = Bitmap.createScaledBitmap(image, image.getWidth()/thumbFactor, image.getHeight()/thumbFactor, false);

                        imageButton.setImageBitmap(thumb);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default: break;
        }
        if(image != null) {
            beacon.setImage(image);
            beacon.saveInBackground();
        }


    }

    public void createBeacon(View view) {
        EditText nameText = (EditText) findViewById(R.id.create_beacon_name_text);
        String beaconName = nameText.getText().toString();

        // beacon.setName(beaconName);

        EditText addressText = (EditText) findViewById(R.id.create_beacon_address_text);
        String address = addressText.getText().toString();
        double lat = 0.0;
        double longitude = 0.0;
        LatLng latlngObject;

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(address, 1);
            lat = addressList.get(0).getLatitude();
            longitude = addressList.get(0).getLongitude();

            Double latE6 = lat * 1E6;
            Double longE6 = lat * 1E6;

            latlngObject = new LatLng(lat, longitude);
        } catch (IOException e) {
            e.printStackTrace();
        }

        EditText tagText = (EditText) findViewById(R.id.create_beacon_tags_text);
        String tags = tagText.getText().toString();
        ArrayList tagsArray = (ArrayList) Arrays.asList(tags.split("\\s+"));

        EditText phoneText = (EditText) findViewById(R.id.create_beacon_phone_text);
        String phoneNumber = phoneText.getText().toString();

        EditText webText = (EditText) findViewById(R.id.create_beacon_web_text);
        String website = webText.getText().toString();

        System.out.println("Finished!");

        finish();
    }

}
