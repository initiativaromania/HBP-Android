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
import initiativaromania.hartabanilorpublici.data.InstitutionListItem;
import initiativaromania.hartabanilorpublici.data.PublicInstitution;

/**
 * Created by claudiu on 4/22/18.
 */

public class StatsInstitutionsFragment extends Fragment {
    private View v;
    public int parentID;

    /* Stats objects */
    public LinkedList<PublicInstitution> piADs;
    public LinkedList<PublicInstitution> piTenders;

    public static StatsInstitutionsFragment newInstance(int parentID) {
        StatsInstitutionsFragment f = new StatsInstitutionsFragment();
        f.parentID = parentID;

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_stats_institutions, container, false);
        System.out.println("StatsInstitutionsFragment on create View");

        displayStats();

        return v;
    }

    /* Display all the available institutions in this stats fragment */
    public void displayStats() {

        if (v == null)
            return;

        if (piADs != null)
            displayInstitutions(piADs, R.id.top_Instit_AD);

        if (piTenders != null)
            displayInstitutions(piTenders, R.id.top_Instit_Tender);

    }

    /* Show contracts in the fragment */
    public void displayInstitutions(LinkedList<PublicInstitution> pis, int listId) {
        if (pis == null)
            return;

        List<InstitutionListItem> orderDetailsList = new ArrayList<>();

        for (final PublicInstitution pi : pis) {
            orderDetailsList.add(new InstitutionListItem() {{
                id = pi.id;
                name = pi.name;
                nrADs = pi.directAcqs;
                nrTenders = pi.tenders;
            }});
        }

        ListView orderList = (ListView) v.findViewById(listId);
        InstitutionListAdapter adapter = new InstitutionListAdapter(getActivity(), parentID, orderDetailsList);
        orderList.setAdapter(adapter);
        orderList.setOnItemClickListener(adapter);
    }
}
