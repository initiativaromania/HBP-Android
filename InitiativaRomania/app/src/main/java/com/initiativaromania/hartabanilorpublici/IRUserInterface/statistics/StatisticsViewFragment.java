/**
 This file is part of "Harta Banilor Publici".

 "Harta Banilor Publici" is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 "Harta Banilor Publici" is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.initiativaromania.hartabanilorpublici.IRUserInterface.statistics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.initiativaromania.hartabanilorpublici.IRUserInterface.statistics.StatisticsPageAdapter;
import com.initiativaromania.hartabanilorpublici.R;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.statistics.AroundStatisticsFragment;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.statistics.TopCompanyFragment;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.statistics.TopVotedContractsFragment;

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
        fList.add(TopCompanyFragment.newInstance());
        return fList;
    }
}