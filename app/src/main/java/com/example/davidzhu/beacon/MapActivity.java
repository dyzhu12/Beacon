package com.example.davidzhu.beacon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.location.Location;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.parse.ParseUser;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.FindCallback;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapActivity extends FragmentActivity implements
        OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        DrawerLayout.DrawerListener,
        LocationListener, GoogleMap.OnMarkerClickListener {

    public static final String TAG = MapActivity.class.getSimpleName();

    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 0;
    private static final int CREATE_BEACON_REQUEST = 1;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private final Map<String, Marker> mapMarkers = new HashMap<String, Marker>();

    private Location mCurrentLocation;
    private Location lastLocation;
    private static final int MAX_POST_SEARCH_RESULTS = 20;

    private int mostRecentMapUpdate;

    private ParseUser user;


    //MUST BE CHANGED TO USER SETTINGS
    private float radius = 2;

    // Debugging switch
    public static final boolean APPDEBUG = false;

    // Debugging tag for the application
    public static final String APPTAG = "Beacon";
    private SupportMapFragment mapFragment;

    private ArrayList<Beacon> visibleBeacons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //creates GoogleApiClient object
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mapFragment.getMap().setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            public void onCameraChange(CameraPosition position) {
                // When the camera changes, update the query
                doMapQuery();
            }
        });


        //creates LocationRequest object
        mLocationRequest = LocationRequest.create()
            .setInterval(10*1000) //10s in millisecs
            .setFastestInterval(1*1000) //1s in millisecs
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(this);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        
        user = ParseUser.getCurrentUser();

        EditText searchBar = (EditText) findViewById(R.id.main_search);
        searchBar.setCursorVisible(false);
        searchBar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchSearch();
            }
        });

    }

    protected void onResume(){
        super.onResume();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        // Disables map toolbar from appearing when marker is clicked
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setMapToolbarEnabled(false);

        //setMyLocationButtons changes view back to user's current location
        mUiSettings.setMyLocationButtonEnabled(false);
        enableMyLocation();
        mMap.setOnMarkerClickListener(this);

        mMap.addMarker(new MarkerOptions().position(new LatLng(38.9897, -76.9378)));

    }

    /**
     *Checks if ACCESS_FINE_LOCATION permission has been granted. If not, requests the user for it
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //check  request fine location permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }

    private void moveToNewLocation(Location location){
        double currentLat = location.getLatitude();
        double currentLong = location.getLongitude();
        float zoomLevel = 15f;

        LatLng latLng = new LatLng(currentLat, currentLong);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(location == null){
                //request location updates
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                moveToNewLocation(location);
            }
        } else{
             //ACCESS_FINE_LOCATION_PERMISSION has not been granted (should never get here, b/c onMapReady takes care of permissions)
              System.out.println("in OnConnected, permissions not granted");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended.");
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("LocationServices", "Connection failed: ConnectionResult.getErrorCode() " + connectionResult.getErrorCode());
    }

    public void onLocationChanged(Location location){
        moveToNewLocation(location);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
               String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                        System.out.println("fine location permission granted, mylocation enabled");
                    }
                } else {
                    // permission denied
                    Log.d(TAG, "ACCESS_FINE_LOCATION_PERMISSION denied");
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds mDefaultTagsNames to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle menu nav drawer item clicks here.
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.my_saved_beacons) {

        } else if (id == R.id.my_created_beacons) {
            intent = new Intent(this, MyCreatedBeaconsActivity.class);
        } else if (id == R.id.notification_settings) {

        } else if (id == R.id.my_account) {
            intent = new Intent(this, MyAccountActivity.class);
        } else if (id == R.id.about) {
            intent = new Intent(this, AboutActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //listener for Create Beacon fab
    public void launchCreateBeacon(View view) {
        Intent intent = new Intent(this, CreateBeaconActivity.class);
        intent.putExtra("createBeacon", true);
        startActivity(intent);
    }

    //listener for User Location fab

    public void centerUserLocation(View view) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null) {
                //request location updates
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                moveToNewLocation(location);
            }
        }
    }


    // listener for Filter toolbar button
    public void showFilters(View view) {
        Intent intent = new Intent(this, FilterBeaconActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.stay);
    }

    // Listener for List Beacons
    public void showBeaconList(View view) {
        Intent intent = new Intent(this, ListBeaconActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.stay);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        ParseUser user = ParseUser.getCurrentUser();
        ((TextView)findViewById(R.id.nav_drawer_email)).setText((String)user.get("email"));
        ((TextView)findViewById(R.id.nav_drawer_name)).setText((String)user.get("username"));
    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    private void doMapQuery() {
        final int myUpdateNumber = ++mostRecentMapUpdate;
        Location myLoc = null;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myLoc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
//        Location myLoc = (mCurrentLocation == null) ? lastLocation : mCurrentLocation;
        // If location info isn't available, clean up any existing markers
        if (myLoc == null) {
            mMap.clear();
            return;
        }
        final ParseGeoPoint myPoint = geoPointFromLocation(myLoc);
        user.put("location",myPoint);
        user.saveInBackground();
        // Create the map Parse query


        //ParseQuery<Beacon> mapQuery = Beacon.getQuery();
        ParseQuery<Beacon> mapQuery = ParseQuery.getQuery("Beacon");
        mapQuery.whereExists("location");

        // Set up additional query filters
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int distance = user.getInt("distanceFilter");
        int popularity = user.getInt("ratingFilter");
        String sort = user.getString("sortFilter");
        int t = user.getInt("timeFilter");
        Date curTime=new Date();
        mapQuery.whereGreaterThanOrEqualTo("startDate", curTime);
        if(t != -1){
            Date maxTime=new Date();
            maxTime.setTime(System.currentTimeMillis()+(t*(60*60*1000)));
            mapQuery.whereLessThanOrEqualTo("startDate", maxTime);
        }



        List<String> savedTags = user.getList("savedTags");
        if(savedTags != null && savedTags.size()>0){
            mapQuery.whereContainedIn("tags",savedTags);
        }

        if(distance != -1){
            mapQuery.whereWithinMiles("location", myPoint, distance);
        }

        if(popularity != -1){
            mapQuery.whereGreaterThanOrEqualTo("popularity",popularity);
        }




        //mapQuery.whereWithinMiles("location", myPoint, 5);
        //mapQuery.whereEqualTo("user","0g6uYAQYon");
        //mapQuery.include("user");

        //mapQuery.whereExists("name");
        //mapQuery.orderByDescending("createdAt");
        //mapQuery.setLimit(20);
        // Kick off the query in the background
        mapQuery.findInBackground(new FindCallback<Beacon>() {
            @Override
            public void done(List<Beacon> objects, ParseException e) {
                if (e != null) {
                    if (StarterApplication.APPDEBUG) {
                        Log.d(StarterApplication.APPTAG, "An error occurred while querying for map posts.", e);
                    }
                    return;
                }
        /*
         * Make sure we're processing results from
         * the most recent update, in case there
         * may be more than one in progress.
         */
                if (myUpdateNumber != mostRecentMapUpdate) {
                    return;
                }

                mMap.clear();

                visibleBeacons = new ArrayList<Beacon>(objects);
                // Loop through the results of the search
                for (Beacon beacon : objects) {

                    String i = beacon.getObjectId();
                    ParseUser c = beacon.getCreator();
                    String a = c.getObjectId();
                    ArrayList<String> tags = beacon.getTags();
                    Date tt = beacon.getStartDate();




                        MarkerOptions markerOpts =
                                new MarkerOptions().position(new LatLng(beacon.getLocation().getLatitude(), beacon
                                        .getLocation().getLongitude())).snippet(beacon.getObjectId());
                        // Display a green marker with the post information

                    if(tags.contains("Food")){
                        markerOpts =
                            markerOpts.title(beacon.getDisplayName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                    }
                    else if(tags.contains("Free")){

                        markerOpts =
                                markerOpts.title(beacon.getDisplayName())
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    }
                    else if(tags.contains("Fun")){
                        markerOpts =
                                markerOpts.title(beacon.getDisplayName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }
                    else if(tags.contains("Fabulous")){
                        markerOpts =
                                markerOpts.title(beacon.getDisplayName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                    }else{
                        markerOpts =
                                markerOpts.title(beacon.getDisplayName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    }


                    /*
                    Free Green
                    Food Blue
                    Fun Red
                    Fabulous Pink

                     */



                        // Display a green marker with the post information
                        /*markerOpts =
                                markerOpts.title(beacon.getDisplayName()).snippet(beacon.getCreator().getObjectId())
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));*/

                        // Add a new marker
                        mapFragment.getMap().addMarker(markerOpts);


                }

            }
        });
    }

    private ParseGeoPoint geoPointFromLocation(Location loc) {
        return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
    }


    @Override
    public boolean onMarkerClick(Marker marker) {


        String id = marker.getSnippet();
        Intent intent = new Intent(this, ViewBeaconActivity.class);
        intent.putExtra("beaconId", id);
        startActivity(intent);
        return false;
    }

    private void launchSearch() {
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }

}
