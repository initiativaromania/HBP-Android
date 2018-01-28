package initiativaromania.hartabanilorpublici.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/*
import com.android.volley.JsonArrayRequest;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
*/

import initiativaromania.hartabanilorpublici.R;
import initiativaromania.hartabanilorpublici.data.Contract;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener,
        TabbedViewPageListener {

    public String oldTitle;
    public String currentQuerry;
    public SearchView searchView;
    TabbedViewPageFragment viewPageFragment;

    private Object[] searchFragments = new Object[4];
    private ContractListFragment directAcqListFragment;
    private ContractListFragment tendersListFragment;
    private CompanyListFragment companyListFragment;
    private InstitutionListFragment piListFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search, container, false);

        /* Build the tabbed View Pager */
        viewPageFragment = (TabbedViewPageFragment)
                getChildFragmentManager().findFragmentById(R.id.search_fragment);
        viewPageFragment.setViewPager(InstitutionFragment.CONTRACT_LIST_FOR_SEARCH);
        viewPageFragment.registerPageListener(this);

        /* Update the main activity title */
        oldTitle = ((HomeActivity) getActivity()).getActionBarTitle();
        ((HomeActivity) getActivity()).setActionBarTitle("Cautare");


        /* Get the SearchView */
        searchView = (SearchView)root.findViewById(R.id.searchText);
        if (searchView == null) {
            System.out.println("No searchView found");
            return null;
        }
        searchView.setOnQueryTextListener(this);
        return root;
    }


    @Override
    public void onStop() {
        ((HomeActivity) getActivity()).setActionBarTitle(oldTitle);
        super.onStop();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        System.out.println("SearchView Submit " + query);

        /* Make sure we didn't just search for this. Avoid double searches */
        if (currentQuerry != null && currentQuerry.equals(query))
            return false;

        currentQuerry = query;
        searchFragments = new Object[4];
        search(viewPageFragment.pagePosition, query);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        System.out.println("SearchView Change " + newText);
        return false;
    }

    @Override
    public void onPageChanged(int position) {
        System.out.println("SearchFragment: position has changed to " + position);
        Object searchFragment = searchFragments[position];

        /* If it hadn't been searched for before, search now */
        if (searchFragment == null)
            search(position, currentQuerry);
        else
            System.out.println("We already have results for pos " + position + " and query " + currentQuerry);
    }


    /* Search for the given query*/
    public void search(int position, String query) {
        System.out.println("Search " + position + " " + query);

        switch (position) {
            case 0:
                /* Public Institutions */
                piListFragment = new InstitutionListFragment();

                searchFragments[position] = piListFragment;
                break;

            case 1:
                /* Companies */
                companyListFragment = new CompanyListFragment();

                searchFragments[position] = companyListFragment;
                break;

            case 2:
                /* Direct Acquisitions */
                directAcqListFragment = new ContractListFragment();

                searchFragments[position] = directAcqListFragment;
                break;

            case 3:
                /* Tenders */
                tendersListFragment = new ContractListFragment();

                searchFragments[position] = tendersListFragment;
                break;

            default:
                System.out.println("SearchFragment: unknown page index");
                return;
        }
    }
}
