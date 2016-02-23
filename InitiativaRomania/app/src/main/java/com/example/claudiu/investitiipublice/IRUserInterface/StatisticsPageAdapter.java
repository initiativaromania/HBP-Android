package com.example.claudiu.investitiipublice.IRUserInterface;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by claudiu on 2/9/16.
 */
public class StatisticsPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private String tabtitles[] = new String[] { "In jurul tau", "Top contracte nejustificate", "Top companii" };

    public StatisticsPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
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
        return tabtitles[position];
    }
}
