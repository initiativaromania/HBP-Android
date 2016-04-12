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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.initiativaromania.hartabanilorpublici.IRUserInterface.objects.ContractListAdapter;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.objects.ContractListItem;
import com.initiativaromania.hartabanilorpublici.R;
import com.initiativaromania.hartabanilorpublici.IRData.CommManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TopVotedContractsFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private static final String TOP_VOTED_CONTRACTS = "Cele mai nejustificate contracte";

    public static TopVotedContractsFragment newInstance() {
        TopVotedContractsFragment f = new TopVotedContractsFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, "TopVotedContractsFragment");
        f.setArguments(bdl);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_fragment, container, false);

        CommManager.requestTop10Contracts(this);
        return v;
    }


    /* Receive and display top 10 contracts */
    public void displayTop10Contracts(JSONObject response) {

        // Update orders
        try {
            List<ContractListItem> orderDetailsList = new ArrayList<>();
            JSONArray orders = response.getJSONArray("all");
            for (int i = 0; i < orders.length(); ++i) {
                final JSONObject order = orders.getJSONObject(i);
                orderDetailsList.add(new ContractListItem() {{
                    id = order.getInt("id");
                    title = order.getString("contract_title");
                    price = order.getString("price");
                }});
            }

            /* Pretty nasty hack, view can be null */
            View view = getView();
            if (view == null)
                return;

            ListView orderList = (ListView) view.findViewById(R.id.list_entities);
            ContractListAdapter adapter = new ContractListAdapter(getActivity(), orderDetailsList);
            orderList.setAdapter(adapter);
            orderList.setOnItemClickListener(adapter);
        } catch (JSONException e) {
            Toast.makeText(getContext(), "Incercati mai tarziu..", Toast.LENGTH_SHORT).show();
        }
    }
}
