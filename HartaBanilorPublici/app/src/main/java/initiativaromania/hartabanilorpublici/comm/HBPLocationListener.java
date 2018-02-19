package initiativaromania.hartabanilorpublici.comm;

/**
 * Created by claudiu on 2/4/18.
 */

import android.app.Activity;
import android.content.Context;
import android.Manifest;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


import com.google.android.gms.maps.model.LatLng;

import java.util.Map;

import initiativaromania.hartabanilorpublici.ui.MapFragment;

public class HBPLocationListener implements LocationListener {

    public static final int HBP_PERMISSION_ACCESS_COURSE_LOCATION      = 19;
    public static final LatLng BUCHAREST_LOCATION                      = new LatLng(44.435503, 26.102513);

    private static HBPLocationListener instance = null;
    private MapFragment mapFragment;

    /**
     * Singleton implementation
     * @return
     */
    public static HBPLocationListener getLocationManager(MapFragment mapFragment) {
        if (instance == null) {
            instance = new HBPLocationListener(mapFragment);
        }
        return instance;
    }

    /**
     * Private constructor
     */
    private HBPLocationListener(MapFragment mapFragment) {
        this.mapFragment = mapFragment;

        getInitialLocation();

        System.out.println("LocationListener created and initialized");
    }


    /**
     * Make sure permissions are granted and setup Location
     */
    private void getInitialLocation() {

        System.out.println("Check GPS self permission");
        if (ContextCompat.checkSelfPermission( mapFragment.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            System.out.println("Insufficient permissions, asking for permission");
            ActivityCompat.requestPermissions((Activity)mapFragment.getContext(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    HBP_PERMISSION_ACCESS_COURSE_LOCATION);
            return;
        }

        System.out.println("HBP has GPS permission");
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
