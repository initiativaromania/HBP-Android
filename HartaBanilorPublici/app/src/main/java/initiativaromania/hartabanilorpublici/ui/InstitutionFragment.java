package initiativaromania.hartabanilorpublici.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

import initiativaromania.hartabanilorpublici.R;
import initiativaromania.hartabanilorpublici.comm.CommManager;
import initiativaromania.hartabanilorpublici.comm.CommManagerResponse;
import initiativaromania.hartabanilorpublici.data.Company;
import initiativaromania.hartabanilorpublici.data.Contract;
import initiativaromania.hartabanilorpublici.data.PublicInstitution;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InstitutionFragment extends Fragment {
    public static final int CONTRACT_LIST_FOR_COMPANY               = 1;
    public static final int CONTRACT_LIST_FOR_PUBLIC_INSTITUTION    = 2;
    public static final String CONTRACT_LIST_TYPE = "contract list type";
    public static final String CONTRACT_LIST_EXTRA = "contract list extra";
    public static final int DIRECT_ACQ_FRAGMENT_INDEX               = 0;
    public static final int TENDER_FRAGMENT_INDEX                   = 1;
    public static final int INSTITUTIONS_FRAGMENT_INDEX             = 2;

    private int type = CONTRACT_LIST_FOR_COMPANY;

    /* The root public institution for this view */
    private PublicInstitution pi;

    /* The root company for this view */
    private Company company;

    private ContractListFragment directAcqListFragment;
    private ContractListFragment tendersListFragment;
    private CompanyListFragment companyListFragment;

    private View originalView;
    private Fragment fragmentCopy;

    private LinkedList<Contract> directAcqs = new LinkedList<Contract>();
    private LinkedList<Contract> tenders = new LinkedList<Contract>();
    private LinkedList<Company> companies = new LinkedList<Company>();
    private LinkedList<PublicInstitution> pis = new LinkedList<PublicInstitution>();

    public String oldTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        originalView = inflater.inflate(R.layout.fragment_institution, container, false);
        fragmentCopy = this;

        /* Init the expandable layout */
        LinearLayout layout = (LinearLayout) originalView.findViewById(R.id.layoutPIName);
        ExpandableRelativeLayout expandableLayout1 = (ExpandableRelativeLayout) originalView
                .findViewById(R.id.expandableLayout1);
        layout.setOnClickListener(new LinearLayout.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Expanding view");
                ExpandableRelativeLayout expandableLayout1 = (ExpandableRelativeLayout) getView()
                        .findViewById(R.id.expandableLayout1);
                expandableLayout1.toggle(); // toggle expand and collapse
            }
        });


        /* Build the View Pager */
        InstitutionViewPageFragment viewPageFragment = (InstitutionViewPageFragment)
                getChildFragmentManager().findFragmentById(R.id.entity_info_fragment);


        /* Get fragment parameters */
        Bundle bundle = new Bundle();
        bundle = getArguments();
        if (bundle == null)
            return originalView;

        type = bundle.getInt(CommManager.BUNDLE_INST_TYPE);
        System.out.println("Institution Arguments: Type" + type );

        if (type == CONTRACT_LIST_FOR_COMPANY) {
            company = new Company();
            company.id = bundle.getInt(CommManager.BUNDLE_COMPANY_ID);
            company.type = bundle.getInt(CommManager.BUNDLE_COMPANY_TYPE);
            company.name = bundle.getString(CommManager.BUNDLE_COMPANY_NAME);

            viewPageFragment.setViewPager(CONTRACT_LIST_FOR_COMPANY);

            System.out.println("Company Arguments: id " + company.id + " name " + company.name +
                    " type " + company.type);

            displayInitCompanyInfo();
        } else {
            pi = new PublicInstitution();
            pi.id = bundle.getInt(CommManager.BUNDLE_PI_ID);
            pi.name = bundle.getString(CommManager.BUNDLE_PI_NAME);
            pi.directAcqs = bundle.getInt(CommManager.BUNDLE_PI_ACQS);
            pi.tenders = bundle.getInt(CommManager.BUNDLE_PI_TENDERS);

            viewPageFragment.setViewPager(CONTRACT_LIST_FOR_PUBLIC_INSTITUTION);
            System.out.println("Institution Arguments: id " + pi.id + " name " + pi.name +
                " acqs " + pi.directAcqs + " tenders " + pi.tenders);

            /* Show initial PI info received as arguments */
            displayInitPIInfo();
        }


        /* Call the server for all the institution info */
        getServerInstitInfo(type);

        return originalView;
    }


    /* Get all the institution info from the server */
    private void getServerInstitInfo(int type) {

        System.out.println("Get all Institution info from server");

         /* Get Server information for a company */
        if (type == CONTRACT_LIST_FOR_COMPANY) {
            switch (company.type) {
                case Company.COMPANY_TYPE_AD:
                    /* Send request to get the init data */
                    CommManager.requestADCompany(new CommManagerResponse() {
                        @Override
                        public void processResponse(JSONArray response) {
                            receiveCompanyInfo(response);
                        }

                        @Override
                        public void onErrorOccurred(String errorMsg) {
                            Toast.makeText(fragmentCopy.getContext(), errorMsg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }, company.id);
                    break;

                case Company.COMPANY_TYPE_TENDER:
                    /* Send request to get the init data */
                    CommManager.requestTenderCompany(new CommManagerResponse() {
                        @Override
                        public void processResponse(JSONArray response) {
                            receiveCompanyInfo(response);
                        }

                        @Override
                        public void onErrorOccurred(String errorMsg) {
                            Toast.makeText(fragmentCopy.getContext(), errorMsg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }, pi.id);
                    break;

                default:
                    System.out.println("Unknown company type");

            }


            /* Get Server information for a Public Institution */
        } else {

             /* Send request to get the init data */
            CommManager.requestPIInfo(new CommManagerResponse() {
                @Override
                public void processResponse(JSONArray response) {
                    receivePIInfo(response);
                }

                @Override
                public void onErrorOccurred(String errorMsg) {
                    Toast.makeText(fragmentCopy.getContext(), errorMsg,
                            Toast.LENGTH_SHORT).show();
                }
            }, pi.id);

             /* Send request to get the institution direct acquisitions */
            CommManager.requestPIAcqs(new CommManagerResponse() {
                @Override
                public void processResponse(JSONArray response) {
                    receivePIAcqs(response);
                }

                @Override
                public void onErrorOccurred(String errorMsg) {
                    Toast.makeText(fragmentCopy.getContext(), errorMsg,
                            Toast.LENGTH_SHORT).show();
                }
            }, pi.id);

             /* Send request to get the institution tenders */
            CommManager.requestPITenders(new CommManagerResponse() {
                @Override
                public void processResponse(JSONArray response) {
                    receivePITenders(response);
                }

                @Override
                public void onErrorOccurred(String errorMsg) {
                    Toast.makeText(fragmentCopy.getContext(), errorMsg,
                            Toast.LENGTH_SHORT).show();
                }
            }, pi.id);

            /* Send request to get the institution AD companies */
            CommManager.requestADCompaniesByPI(new CommManagerResponse() {
                @Override
                public void processResponse(JSONArray response) {
                    receivePICompanies(response, Company.COMPANY_TYPE_AD);
                }

                @Override
                public void onErrorOccurred(String errorMsg) {
                    Toast.makeText(fragmentCopy.getContext(), errorMsg,
                            Toast.LENGTH_SHORT).show();
                }
            }, pi.id);

            /* Send request to get the institution Tender companies */
            CommManager.requestTenderCompaniesByPI(new CommManagerResponse() {
                @Override
                public void processResponse(JSONArray response) {
                    receivePICompanies(response, Company.COMPANY_TYPE_TENDER);
                }

                @Override
                public void onErrorOccurred(String errorMsg) {
                    Toast.makeText(fragmentCopy.getContext(), errorMsg,
                            Toast.LENGTH_SHORT).show();
                }
            }, pi.id);
        }
    }


    /* Receive Company information from the server */
    private void receiveCompanyInfo(JSONArray response) {
        System.out.println("InstitutionFragment: receiveCompany " + response);

        try {
            JSONObject companySummary = response.getJSONObject(0);
            company.address = companySummary.getString(CommManager.JSON_COMPANY_ADDRESS);
            company.CUI = companySummary.getString(CommManager.JSON_COMPANY_CUI);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Show the info received from the server */
        displayServerInfo(type);
    }


    /* Receive Public Institution information from the server */
    private void receivePIInfo(JSONArray response) {
        System.out.println("InstitutionFragment: receivePIInfo " + response);

        try {
            JSONObject piSummary = response.getJSONObject(0);
            pi.CUI = piSummary.getString(CommManager.JSON_PI_CUI);
            pi.address = piSummary.getString(CommManager.JSON_PI_ADDRESS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Show the info received from the server */
        displayServerInfo(type);
    }


    /* Receive Public Institution Direct Acquisitions from the server */
    private void receivePIAcqs(JSONArray response) {
        System.out.println("InstitutionFragment: receivePIAcqs " + response +
            " size " + response.length());

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject acq = response.getJSONObject(i);
                if (acq == null)
                    continue;

                Contract a = new Contract();
                a.type = Contract.CONTRACT_TYPE_DIRECT_ACQUISITION;
                a.id = Integer.parseInt(acq.getString(CommManager.JSON_ACQ_ID));
                a.title = acq.getString(CommManager.JSON_CONTRACT_TITLE);
                a.number = acq.getString(CommManager.JSON_CONTRACT_NR);
                a.valueRON = Double.parseDouble(acq.getString(CommManager.JSON_CONTRACT_VALUE_RON));
                if (type == CONTRACT_LIST_FOR_COMPANY)
                    a.company = company;
                else
                    a.pi = pi;

                directAcqs.add(a);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Show the info received from the server */
        displayDirectAcqs();
    }


    /* Receive Public Institution Tenders from the server */
    private void receivePITenders(JSONArray response) {
        System.out.println("InstitutionFragment: receivePITenders " +
                " size " + response.length());

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject tender = response.getJSONObject(i);
                if (tender == null)
                    continue;

                Contract t = new Contract();
                t.type = Contract.CONTRACT_TYPE_TENDER;
                t.id = Integer.parseInt(tender.getString(CommManager.JSON_TENDER_ID));
                t.title = tender.getString(CommManager.JSON_CONTRACT_TITLE);
                t.number = tender.getString(CommManager.JSON_CONTRACT_NR);
                t.valueRON = Double.parseDouble(tender.getString(CommManager.JSON_CONTRACT_VALUE_RON));
                if (type == CONTRACT_LIST_FOR_COMPANY)
                    t.company = company;
                else
                    t.pi = pi;

                tenders.add(t);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Show the info received from the server */
        displayTenders();
    }


    /* Receive Companies for this institution from the server */
    private void receivePICompanies(JSONArray response, int type) {
        System.out.println("InstitutionFragment: receivePICompanies " +
                " size " + response.length() + " type " + type);

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject company = response.getJSONObject(i);
                if (company == null)
                    continue;

                Company c = new Company();
                c.type = type;
                c.id = Integer.parseInt(company.getString(CommManager.JSON_COMPANY_ID));
                c.name = company.getString(CommManager.JSON_COMPANY_NAME);

                companies.add(c);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Show the info received from the server */
        displayCompanies();
    }


    /* PI name, acq, tenders */
    private void displayInitPIInfo() {
        oldTitle = ((HomeActivity) getActivity()).getActionBarTitle();
        ((HomeActivity) getActivity()).setActionBarTitle("Institutie Publica");


        TextView text = ((TextView) originalView.findViewById(R.id.institutionName));
        if (text != null)
            text.setText(pi.name);

        text = ((TextView) originalView.findViewById(R.id.nrDirectAcquisitions));
        if (text != null)
            text.setText(pi.directAcqs + "");

        text = ((TextView) originalView.findViewById(R.id.nrTenders));
        if (text != null)
            text.setText(pi.tenders + "");
    }


    /* Company name, address */
    private void displayInitCompanyInfo() {
        oldTitle = ((HomeActivity) getActivity()).getActionBarTitle();
        ((HomeActivity) getActivity()).setActionBarTitle("Companie");


        TextView text = ((TextView) originalView.findViewById(R.id.institutionName));
        if (text != null)
            text.setText(company.name);

        text = ((TextView) originalView.findViewById(R.id.nrDirectAcquisitions));
        if (text != null)
            text.setText(0 + "");

        text = ((TextView) originalView.findViewById(R.id.nrTenders));
        if (text != null)
            text.setText(0 + "");
    }


    /* Institution CUI, Address */
    private void displayServerInfo(int type) {
        TextView text = ((TextView) originalView.findViewById(R.id.piCUI));
        if (text != null)
            text.setText(type == CONTRACT_LIST_FOR_COMPANY ?
                    company.CUI : pi.CUI);

        text = ((TextView) originalView.findViewById(R.id.piAddress));
        if (text != null)
            text.setText(type == CONTRACT_LIST_FOR_COMPANY ?
                    company.address : pi.address);
    }


    /* Fill the list of direct acquisitions for this institution */
    private void displayDirectAcqs() {

        /* Fill the contract list fragment */
        directAcqListFragment = (ContractListFragment) InstitutionViewPageFragment
                .pageAdapter.fragments.get(DIRECT_ACQ_FRAGMENT_INDEX);
        if (directAcqListFragment != null) {
            directAcqListFragment.setContracts(directAcqs);
            directAcqListFragment.displayContracts();
        } else
            System.out.println("NULL contract list fragment");
    }


    /* Fill the list of tenders for this institution */
    private void displayTenders() {

        /* Fill the contract list fragment */
        tendersListFragment = (ContractListFragment) InstitutionViewPageFragment
                .pageAdapter.fragments.get(TENDER_FRAGMENT_INDEX);
        if (tendersListFragment != null) {
            tendersListFragment.setContracts(tenders);
            tendersListFragment.displayContracts();
        } else
            System.out.println("NULL contract list fragment");
    }


    /* Fil lthe list of companies for this institution */
    private void displayCompanies() {

        /* Fill the companies list fragment */
        companyListFragment = (CompanyListFragment) InstitutionViewPageFragment
                .pageAdapter.fragments.get(INSTITUTIONS_FRAGMENT_INDEX);
        if (companyListFragment != null) {
            companyListFragment.setCompanies(companies);
            companyListFragment.displayCompanies();
        } else
            System.out.println("NULL company list fragment");
    }

    @Override
    public void onStop() {
        ((HomeActivity) getActivity()).setActionBarTitle(oldTitle);
        super.onStop();
    }
}
