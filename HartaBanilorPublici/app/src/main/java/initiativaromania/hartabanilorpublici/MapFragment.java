package initiativaromania.hartabanilorpublici;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;



public class MapFragment extends android.support.v4.app.Fragment
        implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnCameraIdleListener {

    private static final int MAP_DEFAULT_ZOOM       = 10;


    /* Setup Objects */
    private ClusterManager<PublicInstitution> clusterManager = null;
    private View originalView;

    /* UI Objects */
    private GoogleMap mMap;
    private BitmapDescriptor bitmapIcon = null;

    /* Data objects */
    HashMap<Marker, PublicInstitution> markerPublicInstitutions;
    ArrayList<PublicInstitution> tmpPIs;


    public MapFragment(){};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        originalView = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        System.out.println("On create view");

        return originalView;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(this);
        mMap.setOnCameraIdleListener(this);

        // Add a marker in Bucharest and move the camera
        LatLng bucharest = new LatLng(44.435503, 26.102513);
        mMap.addMarker(new MarkerOptions().position(bucharest).title("Locatia ta"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bucharest));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bucharest, MAP_DEFAULT_ZOOM));

        /* Init data from server */
        initData();
    }


    @Override
    public void onMapLoaded() {
        System.out.println("MAP IS LOADED");

        System.out.println("Showing all the public institutions");

        /* Show all the public institutions on the map */
        buildPICluster();

        /* TODO Replace with the current location */
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.435503, 26.102513), MAP_DEFAULT_ZOOM));
    }

    @Override
    public void onCameraIdle() {
        System.out.println("Camera is idle");
        if (clusterManager != null)
            clusterManager.onCameraIdle();
    }


    /* TODO replace tmp method */
    public void getPublicInstitutions() {
        tmpPIs = new ArrayList<PublicInstitution>();

        Random randomGenerator = new Random();
        for (int i = 0; i < 13000; i++) {
            int randomlat = randomGenerator.nextInt(370);
            int randomlong = randomGenerator.nextInt(600);
            double latitude = 44 + (double)randomlat / 100;
            double longitude = 22.5 + (double)randomlong / 100;
            tmpPIs.add(new PublicInstitution("Spitalul Universitar Bucuresti",longitude, latitude));
        }
    }


    public void buildPICluster() {
        int index = 0;
        clusterManager = new ClusterManager<PublicInstitution>(this.getContext(), mMap);

        for (PublicInstitution pi : tmpPIs) {
            if (mMap.getProjection().getVisibleRegion().latLngBounds.contains(pi.position)) {
                index++;
                clusterManager.addItem(pi);
            }
        }

        LatLngBounds llb = mMap.getProjection().getVisibleRegion().latLngBounds;
        System.out.println("Added " + index + " public institutions to the map");
        System.out.println("Bounds " + llb);
    }

    /**
     * Initialize data from the server on the UI
     */
    private void initData() {
        System.out.println("Getting all the data from server");

        /* Init the hashmap Marker - Buyer */
        markerPublicInstitutions = new HashMap<Marker, PublicInstitution>();

        /* Create the list of public institutions */
        getPublicInstitutions();

    }
}
