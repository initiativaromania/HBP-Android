package initiativaromania.hartabanilorpublici.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private int type = CONTRACT_LIST_FOR_COMPANY;

    /* The root public institution for this view */
    private PublicInstitution pi;

    /* The root company for this view */
    private Company company;

    private ContractListFragment directAcqListFragment;
    private ContractListFragment tendersListFragment;

    private View originalView;
    private Fragment fragmentCopy;

    private LinkedList<Contract> directAcqs = new LinkedList<Contract>();
    private LinkedList<Contract> tenders = new LinkedList<Contract>();
    private LinkedList<Company> companies = new LinkedList<Company>();
    private LinkedList<PublicInstitution> pis = new LinkedList<PublicInstitution>();




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        originalView = inflater.inflate(R.layout.fragment_institution, container, false);
        fragmentCopy = this;
        pi = new PublicInstitution();

        /* Init the expandable layout */
        LinearLayout layout = (LinearLayout) originalView.findViewById(R.id.layoutPIName);
        ExpandableRelativeLayout expandableLayout1 = (ExpandableRelativeLayout) originalView.findViewById(R.id.expandableLayout1);
        layout.setOnClickListener(new LinearLayout.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Expanding view");
                ExpandableRelativeLayout expandableLayout1 = (ExpandableRelativeLayout) getView().findViewById(R.id.expandableLayout1);
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
            viewPageFragment.setViewPager(CONTRACT_LIST_FOR_COMPANY);
        } else {
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
        getServerPIInfo(type);

        return originalView;
    }


    /* Get all the institution info from the server */
    private void getServerPIInfo(int type) {

        System.out.println("Get all PI info from server");

         /* Get Contract List */
        if (type == CONTRACT_LIST_FOR_COMPANY) {
//            CommManager.requestCompanyDetails(this, name);
//            CommManager.requestBuyersForFirm(this, name);

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
        }
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
        displayServerPIInfo();
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


    /* PI name, acq, tenders */
    private void displayInitPIInfo() {
        TextView text = ((TextView) originalView.findViewById(R.id.publicInstitutionName));
        if (text != null)
            text.setText(pi.name);

        text = ((TextView) originalView.findViewById(R.id.nrDirectAcquisitions));
        if (text != null)
            text.setText(pi.directAcqs + "");

        text = ((TextView) originalView.findViewById(R.id.nrTenders));
        if (text != null)
            text.setText(pi.tenders + "");
    }


    /* PI CUI, Address */
    private void displayServerPIInfo() {
        TextView text = ((TextView) originalView.findViewById(R.id.piCUI));
        if (text != null)
            text.setText(pi.CUI);

        text = ((TextView) originalView.findViewById(R.id.piAddress));
        if (text != null)
            text.setText(pi.address);
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


    /* Fil lthe list of tenders for this institution */
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
}
