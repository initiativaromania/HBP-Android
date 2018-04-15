package initiativaromania.hartabanilorpublici.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import initiativaromania.hartabanilorpublici.R;

/**
 * Created by claudiu on 2/4/18.
 */

public class StatsFragment extends Fragment implements TabbedViewPageListener{

    private static final String STATS_FRAGMENT_NAME         = "Statistici";
    public static final int STATS_NAVIGATION_ID            = 2;

    private Fragment fragmentCopy;
    private String oldTitle;

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

        return root;
    }

    @Override
    public void onStop() {
        ((HomeActivity) getActivity()).setActionBarTitle(oldTitle);
        super.onStop();
    }

    @Override
    public void onPageChanged(int position) {
        System.out.println("StatsFragment: position has changed to " + position);
    }
}
