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

/**
 * Created by claudiu on 4/22/18.
 */

public class StatsCompaniesFragment extends Fragment {
    private View v;
    public int parentID;

    /* Stats objects */
    public LinkedList<Company> companyADs;
    public LinkedList<Company> companyTenders;

    public static StatsCompaniesFragment newInstance(int parentID) {
        StatsCompaniesFragment f = new StatsCompaniesFragment();
        f.parentID = parentID;

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_stats_companies, container, false);
        System.out.println("StatsCompaniesFragment on create View");

        return v;
    }

    /* Display all the available institutions in this stats fragment */
    public void displayStats() {
        if (companyADs != null)
            displayCompanies(companyADs, R.id.top_Companies_AD);

        if (companyTenders != null)
            displayCompanies(companyTenders, R.id.top_Companies_Tender);

    }

    /* Show contracts in the fragment */
    public void displayCompanies(LinkedList<Company> companies, int listId) {
        if (companies == null)
            return;
        List<CompanyListItem> orderDetailsList = new ArrayList<>();

        for (final Company company : companies) {
            orderDetailsList.add(new CompanyListItem() {{
                id = company.id;
                type = company.type;
                name = company.name;
            }});
        }

        ListView orderList = (ListView) v.findViewById(listId);
        CompanyListAdapter adapter = new CompanyListAdapter(getActivity(), parentID, orderDetailsList);
        orderList.setAdapter(adapter);
        orderList.setOnItemClickListener(adapter);
    }
}
