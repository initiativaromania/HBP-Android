package initiativaromania.hartabanilorpublici.ui;

import android.os.Bundle;
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

public class TabbedViewPageFragment extends Fragment {

    private String tabTitlesCompany[] = new String[]{"Achizitii directe", "Licitatii", "Institutii publice"};
    private String tabTitlesInstitution[] = new String[]{"Achizitii directe", "Licitatii", "Companii"};
    private String tabTitlesSearch[] = new String[]{"Institutii publice", "Companii",
            "Achizitii directe", "Licitatii"};

    public static EntityViewPageAdapter pageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("On create view TabbedViewPageFragment fragment");
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
        List<Fragment> fragments = buildFragments(fragmentType);

        /* Set up the viewpager */
        switch (fragmentType) {
            case InstitutionFragment.CONTRACT_LIST_FOR_PUBLIC_INSTITUTION:
                tabTitles = tabTitlesInstitution;
                break;

            case InstitutionFragment.CONTRACT_LIST_FOR_COMPANY:
                tabTitles = tabTitlesCompany;
                break;

            case InstitutionFragment.CONTRACT_LIST_FOR_SEARCH:
                tabTitles = tabTitlesSearch;
                break;

            default:
                tabTitles = null;
                System.out.println("TabbedViewPageFragment no fragment type");
        }

        pageAdapter = new EntityViewPageAdapter(getFragmentManager(), fragments, tabTitles);
        ViewPager pager = (ViewPager) getView().findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);
    }


    /* Build the viewpager fragments */
    private List<Fragment> buildFragments(int fragmentType) {
        List<Fragment> fList = new ArrayList<Fragment>();

        if (fragmentType == InstitutionFragment.CONTRACT_LIST_FOR_SEARCH) {
            fList.add(InstitutionListFragment.newInstance());
            fList.add(CompanyListFragment.newInstance());
            fList.add(ContractListFragment.newInstance());
            fList.add(ContractListFragment.newInstance());
            return fList;
        }

        fList.add(ContractListFragment.newInstance());
        fList.add(ContractListFragment.newInstance());

        if (fragmentType == InstitutionFragment.CONTRACT_LIST_FOR_PUBLIC_INSTITUTION)
            fList.add(CompanyListFragment.newInstance());
        else
            fList.add(InstitutionListFragment.newInstance());

        return fList;
    }
}
