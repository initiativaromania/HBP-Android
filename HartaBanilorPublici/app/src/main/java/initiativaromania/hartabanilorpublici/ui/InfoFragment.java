package initiativaromania.hartabanilorpublici.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import initiativaromania.hartabanilorpublici.R;

/**
 * Created by claudiu on 2/4/18.
 */

public class InfoFragment extends Fragment{

    private static final String INFO_FRAGMENT_NAME          = "Info";
    public static final int INFO_NAVIGATION_ID              = 3;

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
