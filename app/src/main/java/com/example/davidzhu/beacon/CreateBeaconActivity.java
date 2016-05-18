package com.example.davidzhu.beacon;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.barcode.Barcode;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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

    private long startTimeNum;
    private long endTimeNum;

    private static final int MY_PERMISSIONS_READ_STORAGE = 0;
    private boolean editMode;
    private Calendar calendar = Calendar.getInstance();

    private EditText nameText;
    private EditText addressText;
    private EditText tagText;
    private EditText phoneText;
    private EditText webText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_beacon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.create_beacon_toolbar);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // Initialize Edit Texts
        nameText = (EditText) findViewById(R.id.create_beacon_name_text);
        addressText = (EditText) findViewById(R.id.create_beacon_address_text);
        tagText = (EditText) findViewById(R.id.create_beacon_tags_text);
        phoneText = (EditText) findViewById(R.id.create_beacon_phone_text);
        webText = (EditText) findViewById(R.id.create_beacon_web_text);

        //Date picker
        dateText = (EditText) findViewById(R.id.create_beacon_date_text);
        dateText.setOnFocusChangeListener(this);

        editMode = !((boolean) getIntent().getExtras().get("createBeacon"));
        if (!editMode) {
            ab.setTitle(R.string.create_beacon);
            beacon = new Beacon();
        } else {
            ab.setTitle(R.string.edit_beacon);

            ParseQuery<Beacon> query = ParseQuery.getQuery("Beacon");
            query.whereEqualTo("objectId", getIntent().getExtras().get("beaconId"));
            final CreateBeaconActivity self = this;
            query.findInBackground(new FindCallback<Beacon>() {
                @Override
                public void done(List<Beacon> objects, ParseException e) {
                    if (e == null && objects != null && objects.size() > 0) {
                        beacon = objects.get(0);

                        nameText.setText(beacon.getString("name"));
                        addressText.setText(beacon.getString("address"));

                        ArrayList<String> tags = (ArrayList<String>) beacon.get("tags");
                        String tagString = "";
                        for (int i = 0; i < tags.size(); i++) {
                            tagString += tags.get(i) + " ";
                        }
                        tagString.trim();
                        tagText.setText(tagString);

                        phoneText.setText(beacon.getString("phone"));
                        webText.setText(beacon.getString("website"));

                        SimpleDateFormat dayFormat = new SimpleDateFormat("MMM d", Locale.US);
                        String day = dayFormat.format(beacon.getDate("startDate"));

                        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                        String time = " from " + timeFormat.format(beacon.getDate("startDate")) + " to " +
                                timeFormat.format(beacon.getDate("endDate"));

                        dateText.setText(day + time);

                        ImageButton imageButton = (ImageButton) findViewById(R.id.add_photo_button);
                        if (beacon.getImage()!= null) {
                            try {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(
                                        beacon.getImage().getData(),
                                        0,
                                        beacon.getImage().getData().length);
                                imageButton.setImageBitmap(bitmap);
                                imageButton.setBackground(null);
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }


                    }
                }
            });
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.create_beacon_submit) {
            createBeacon();
        }
        if (id == android.R.id.home && editMode) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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
                    startTimeNum = calendar.getTimeInMillis();
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
                    endTimeNum = calendar.getTimeInMillis();
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_READ_STORAGE);
        } else {
            showDialog();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_READ_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   showDialog();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void showDialog() {
        final CharSequence[] items = { "Take Photo", "Choose Photo" };
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setTitle(R.string.change_photo);
        dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {

                    takePhoto();

                } else if (items[item].equals("Choose Photo")) {
                    choosePhoto();
                }

            }
        });
        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {}
        });

        dialogBuilder.show();


    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST);
        }

    }
    public void choosePhoto() {
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
                        beacon.setImage(image);
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

    public void createBeacon() {
        String beaconName;
        beaconName = nameText.getText().toString();

        if (!beaconName.equals(null)) {
            beacon.setDisplayName(beaconName);
        }

        String address = addressText.getText().toString();
        if (!address.equals(null)) {
            beacon.setAddress(address);
        }


        double lat = 0.0;
        double longitude = 0.0;

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(address, 1);

            if (addressList.size() > 0) {
                lat = addressList.get(0).getLatitude();
                longitude = addressList.get(0).getLongitude();

                ParseGeoPoint geoPoint = new ParseGeoPoint(lat, longitude);
                beacon.setLocation(geoPoint);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (beaconDate != null) {
            beaconDate.setTime(startTimeNum);
            beacon.setStartDate(beaconDate);

            beaconDate.setTime(endTimeNum);
            beacon.setEndDate(beaconDate);

        }

        String[] tags = tagText.getText().toString().split("\\s+");
        ArrayList<String> tagsArray = new ArrayList<String>();
        for (int i = 0; i < tags.length; i++) {
            tagsArray.add(tags[i]);
        }

        if (tagsArray.size() > 0) {
            beacon.setTags(tagsArray);
        }

        String phoneNumber = phoneText.getText().toString();
        if (!phoneNumber.equals(null)) {
            beacon.setPhone(phoneNumber);
        }

        String website = webText.getText().toString();
        if (!website.equals(null)) {
            beacon.setWebsite(website);
        }




        if (editMode) {
            Intent intent = new Intent();
            intent.putExtra("beaconId", beacon.getObjectId());
            setResult(RESULT_OK, intent);
        } else {
            beacon.initPopularity();
            beacon.setCreator(ParseUser.getCurrentUser());
        }

        beacon.initPopularity();
        beacon.saveInBackground();
        finish();
    }
}
