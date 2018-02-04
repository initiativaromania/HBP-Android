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

public class InfoFragment extends Fragment{

    private static final String INFO_FRAGMENT_NAME          = "Info";

    private Fragment fragmentCopy;
    public String oldTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_info, container, false);
        fragmentCopy = this;

        /* Update the main activity title */
        oldTitle = ((HomeActivity) getActivity()).getActionBarTitle();
        ((HomeActivity) getActivity()).setActionBarTitle(INFO_FRAGMENT_NAME);

        return root;
    }

    @Override
    public void onStop() {
        ((HomeActivity) getActivity()).setActionBarTitle(oldTitle);
        super.onStop();
    }
}
