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
import initiativaromania.hartabanilorpublici.data.InstitutionListItem;
import initiativaromania.hartabanilorpublici.data.PublicInstitution;

/**
 * Created by claudiu on 25/11/17.
 */

public class InstitutionListFragment  extends Fragment {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private View v;
    public LinkedList<PublicInstitution> pis;

    public static InstitutionListFragment newInstance() {
        InstitutionListFragment f = new InstitutionListFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, "InstitutionListFragment");
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_list, container, false);

        displayPIs();

        return v;
    }


    /* Show companies in the fragment */
    public void displayPIs() {
        if (pis == null || v == null)
            return;

        System.out.println("Displaying pis in fragment, size " + pis.size());
        List<InstitutionListItem> orderDetailsList = new ArrayList<>();

        for (final PublicInstitution pi : pis) {
            orderDetailsList.add(new InstitutionListItem() {{
                id = pi.id;
                name = pi.name;
            }});
        }

        ListView orderList = (ListView) v.findViewById(R.id.list_entities);
        InstitutionListAdapter adapter = new InstitutionListAdapter(getActivity(), orderDetailsList);
        orderList.setAdapter(adapter);
        orderList.setOnItemClickListener(adapter);
    }


    /* Set companies in the fragment */
    public void setPIs(LinkedList<PublicInstitution> pis) {
        this.pis = pis;
    };
}