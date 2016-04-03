package com.initiativaromania.hartabanilorpublici.IRUserInterface.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.initiativaromania.hartabanilorpublici.IRData.CommManager;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.objects.CompanyListAdapter;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.objects.ContractListAdapter;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.objects.ContractListItem;
import com.initiativaromania.hartabanilorpublici.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by claudiu on 3/30/16.
 */
public class CompanyListFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private View v;


    public static CompanyListFragment newInstance() {
        CompanyListFragment f = new CompanyListFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, "CompanyListFragment");
        f.setArguments(bdl);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_fragment, container, false);

        return v;
    }


    /* Display companies in a list */
    public void displayCompanies(LinkedList<String> companies) {
        /* Pretty nasty hack, view can be null */
        View view = getView();
        if (view == null)
            return;

        ListView companyList = (ListView) view.findViewById(R.id.list_entities);
        CompanyListAdapter adapter = new CompanyListAdapter(getActivity(), companies);
        companyList.setAdapter(adapter);
        companyList.setOnItemClickListener(adapter);
    }
}
