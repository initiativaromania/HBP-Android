package com.example.claudiu.investitiipublice.IRUserInterface;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.claudiu.initiativaromania.R;
import com.example.claudiu.investitiipublice.IRObjects.Company;
import com.example.claudiu.investitiipublice.IRObjects.Contract;
import com.example.claudiu.investitiipublice.IRObjects.CommManager;
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


    /* Setup Objects */
    private GoogleMap mMap;
    private IRLocationListener locationListener = null;
    private IRSeekBarListener seekBarListener = null;
    private String tabtitles[] = new String[] {TAB_MAP, TAB_STATISTICS };

    /* UI objects */
    public static Circle circle;
    private Marker currentPos;
    public static Location currentLocation;
    private SeekBar seekBar;
    private SupportMapFragment mapFragment;
    private int currentTab = 0, lastTab = 0;
    private Context context;
    private BitmapDescriptor bitmapIcon = null;

    /* Data objects */
    HashMap<Marker, Contract> markerContracts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        System.out.println("On create homescrewn");

        context = this;

        /* Initialize UI components */
        initUI();
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

        /* Resume connection to GPS */
        if (this.locationListener != null) {
            this.locationListener.setupLocation(true);
        }
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


        /* Transparent layer with information */
        Button okButton = (Button) findViewById(R.id.okButton);
        if (okButton != null) {
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("OK button pushed");

                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_down);
                    LinearLayout linear = (LinearLayout) findViewById(R.id.transparentLayer);
                    if (linear == null)
                        System.out.println("Frame layout is null");
                    else
                        System.out.println("Frame layout ok");
                    linear.startAnimation(animation);
                    linear.setVisibility(View.INVISIBLE);

                    Toast.makeText(getBaseContext(),
                            "Apasa pe simbolul € din jurul tau",
                            Toast.LENGTH_LONG).show();


                }
            });
        }

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


    /**
     * Initiatlize data from the server on the UI
     */
    private void initData() {
        System.out.println("Getting all the data from server");

        /* Init the hashmap Marker - Contracts */
        markerContracts = new HashMap<Marker, Contract>();

        /* Send a request to get all the cotnracts */
        CommManager.init(this);

        if (CommManager.contracts.isEmpty())
            /* Get all the contracts if not available */
            CommManager.requestAllContracts(this);
        else
            /* Show all the contracts on the map */
            displayContracts();
    }


    /* Receive all the contracts from the server */
    public void receiveAllContracts(JSONObject response) {
        System.out.println("Received new contracts");
        CommManager.contracts = new LinkedList<Contract>();
        Contract contract;

        /* Parse the list of contracts */
        try {
            JSONArray jsonContractList = response.getJSONArray("orders");

            for (int i = 0; i < jsonContractList.length(); i++) {
                JSONObject obj = jsonContractList.getJSONObject(i);

                contract = new Contract();
                contract.id = obj.getInt("id");
                contract.latitude = obj.getDouble("lat");
                contract.longitude = obj.getDouble("lng");
                contract.title = obj.getString("title");
                contract.company = new Company();
                contract.company.name = obj.getString("company");
                contract.authority = obj.getString("buyer");

                CommManager.contracts.add(contract);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Show the received contracts */
        displayContracts();
    }


    /* Display each contract from the list in CommManager */
    public void displayContracts() {

        System.out.println("Displaying the available contracts");

        /* Prepare the bitmap image */
        if (bitmapIcon == null) {
            Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("euro", "drawable", getPackageName()));
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, imageBitmap.getWidth() / 4,
                    imageBitmap.getHeight() / 4, false);
            bitmapIcon = BitmapDescriptorFactory.fromBitmap(resizedBitmap);
        }

        /* Set a pin for each contract */
        for (Contract contract: CommManager.contracts) {
            /* Add pin to the map */
            LatLng location = new LatLng(contract.latitude, contract.longitude);

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title("Autoritate contractanta")
                    .icon(bitmapIcon));


            markerContracts.put(marker, contract);
        }

         /* Set on click listener for each pin */
        mMap.setOnMarkerClickListener(
                new GoogleMap.OnMarkerClickListener() {

                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Contract contract = markerContracts.get(marker);


                        if (contract != null) {
                             /* Start a separate view for a company */
                            Intent intent = new Intent(getBaseContext(), ContractListActivity.class);
                            intent.putExtra(ContractListActivity.CONTRACT_LIST_TYPE, ContractListActivity.CONTRACT_LIST_FOR_BUYER);
                            intent.putExtra(ContractListActivity.CONTRACT_LIST_EXTRA, contract.authority);
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
        int viewId;
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
