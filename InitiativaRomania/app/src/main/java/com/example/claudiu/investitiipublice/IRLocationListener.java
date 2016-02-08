package com.example.claudiu.investitiipublice;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by claudiu on 2/8/16.
 */
public class IRLocationListener implements LocationListener {
    public static final int IR_PERMISSION_ACCESS_COURSE_LOCATION = 19;

    //The minimum distance to change updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    //The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000;//1000 * 60 * 1; // 1 minute

    private final static boolean forceNetwork = false;
    private static IRLocationListener instance = null;
    private LocationManager locationManager;
    private MainActivity act;
    public Location location;
    public double longitude;
    public double latitude;


    /**
     * Singleton implementation
     * @return
     */
    public static IRLocationListener getLocationManager(MainActivity context) {
        if (instance == null) {
            instance = new IRLocationListener(context);
        }
        return instance;
    }

    /**
     * Local constructor
     */
    private IRLocationListener(MainActivity context) {
        this.act = context;

        initLocationService(context);
        System.out.println("LocationService inited");
    }

    /**
     * Make sure permissions are granted
     */
    private void initLocationService(MainActivity context) {

        if ( ContextCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            System.out.println("Insufficient permissions, asking for permission");
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    IRLocationListener.IR_PERMISSION_ACCESS_COURSE_LOCATION);
            return;
        }

        setupLocation();
    }

    /**
     * Resume location setup after permissions have been granted
     */
    public void setupLocation() {
        System.out.println("Setup location resume");
        this.locationManager = (LocationManager) act.getSystemService(Context.LOCATION_SERVICE);

        // Get GPS and network status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (forceNetwork) isGPSEnabled = false;

        if (!isNetworkEnabled && !isGPSEnabled) {
            System.out.println("Cannot get loc");
            return;
        }

        System.out.println("Can get loc");

        try {
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    act.setInitialPosition(location);
                }
            }//end if

            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    act.setInitialPosition(location);
                }
            }
        } catch (SecurityException se) {
            System.out.println("Unable to resume location setup");
        }
    }




    @Override
    public void onLocationChanged(Location location)     {
        // do stuff here with location object
        act.updateLocationComponents(location);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public void onProviderEnabled(String provider) {}

    public void onProviderDisabled(String provider) {}
}
