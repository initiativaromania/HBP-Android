package com.initiativaromania.hartabanilorpublici.IRUserInterface.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.initiativaromania.hartabanilorpublici.IRData.CommManager;
import com.initiativaromania.hartabanilorpublici.IRData.Contract;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.activities.MainActivity;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.objects.ContractListAdapter;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.objects.ContractListItem;
import com.initiativaromania.hartabanilorpublici.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by claudiu on 3/30/16.
 */
public class ContractListFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private View v;

    public static ContractListFragment newInstance() {
        ContractListFragment f = new ContractListFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, "ContractListFragment");
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.contract_list_fragment, container, false);

        return v;
    }


    /* Show contracts in the fragment */
    public void displayContracts(LinkedList<Contract> contracts) {
        System.out.println("Displaying contracts in fragment");
        List<ContractListItem> orderDetailsList = new ArrayList<>();

        for (final Contract contract : contracts) {
            orderDetailsList.add(new ContractListItem() {{
                id = contract.id;
                title = contract.title;
                price = "";
            }});
        }

         /* Pretty nasty hack */
        View view = getView();
        if (view == null) {
            System.out.println("Should have died here");
            return;
        }

        ListView orderList = (ListView) view.findViewById(R.id.list_entities);
        ContractListAdapter adapter = new ContractListAdapter(getActivity(), orderDetailsList);
        orderList.setAdapter(adapter);
        orderList.setOnItemClickListener(adapter);

    };
}
