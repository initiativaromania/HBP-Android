package com.example.claudiu.investitiipublice.IRUserInterface.statistics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.claudiu.initiativaromania.R;
import com.example.claudiu.investitiipublice.IRObjects.CommManager;
import com.example.claudiu.investitiipublice.error.ErrorManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AroundStatisticsFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static AroundStatisticsFragment newInstance() {
        AroundStatisticsFragment f = new AroundStatisticsFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, "AroundStatisticsFragment");
        f.setArguments(bdl);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.statistics_around_fragment, container, false);

        CommManager.setStatisticsData(this, 0, 0, 0);
        return v;
    }

    public void dataUpdated(JSONObject response) {

        // Update orders
        try {
            List<StatisticsOrderDetails> orderDetailsList = new ArrayList<>();
            JSONArray orders = response.getJSONArray("orders");
            for (int i = 0; i < orders.length(); ++i) {
                final JSONObject order = orders.getJSONObject(i);
                orderDetailsList.add(new StatisticsOrderDetails() {{
                    id = order.getInt("id");
                    title = order.getString("contract_title");
                    price = order.getString("price");
                }});
            }

            ListView orderList = (ListView) getView().findViewById(R.id.statistics_around_order_list);
            StatisticsContractRowAdapter adapter = new StatisticsContractRowAdapter(getActivity(), orderDetailsList);
            orderList.setAdapter(adapter);
            orderList.setOnItemClickListener(adapter);
        } catch (JSONException e) {
            ErrorManager.handleError(getContext(), e);
        }

        // Update tags
        try {
            List<String> categories  = new ArrayList<>();
            JSONArray orders = response.getJSONArray("categories");
            for (int i = 0; i < orders.length(); ++i) {
                categories.add(orders.getString(i));
            }

            ListView categoryList = (ListView) getView().findViewById(R.id.statistics_around_categories_list);
            StatisticsCategoryRowAdapter adapter = new StatisticsCategoryRowAdapter(getActivity(), categories);
            categoryList.setAdapter(adapter);
            categoryList.setOnItemClickListener(adapter);
        } catch (JSONException e) {
            ErrorManager.handleError(getContext(), e);
        }

    }

    protected class StatisticsOrderDetails {
        int id;
        String title;
        String price;
    }
}
