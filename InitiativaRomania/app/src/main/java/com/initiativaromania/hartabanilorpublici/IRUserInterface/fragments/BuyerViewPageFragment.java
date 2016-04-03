package com.initiativaromania.hartabanilorpublici.IRUserInterface.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.initiativaromania.hartabanilorpublici.IRUserInterface.objects.EntityViewPageAdapter;
import com.initiativaromania.hartabanilorpublici.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by claudiu on 4/3/16.
 */
public class BuyerViewPageFragment extends Fragment {

    private String tabtitles[] = new String[] { "Contracte", "Companii"};
    public static EntityViewPageAdapter pageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("On create view statistics");
        return inflater.inflate(R.layout.viewpager_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        System.out.println("CREATING THE Buyer view page fragment");

        List<Fragment> fragments = getFragments();
        pageAdapter = new EntityViewPageAdapter(getFragmentManager(), fragments, tabtitles);
        ViewPager pager =
                (ViewPager)getView().findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);

    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(ContractListFragment.newInstance());
        fList.add(CompanyListFragment.newInstance());
        return fList;
    }
}
