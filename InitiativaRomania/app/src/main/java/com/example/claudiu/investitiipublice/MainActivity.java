package com.example.claudiu.investitiipublice;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.claudiu.initiativaromania.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    /* UI consts */
    private static final String TAB_MAP             = "Harta";
    private static final String TAB_STATISTICS      = "Statistici";
    public static final int CIRCLE_DEFAULT_RADIUS   = 550;
    public static final int CIRCLE_MIN_RADIUS       = 100;
    public static final int CIRCLE_MAX_RADIUS       = 5000;
    private static final int CIRCLE_ALPHA           = 60;
    private static final int CIRCLE_MARGIN          = 2;
    private static final int MAP_DEFAULT_ZOOM       = 14;
    private static final int DEFAULT_COLOR_RED      = 119;
    private static final int DEFAULT_COLOR_GREEN    = 203;
    private static final int DEFAULT_COLOR_BLUE     = 212;


    /* Setup Objects */
    private GoogleMap mMap;
    private IRLocationListener locationListener = null;
    private IRSeekBarListener seekBarListener = null;

    /* UI objects */
    private Circle circle;
    private Marker currentPos;
    private SeekBar seekBar;
    private SupportMapFragment mapFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);
        System.out.println("On create homescrewn");

        /* Initialize UI components */
        initUI();
    }


    /**
     * Initialize UI components
     */
    private void initUI() {
        /* Tab Bar */
        tabSetup();

        /* Seek bar */
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBarListener = new IRSeekBarListener(circle);
        seekBar.setOnSeekBarChangeListener(seekBarListener);
        seekBar.setProgress(100 * (CIRCLE_DEFAULT_RADIUS - CIRCLE_MIN_RADIUS) / (CIRCLE_MAX_RADIUS - CIRCLE_MIN_RADIUS));

        /* Obtain the SupportMapFragment and get notified when the map is ready to be used. */
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /* Setup tab navigation bar */
    private void tabSetup() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("map");
        tabSpec.setContent(R.id.tabMap);
        tabSpec.setIndicator(TAB_MAP);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("statistics");
        tabSpec.setContent(R.id.tabStatistics);
        tabSpec.setIndicator(TAB_STATISTICS);
        tabHost.addTab(tabSpec);

        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(18);
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
        System.out.println("Map ready");

        mMap = googleMap;

        // Add a marker in Bucharest and move the camera
        LatLng bucharest = new LatLng(44.435503, 26.102513);
        circle = mMap.addCircle(new CircleOptions().center(bucharest)
                .radius(CIRCLE_DEFAULT_RADIUS)
                .fillColor(Color.argb(CIRCLE_ALPHA, DEFAULT_COLOR_RED, DEFAULT_COLOR_GREEN, DEFAULT_COLOR_BLUE))
                .strokeColor(Color.rgb(DEFAULT_COLOR_RED, DEFAULT_COLOR_GREEN, DEFAULT_COLOR_BLUE)).strokeWidth(CIRCLE_MARGIN));
        seekBarListener.setCircle(circle);

        currentPos = mMap.addMarker(new MarkerOptions().position(bucharest).title("Marker in Bucharest"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bucharest));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bucharest, MAP_DEFAULT_ZOOM));

        /* Initialize GPS location */
        this.locationListener = IRLocationListener.getLocationManager(this);
    }


    /**
     * Permission handler for location
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case IRLocationListener.IR_PERMISSION_ACCESS_COURSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Permissions granted");
                    locationListener.setupLocation();

                } else {
                    System.out.println("Permissions were not granted");
                }
                return;
            }
        }
    }


    /**
     * Set the view point on the current location
     * @param location
     */
    public void setInitialPosition(Location location) {
        updateLocationComponents(location);

        // Zoom to the current location
        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, MAP_DEFAULT_ZOOM));
    }

    /**
     * Update the map with the current GPS location
     * @param location
     */
    public void updateLocationComponents(Location location) {
        System.out.println("Location update lat " + location.getLatitude() + " long " + location.getLongitude());

        // Move the circle and the pin to the current location
        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
        this.circle.setCenter(current);
        this.currentPos.setPosition(current);
    }
}
