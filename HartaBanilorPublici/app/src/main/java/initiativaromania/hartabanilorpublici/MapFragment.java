package initiativaromania.hartabanilorpublici;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.LinkedList;


public class MapFragment extends android.support.v4.app.Fragment
        implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnCameraIdleListener,
        GoogleMap.InfoWindowAdapter, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener{

    private static final int MAP_DEFAULT_ZOOM       = 10;


    /* Setup Objects */
    private ClusterManager<PublicInstitution> clusterManager = null;
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


    public MapFragment(){};


    /**
     * Initialize data from the server on the UI
     */
    private void initData() {
        System.out.println("Getting all the data from server");

        /* Init the hashmap Marker - Buyer */
        markerPublicInstitutions = new HashMap<Marker, PublicInstitution>();

        /* Build an empty cluster of markers */
        clusterManager = new ClusterManager<PublicInstitution>(this.getContext(), mMap);

        /* Create the list of public institutions */
        PublicInstitutionsManager.populatePIs();
        PublicInstitutionsManager.resetPiSet();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        originalView = inflater.inflate(R.layout.fragment_map, container, false);
        mWindow = inflater.inflate(R.layout.custom_info_window, null);
        mContents = inflater.inflate(R.layout.custom_info_contents, null);

        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        System.out.println("On create view");

        fragmentCopy = this;

        return originalView;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setInfoWindowAdapter(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);

        System.out.println("MAP IS READY");

        // Add a marker in Bucharest and move the camera
        LatLng bucharest = new LatLng(44.435503, 26.102513);
        mMap.addMarker(new MarkerOptions().position(bucharest).title("Locatia ta"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bucharest));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bucharest, MAP_DEFAULT_ZOOM));
    }


    /* Removes visible PIs from a set and displays them in the cluster */
    public void addVisiblePIs() {
        int index = 0;
        LinkedList<PublicInstitution> visiblePIs = PublicInstitutionsManager.popVisiblePIs(currentBounds);

        System.out.println("Adding visible public institution to the cluster");

        for (PublicInstitution pi : visiblePIs)
            clusterManager.addItem(pi);

        System.out.println("Added " + visiblePIs.size() + " public institutions to the map");
        System.out.println("Bounds " + currentBounds);
    }


    @Override
    public void onMapLoaded() {
        System.out.println("MAP IS LOADED");

        System.out.println("Showing all the public institutions");

        initData();

        currentBounds = mMap.getProjection().getVisibleRegion().latLngBounds;

        /* Add the visible PIs */
        addVisiblePIs();
        clusterManager.cluster();

        /* TODO Replace with the current location */
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.435503, 26.102513), MAP_DEFAULT_ZOOM));
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

    @Override
    public View getInfoWindow(Marker marker) {
        render(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        render(marker, mContents);
        return mContents;
    }

    private void render(Marker marker, View view) {
        ((ImageView) view.findViewById(R.id.badge)).setImageResource(R.drawable.parliament50);

        String title = marker.getTitle();
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (title != null) {
            // Spannable string allows us to edit the formatting of the text.
            SpannableString titleText = new SpannableString(title);
            titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleText.length(), 0);
            titleUi.setText(titleText);
        } else {
            titleUi.setText("");
        }

        String snippet = marker.getSnippet();
        TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        if (snippet != null && snippet.length() > 12) {
            SpannableString snippetText = new SpannableString(snippet);
            snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
            snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
            snippetUi.setText(snippetText);
        } else {
            snippetUi.setText("Snippet Text");
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this.getContext(), "Click Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        System.out.println("Click pe marker");
        return false;
    }
}
