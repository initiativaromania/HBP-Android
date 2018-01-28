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
import android.widget.Toast;

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
import initiativaromania.hartabanilorpublici.comm.CommManager;
import initiativaromania.hartabanilorpublici.comm.CommManagerResponse;
import initiativaromania.hartabanilorpublici.data.Contract;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener,
        TabbedViewPageListener {

    public String oldTitle;
    public String currentQuerry;
    public int currentPosition;
    public SearchView searchView;
    TabbedViewPageFragment viewPageFragment;
    private Fragment fragmentCopy;

    private Object[] searchFragments = new Object[4];
    private ContractListFragment directAcqListFragment;
    private ContractListFragment tendersListFragment;
    private CompanyListFragment companyListFragment;
    private InstitutionListFragment piListFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search, container, false);
        fragmentCopy = this;

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
        currentPosition = position;

        /* Nothing to do if we have no query */
        if (currentQuerry == null)
            return;

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
                /* Search the public institution using the server */
                CommManager.searchPublicInstitution(new CommManagerResponse() {
                    @Override
                    public void processResponse(JSONArray response) {
                        receivePISearchResults(response);
                    }

                    @Override
                    public void onErrorOccurred(String errorMsg) {
                        if (fragmentCopy.getContext() != null) {
                            Toast.makeText(fragmentCopy.getContext(), errorMsg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, query);

                piListFragment = new InstitutionListFragment();
                searchFragments[position] = piListFragment;

                break;

            case 1:
                /* Search the Company using the server */
                CommManager.searchCompany(new CommManagerResponse() {
                    @Override
                    public void processResponse(JSONArray response) {
                        receiveCompanySearchResults(response);
                    }

                    @Override
                    public void onErrorOccurred(String errorMsg) {
                        if (fragmentCopy.getContext() != null) {
                            Toast.makeText(fragmentCopy.getContext(), errorMsg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, query);

                companyListFragment = new CompanyListFragment();
                searchFragments[position] = companyListFragment;
                break;

            case 2:
                /* Direct Acquisitions */
                /* Search the direct acquisitions using the server */
                CommManager.searchAD(new CommManagerResponse() {
                    @Override
                    public void processResponse(JSONArray response) {
                        receiveADSearchResults(response);
                    }

                    @Override
                    public void onErrorOccurred(String errorMsg) {
                        if (fragmentCopy.getContext() != null)
                            Toast.makeText(fragmentCopy.getContext(), errorMsg,
                                    Toast.LENGTH_SHORT).show();
                    }
                }, query);

                directAcqListFragment = new ContractListFragment();
                searchFragments[position] = directAcqListFragment;
                break;

            case 3:
                /* Tenders */
                /* Search the tenders using the server */
                CommManager.searchTender(new CommManagerResponse() {
                    @Override
                    public void processResponse(JSONArray response) {
                        receiveTenderSearchResults(response);
                    }

                    @Override
                    public void onErrorOccurred(String errorMsg) {
                        if (fragmentCopy.getContext() != null)
                            Toast.makeText(fragmentCopy.getContext(), errorMsg,
                                    Toast.LENGTH_SHORT).show();
                    }
                }, query);

                tendersListFragment = new ContractListFragment();
                searchFragments[position] = tendersListFragment;
                break;

            default:
                System.out.println("SearchFragment: unknown page index");
                return;
        }
    }


    /* Receive PI search response from server */
    private void receivePISearchResults(JSONArray response) {
        /* Public Institutions */
        System.out.println("Search Result " + response);

    }


    /* Receive PI search response from server */
    private void receiveCompanySearchResults(JSONArray response) {
        /* Public Institutions */
        System.out.println("Search Result " + response);

    }


    /* Receive PI search response from server */
    private void receiveADSearchResults(JSONArray response) {
        /* Public Institutions */
        System.out.println("Search Result " + response);

    }


    /* Receive PI search response from server */
    private void receiveTenderSearchResults(JSONArray response) {
        /* Public Institutions */
        System.out.println("Search Result " + response);

    }
}
