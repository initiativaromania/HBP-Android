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

package com.initiativaromania.hartabanilorpublici.IRUserInterface;

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
    public static final int IR_PERMISSION_ACCESS_COURSE_LOCATION    = 19;
    private static final int LOCATION_UPDATES_LIMIT                 = 3;

    //The minimum distance to change updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    //The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000;//1000 * 60 * 1; // 1 minute

    private final static boolean forceNetwork = false;
    private static IRLocationListener instance = null;
    private LocationManager locationManager;
    private MainActivity act;
    public Location location;
    private int locationUpdates = 0;


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
     * Make sure permissions are granted and setup Location
     */
    public void initLocationService(MainActivity context) {

        if ( ContextCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            System.out.println("Insufficient permissions, asking for permission");
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    IRLocationListener.IR_PERMISSION_ACCESS_COURSE_LOCATION);
            return;
        }

        setupLocation(false);
    }

    /**
     * Resume location setup after permissions have been granted
     */
    public void setupLocation(boolean onResume) {
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
                if (onResume == false && locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    act.setInitialPosition(location);
                }
            }//end if

            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                if (onResume == false && locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    act.setInitialPosition(location);
                }
            }
        } catch (SecurityException se) {
            System.out.println("Unable to resume location setup");
        }
    }


    /* Pause the GPS Listener when the activity is paused */
    public void pauseGPSListener() {
        try {
            if (this.locationManager != null)
                this.locationManager.removeUpdates(this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        locationUpdates++;

        /* Set the camera and the UI objects on the current location
        for the first couple of location updates */
        if (locationUpdates < LOCATION_UPDATES_LIMIT) {
            act.setInitialPosition(location);
            return;
        }

        /* Update only the UI components for the rest of the location updates */
        act.updateLocationComponents(location);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public void onProviderEnabled(String provider) {}

    public void onProviderDisabled(String provider) {}
}
