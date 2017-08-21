package initiativaromania.hartabanilorpublici;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private MapFragment mapFragment = null;
    private SearchFragment searchFragment = null;


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
                    if (searchFragment == null)
                        searchFragment = new SearchFragment();
                    fragment = searchFragment;
                    break;

                case R.id.navigation_info:
                    if (searchFragment == null)
                        searchFragment = new SearchFragment();
                    fragment = searchFragment;
                    break;
            }

            setFragment(fragment);

            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mapFragment = new MapFragment();
        setFragment(mapFragment);
    }
}
