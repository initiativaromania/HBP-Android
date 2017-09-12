package initiativaromania.hartabanilorpublici.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONObject;

import initiativaromania.hartabanilorpublici.R;

public class InstitutionFragment extends Fragment {
    public static final int CONTRACT_LIST_FOR_COMPANY               = 1;
    public static final int CONTRACT_LIST_FOR_PUBLIC_INSTITUTION    = 2;
    public static final String CONTRACT_LIST_TYPE = "contract list type";
    public static final String CONTRACT_LIST_EXTRA = "contract list extra";

    private int type;

    private ContractListFragment contractListFragment;
    private View originalView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        originalView = inflater.inflate(R.layout.fragment_institution, container, false);


        ParticipantViewPageFragment viewPageFragment = (ParticipantViewPageFragment)
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

        return originalView;
    }

}
