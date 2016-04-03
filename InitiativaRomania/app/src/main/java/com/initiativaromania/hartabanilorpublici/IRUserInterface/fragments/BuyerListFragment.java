package com.initiativaromania.hartabanilorpublici.IRUserInterface.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.initiativaromania.hartabanilorpublici.IRUserInterface.objects.BuyerListAdapter;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.objects.CompanyListAdapter;
import com.initiativaromania.hartabanilorpublici.R;

import java.util.LinkedList;

/**
 * Created by claudiu on 3/30/16.
 */
public class BuyerListFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private View v;

    public static BuyerListFragment newInstance() {
        BuyerListFragment f = new BuyerListFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, "BuyerListFragment");
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_fragment, container, false);

        return v;
    }


    /* Display companies in a list */
    public void displayBuyers(LinkedList<String> buyers) {
        /* Pretty nasty hack, view can be null */
        View view = getView();
        if (view == null)
            return;

        ListView buyerList = (ListView) view.findViewById(R.id.list_entities);
        BuyerListAdapter adapter = new BuyerListAdapter(getActivity(), buyers);
        buyerList.setAdapter(adapter);
        buyerList.setOnItemClickListener(adapter);
    }
}
