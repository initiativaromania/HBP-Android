package initiativaromania.hartabanilorpublici20;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeActivity extends FragmentActivity implements OnMapReadyCallback {

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

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        System.out.println("On create homescreen");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap = googleMap;

        // Add a marker in Bucharest and move the camera
        LatLng bucharest = new LatLng(44.435503, 26.102513);
        mMap.addMarker(new MarkerOptions().position(bucharest));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bucharest));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bucharest, MAP_DEFAULT_ZOOM));
    }
}
