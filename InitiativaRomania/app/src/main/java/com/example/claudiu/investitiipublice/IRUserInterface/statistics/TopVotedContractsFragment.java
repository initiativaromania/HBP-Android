package com.example.claudiu.investitiipublice.IRUserInterface.statistics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.claudiu.initiativaromania.R;
import com.example.claudiu.investitiipublice.IRObjects.CommManager;
import com.example.claudiu.investitiipublice.error.ErrorManager;

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
        View v = inflater.inflate(R.layout.statistics_voted_entities_fragment, container, false);

        CommManager.requestTop10Contracts(this);
        return v;
    }


    /* Receive and display top 10 contracts */
    public void displayTop10Contracts(JSONObject response) {

        // Update orders
        try {
            List<StatisticsContractDetails> orderDetailsList = new ArrayList<>();
            JSONArray orders = response.getJSONArray("all");
            for (int i = 0; i < orders.length(); ++i) {
                final JSONObject order = orders.getJSONObject(i);
                orderDetailsList.add(new StatisticsContractDetails() {{
                    id = order.getInt("id");
                    title = order.getString("contract_title");
                    price = order.getString("price");
                }});
            }

            /* Pretty nasty hack, view can be null */
            View view = getView();
            if (view == null)
                return;

            ListView orderList = (ListView) view.findViewById(R.id.statistics_top_entities);
            StatisticsContractRowAdapter adapter = new StatisticsContractRowAdapter(getActivity(), orderDetailsList);
            orderList.setAdapter(adapter);
            orderList.setOnItemClickListener(adapter);
        } catch (JSONException e) {
            ErrorManager.handleError(getContext(), e);
        }
    }
}
