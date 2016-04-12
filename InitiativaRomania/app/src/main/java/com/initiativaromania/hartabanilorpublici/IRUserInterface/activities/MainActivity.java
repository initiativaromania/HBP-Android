/**
 This file is part of "Harta Banilor Publici".

 "Harta Banilor Publici" is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 "Harta Banilor Publici" is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.initiativaromania.hartabanilorpublici.IRUserInterface.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.initiativaromania.hartabanilorpublici.IRData.Buyer;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.map.IRLocationListener;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.map.IRSeekBarListener;
import com.initiativaromania.hartabanilorpublici.R;
import com.initiativaromania.hartabanilorpublici.IRData.CommManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    /* UI consts */
    private static final String TAB_MAP             = "Harta";
    private static final String TAB_STATISTICS      = "Contracte";
    public static final int CIRCLE_DEFAULT_RADIUS   = 1100;
    public static final int CIRCLE_MIN_RADIUS       = 100;
    public static final int CIRCLE_MAX_RADIUS       = 5000;
    private static final int CIRCLE_ALPHA           = 70;
    private static final int CIRCLE_MARGIN          = 2;
    private static final int MAP_DEFAULT_ZOOM       = 13;
    private static final int DEFAULT_COLOR_RED      = 119;
    private static final int DEFAULT_COLOR_GREEN    = 203;
    private static final int DEFAULT_COLOR_BLUE     = 212;
    private static final int DEFAULT_COLOR_HUE      = 190;
    public static final String EXTRA_DISPLAY_INFOGRAPHIC = "Display infographic";


    /* Setup Objects */
    private GoogleMap mMap;
    public static IRLocationListener locationListener = null;
    private IRSeekBarListener seekBarListener = null;
    private String tabtitles[] = new String[] {TAB_MAP, TAB_STATISTICS };
    private boolean displayInfographic = true;

    /* UI objects */
    public static Circle circle;
    private Marker currentPos;
    public static Location currentLocation;
    private SeekBar seekBar;
    public static TextView seekBarValue;
    private SupportMapFragment mapFragment;
    private int currentTab = 0, lastTab = 0;
    public static Context context;
    private BitmapDescriptor bitmapIcon = null;
    public static Animation animationFadeIn;
    public static Animation animationFadeOut;


    /* Data objects */
    HashMap<Marker, Buyer> markerBuyers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        System.out.println("On create homescreen");

        context = this;

        /* Initialize communication with the server */
        CommManager.init(this);

        /* Initialize UI components */
        initUI();
    }

    @Override
    public void onStart() {
        super.onStart();

        System.out.println(" MainActivity: On start()");
    }


    @Override
    protected void onPause() {
        super.onPause();

        /* Disable the GPS Listener */
        if (this.locationListener != null)
            this.locationListener.pauseGPSListener();
    }


    @Override
    protected void onResume() {
        super.onResume();

        System.out.println(" MainActivity: On Resume()");

        /* Resume connection to GPS */
        if (this.locationListener != null) {
            this.locationListener.setupLocation(true);
        }
    }


    /* Initialize transparent view */
    private void initTransparentView() {

        /* Avoid displaying infographic if the home button has been pushed */
        Intent intent = getIntent();
        displayInfographic = intent.getBooleanExtra(EXTRA_DISPLAY_INFOGRAPHIC, true);
        System.out.println("Display info " + displayInfographic);
        if (!displayInfographic) {
            LinearLayout linear = (LinearLayout) findViewById(R.id.transparentLayer);;
            linear.setVisibility(View.INVISIBLE);
            return;
        }

        /* Setup the OK button */
        Button okButton = (Button) findViewById(R.id.okButton);
        if (okButton != null) {
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("OK button pushed");

                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_down);
                    LinearLayout linear = (LinearLayout) findViewById(R.id.transparentLayer);
                    linear.startAnimation(animation);
                    linear.setVisibility(View.INVISIBLE);

                    Toast toast = Toast.makeText(getBaseContext(),
                            "Apasa pe simbolurile € din jurul tau",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                    /* Display initial seek bar value */
                    Rect thumbRect = seekBar.getThumb().getBounds();
                    seekBarValue.setX(thumbRect.exactCenterX());
                    DecimalFormat dm = new DecimalFormat("###,###.###");
                    seekBarValue.setText(" " + String.valueOf(dm.format(CommManager.aroundTotalSum)) + " EURO ");
                    seekBarValue.setVisibility(View.VISIBLE);
                    seekBarValue.startAnimation(animationFadeIn);
                }
            });
        }

        /* Send request to get the init data */
        CommManager.requestInitData(this);
    }


    /**
     * Initialize UI components
     */
    private void initUI() {

        FacebookSdk.sdkInitialize(getApplicationContext());

        /* Tab Bar */
        tabSetup();


        /* Seek bar */
        seekBarValue = (TextView) findViewById(R.id.seekBarValue);

        /* SeekBar value animations */
        animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fadein);
        animationFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                seekBarValue.startAnimation(animationFadeOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        animationFadeOut = AnimationUtils.loadAnimation(context, R.anim.fadeout);
        animationFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                seekBarValue.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBarListener = new IRSeekBarListener(circle);
        seekBar.setOnSeekBarChangeListener(seekBarListener);
        seekBar.setProgress(100 * (CIRCLE_DEFAULT_RADIUS - CIRCLE_MIN_RADIUS) / (CIRCLE_MAX_RADIUS - CIRCLE_MIN_RADIUS));


        /* Transparent layer with information */
        initTransparentView();

        /* Information button */
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        if (imageButton != null) {
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     /* Start the info activity */
                    Intent intent = new Intent(getBaseContext(), InfoActivity.class);
                    startActivity(intent);
                }
            });
        }

        /* Obtain the SupportMapFragment and get notified when the map is ready to be used. */
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /* Receive and display init data in the infographic */
    public void receiveInitData(JSONObject response) {
        System.out.println("Receiving init data");

        try {
            TextView tv = (TextView) findViewById(R.id.textViewNrBuyers);
            if (tv != null)
                tv.setText(response.getString("buyers"));

            tv = (TextView) findViewById(R.id.textViewNrCompanies);
            if (tv != null)
                tv.setText(response.getString("companies"));

            tv = (TextView) findViewById(R.id.textViewNrContracts);
            if (tv != null)
                tv.setText(response.getString("contracts"));

            tv = (TextView) findViewById(R.id.textViewValueContracts);
            if (tv != null) {
                double totalValue = Double.parseDouble(response.getString("sumprice"));
                totalValue /= 1000000;
                tv.setText(String.format("%.2f", totalValue));
            }

            tv = (TextView) findViewById(R.id.textViewNrJustifies);
            if (tv != null)
                tv.setText(response.getString("justifies"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize data from the server on the UI
     */
    private void initData() {
        System.out.println("Getting all the data from server");

        /* Init the hashmap Marker - Buyer */
        markerBuyers = new HashMap<Marker, Buyer>();

        /* Send a request to get all the buyers */
        if (CommManager.buyers.isEmpty())
            /* Get all the buyers if not already available */
            CommManager.requestAllBuyers(this);
        else
            /* Show all the contracts on the map */
            displayBuyers();
    }


    /* Receive all the buyers from the server */
    public void receiveAllBuyers(JSONObject response) {
        System.out.println("Receiving all the buyers");
        CommManager.buyers = new LinkedList<Buyer>();
        Buyer buyer;

        /* Parse the list of buyers */
        try {
            JSONArray jsonBuyersList = response.getJSONArray("buyers");

            for (int i = 0; i < jsonBuyersList.length(); i++) {
                JSONObject obj = jsonBuyersList.getJSONObject(i);

                buyer = new Buyer();
                buyer.latitude = obj.getDouble("lat");
                buyer.longitude = obj.getDouble("lng");
                buyer.name = obj.getString("buyer");
                buyer.totalPrice = obj.getDouble("sum");

                CommManager.buyers.add(buyer);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommManager.updateAroundBuyers();

        /* Show the received buyers */
        displayBuyers();
    }


    /* Display each buyer from the list in CommManager */
    public void displayBuyers() {

        System.out.println("Displaying the available buyers");

        /* Prepare the bitmap image */
        if (bitmapIcon == null) {
            Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("euro", "drawable", getPackageName()));
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, imageBitmap.getWidth() / 4,
                    imageBitmap.getHeight() / 4, false);
            bitmapIcon = BitmapDescriptorFactory.fromBitmap(resizedBitmap);
        }

        /* Set a pin for each buyer */
        for (Buyer buyer: CommManager.buyers) {
            /* Add pin to the map */
            LatLng location = new LatLng(buyer.latitude, buyer.longitude);

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title("Autoritate contractanta")
                    .icon(bitmapIcon));


            markerBuyers.put(marker, buyer);
        }

         /* Set on click listener for each pin */
        mMap.setOnMarkerClickListener(
                new GoogleMap.OnMarkerClickListener() {

                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Buyer buyer = markerBuyers.get(marker);


                        if (buyer != null) {
                             /* Start a separate view for a buyer */
                            Intent intent = new Intent(getBaseContext(), ParticipantActivity.class);
                            intent.putExtra(ParticipantActivity.CONTRACT_LIST_TYPE, ParticipantActivity.CONTRACT_LIST_FOR_BUYER);
                            intent.putExtra(ParticipantActivity.CONTRACT_LIST_EXTRA, buyer.name);
                            startActivity(intent);
                        } else {
                            /* Offer details about the user's position */
                            Toast.makeText(getBaseContext(),
                                    "Asta e pozitia ta. Modifica aria de interes si vezi statistici " +
                                            "despre contractele din jurul tau",
                                    Toast.LENGTH_LONG).show();
                        }

                        return true;
                    }
                }
        );
    }

    /**
     * Create primary tab view
     * @param context
     * @param tabText
     * @return
     */
    private View createTabView(Context context, String tabText) {
        View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
        TextView tv = (TextView) view.findViewById(R.id.tabsText);
        tv.setText(tabText);
        return view;
    }


    /* Setup tab navigation bar */
    private void tabSetup() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        View tabView = createTabView(this, TAB_MAP);
        TabHost.TabSpec spec = tabHost.newTabSpec("tab1").setIndicator(tabView)
                .setContent(R.id.tabMap);
        tabHost.addTab(spec);

        tabView = createTabView(this, TAB_STATISTICS);
        spec = tabHost.newTabSpec("tab2").setIndicator(tabView)
                .setContent(R.id.tabStatistics);
        tabHost.addTab(spec);
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

        currentPos = mMap.addMarker(new MarkerOptions().position(bucharest).title("Locatia ta"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bucharest));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bucharest, MAP_DEFAULT_ZOOM));

        /* Initialize GPS location */
        this.locationListener = IRLocationListener.getLocationManager(this);

        /* Init data from server */
        initData();
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
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Permissions granted");
                    locationListener.setupLocation(false);

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
        System.out.println("Set initial position");

        if (location == null)
            return;

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

        if (location == null)
            return;

        this.currentLocation = location;

        System.out.println("Location update lat " + location.getLatitude() + " long " + location.getLongitude());

        // Move the circle and the pin to the current location
        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
        this.circle.setCenter(current);
        this.currentPos.setPosition(current);
    }

}
