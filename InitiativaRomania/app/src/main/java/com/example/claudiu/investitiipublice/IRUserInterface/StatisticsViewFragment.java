package com.example.claudiu.investitiipublice.IRUserInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import com.example.claudiu.initiativaromania.R;
import com.example.claudiu.investitiipublice.IRUserInterface.statistics.AroundStatisticsFragment;
import com.example.claudiu.investitiipublice.IRUserInterface.statistics.TopVotedContractsFragment;

import java.util.ArrayList;
import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by claudiu on 2/9/16.
 */
public class StatisticsViewFragment extends Fragment {

    private StatisticsPageAdapter pageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("On create view statistics");
        return inflater.inflate(R.layout.statistics_tab_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        System.out.println("CREATING THE STATISTICS VIEW ACTIVITY");

        List<Fragment> fragments = getFragments();
        pageAdapter = new StatisticsPageAdapter(getFragmentManager(), fragments);
        ViewPager pager =
                (ViewPager)getView().findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(AroundStatisticsFragment.newInstance());
        fList.add(TopVotedContractsFragment.newInstance());
        fList.add(StatisticsFragment.newInstance("Fragment 3"));
        return fList;
    }
}