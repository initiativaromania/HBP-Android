package initiativaromania.hartabanilorpublici;

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


/**
 * @// TODO: 25.09.2017  Remove Log.d
 */
public class SearchFragment extends Fragment {

    ExpandableListAdapter searchAdapter;
    ExpandableListView searchListView;
    List<String> searchTitles;
    HashMap<String, List<String>> searchData;
    private static final String TAG = "MyActivity";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        // Add adapter for auto-complete tv
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);

        AutoCompleteTextView textView = (AutoCompleteTextView)
                root.findViewById(R.id.autocompleteSearch);
        textView.setAdapter(adapter);

        searchListView = (ExpandableListView) root.findViewById(R.id.searchData1);
        if (searchListView == null) {
            Log.d(TAG, "null searchListView");
        }

        // Add listener for the search button
        Button btnSearch = root.findViewById(R.id.searchButton);
        btnSearch.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                /* Todo: remove me */
                tempListData();
                View slv = (View) v.getParent().getParent();
                AutoCompleteTextView tv = slv.findViewById(R.id.autocompleteSearch);

                if (tv == null) {
                    Log.e(TAG, "[Search] cannot find autocomplete text view");
                    return;
                }
                /* Todo: update after merge */
                /*
                JsonArrayRequest dbRequest = new JsonArrayRequest(Request.Method.GET, SEARCH_URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mTxtDisplay.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
                */
                searchListView = (ExpandableListView) slv.findViewById(R.id.searchData1);
                if (searchListView == null) {
                    Log.e(TAG, "[Search] cannot find searchListView");
                    return;
                }

                searchAdapter  = new ExpandableSearchListAdapter(getActivity(), searchTitles, searchData);
                searchListView.setAdapter(searchAdapter);
            }
        });

        return root;
    }

    /* todo: Remove me after web search request is available */
    private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain", "Belarus"
    };
    /* todo: Remove me after web search request is available */
    private void tempListData() {
        searchTitles = new ArrayList<String>();
        searchData = new HashMap<String, List<String>>();

        // Adding child data
        searchTitles.add("Top 250");
        searchTitles.add("Now Showing");
        searchTitles.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        searchData.put(searchTitles.get(0), top250); // Header, Child data
        searchData.put(searchTitles.get(1), nowShowing);
        searchData.put(searchTitles.get(2), comingSoon);
    }
}
