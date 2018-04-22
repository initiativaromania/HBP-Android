package initiativaromania.hartabanilorpublici.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import initiativaromania.hartabanilorpublici.R;
import initiativaromania.hartabanilorpublici.data.Contract;
import initiativaromania.hartabanilorpublici.data.ContractListItem;

/**
 * Created by claudiu on 4/20/18.
 */

public class StatsContractsFragment extends Fragment{

    private View v;
    public int parentID;

    /* Stats objects */
    public LinkedList<Contract> ads;
    public LinkedList<Contract> tenders;

    public static StatsContractsFragment newInstance(int parentID) {
        StatsContractsFragment f = new StatsContractsFragment();
        f.parentID = parentID;

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_stats_contracts, container, false);
        System.out.println("StatsContractsFragment on create View");

        return v;
    }

    /* Display all the available contracts in this stats fragment */
    public void displayStats() {
        if (ads != null)
            displayContracts(ads, R.id.top_ADs_value);

        if (tenders != null)
            displayContracts(tenders, R.id.top_Tenders_value);

    }

    /* Show contracts in the fragment */
    public void displayContracts(LinkedList<Contract> contracts, int listId) {
        if (contracts == null)
            return;

        System.out.println("Displaying contracts in fragment");
        List<ContractListItem> orderDetailsList = new ArrayList<>();

        for (final Contract contract : contracts) {
            orderDetailsList.add(new ContractListItem() {{
                id = contract.id;
                type = contract.type;
                title = contract.title;
                price = contract.valueRON;
                pi = contract.pi;
                company = contract.company;
                date = contract.date != null ? contract.date : "-";
            }});
        }


        ListView orderList = (ListView) v.findViewById(listId);
        ContractListAdapter adapter = new ContractListAdapter(getActivity(), parentID,
                orderDetailsList);
        orderList.setAdapter(adapter);
        orderList.setOnItemClickListener(adapter);
    }
}
