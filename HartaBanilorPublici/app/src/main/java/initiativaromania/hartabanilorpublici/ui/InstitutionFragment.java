package initiativaromania.hartabanilorpublici.ui;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

import initiativaromania.hartabanilorpublici.R;
import initiativaromania.hartabanilorpublici.data.Company;
import initiativaromania.hartabanilorpublici.data.Contract;
import initiativaromania.hartabanilorpublici.data.PublicInstitution;

public class InstitutionFragment extends Fragment {
    public static final int CONTRACT_LIST_FOR_COMPANY               = 1;
    public static final int CONTRACT_LIST_FOR_PUBLIC_INSTITUTION    = 2;
    public static final String CONTRACT_LIST_TYPE = "contract list type";
    public static final String CONTRACT_LIST_EXTRA = "contract list extra";
    public static final int DIRECT_ACQ_FRAGMENT_INDEX               = 0;
    public static final int TENDER_FRAGMENT_INDEX                   = 1;

    private int type;

    private ContractListFragment directAcqListFragment;
    private ContractListFragment tendersListFragment;

    private View originalView;

    private LinkedList<Contract> directAcqs = new LinkedList<Contract>();
    private LinkedList<Contract> tenders = new LinkedList<Contract>();
    private LinkedList<Company> companies = new LinkedList<Company>();
    private LinkedList<PublicInstitution> pis = new LinkedList<PublicInstitution>();


    /* Fill the list of direct acquisitions for this institution */
    private void displayDirectAcqs() {

        for (int i = 0; i < 10; i++) {
            Contract contract = new Contract();
            contract.title = "Laptop de test " + i;

            directAcqs.add(contract);
        }

        /* Fill the contract list fragment */
        directAcqListFragment = (ContractListFragment) InstitutionViewPageFragment
                .pageAdapter.fragments.get(DIRECT_ACQ_FRAGMENT_INDEX);
        if (directAcqListFragment != null) {
            directAcqListFragment.setContracts(directAcqs);
        } else
            System.out.println("NULL contract list fragment");
    }


    /* Fil lthe list of tenders for this institution */
    private void displayTenders() {

        for (int i = 0; i < 10; i++) {
            Contract contract = new Contract();
            contract.title = "Laptop de test " + i;

            tenders.add(contract);
        }

        /* Fill the contract list fragment */
        tendersListFragment = (ContractListFragment) InstitutionViewPageFragment
                .pageAdapter.fragments.get(TENDER_FRAGMENT_INDEX);
        if (tendersListFragment != null) {
            tendersListFragment.setContracts(tenders);
        } else
            System.out.println("NULL contract list fragment");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        originalView = inflater.inflate(R.layout.fragment_institution, container, false);


        InstitutionViewPageFragment viewPageFragment = (InstitutionViewPageFragment)
                getChildFragmentManager().findFragmentById(R.id.entity_info_fragment);


        if (viewPageFragment != null)
            System.out.println("Ok fragment");
        else
            System.out.println("Not ok fragment");


        type = CONTRACT_LIST_FOR_PUBLIC_INSTITUTION;


        /* Get Contract List */
        if (type == CONTRACT_LIST_FOR_COMPANY) {
            viewPageFragment.setViewPager(CONTRACT_LIST_FOR_COMPANY);
//            CommManager.requestCompanyDetails(this, name);
//            CommManager.requestBuyersForFirm(this, name);
        } else {
            viewPageFragment.setViewPager(CONTRACT_LIST_FOR_PUBLIC_INSTITUTION);
//            CommManager.requestBuyerDetails(new ICommManagerResponse() {
//                @Override
//                public void processResponse(JSONObject response) {
//                    ParticipantActivity.this.receiveBuyerDetails(response);
//                }
//                @Override
//                public void onErrorOccurred(String errorMsg)
//                {
//                    Toast.makeText(ParticipantActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
//                }
//            }, name);
//            CommManager.requestFirmsForBuyer(this, name);
        }


        // TODO call the server
        displayDirectAcqs();
        displayTenders();

        return originalView;
    }

}
