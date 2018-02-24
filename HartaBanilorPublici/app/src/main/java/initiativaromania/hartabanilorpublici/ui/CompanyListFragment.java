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
import initiativaromania.hartabanilorpublici.data.Company;
import initiativaromania.hartabanilorpublici.data.CompanyListItem;
import initiativaromania.hartabanilorpublici.data.Contract;
import initiativaromania.hartabanilorpublici.data.ContractListItem;

/**
 * Created by claudiu on 11/15/17.
 */

public class CompanyListFragment  extends Fragment {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private View v;
    public LinkedList<Company> companies;
    private int parentID;

    public static CompanyListFragment newInstance(int parentID) {
        CompanyListFragment f = new CompanyListFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, "CompanyListFragment");
        f.setArguments(bdl);
        f.parentID = parentID;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_list, container, false);

        displayCompanies();

        return v;
    }


    /* Show companies in the fragment */
    public void displayCompanies() {
        if (companies == null || v == null)
            return;

        System.out.println("Displaying companies in fragment");
        List<CompanyListItem> orderDetailsList = new ArrayList<>();

        for (final Company company : companies) {
            orderDetailsList.add(new CompanyListItem() {{
                id = company.id;
                type = company.type;
                name = company.name;
            }});
        }

        ListView orderList = (ListView) v.findViewById(R.id.list_entities);
        CompanyListAdapter adapter = new CompanyListAdapter(getActivity(), parentID, orderDetailsList);
        orderList.setAdapter(adapter);
        orderList.setOnItemClickListener(adapter);
    }


    /* Set companies in the fragment */
    public void setCompanies(LinkedList<Company> companies) {
        this.companies = companies;
    };

    /* Clear the list of companies in the fragment */
    public void clearCompanies() {
        this.companies = new LinkedList<>();
    }
}
