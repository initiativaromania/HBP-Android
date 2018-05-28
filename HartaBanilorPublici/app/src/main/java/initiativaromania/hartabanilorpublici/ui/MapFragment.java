package initiativaromania.hartabanilorpublici.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import android.content.pm.PackageManager;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.android.gms.location.places.GeoDataClient;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import initiativaromania.hartabanilorpublici.comm.CommManager;
import initiativaromania.hartabanilorpublici.comm.CommManagerResponse;
import initiativaromania.hartabanilorpublici.comm.HBPLocationListener;
import initiativaromania.hartabanilorpublici.data.PublicInstitutionsManager;
import initiativaromania.hartabanilorpublici.R;
import initiativaromania.hartabanilorpublici.data.PublicInstitution;


public class MapFragment extends android.support.v4.app.Fragment
        implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnCameraIdleListener,
        GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener,
        ClusterManager.OnClusterItemClickListener, ClusterManager.OnClusterClickListener {

    private static final int MAP_DEFAULT_ZOOM                           = 10;
    private static final int MAP_DETAILED_ZOOM                          = 13;
    public static final int HBP_PERMISSION_ACCESS_COURSE_LOCATION       = 19;
    private static final int LOCATION_UPDATE_INTERVAL                   = 30;
    private static final int LOCATION_UPDATE_INTERVAL_MIN               = 10;
    public static final int MAP_NAVIGATION_ID                           = 0;

    /* Setup Objects */
    public ClusterManager<PublicInstitution> clusterManager = null;
    private View originalView;

    /* UI Objects */
    private GoogleMap mMap;
    private LatLngBounds currentBounds;
    private BitmapDescriptor bitmapIcon = null;
    private View mWindow;
    private View mContents;

    /* Data objects */
    HashMap<Marker, PublicInstitution> markerPublicInstitutions;
    ArrayList<PublicInstitution> tmpPIs;
    MapFragment fragmentCopy;
    LayoutInflater layoutInflater;
    boolean clickedOnItem = false;
    boolean dataInited = false;
    PublicInstitution clickedPI;
    Marker clickedMarker;
    Location currentLocation;
    boolean mLocationPermissionGranted;

    /* Location objects */
    GeoDataClient mGeoDataClient;
    PlaceDetectionClient mPlaceDetectionClient;
    FusedLocationProviderClient mFusedLocationProviderClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;



    public MapFragment() {
    };


    /**
     * Initialize data from the server on the UI
     */
    private void initData() {
        if (dataInited == true)
            return;

        /* Initialize location objects */
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());

        System.out.println("Getting all the data from server");

        /* Init the hashmap Marker - Buyer */
        markerPublicInstitutions = new HashMap<Marker, PublicInstitution>();

        /* Read the list of public institutions */
        PublicInstitutionsManager.populatePIs(this);


        dataInited = true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.layoutInflater = inflater;
        originalView = inflater.inflate(R.layout.fragment_map, container, false);
        mWindow = inflater.inflate(R.layout.custom_info_window, null);
        mContents = inflater.inflate(R.layout.custom_info_contents, null);

        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        System.out.println("On create view");

        fragmentCopy = this;

        initData();

        return originalView;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("ON MAP READY");
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setInfoWindowAdapter(this);
        mMap.setOnInfoWindowClickListener(this);

        /* Set the style */
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this.getActivity(), R.raw.silver_style));

        /* Build an empty cluster of markers */
        if (clusterManager == null) {
            System.out.println("Cluster manager is not null");
            clusterManager = new ClusterManager<PublicInstitution>(this.getContext(), mMap);

            clusterManager.setOnClusterItemClickListener(this);
            clusterManager.setOnClusterClickListener(this);
            clusterManager.setRenderer(new PinClusterRenderer(this.getActivity(), mMap, clusterManager));
            mMap.setOnMarkerClickListener(clusterManager);
            System.out.println("MAP IS READY");
        }

        LatLng bucharest = HBPLocationListener.BUCHAREST_LOCATION;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bucharest));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bucharest, MAP_DEFAULT_ZOOM));


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_UPDATE_INTERVAL); // two minute interval
        mLocationRequest.setFastestInterval(LOCATION_UPDATE_INTERVAL_MIN);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(this.getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        getLocationPermission();
    }


    /* Clear the cluster and add only those PIs that are visible in the current projection */
    public void addVisiblePIs() {
        int index = 0;
        LinkedList<PublicInstitution> visiblePIs = PublicInstitutionsManager.getVisiblePIs(currentBounds);

        /* Clear the cluster */
        clusterManager.clearItems();

        for (PublicInstitution pi : visiblePIs)
            clusterManager.addItem(pi);

        System.out.println("Added " + visiblePIs.size() + " public institutions to the map");
        System.out.println("Bounds " + currentBounds);
    }


    @Override
    public void onMapLoaded() {
        System.out.println("MAP IS LOADED");

        System.out.println("Showing all the public institutions");

        currentBounds = mMap.getProjection().getVisibleRegion().latLngBounds;

        /* Add the visible PIs */
        addVisiblePIs();
        clusterManager.cluster();
    }

    @Override
    public void onCameraIdle() {

        System.out.println("CAMERA IS IDLE");
        if (clusterManager != null) {
            clusterManager.onCameraIdle();

            currentBounds = mMap.getProjection().getVisibleRegion().latLngBounds;

            new Thread(new Runnable() {
                public void run() {
                    System.out.println("Public institution thread running");
                    addVisiblePIs();

                    fragmentCopy.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            clusterManager.cluster();
                        }
                    });
                    System.out.println("Public institution thread done");
                }
            }).start();
        }
    }


    /* Receive Public Institution Summary from the server */
    public void receivePISummary(JSONArray response) {
        System.out.println("MapFragment: receivePISummary " + response);

        try {
            JSONObject piSummary = response.getJSONObject(0);
            clickedPI.name = piSummary.getString(CommManager.JSON_PI_NAME);
            clickedPI.directAcqs = Integer.parseInt(piSummary.getString(CommManager.JSON_PI_NO_ACQS));
            clickedPI.tenders = Integer.parseInt(piSummary.getString(CommManager.JSON_PI_NO_TENDERS));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        clickedOnItem = true;
        clickedMarker.showInfoWindow();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        System.out.println("Get info window");
        clickedMarker = marker;

        if (clickedOnItem && clickedPI.name.equals("") == false)
            render(marker, mWindow, clickedPI.name,
                    clickedPI.directAcqs, clickedPI.tenders);
        else
            return null;

        clickedOnItem = false;
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        System.out.println("Get info contents");
        if (clickedOnItem && clickedPI.name.equals("") == false)
            render(marker, mWindow, clickedPI.name,
                    clickedPI.directAcqs, clickedPI.tenders);
        else
            return null;

        return mWindow;
    }

    private void render(Marker marker, View view, String piName, int acqs, int tenders) {
        System.out.println("Rendering Info window " + piName + " " + acqs + " " + tenders);
        ((ImageView) view.findViewById(R.id.badge)).setImageResource(R.drawable.pi75);


        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (titleUi != null) {
            // Spannable string allows us to edit the formatting of the text.
            SpannableString titleText = new SpannableString(piName);
            titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleText.length(), 0);
            titleUi.setText(titleText);
        } else {
            titleUi.setText("");
        }

        TextView ad = ((TextView) view.findViewById(R.id.textAchizitiiDirecte));
        if (ad != null) {
            SpannableString text = new SpannableString("Achizitii Directe ");
            text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), 0);
            ad.setText(text);
        }

        TextView adNr = ((TextView) view.findViewById(R.id.nrAchizitiiDirecte));
        if (adNr != null) {
            SpannableString text = new SpannableString("" + acqs);
            text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), 0);
            adNr.setText(text);
        }

        TextView lic = ((TextView) view.findViewById(R.id.textLicitatii));
        if (lic != null) {
            SpannableString text = new SpannableString("Licitatii ");
            text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), 0);
            lic.setText(text);
        }

        TextView licNr = ((TextView) view.findViewById(R.id.nrLicitatii));
        if (licNr != null) {
            SpannableString text = new SpannableString("" + tenders);
            text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), 0);
            licNr.setText(text);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        System.out.println("Click on info window");
        Fragment publicInstitutionFragment = new InstitutionFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        /* Build Fragment Arguments */
        Bundle bundle = new Bundle();
        bundle.putInt(CommManager.BUNDLE_PI_ID, clickedPI.id);
        bundle.putInt(CommManager.BUNDLE_INST_TYPE, InstitutionFragment.CONTRACT_LIST_FOR_PUBLIC_INSTITUTION);
        bundle.putString(CommManager.BUNDLE_PI_NAME, clickedPI.name);
        bundle.putInt(CommManager.BUNDLE_PI_ACQS, clickedPI.directAcqs);
        bundle.putInt(CommManager.BUNDLE_PI_TENDERS, clickedPI.tenders);
        publicInstitutionFragment.setArguments(bundle);

        /* Got the Institution Fragment */
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content, publicInstitutionFragment)
                .addToBackStack(publicInstitutionFragment.getClass().getName())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    @Override
    public boolean onClusterItemClick(ClusterItem clusterItem) {
        clickedPI = (PublicInstitution)clusterItem;
        clickedOnItem = true;
        System.out.println("Click pe cluster item "  + clickedPI.id);

        /* Send request to get the PI summary */
        CommManager.requestPISummary(new CommManagerResponse() {
            @Override
            public void processResponse(JSONArray response) {
                receivePISummary(response);
            }

            @Override
            public void onErrorOccurred(String errorMsg) {
                if (fragmentCopy != null)
                    Toast.makeText(fragmentCopy.getContext(), errorMsg,
                            Toast.LENGTH_SHORT).show();
            }
        }, clickedPI.id);

        return false;
    }

    @Override
    public boolean onClusterClick(Cluster cluster) {
        ArrayList<PublicInstitution> piMarkers = (ArrayList<PublicInstitution>)cluster.getItems();
        LatLngBounds.Builder builder = LatLngBounds.builder();

        if (piMarkers.size() < 1)
            return false;

        for (PublicInstitution pi : piMarkers) {
            builder.include(pi.getPosition());
        }

        final LatLngBounds bounds = builder.build();

        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception error) {
            System.out.println(error.getMessage());
        }

        return true;
    }


    /* Check and/or ask for location permission*/
    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
    System.out.println("MapFragment: Get Location Permission");
        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            System.out.println("MapFragment: we got permissions");
            updateLocationUI();
        } else {
            ActivityCompat.requestPermissions((Activity) this.getContext(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    HBP_PERMISSION_ACCESS_COURSE_LOCATION);
        }
    }


    LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            System.out.println("MapFragment: On location result");
            for (Location location : locationResult.getLocations()) {



                System.out.println("Lat " + location.getLatitude() + " lng " + location.getLongitude());
                if (mLastLocation == null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(
                            new LatLng(location.getLatitude(),
                                    location.getLongitude())));

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                            location.getLongitude()), MAP_DETAILED_ZOOM));
                }

                mLastLocation = location;
            }
        };

    };

    /* Initialize Google Maps location specific UI */
    public void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        System.out.println("MapFragment: Update Location UI, requesting location");
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                currentLocation = null;
            }
        } catch (SecurityException e) {
            System.out.println("Exception: %s" + e.getMessage());
        }
    }
}
