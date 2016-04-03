package com.initiativaromania.hartabanilorpublici.IRUserInterface.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.initiativaromania.hartabanilorpublici.IRUserInterface.activities.ParticipantActivity;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.objects.EntityViewPageAdapter;
import com.initiativaromania.hartabanilorpublici.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by claudiu on 3/30/16.
 */
public class ParticipantViewPageFragment extends Fragment {

    private String tabTitlesCompany[] = new String[] { "Contracte", "Institutii"};
    private String tabTitlesBuyer[] = new String[] { "Contracte", "Companii"};
    public static EntityViewPageAdapter pageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("On create view CompanyViewPage fragment");
        return inflater.inflate(R.layout.viewpager_fragment, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        System.out.println("CREATING THE Company view page fragment");
    }


    /* Set the viewpager with the corresponding fragments */
    public void setViewPager(int fragmentType) {
        System.out.println("Setting participant view pager to " + fragmentType);
        String tabTitles[];

        /* Get the fragments */
        List<Fragment> fragments = getFragments(fragmentType);

        /* Set up the viewpager */
        if (fragmentType == ParticipantActivity.CONTRACT_LIST_FOR_BUYER)
            tabTitles = tabTitlesBuyer;
        else
            tabTitles = tabTitlesCompany;

        pageAdapter = new EntityViewPageAdapter(getFragmentManager(), fragments, tabTitles);
        ViewPager pager =
                (ViewPager)getView().findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);
    }


    /* Build the viewpager fragments */
    private List<Fragment> getFragments(int fragmentType) {
        List<Fragment> fList = new ArrayList<Fragment>();

        fList.add(ContractListFragment.newInstance());

        if (fragmentType == ParticipantActivity.CONTRACT_LIST_FOR_BUYER)
            fList.add(CompanyListFragment.newInstance());
        else
            fList.add(BuyerListFragment.newInstance());

        return fList;
    }
}
