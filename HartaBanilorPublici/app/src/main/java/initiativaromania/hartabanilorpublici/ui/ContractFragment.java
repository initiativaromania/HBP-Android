package initiativaromania.hartabanilorpublici.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import initiativaromania.hartabanilorpublici.R;

/**
 * Created by claudiu on 10/17/17.
 */

public class ContractFragment extends Fragment {
    private View originalView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        originalView = inflater.inflate(R.layout.fragment_contract, container, false);

        return originalView;
    }
}
