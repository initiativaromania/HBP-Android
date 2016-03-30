/**
 This file is part of "Harta Banilor Publici".

 "Harta Banilor Publici" is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 "Harta Banilor Publici" is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.initiativaromania.hartabanilorpublici.IRUserInterface.objects;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.initiativaromania.hartabanilorpublici.IRUserInterface.map.InfoActivity;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.map.MainActivity;
import com.initiativaromania.hartabanilorpublici.R;
import com.initiativaromania.hartabanilorpublici.IRObjects.Contract;
import com.initiativaromania.hartabanilorpublici.IRObjects.CommManager;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.statistics.StatisticsContractRowAdapter;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.statistics.StatisticsContractDetails;

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
    public static final String AUTHORITY_ACTIVITY_NAME  = "Institutie";
    public static final String CONTRACT_LIST_TYPE       = "contract list type";
    public static final String CONTRACT_LIST_EXTRA      = "contract list extra";
    public static final int CONTRACT_LIST_FOR_COMPANY   = 1;
    public static final int CONTRACT_LIST_FOR_BUYER     = 2;

    private int type;
    private String name;
    private LinkedList<Contract> contracts;
    private double totalValue = 0;

    private void initUI() {
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

         /* Information button */
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButtonContractList);
        if (imageButton != null) {
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     /* Start the info activity */
                    Intent intent = new Intent(getBaseContext(), InfoActivity.class);
                    startActivity(intent);
                }
            });
        }

        /* IR home button */
        ImageButton ir = (ImageButton)findViewById(R.id.imageViewContract);
        if (ir != null) {
            ir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Click IR");

                     /* Go to the homepage */
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.putExtra(MainActivity.EXTRA_DISPLAY_INFOGRAPHIC, false);
                    startActivity(intent);
                }
            });
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
        initUI();

        /* Get Contract List */
        if (type == CONTRACT_LIST_FOR_COMPANY)
            CommManager.requestCompanyDetails(this, name);
        else
            CommManager.requestBuyerDetails(this, name);
    }

}
