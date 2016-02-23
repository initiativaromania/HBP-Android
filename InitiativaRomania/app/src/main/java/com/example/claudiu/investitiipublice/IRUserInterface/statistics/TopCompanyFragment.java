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

/**
 * Created by claudiu on 2/21/16.
 */
public class TopCompanyFragment extends Fragment {

    private static final String TOP_COMPANIES = "Companiile cu cele mai valoroase contracte:";
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static TopCompanyFragment newInstance() {
        TopCompanyFragment f = new TopCompanyFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, "TopCompanyFragment");
        f.setArguments(bdl);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.statistics_voted_entities_fragment, container, false);

        CommManager.requestTop10Companies(this);
        return v;
    }


    /* Receive and display top 10 contracts */
    public void displayTop10Companies(JSONObject response) {

        // Update orders
        try {
            List<String> companiesList = new ArrayList<String>();
            JSONArray companiesJSON = response.getJSONArray("all");
            for (int i = 0; i < companiesJSON.length(); ++i) {
                final JSONObject companyJSON = companiesJSON.getJSONObject(i);

                companiesList.add(companyJSON.getString("company"));
            }

            ListView companyList = (ListView) getView().findViewById(R.id.statistics_top_entities);
            StatisticsCompanyAdapter adapter = new StatisticsCompanyAdapter(getActivity(), companiesList);
            companyList.setAdapter(adapter);
            companyList.setOnItemClickListener(adapter);
        } catch (JSONException e) {
            ErrorManager.handleError(getContext(), e);
        }
    }
}