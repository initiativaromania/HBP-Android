package initiativaromania.hartabanilorpublici.ui;

import android.os.Bundle;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import initiativaromania.hartabanilorpublici.R;

/**
 * Created by claudiu on 9/12/17.
 */

public class ParticipantViewPageFragment extends Fragment {

    private String tabTitlesCompany[] = new String[]{"Achizitii directe", "Licitatii", "Institutii"};
    private String tabTitlesBuyer[] = new String[]{"Achizitii directe", "Licitatii", "Companii"};
    public static EntityViewPageAdapter pageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("On create view ParticipantViewPageFragment fragment");
        return inflater.inflate(R.layout.fragment_viewpager, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        System.out.println("CREATING THE Participant view page fragment");
    }


    /* Set the viewpager with the corresponding fragments */
    public void setViewPager(int fragmentType) {
        System.out.println("Setting participant view pager to " + fragmentType);
        String tabTitles[];

        /* Get the fragments */
        List<Fragment> fragments = getFragments(fragmentType);

        /* Set up the viewpager */
        if (fragmentType == InstitutionFragment.CONTRACT_LIST_FOR_PUBLIC_INSTITUTION)
            tabTitles = tabTitlesBuyer;
        else
            tabTitles = tabTitlesCompany;

        pageAdapter = new EntityViewPageAdapter(getFragmentManager(), fragments, tabTitles);
        ViewPager pager =
                (ViewPager) getView().findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);
    }


    /* Build the viewpager fragments */
    private List<Fragment> getFragments(int fragmentType) {
        List<Fragment> fList = new ArrayList<Fragment>();

        fList.add(ContractListFragment.newInstance());
        fList.add(ContractListFragment.newInstance());

        if (fragmentType == InstitutionFragment.CONTRACT_LIST_FOR_PUBLIC_INSTITUTION)
            fList.add(ContractListFragment.newInstance());
        else
            fList.add(ContractListFragment.newInstance());

        return fList;
    }
}
