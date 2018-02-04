package initiativaromania.hartabanilorpublici.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;;
import android.view.MenuItem;
import android.widget.TextView;

import initiativaromania.hartabanilorpublici.R;
import initiativaromania.hartabanilorpublici.comm.CommManager;

public class HomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private MapFragment mapFragment = null;
    private SearchFragment searchFragment = null;
    private StatsFragment statsFragment = null;
    private InfoFragment infoFragment = null;


    /* Set the current content of the bottom navigation view */
    private void setFragment(Fragment fragment) {
        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_map:
                    if (mapFragment == null)
                        mapFragment = new MapFragment();
                    fragment = mapFragment;
                    break;

                case R.id.navigation_search:
                    if (searchFragment == null)
                        searchFragment = new SearchFragment();
                    fragment = searchFragment;
                    System.out.println("Search");
                    break;

                case R.id.navigation_top:
                    if (statsFragment == null)
                        statsFragment = new StatsFragment();
                    fragment = statsFragment;
                    break;

                case R.id.navigation_info:
                    if (infoFragment == null)
                        infoFragment = new InfoFragment();
                    fragment = infoFragment;
                    break;
            }

            setFragment(fragment);

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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /* Initialize communication with the server */
        CommManager.init(this);

        mapFragment = new MapFragment();
        setFragment(mapFragment);
    }
}
