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

package com.initiativaromania.hartabanilorpublici.IRUserInterface.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.initiativaromania.hartabanilorpublici.IRData.ICommManagerResponse;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.objects.ContractListAdapter;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.objects.ContractListItem;
import com.initiativaromania.hartabanilorpublici.R;
import com.initiativaromania.hartabanilorpublici.IRData.CommManager;
import com.initiativaromania.hartabanilorpublici.IRData.Contract;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.activities.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AroundStatisticsFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public View v;
    private ListView orderList;
    private List<ContractListItem> orderDetailsList;
    public static int currentBuyerToProcess;
    private ContractListAdapter statisticsAroundAdapter;


    public static AroundStatisticsFragment newInstance() {
        AroundStatisticsFragment f = new AroundStatisticsFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, "AroundStatisticsFragment");
        f.setArguments(bdl);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.statistics_around_fragment, container, false);

        orderDetailsList = new ArrayList<>();
        orderList = (ListView) v.findViewById(R.id.statistics_around_order_list);
        statisticsAroundAdapter = new ContractListAdapter(getActivity(), orderDetailsList);
        orderList.setAdapter(statisticsAroundAdapter);
        orderList.setOnItemClickListener(statisticsAroundAdapter);
        orderList.setOnScrollListener(new EndlessScrollListener(this));

        currentBuyerToProcess = 0;
        getMoreAroundStatistics();

        return v;
    }


    /* Show statistics from your area */
    public void displayStatsInArea() {
        float distance[] = {0};

        /* Ask again for location */
        Location l = MainActivity.currentLocation;
        if (l == null) {
            MainActivity.locationListener.initLocationService((MainActivity) MainActivity.context);
            return;
        }

        LinkedList<Contract> areaContracts = new LinkedList<Contract>();

        /* Show contracts in area */


        /* Walk through all the contracts */
        for (final Contract contract : CommManager.localContracts) {

            distance[0] = Float.MAX_VALUE;

            /* Determine if a contract is in our area */
            Location.distanceBetween(l.getLatitude(), l.getLongitude(), contract.latitude,
                    contract.longitude, distance);
            if (distance[0] < MainActivity.circle.getRadius()) {
                //System.out.println("contract " + contract.title + " is in the circle " + distance[0]);
                orderDetailsList.add(new ContractListItem() {{
                    id = contract.id;
                    title = contract.title;
                    price = "";
                }});

                areaContracts.add(contract);
            }
        }

        if (areaContracts.size() == 0)
            Toast.makeText(getContext(), "Nu e niciun contract in jurul tau", Toast.LENGTH_SHORT).show();
    }

    public void getMoreAroundStatistics() {

        if (CommManager.buyers.size() > currentBuyerToProcess)
        {
            CommManager.requestBuyerDetails(new ICommManagerResponse() {
                @Override
                public void processResponse(JSONObject response) {
                    try {
                        JSONArray contractsJSON = response.getJSONArray("orders");

                        for (int i = 0; i < contractsJSON.length(); i++) {
                            final JSONObject contractJSON = contractsJSON.getJSONObject(i);
                            AroundStatisticsFragment.this.orderDetailsList.add(new ContractListItem() {{
                                id = Integer.parseInt(contractJSON.getString("id"));
                                title = contractJSON.getString("contract_title");
                                price = "";
                            }});
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AroundStatisticsFragment.this.statisticsAroundAdapter.notifyDataSetChanged();
                }
                @Override
                public void onErrorOccurred(String errorMsg) {
                    Toast.makeText(AroundStatisticsFragment.this.getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            }, CommManager.buyers.get(currentBuyerToProcess++).name);
        }
    }
}


class EndlessScrollListener implements AbsListView.OnScrollListener {

    final int visibleThreshold = 5;
    private int previousTotal = 0;
    private boolean loading = true;

    AroundStatisticsFragment viewFragment;

    public EndlessScrollListener(AroundStatisticsFragment view) {
        viewFragment = view;
    }

    @Override
    public void onScroll(final AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                viewFragment.v.findViewById(R.id.statistics_around_loading_progress).setVisibility(View.GONE);
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            viewFragment.v.findViewById(R.id.statistics_around_loading_progress).setVisibility(View.VISIBLE);
            viewFragment.getMoreAroundStatistics();
            loading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}
