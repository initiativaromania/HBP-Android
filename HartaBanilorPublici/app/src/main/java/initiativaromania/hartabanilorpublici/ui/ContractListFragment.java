package initiativaromania.hartabanilorpublici.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * Created by claudiu on 9/12/17.
 */

public class ContractListFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private View v;
    public LinkedList<Contract> contracts;
    public int parentID;

    public static ContractListFragment newInstance(int parentID) {
        ContractListFragment f = new ContractListFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, "ContractListFragment");
        f.setArguments(bdl);
        f.parentID = parentID;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_list, container, false);

        displayContracts();

        return v;
    }


    /* Show contracts in the fragment */
    public void displayContracts() {
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
            }});
        }


        ListView orderList = (ListView) v.findViewById(R.id.list_entities);
        ContractListAdapter adapter = new ContractListAdapter(getActivity(), parentID,
                orderDetailsList);
        orderList.setAdapter(adapter);
        orderList.setOnItemClickListener(adapter);
    }


    /* Set contracts in the fragment */
    public void setContracts(LinkedList<Contract> contracts) {
        this.contracts = contracts;
    }

    /* Clear the contracts in the fragment */
    public void clearContracts() {
        this.contracts = new LinkedList<>();
    }
}
