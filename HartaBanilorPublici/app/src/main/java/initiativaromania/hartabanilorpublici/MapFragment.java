package initiativaromania.hartabanilorpublici;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class MapFragment extends android.support.v4.app.Fragment
        implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnCameraIdleListener,
        GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener,
        ClusterManager.OnClusterItemClickListener {

    private static final int MAP_DEFAULT_ZOOM       = 10;


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


    public MapFragment(){};


    /**
     * Initialize data from the server on the UI
     */
    private void initData() {
        if (dataInited == true)
            return;

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

        /* Build an empty cluster of markers */
        if (clusterManager == null) {
            System.out.println("Cluster manager is null");
            clusterManager = new ClusterManager<PublicInstitution>(this.getContext(), mMap);
            clusterManager.setOnClusterItemClickListener(this);
            mMap.setOnMarkerClickListener(clusterManager);
            //clusterManager.setRenderer(new PublicInstitutionRenderer());
            System.out.println("MAP IS READY");

            // TODO get the current location
            LatLng bucharest = new LatLng(44.435503, 26.102513);
            //mMap.addMarker(new MarkerOptions().position(bucharest).title("Locatia ta"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(bucharest));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bucharest, MAP_DEFAULT_ZOOM));
        }
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

    @Override
    public View getInfoWindow(Marker marker) {
        System.out.println("Get info window");
        if (clickedOnItem)
            render(marker, mWindow);
        else
            return null;

        clickedOnItem = false;
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        System.out.println("Get info contents");
        render(marker, mContents);
        return mContents;
    }

    private void render(Marker marker, View view) {
        System.out.println("Rendering Info window");
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

        TextView ad = ((TextView) view.findViewById(R.id.textAchizitiiDirecte));
        if (ad != null) {
            SpannableString text = new SpannableString("Achizitii Directe");
            text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), 0);
            ad.setText(text);
        }

        TextView adNr = ((TextView) view.findViewById(R.id.nrAchizitiiDirecte));
        if (adNr != null) {
            SpannableString text = new SpannableString("340");
            text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), 0);
            adNr.setText(text);
        }

        TextView lic = ((TextView) view.findViewById(R.id.textLicitatii));
        if (lic != null) {
            SpannableString text = new SpannableString("Licitatii");
            text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), 0);
            lic.setText(text);
        }

        TextView licNr = ((TextView) view.findViewById(R.id.nrLicitatii));
        if (licNr != null) {
            SpannableString text = new SpannableString("559");
            text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), 0);
            licNr.setText(text);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        System.out.println("Click on info window");
        Fragment publicInstitutionFragment = new PublicInstitutionFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content, publicInstitutionFragment).addToBackStack("TAG")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    @Override
    public boolean onClusterItemClick(ClusterItem clusterItem) {
        System.out.println("Click pe cluster item");
        clickedOnItem = true;
        return false;
    }

//    private class PublicInstitutionRenderer extends DefaultClusterRenderer<PublicInstitution> {
//        private final IconGenerator mIconGenerator = new IconGenerator(getContext());
//        private final IconGenerator mClusterIconGenerator = new IconGenerator(getContext());
//        private final ImageView mImageView;
//        private final ImageView mClusterImageView;
//        private final int mDimension;
//
//        public PublicInstitutionRenderer() {
//            super(getContext(), mMap, clusterManager);
//
//            View multiProfile = layoutInflater.inflate(R.layout.multi_profile, null);
//            mClusterIconGenerator.setContentView(multiProfile);
//            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);
//
//            mImageView = new ImageView(getContext());
//            mDimension = (int) 50;
//            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
//            int padding = (int) 2;
//            mImageView.setPadding(padding, padding, padding, padding);
//            mIconGenerator.setContentView(mImageView);
//        }
//
//        @Override
//        protected void onBeforeClusterItemRendered(PublicInstitution pi, MarkerOptions markerOptions) {
//            // Draw a single person.
//            // Set the info window to show their name.
//            mImageView.setImageResource(R.drawable.parliament50);
//            Bitmap icon = mIconGenerator.makeIcon();
//            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(pi.name);
//        }
//
//        @Override
//        protected void onBeforeClusterRendered(Cluster<PublicInstitution> cluster, MarkerOptions markerOptions) {
//            mClusterImageView.setImageResource(R.drawable.parliament50);
//            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
//            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
//        }
//
//        @Override
//        protected boolean shouldRenderAsCluster(Cluster cluster) {
//            // Always render clusters.
//            return cluster.getSize() > 1;
//        }
//    }
}
