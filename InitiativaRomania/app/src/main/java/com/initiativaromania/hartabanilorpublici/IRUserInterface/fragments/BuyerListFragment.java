package com.initiativaromania.hartabanilorpublici.IRUserInterface.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by claudiu on 3/30/16.
 */
public class BuyerListFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private View v;

    public static BuyerListFragment newInstance() {
        BuyerListFragment f = new BuyerListFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, "BuyerListFragment");
        f.setArguments(bdl);
        return f;
    }
}
