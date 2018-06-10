package initiativaromania.hartabanilorpublici.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import initiativaromania.hartabanilorpublici.R;
import initiativaromania.hartabanilorpublici.comm.CommManager;
import initiativaromania.hartabanilorpublici.comm.CommManagerResponse;
import initiativaromania.hartabanilorpublici.data.Company;
import initiativaromania.hartabanilorpublici.data.Contract;
import initiativaromania.hartabanilorpublici.data.PublicInstitution;

/**
 * Created by claudiu on 2/4/18.
 */

public class StatsFragment extends Fragment implements TabbedViewPageListener{

    private static final String STATS_FRAGMENT_NAME         = "Statistici";
    public static final int STATS_NAVIGATION_ID             = 2;

    private static final int STATS_CONTRACTS_INDEX          = 0;
    private static final int STATS_INSTITUTIONS_INDEX       = 1;
    private static final int STATS_COMPANIES_INDEX          = 2;

    private Fragment fragmentCopy;
    private String oldTitle;

    private StatsContractsFragment statsContractsFragment;
    private StatsInstitutionsFragment statsInstitutionsFragment;
    private StatsCompaniesFragment statsCompaniesFragment;

    TabbedViewPageFragment viewPageFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_stats, container, false);
        fragmentCopy = this;

        /* Build the tabbed View Pager */
        viewPageFragment = (TabbedViewPageFragment)
                getChildFragmentManager().findFragmentById(R.id.stats_tabbed_fragment);
        viewPageFragment.setViewPager(InstitutionFragment.CONTRACT_LIST_FOR_STATS);
        viewPageFragment.registerPageListener(this);

        /* Update the main activity title */
        oldTitle = ((HomeActivity) getActivity()).getActionBarTitle();
        ((HomeActivity) getActivity()).setActionBarTitle(STATS_FRAGMENT_NAME);

        /* Get the tabbed fragments */
        statsContractsFragment = (StatsContractsFragment) viewPageFragment
                .pageAdapter.fragments.get(STATS_CONTRACTS_INDEX);
        statsInstitutionsFragment = (StatsInstitutionsFragment) viewPageFragment
                .pageAdapter.fragments.get(STATS_INSTITUTIONS_INDEX);
        statsCompaniesFragment = (StatsCompaniesFragment) viewPageFragment
                .pageAdapter.fragments.get(STATS_COMPANIES_INDEX);


        getServerInfo(STATS_CONTRACTS_INDEX);

        return root;
    }

    /*
     * GET Server info
     */

    /* Get server info for each tab */
    public void getServerInfo(int index) {
        System.out.println("StatsFragment: Get server info for " + index);

        switch (index) {
            case STATS_CONTRACTS_INDEX:
                if (statsContractsFragment.ads == null ||
                        statsContractsFragment.tenders == null)
                    getServerContractsInfo();
                break;

            case STATS_INSTITUTIONS_INDEX:
                if (statsInstitutionsFragment.piADs == null ||
                        statsInstitutionsFragment.piTenders == null)
                    getServerInstitutionsInfo();
                break;

            case STATS_COMPANIES_INDEX:
                if (statsCompaniesFragment.companyTenders == null ||
                        statsCompaniesFragment.companyADs == null)
                    getServerCompaniesInfo();
                break;

            default:
                System.out.println("StatsFragment Unknown tab index");
        }
    }


    /* Get server info for the Contracts tab */
    public void getServerContractsInfo() {
        System.out.println("StatsFragment: getServerContractsInfo");

        CommManager.requestStatsADsByValue(new CommManagerResponse() {
            @Override
            public void processResponse(JSONArray response) {
                receiveStatsADsByValue(response);
            }

            @Override
            public void onErrorOccurred(String errorMsg) {
                if (fragmentCopy.getContext() != null) {
                    Toast.makeText(fragmentCopy.getContext(), errorMsg,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        CommManager.requestStatsTenderssByValue(new CommManagerResponse() {
            @Override
            public void processResponse(JSONArray response) {
                receiveStatsTendersByValue(response);
            }

            @Override
            public void onErrorOccurred(String errorMsg) {
                if (fragmentCopy.getContext() != null) {
                    Toast.makeText(fragmentCopy.getContext(), errorMsg,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /* Get server info for the Contracts tab */
    public void getServerInstitutionsInfo() {
        System.out.println("StatsFragment: getServerInstitutionsInfo");

        CommManager.requestStatsInstitutionsByADCount(new CommManagerResponse() {
            @Override
            public void processResponse(JSONArray response) {
                receiveStatsADInstitutions(response);
            }

            @Override
            public void onErrorOccurred(String errorMsg) {
                if (fragmentCopy.getContext() != null) {
                    Toast.makeText(fragmentCopy.getContext(), errorMsg,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        CommManager.requestStatsInstitutionsByTenderCount(new CommManagerResponse() {
            @Override
            public void processResponse(JSONArray response) {
                receiveStatsTenderInstitutions(response);
            }

            @Override
            public void onErrorOccurred(String errorMsg) {
                if (fragmentCopy.getContext() != null) {
                    Toast.makeText(fragmentCopy.getContext(), errorMsg,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /* Get server info for the Contracts tab */
    public void getServerCompaniesInfo() {
        System.out.println("StatsFragment: getServerCompaniesInfo");

        CommManager.requestStatsCompaniesByADCount(new CommManagerResponse() {
            @Override
            public void processResponse(JSONArray response) {
                receiveStatsADCompanies(response);
            }

            @Override
            public void onErrorOccurred(String errorMsg) {
                if (fragmentCopy.getContext() != null) {
                    Toast.makeText(fragmentCopy.getContext(), errorMsg,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        CommManager.requestStatsCompaniesByTenderCount(new CommManagerResponse() {
            @Override
            public void processResponse(JSONArray response) {
                receiveStatsTenderCompanies(response);
            }

            @Override
            public void onErrorOccurred(String errorMsg) {
                if (fragmentCopy.getContext() != null) {
                    Toast.makeText(fragmentCopy.getContext(), errorMsg,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /*
     * RECEIVE Server info
     */

    /* Receive TOP 10 ADs by value */
    private void receiveStatsADsByValue(JSONArray response) {
        System.out.println("StatsFragment: receiveStatsADsByValue " + response);
        LinkedList<Contract> adContracts = new LinkedList<Contract>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject acq = response.getJSONObject(i);

                Contract a = new Contract();
                a.type = Contract.CONTRACT_TYPE_DIRECT_ACQUISITION;
                a.id = Integer.parseInt(acq.getString(CommManager.JSON_ACQ_ID));
                a.title = acq.getString(CommManager.JSON_CONTRACT_TITLE);
                a.valueRON = acq.getDouble(CommManager.JSON_AD_VALUE_RON);
                a.date = acq.getString(CommManager.JSON_AD_DATE);

                adContracts.add(a);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        statsContractsFragment.ads = adContracts;
        statsContractsFragment.displayStats();

    }

    /* Receive TOP 10 Tenders by value */
    private void receiveStatsTendersByValue(JSONArray response) {
        System.out.println("StatsFragment: receiveStatsTendersByValue " + response);
        LinkedList<Contract> tenderContracts = new LinkedList<Contract>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject tender = response.getJSONObject(i);
                if (tender == null)
                    continue;

                Contract t = new Contract();
                t.type = Contract.CONTRACT_TYPE_TENDER;
                t.id = Integer.parseInt(tender.getString(CommManager.JSON_TENDER_ID_STAT));
                t.title = tender.getString(CommManager.JSON_CONTRACT_TITLE);
                t.valueRON = tender.getDouble(CommManager.JSON_TENDER_VALUE_RON);
                t.date = tender.getString(CommManager.JSON_TENDER_DATE);

                tenderContracts.add(t);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        statsContractsFragment.tenders = tenderContracts;
        statsContractsFragment.displayStats();
    }


    /* Receive TOP 10 Institutions by AD count */
    private void receiveStatsADInstitutions(JSONArray response) {
        System.out.println("StatsFragment: receiveStatsADInstitutions " + response);

        LinkedList<PublicInstitution> adInstitutions = new LinkedList<PublicInstitution>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject acq = response.getJSONObject(i);

                PublicInstitution a = new PublicInstitution();
                a.id = acq.getInt(CommManager.JSON_INSTITUTION_ID);
                a.name = acq.getString(CommManager.JSON_COMPANY_NAME);
                a.directAcqs = acq.getInt(CommManager.JSON_PI_NR_AD);

                adInstitutions.add(a);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        statsInstitutionsFragment.piADs = adInstitutions;
        statsInstitutionsFragment.displayStats();
    }


    /* Receive TOP 10 Institutions by Tender count */
    private void receiveStatsTenderInstitutions(JSONArray response) {
        System.out.println("StatsFragment: receiveStatsTenderInstitutions " + response);

        LinkedList<PublicInstitution> tenderInstitutions = new LinkedList<PublicInstitution>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject acq = response.getJSONObject(i);

                PublicInstitution a = new PublicInstitution();
                a.id = acq.getInt(CommManager.JSON_INSTITUTION_ID);
                a.name = acq.getString(CommManager.JSON_COMPANY_NAME);
                a.tenders = acq.getInt(CommManager.JSON_PI_NR_TENDERS);

                tenderInstitutions.add(a);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        statsInstitutionsFragment.piTenders = tenderInstitutions;
        statsInstitutionsFragment.displayStats();
    }


    /* Receive TOP 10 Companies by AD count */
    private void receiveStatsADCompanies(JSONArray response) {
        System.out.println("StatsFragment: receiveStatsADCompanies " + response);

        LinkedList<Company> adCompanies = new LinkedList<Company>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject acq = response.getJSONObject(i);

                Company a = new Company();
                a.type = Company.COMPANY_TYPE_AD;
                a.id = acq.getInt(CommManager.JSON_COMPANY_ID);
                a.name = acq.getString(CommManager.JSON_COMPANY_NAME);
                a.nrADs = acq.getInt(CommManager.JSON_PI_NR_AD) + "";

                adCompanies.add(a);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        statsCompaniesFragment.companyADs = adCompanies;
        statsCompaniesFragment.displayStats();
    }


    /* Receive TOP 10 Companies by Tender count */
    private void receiveStatsTenderCompanies(JSONArray response) {
        System.out.println("StatsFragment: receiveStatsTenderCompanies " + response);

        LinkedList<Company> tenderCompanies = new LinkedList<Company>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject acq = response.getJSONObject(i);

                Company a = new Company();
                a.type = Company.COMPANY_TYPE_TENDER;
                a.id = acq.getInt(CommManager.JSON_COMPANY_ID);
                a.name = acq.getString(CommManager.JSON_COMPANY_NAME);
                a.nrTenders = acq.getInt(CommManager.JSON_PI_NR_TENDERS) + "";

                tenderCompanies.add(a);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        statsCompaniesFragment.companyTenders = tenderCompanies;
        statsCompaniesFragment.displayStats();
    }


    @Override
    public void onStop() {
        ((HomeActivity) getActivity()).setActionBarTitle(oldTitle);
        super.onStop();
    }

    @Override
    public void onPageChanged(int position) {
        System.out.println("StatsFragment: position has changed to " + position);

        getServerInfo(position);
    }
}
