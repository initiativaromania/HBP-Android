package initiativaromania.hartabanilorpublici.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
import com.android.volley.JsonArrayRequest;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
*/

import initiativaromania.hartabanilorpublici.R;

public class SearchFragment extends Fragment {

    ExpandableListAdapter searchAdapter;
    ExpandableListView searchListView;
    List<String> searchTitles;
    HashMap<String, List<String>> searchData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search, container, false);

        /* Build the tabbed View Pager */
        TabbedViewPageFragment viewPageFragment = (TabbedViewPageFragment)
                getChildFragmentManager().findFragmentById(R.id.search_fragment);
        viewPageFragment.setViewPager(InstitutionFragment.CONTRACT_LIST_FOR_SEARCH);

        return root;
    }


}
