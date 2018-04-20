package initiativaromania.hartabanilorpublici.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import initiativaromania.hartabanilorpublici.R;

/**
 * Created by claudiu on 4/20/18.
 */

public class StatsContractsFragment extends Fragment {
    private View v;
    public int parentID;

    public static StatsContractsFragment newInstance(int parentID) {
        StatsContractsFragment f = new StatsContractsFragment();
        f.parentID = parentID;

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_stats_contracts, container, false);
        System.out.println("StatsContractsFragment on create View");

        return v;
    }
}
