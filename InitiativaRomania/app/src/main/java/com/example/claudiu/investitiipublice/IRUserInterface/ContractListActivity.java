package com.example.claudiu.investitiipublice.IRUserInterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.claudiu.initiativaromania.R;
import com.example.claudiu.investitiipublice.IRObjects.Contract;
import com.example.claudiu.investitiipublice.IRObjects.CommManager;
import com.example.claudiu.investitiipublice.IRUserInterface.statistics.StatisticsContractRowAdapter;
import com.example.claudiu.investitiipublice.IRUserInterface.statistics.StatisticsContractDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by claudiu on 2/12/16.
 */
public class ContractListActivity extends Activity {
    public static final String COMPANY_ACTIVITY_NAME    = "Companie";
    public static final String AUTHORITY_ACTIVITY_NAME  = "Cumparator";
    public static final String CONTRACT_LIST_TYPE       = "contract list type";
    public static final String CONTRACT_LIST_EXTRA      = "contract list extra";
    public static final int CONTRACT_LIST_FOR_COMPANY   = 1;
    public static final int CONTRACT_LIST_FOR_BUYER     = 2;

    private int type;
    private String name;
    private LinkedList<Contract> contracts;
    private double totalValue = 0;

    private void setTextViews() {
        Intent intent = getIntent();
        this.type = intent.getIntExtra(CONTRACT_LIST_TYPE, -1);

        System.out.println("Type of Contract list is " + this.type);
        name = intent.getStringExtra(CONTRACT_LIST_EXTRA);


        /* Determine if the activity is for a Company or for a Mayor Office */
        if (type == CONTRACT_LIST_FOR_COMPANY) {

            TextView tv = (TextView)findViewById(R.id.textViewContractList);
            if (tv != null)
                tv.setText(COMPANY_ACTIVITY_NAME);
        } else {
            TextView tv = (TextView)findViewById(R.id.textViewContractList);
            if (tv != null)
                tv.setText(AUTHORITY_ACTIVITY_NAME);
        }

        TextView tv = (TextView)findViewById(R.id.textEntityName);
        if (tv != null)
            tv.setText(name);
    }


    /* Show the contracts in a predefined list view */
    private double displayContracts(JSONArray contractsJSON) {
        List<StatisticsContractDetails> orderDetailsList = new ArrayList<>();
        double totalValue = 0;

        try {
            for (int i = 0; i < contractsJSON.length(); i++) {
                JSONObject contractJSON = contractsJSON.getJSONObject(i);

                final Contract contract = new Contract();
                contract.id = Integer.parseInt(contractJSON.getString("id"));
                contract.title = contractJSON.getString("contract_title");
                contract.valueEUR = contractJSON.getString("price");

                totalValue += Double.parseDouble(contract.valueEUR);

                orderDetailsList.add(new StatisticsContractDetails() {{
                    id = contract.id;
                    title = contract.title;
                    price = contract.valueEUR;
                }});

                System.out.println("Contract " + contract.id + ", title " + contract.title);

                ListView orderList = (ListView) findViewById(R.id.entityContractList);
                StatisticsContractRowAdapter adapter = new StatisticsContractRowAdapter(this, orderDetailsList);
                orderList.setAdapter(adapter);
                orderList.setOnItemClickListener(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return totalValue;
    }


    /* Receive the list of contracts per company and display it */
    public void receiveCompanyDetails(JSONObject response) {

        /* Set total value of the contracts */
        try {
            totalValue = Double.parseDouble(response.getString("ordersSum"));
            DecimalFormat dm = new DecimalFormat("###,###.###");
            TextView tv = (TextView)findViewById(R.id.textTotalValue);
            tv.setText(String.valueOf(dm.format(totalValue) + " EUR"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Set the contracts */
        try {
            JSONArray contractsJSON = response.getJSONArray("topOrders");

            displayContracts(contractsJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /* Receive the list of contracts per buyer and display it */
    public void receiveBuyerDetails(JSONObject response) {

         /* Set the contracts */
        try {
            JSONArray contractsJSON = response.getJSONArray("orders");

            totalValue = displayContracts(contractsJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Fill in the total value of all the contracts */
        DecimalFormat dm = new DecimalFormat("###,###.###");
        TextView tv = (TextView)findViewById(R.id.textTotalValue);
        tv.setText(String.valueOf(dm.format(totalValue) + " EUR"));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_contracts);

        /* Set header text views */
        setTextViews();

        /* Get Contract List */
        if (type == CONTRACT_LIST_FOR_COMPANY)
            CommManager.requestCompanyDetails(this, name);
        else
            CommManager.requestBuyerDetails(this, name);
    }

}
