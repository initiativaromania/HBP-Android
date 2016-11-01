package com.initiativaromania.hartabanilorpublici.IRUserInterface.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.initiativaromania.hartabanilorpublici.IRData.Buyer;
import com.initiativaromania.hartabanilorpublici.IRData.Category;
import com.initiativaromania.hartabanilorpublici.IRData.CommManager;
import com.initiativaromania.hartabanilorpublici.IRData.Company;
import com.initiativaromania.hartabanilorpublici.IRData.Contract;

import com.initiativaromania.hartabanilorpublici.IRUserInterface.activities.MainActivity;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.activities.ParticipantActivity;
import com.initiativaromania.hartabanilorpublici.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;


public class ContractPageFragment extends Fragment  {

    private static final String TAG = ContractPageFragment.class.getName();
    private static final String CONTRACT = "contract";
    private Integer contractId ;
    private Contract contract =new Contract() ;
    private static final int MINIMUM_VOTES          = 300;
    private static final String URL_HBP             = "http://initiativaromania.ro/proiecte/harta-banilor-publici/";
    private static final String URL_HBP_BANNER      = "http://initiativaromania.ro/wp-content/uploads/2015/11/1280x720.png";
    private static final String JUSTIFY_PREFFERENCE = "justify_prefference";

    private SharedPreferences just_prefs;

    private OnFragmentInteractionListener mListener;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    ShareButton shareButton;

    public ContractPageFragment() {
        // Required empty public constructor
    }

    public static ContractPageFragment newInstance(Integer contractId) {

        ContractPageFragment fragment = new ContractPageFragment();
        Bundle args = new Bundle();
        args.putSerializable(CONTRACT, contractId);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contractId = (Integer) getArguments().getSerializable(CONTRACT);
        }
        just_prefs = getActivity().getSharedPreferences(JUSTIFY_PREFFERENCE, 0);

        CommManager.requestContract(this,contractId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

                ViewGroup rootView = (ViewGroup) inflater.inflate(
                        R.layout.contract_view, container, false);

                return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /* Initia Facebook share dialog */
    private void initFacebook() {
        FacebookSdk.sdkInitialize(getActivity());

        shareDialog = new ShareDialog(this);
        callbackManager = CallbackManager.Factory.create();

        /* Init button */
        shareButton = (ShareButton)getView().findViewById(R.id.fb_share_button);
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(URL_HBP))
                .setContentTitle(contract.buyer.name + " a cheltuit " + contract.valueEUR +
                        " EURO pentru " + contract.title)
                .setImageUrl(Uri.parse(URL_HBP_BANNER))
                .build();
        shareButton.setShareContent(linkContent);


        shareButton.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.i("FB Share button", "success");
            }

            @Override
            public void onCancel() {
                Log.i("FB Share button", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i("FB Share button", "error");
            }
        });
    }
    /* Show contract details in the contract view */
    private void displayContract() {

        /* Show contract title */
        TextView tv = (TextView)getView().findViewById(R.id.textViewTitluContract);
        if (tv != null)
            tv.setText(contract.title);

        /* Show Contract number */
        tv = (TextView)getView().findViewById(R.id.textContractNr);
        if (tv != null)
            tv.setText(contract.number);

        /* Show company */
        tv = (TextView)getView().findViewById(R.id.textCompany);
        if (tv != null) {
            tv.setText(contract.company.name);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG,"Click on company");

                    /* Start a separate view for a company */
                    Intent intent = new Intent(getActivity() , ParticipantActivity.class);
                    intent.putExtra(ParticipantActivity.CONTRACT_LIST_TYPE, ParticipantActivity.CONTRACT_LIST_FOR_COMPANY);
                    intent.putExtra(ParticipantActivity.CONTRACT_LIST_EXTRA, contract.company.name);
                    startActivity(intent);
                }
            });
        }

        /* Show the buyeer */
        tv = (TextView)getView().findViewById(R.id.textPrimarie);
        if (tv != null) {
            tv.setText(contract.buyer.name);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG,"Click on authority");

                    /* Start a separate view for a company */
                    Intent intent = new Intent(getActivity(), ParticipantActivity.class);
                    intent.putExtra(ParticipantActivity.CONTRACT_LIST_TYPE, ParticipantActivity.CONTRACT_LIST_FOR_BUYER);
                    intent.putExtra(ParticipantActivity.CONTRACT_LIST_EXTRA, contract.buyer.name);
                    startActivity(intent);
                }
            });
        }

        /* Show the date of the contract */
        tv = (TextView)getView().findViewById(R.id.textData);
        if (tv != null)
            tv.setText(contract.date);

        /* Show the value of the contract */
        tv = (TextView)getView().findViewById(R.id.textValue);
        double price = Double.parseDouble(contract.valueEUR);
        DecimalFormat dm = new DecimalFormat("###,###.###");
        if (tv != null)
            tv.setText(String.valueOf(dm.format(price)) + " EUR");

        /* Show the contract's CPV code */
        tv = (TextView)getView().findViewById(R.id.textCPV);
        if (tv != null)
            tv.setText(contract.CPVCode);


        /* Setup the Justify button */
        Log.i(TAG,"Contract votes " + contract.votes);
        Button button = (Button)getView().findViewById(R.id.button);
        if (button != null) {
            button.setText("Cere justificare (" + contract.votes + ")");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /* Check whether you have voted before */
                    int votedBefore = just_prefs.getInt("Contract" + contractId, -1);
                    Log.i(TAG,"Saved prefference value for contract " + contractId + " id " + votedBefore);

                    if (votedBefore == -1) {
                        Log.i(TAG,"Never voted. Calling justify from button");
                        CommManager.justifyContract(ContractPageFragment.this, contract);
                    } else
                        Toast.makeText(getActivity(), "Ai mai cerut o data justificarea acestui contract",
                                Toast.LENGTH_SHORT).show();
                }
            });
        }

        getView().invalidate();
    }

    /* Update the Justify button when the justify succeeds */
    public void contractJustification() {
        contract.votes++;
        Button button = (Button)getView().findViewById(R.id.button);
        if (button != null) {
            button.setText("Cere justificare (" + contract.votes + ")");
        }
        getView().invalidate();

        /* Remember the fact that you voted for this contract */
        SharedPreferences.Editor editor = just_prefs.edit();
        editor.putInt("Contract" + contractId, 1);
        editor.commit();

        Toast.makeText(getActivity(), "Cererea ta a fost inregistrata. La " + MINIMUM_VOTES +
                " de cereri Initiativa Romania va cere detalii despre acest contract si anexele sale.", Toast.LENGTH_LONG).show();
    }

    /* Receive the contract details from the server and display them */
    public void receiveContract(JSONObject response) {

        try {
            contract.id=response.getInt("id");
            contract.CPVCode = response.getString("CPVCode");
            contract.address = response.getString("address");
            contract.company = new Company();
            contract.buyer = new Buyer();
            contract.buyer.name = response.getString("buyer");
            contract.votes = Integer.parseInt(response.getString("justify"));
            contract.company.name = response.getString("company");
            contract.number = response.getString("contract_nr");
            contract.title = response.getString("contract_title");
            contract.valueEUR = response.getString("price");
            contract.date = response.getString("start_date");

            /* Save all the categories */
            JSONArray categoriesJSON = response.getJSONArray("categories");
            for (int i = 0; i < categoriesJSON.length(); i++) {

                Category category = new Category();
                category.name = categoriesJSON.get(i).toString();
                contract.addCategory(category);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        displayContract();

         /* Initiatilize Facebook share dialog */
        initFacebook();

    }

}
