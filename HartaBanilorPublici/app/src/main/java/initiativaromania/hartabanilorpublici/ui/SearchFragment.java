package initiativaromania.hartabanilorpublici.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

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
import initiativaromania.hartabanilorpublici.data.Company;
import initiativaromania.hartabanilorpublici.data.Contract;
import initiativaromania.hartabanilorpublici.data.PublicInstitution;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener, TabbedViewPageListener {

    public static final String SEARCH_NO_RESULT_MSG                 = "Niciun rezultat";

    public static final int INSTITUTIONS_FRAGMENT_INDEX             = 0;
    public static final int COMPANIES_FRAGMENT_INDEX                = 1;
    public static final int DIRECT_ACQ_FRAGMENT_INDEX               = 2;
    public static final int TENDER_FRAGMENT_INDEX                   = 3;


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

    private LinkedList<Contract> directAcqs = new LinkedList<Contract>();
    private LinkedList<Contract> tenders = new LinkedList<Contract>();
    private LinkedList<Company> companies = new LinkedList<Company>();
    private LinkedList<PublicInstitution> pis = new LinkedList<PublicInstitution>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search, container, false);
        fragmentCopy = this;
        currentQuerry = null;

        /* Build the tabbed View Pager */
        viewPageFragment = (TabbedViewPageFragment)
                getChildFragmentManager().findFragmentById(R.id.search_results_fragment);
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
        searchView.setOnCloseListener(this);

        piListFragment = null;
        directAcqListFragment = null;
        tendersListFragment = null;
        companyListFragment = null;

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
        if (newText.equals(""))
            onClose();

        return false;
    }


    @Override
    public boolean onClose() {
        System.out.println("Pressed search close");
        currentQuerry = "";
        pis.clear();
        displayPIs();

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
            case INSTITUTIONS_FRAGMENT_INDEX:
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

            case COMPANIES_FRAGMENT_INDEX:
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

            case DIRECT_ACQ_FRAGMENT_INDEX:
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

            case TENDER_FRAGMENT_INDEX:
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

        pis.clear();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject piObj = response.getJSONObject(i);
                if (piObj == null)
                    continue;

                PublicInstitution pi = new PublicInstitution();
                pi.id = Integer.parseInt(piObj.getString(CommManager.JSON_SEARCH_INSTITUTION_ID));
                pi.name = piObj.getString(CommManager.JSON_COMPANY_PI_NAME);
                pi.CUI = piObj.getString(CommManager.JSON_COMPANY_CUI);

                pis.add(pi);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("Trying to display public institutions, size " + pis.size());

        /* Show toast if no results */
        if (pis.size() == 0 && fragmentCopy.getContext() != null)
            Toast.makeText(fragmentCopy.getContext(), SEARCH_NO_RESULT_MSG,
                    Toast.LENGTH_SHORT).show();

        /* Show the info received from the server */
        displayPIs();
    }


    /* Receive PI search response from server */
    private void receiveCompanySearchResults(JSONArray response) {


    }


    /* Receive PI search response from server */
    private void receiveADSearchResults(JSONArray response) {
        System.out.println("Search Result " + response);

    }


    /* Receive PI search response from server */
    private void receiveTenderSearchResults(JSONArray response) {
        System.out.println("Search Result " + response);

    }


    /* Fill the list of public institutions */
    private void displayPIs() {

        /* Fill the companies list fragment */
        piListFragment = (InstitutionListFragment) viewPageFragment
                .pageAdapter.fragments.get(INSTITUTIONS_FRAGMENT_INDEX);
        if (piListFragment != null) {
            piListFragment.clearPIs();
            piListFragment.setPIs(pis);
            piListFragment.displayPIs();
        } else
            System.out.println("NULL pi list fragment");
    }

}
