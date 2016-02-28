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

package com.example.claudiu.investitiipublice.IRUserInterface.statistics;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.claudiu.initiativaromania.R;
import com.example.claudiu.investitiipublice.IRObjects.CommManager;
import com.example.claudiu.investitiipublice.IRObjects.Contract;
import com.example.claudiu.investitiipublice.IRUserInterface.MainActivity;
import com.example.claudiu.investitiipublice.error.ErrorManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AroundStatisticsFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private View v;

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

        if (MainActivity.currentLocation != null)
            CommManager.requestStatsAround(this, 0, 0, 0);


        /* Refresh button */
        Button button = (Button)v.findViewById(R.id.buttonActualizeaza);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /* Check whether you have voted before */
                    System.out.println("Actualizare lista");
                    displayStatsInArea();

                }
            });
        }

        return v;
    }


    /* Show statistics from your area */
    public void displayStatsInArea() {
        float distance[] = {0};
        Location l = MainActivity.currentLocation;
        LinkedList<Contract> areaContracts = new LinkedList<Contract>();

        /* Show contracts in area */
        List<StatisticsContractDetails> orderDetailsList = new ArrayList<>();

        /* Walk through all the contracts */
        for (final Contract contract : CommManager.contracts) {

            distance[0] = Float.MAX_VALUE;

            /* Determine if a contract is in our area */
            Location.distanceBetween(l.getLatitude(), l.getLongitude(), contract.latitude,
                    contract.longitude, distance);
            if (distance[0] < MainActivity.circle.getRadius()) {
                //System.out.println("contract " + contract.title + " is in the circle " + distance[0]);
                orderDetailsList.add(new StatisticsContractDetails() {{
                    id = contract.id;
                    title = contract.title;
                    price = "";
                }});

                areaContracts.add(contract);
            }
        }

        /* Pretty nasty hack */
        View view = getView();
        if (view == null) {
            System.out.println("Should have died here");
            return;
        }

        ListView orderList = (ListView) view.findViewById(R.id.statistics_around_order_list);
        StatisticsContractRowAdapter adapter = new StatisticsContractRowAdapter(getActivity(), orderDetailsList);
        orderList.setAdapter(adapter);
        orderList.setOnItemClickListener(adapter);

        if (areaContracts.size() == 0)
            Toast.makeText(getContext(), "Nu e niciun contract in jurul tau", Toast.LENGTH_SHORT).show();

    }


}
