package initiativaromania.hartabanilorpublici.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

    private static final String SEARCH_FRAGMENT_NAME                = "Căutare";
    private static final String SEARCH_NO_PI                        = "Nicio instituție publica";
    private static final String SEARCH_NO_AD_COMPANY                = "Nicio o companie cu achizitii directe";
    private static final String SEARCH_NO_TENDER_COMPANY            = "Nicio o companie cu licitatii";
    private static final String SEARCH_NO_AD                        = "Nicio achizitie directa";
    private static final String SEARCH_NO_TENDER                    = "Nicio licitatie";

    public static final int SEARCH_NAVIGATION_ID                    = 1;

    public static final int INSTITUTIONS_FRAGMENT_INDEX             = 0;
    public static final int COMPANIES_FRAGMENT_INDEX                = 1;
    public static final int DIRECT_ACQ_FRAGMENT_INDEX               = 2;
    public static final int TENDER_FRAGMENT_INDEX                   = 3;


    public String oldTitle;
    public String currentQuerry;
    public SearchView searchView;
    TabbedViewPageFragment viewPageFragment;
    private Fragment fragmentCopy;

    private Object[] searchFragments = new Object[4];
    private ContractListFragment directAcqListFragment;
    private ContractListFragment tendersListFragment;
    private CompanyListFragment companyListFragment;
    private InstitutionListFragment piListFragment;

    private LinkedList<Contract> ads = new LinkedList<Contract>();
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
        ((HomeActivity) getActivity()).setActionBarTitle(SEARCH_FRAGMENT_NAME);


        /* Get the SearchView */
        searchView = (SearchView)root.findViewById(R.id.searchText);
        if (searchView == null) {
            System.out.println("No searchView found");
            return null;
        }
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                System.out.println("Focus");
                if (hasFocus){
                    InputMethodManager imm = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                }
            }
        });

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

        /* Clear all search results */
        clearSearchPIs();

        clearSearchCompanies();

        clearSearchADs();

        clearSearchTenders();

        return false;
    }


    @Override
    public void onPageChanged(int position) {
        System.out.println("SearchFragment: position has changed to " + position);
        Object searchFragment = searchFragments[position];

        /* Nothing to do if we have no query */
        if (currentQuerry == null || currentQuerry.equals(""))
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

                /* Clear previous search results */
                clearSearchPIs();

                piListFragment = (InstitutionListFragment) viewPageFragment
                        .pageAdapter.fragments.get(INSTITUTIONS_FRAGMENT_INDEX);
                searchFragments[position] = piListFragment;
                piListFragment.displayProgressBar();

                /* Search the public institution using the server */
                CommManager.searchPublicInstitution(new CommManagerResponse() {
                    @Override
                    public void processResponse(JSONArray response) {
                        receivePISearchResults(response);
                    }

                    @Override
                    public void onErrorOccurred(String errorMsg) {
                        if (fragmentCopy.getContext() != null) {
                            piListFragment.hideProgressBar();
                            Toast.makeText(fragmentCopy.getContext(), errorMsg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, query);

                break;

            case COMPANIES_FRAGMENT_INDEX:

                /* Clear previous search results */
                clearSearchCompanies();

                companyListFragment = (CompanyListFragment) viewPageFragment
                        .pageAdapter.fragments.get(COMPANIES_FRAGMENT_INDEX);
                searchFragments[position] = companyListFragment;
                companyListFragment.displayProgressBar();

                /* Search AD Companies using the server */
                CommManager.searchADCompany(new CommManagerResponse() {
                    @Override
                    public void processResponse(JSONArray response) {
                        receiveADCompanySearchResults(response);
                    }

                    @Override
                    public void onErrorOccurred(String errorMsg) {
                        if (fragmentCopy.getContext() != null) {
                            companyListFragment.hideProgressBar();
                            Toast.makeText(fragmentCopy.getContext(), errorMsg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, query);

                /* Search Tender Companies using the server */
                CommManager.searchTenderCompany(new CommManagerResponse() {
                    @Override
                    public void processResponse(JSONArray response) {
                        receiveTenderCompanySearchResults(response);
                    }

                    @Override
                    public void onErrorOccurred(String errorMsg) {
                        if (fragmentCopy.getContext() != null) {
                            companyListFragment.hideProgressBar();
                            Toast.makeText(fragmentCopy.getContext(), errorMsg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, query);

                break;

            case DIRECT_ACQ_FRAGMENT_INDEX:
                /* Direct Acquisitions */

                /* Clear previous search results */
                clearSearchADs();

                directAcqListFragment = (ContractListFragment) viewPageFragment
                        .pageAdapter.fragments.get(DIRECT_ACQ_FRAGMENT_INDEX);
                searchFragments[position] = directAcqListFragment;
                directAcqListFragment.displayProgressBar();

                /* Search the direct acquisitions using the server */
                CommManager.searchAD(new CommManagerResponse() {
                    @Override
                    public void processResponse(JSONArray response) {
                        receiveADSearchResults(response);
                    }

                    @Override
                    public void onErrorOccurred(String errorMsg) {
                        if (fragmentCopy.getContext() != null) {
                            directAcqListFragment.hideProgressBar();
                            Toast.makeText(fragmentCopy.getContext(), errorMsg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, query);

                break;

            case TENDER_FRAGMENT_INDEX:
                /* Tenders */

                /* Clear previous search results */
                clearSearchTenders();

                tendersListFragment = (ContractListFragment) viewPageFragment
                        .pageAdapter.fragments.get(TENDER_FRAGMENT_INDEX);
                searchFragments[position] = tendersListFragment;
                tendersListFragment.displayProgressBar();

                /* Search the tenders using the server */
                CommManager.searchTender(new CommManagerResponse() {
                    @Override
                    public void processResponse(JSONArray response) {
                        receiveTenderSearchResults(response);
                    }

                    @Override
                    public void onErrorOccurred(String errorMsg) {
                        if (fragmentCopy.getContext() != null) {
                            tendersListFragment.hideProgressBar();
                            Toast.makeText(fragmentCopy.getContext(), errorMsg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, query);


                break;

            default:
                System.out.println("SearchFragment: unknown page index");
                return;
        }
    }


    /* Receive PI search response from server */
    private void receivePISearchResults(JSONArray response) {
        /* Public Institutions */
        System.out.println("PI Search Result " + response);

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
            Toast.makeText(fragmentCopy.getContext(), SEARCH_NO_PI,
                    Toast.LENGTH_SHORT).show();

        /* Show the info received from the server */
        displayPIs();
    }


    /* Generic function to receive and list company search results */
    private void receiveCompanySearchResults(JSONArray response, int companyType) {

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject cObj = response.getJSONObject(i);
                if (cObj == null)
                    continue;

                Company company = new Company();
                company.id = Integer.parseInt(cObj.getString(CommManager.JSON_COMPANY_ID));
                company.name = cObj.getString(CommManager.JSON_COMPANY_NAME);
                company.type = companyType;

                companies.add(company);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("Trying to display companies, size " + companies.size());

        /* Show toast if no results */
        if (companies.size() == 0 && fragmentCopy.getContext() != null) {
            if (companyType == Company.COMPANY_TYPE_AD)
                Toast.makeText(fragmentCopy.getContext(), SEARCH_NO_AD_COMPANY,
                        Toast.LENGTH_SHORT).show();
            if (companyType == Company.COMPANY_TYPE_TENDER)
                Toast.makeText(fragmentCopy.getContext(), SEARCH_NO_TENDER_COMPANY,
                        Toast.LENGTH_SHORT).show();
        }

        /* Show the info received from the server */
        displayCompanies();

    }


    /* Receive Tender company search response from server */
    private void receiveTenderCompanySearchResults(JSONArray response) {

        /* Tender Companies */
        System.out.println("Tender Company Search Result " + response);

        receiveCompanySearchResults(response, Company.COMPANY_TYPE_TENDER);
    }


    /* Receive Ad Company search response from server */
    private void receiveADCompanySearchResults(JSONArray response) {

        /* AD Companies */
        System.out.println("AD Company Search Result " + response);

        receiveCompanySearchResults(response, Company.COMPANY_TYPE_AD);
    }


    /* Receive AD Contract search response from server */
    private void receiveADSearchResults(JSONArray response) {
        System.out.println("AD Contract Search Result " + response);

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject conObj = response.getJSONObject(i);
                if (conObj == null)
                    continue;

                Contract con = new Contract();
                con.type     = Contract.CONTRACT_TYPE_DIRECT_ACQUISITION;
                con.id       = Integer.parseInt(conObj.getString(CommManager.JSON_ACQ_ID));
                con.title    = conObj.getString(CommManager.JSON_CONTRACT_TITLE);
                con.valueRON = Double.parseDouble(conObj.getString(CommManager.JSON_CONTRACT_VALUE_RON));

                ads.add(con);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("Trying to display ADs, size " + ads.size());

        /* Show toast if no results */
        if (ads.size() == 0 && fragmentCopy.getContext() != null)
            Toast.makeText(fragmentCopy.getContext(), SEARCH_NO_AD,
                    Toast.LENGTH_SHORT).show();

        /* Show the info received from the server */
        displayADs();
    }


    /* Receive PI search response from server */
    private void receiveTenderSearchResults(JSONArray response) {
        System.out.println("Tender Contract Search Result " + response);

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject conObj = response.getJSONObject(i);
                if (conObj == null)
                    continue;

                Contract con = new Contract();
                con.type     = Contract.CONTRACT_TYPE_TENDER;
                con.id       = Integer.parseInt(conObj.getString(CommManager.JSON_TENDER_ID));
                con.title    = conObj.getString(CommManager.JSON_CONTRACT_TITLE);
                con.valueRON = Double.parseDouble(conObj.getString(CommManager.JSON_CONTRACT_VALUE_RON));

                tenders.add(con);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("Trying to display Tenders, size " + tenders.size());

        /* Show toast if no results */
        if (tenders.size() == 0 && fragmentCopy.getContext() != null)
            Toast.makeText(fragmentCopy.getContext(), SEARCH_NO_TENDER,
                    Toast.LENGTH_SHORT).show();

        /* Show the info received from the server */
        displayTenders();
    }



    /* Clear all the public institutions from the search fragment */
    private void clearSearchPIs() {
        pis.clear();;

        if (piListFragment != null) {
            piListFragment.clearPIs();
            piListFragment.displayPIs();
        }
    }

    /* Clear all the companies from the search fragment */
    private void clearSearchCompanies() {
        companies.clear();;

        if (companyListFragment != null) {
            companyListFragment.clearCompanies();
            companyListFragment.displayCompanies();
        }
    }

    /* Clear all the ADs from the search fragment */
    private void clearSearchADs() {
        ads.clear();;

        if (directAcqListFragment != null) {
            directAcqListFragment.clearContracts();
            directAcqListFragment.displayContracts();
        }
    }

    /* Clear all the Tenders from the search fragment */
    private void clearSearchTenders() {
        tenders.clear();;

        if (tendersListFragment != null) {
            tendersListFragment.clearContracts();
            tendersListFragment.displayContracts();
        }
    }


    /* Fill the list of public institutions */
    private void displayPIs() {

        /* Fill the public institutions list fragment */

        if (piListFragment != null) {
            piListFragment.hideProgressBar();
            piListFragment.clearPIs();
            piListFragment.setPIs(pis);
            piListFragment.displayPIs();
        } else
            System.out.println("NULL pi list fragment");
    }

    /* Fill the list of Companies */
    private void displayCompanies() {

        /* Add companies in the companies list fragment */

        if (companyListFragment != null) {
            companyListFragment.hideProgressBar();
            companyListFragment.setCompanies(companies);
            companyListFragment.displayCompanies();
        } else
            System.out.println("NULL company list fragment");
    }

    /* Fill the list of ADs */
    private void displayADs() {

        /* Add ADs in the ADs list fragment */

        if (directAcqListFragment != null) {
            directAcqListFragment.hideProgressBar();
            directAcqListFragment.setContracts(ads);
            directAcqListFragment.displayContracts();
        } else
            System.out.println("NULL ads list fragment");
    }

    /* Fill the list of Tenders */
    private void displayTenders() {

        /* Add Tenders in the Tender list fragment */

        if (tendersListFragment != null) {
            tendersListFragment.hideProgressBar();
            tendersListFragment.setContracts(tenders);
            tendersListFragment.displayContracts();
        } else
            System.out.println("NULL tender list fragment");
    }

}
