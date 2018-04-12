package initiativaromania.hartabanilorpublici.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import initiativaromania.hartabanilorpublici.R;
import initiativaromania.hartabanilorpublici.comm.CommManager;

public class HomeActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private FragmentManager fragmentManager;

    private MapFragment mapFragment = null;
    private SearchFragment searchFragment = null;
    private StatsFragment statsFragment = null;
    private InfoFragment infoFragment = null;
    private Fragment currentFragment = null;
    BottomNavigationView navigation;
    ArrayList<Fragment> tabbedFragments;


    /* Set the current content of the bottom navigation view */
    private void setFragment(Fragment fragment) {
        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content, fragment, fragmentTag);
            ft.addToBackStack(backStateName);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_map:
                    if (mapFragment == null) {
                        mapFragment = new MapFragment();
                        tabbedFragments.add(MapFragment.MAP_NAVIGATION_ID, mapFragment);
                    }
                    currentFragment = mapFragment;
                    break;

                case R.id.navigation_search:
                    if (searchFragment == null) {
                        searchFragment = new SearchFragment();
                        tabbedFragments.add(SearchFragment.SEARCH_NAVIGATION_ID, searchFragment);
                    }
                    currentFragment = searchFragment;
                    System.out.println("Search");
                    break;

                case R.id.navigation_top:
                    if (statsFragment == null) {
                        statsFragment = new StatsFragment();
                        tabbedFragments.add(StatsFragment.STATS_NAVIGATION_ID, statsFragment);
                    }
                    currentFragment = statsFragment;
                    break;

                case R.id.navigation_info:
                    if (infoFragment == null) {
                        infoFragment = new InfoFragment();
                        tabbedFragments.add(InfoFragment.INFO_NAVIGATION_ID, infoFragment);
                    }
                    currentFragment = infoFragment;
                    break;
            }

            System.out.println("Current is now " + currentFragment.getClass().getName());
            setFragment(currentFragment);

            return true;
        }

    };


    public void setActionBarTitle(String title) {
        TextView titleText = ((TextView) findViewById(R.id.activityTest));
        if (titleText == null)
            return;

        titleText.setText(title);
    }

    public String getActionBarTitle() {
        TextView titleText = ((TextView) findViewById(R.id.activityTest));
        if (titleText == null)
            return "";

        return titleText.getText().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /* Initialize communication with the server */
        CommManager.init(this);

        tabbedFragments = new ArrayList<Fragment>(4);
        mapFragment = new MapFragment();
        tabbedFragments.add(MapFragment.MAP_NAVIGATION_ID, mapFragment);
        tabbedFragments.add(SearchFragment.SEARCH_NAVIGATION_ID, null);
        tabbedFragments.add(StatsFragment.STATS_NAVIGATION_ID, null);
        tabbedFragments.add(InfoFragment.INFO_NAVIGATION_ID, null);

        setFragment(mapFragment);
    }

    /**
     * Permission handler for GPS location
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MapFragment.HBP_PERMISSION_ACCESS_COURSE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("GPS permissions granted");
                    mapFragment.mLocationPermissionGranted = true;
                } else {
                    System.out.println("GPS permissions denied");
                    mapFragment.mLocationPermissionGranted = false;
                }
            }
        }

        mapFragment.updateLocationUI();
    }

    private Fragment getCurrentFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();

        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager
                .getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);

        return currentFragment;
    }

    private Fragment getPreviousFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager
                .getBackStackEntryCount() - 2).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);

        return currentFragment;
    }

    @Override
    /* Some hack to make the bottom navigation bar tabs enabled on back press.
     * They don't do it by default
     */
    public void onBackPressed() {
        Fragment prevFragment;
        int i;

        if (getCurrentFragment() == mapFragment)
            System.exit(0);

        prevFragment = getPreviousFragment();

        for (i = 0; i < tabbedFragments.size(); i++) {
            if (tabbedFragments.get(i) == prevFragment)
                break;
        }

        navigation.getMenu().getItem(i).setCheckable(true).setChecked(true);

        super.onBackPressed();
    }
}
