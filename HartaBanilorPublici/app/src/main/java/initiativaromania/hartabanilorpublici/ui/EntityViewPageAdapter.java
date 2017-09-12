package initiativaromania.hartabanilorpublici.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by claudiu on 9/12/17.
 */

public class EntityViewPageAdapter extends FragmentPagerAdapter {
    public List<Fragment> fragments;
    private String tabTitles[];

    public EntityViewPageAdapter(FragmentManager fm, List<Fragment> fragments, String tabTitles[]) {
        super(fm);
        this.fragments = fragments;
        this.tabTitles = tabTitles;
    }
    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
