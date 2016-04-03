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

package com.initiativaromania.hartabanilorpublici.IRUserInterface.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.initiativaromania.hartabanilorpublici.IRUserInterface.fragments.BuyerListFragment;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.fragments.CompanyListFragment;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.fragments.ParticipantViewPageFragment;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.fragments.ContractListFragment;
import com.initiativaromania.hartabanilorpublici.R;
import com.initiativaromania.hartabanilorpublici.IRData.Contract;
import com.initiativaromania.hartabanilorpublici.IRData.CommManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.LinkedList;

/**
 * Created by claudiu on 2/12/16.
 */
public class ParticipantActivity extends FragmentActivity {
    public static final String COMPANY_ACTIVITY_NAME = "Companie";
    public static final String AUTHORITY_ACTIVITY_NAME = "Institutie";
    public static final String CONTRACT_LIST_TYPE = "contract list type";
    public static final String CONTRACT_LIST_EXTRA = "contract list extra";
    public static final int CONTRACT_LIST_FOR_COMPANY = 1;
    public static final int CONTRACT_LIST_FOR_BUYER = 2;

    private int type;
    private String name;
    private LinkedList<Contract> contracts = new LinkedList<Contract>();
    private LinkedList<String> companies = new LinkedList<String>();
    private LinkedList<String> buyers = new LinkedList<String>();
    private double totalValue = 0;

    private ContractListFragment contractListFragment;
    private CompanyListFragment companyListFragment;
    private BuyerListFragment buyerListFragment;

    private void initUI() {
        Intent intent = getIntent();
        this.type = intent.getIntExtra(CONTRACT_LIST_TYPE, -1);

        System.out.println("Type of Contract list is " + this.type);
        name = intent.getStringExtra(CONTRACT_LIST_EXTRA);


        /* Determine if the activity is for a Company or for a Mayor Office */
        if (type == CONTRACT_LIST_FOR_COMPANY) {

            TextView tv = (TextView) findViewById(R.id.textViewContractList);
            if (tv != null)
                tv.setText(COMPANY_ACTIVITY_NAME);
        } else {
            TextView tv = (TextView) findViewById(R.id.textViewContractList);
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
        ImageButton ir = (ImageButton) findViewById(R.id.imageViewContract);
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

        TextView tv = (TextView) findViewById(R.id.textEntityName);
        if (tv != null)
            tv.setText(name);
    }


    /* Parse data, fill fragments, display info */
    private double displayContracts(JSONArray contractsJSON) {

        /* Get data from server's response */
        double totalValue = 0;

        try {
            for (int i = 0; i < contractsJSON.length(); i++) {
                JSONObject contractJSON = contractsJSON.getJSONObject(i);

                final Contract contract = new Contract();
                contract.id = Integer.parseInt(contractJSON.getString("id"));
                contract.title = contractJSON.getString("contract_title");
                contract.valueEUR = contractJSON.getString("price");

                totalValue += Double.parseDouble(contract.valueEUR);

                contracts.add(contract);

                System.out.println("Contract " + contract.id + ", title " + contract.title);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Fill the contract list fragment */
        contractListFragment = (ContractListFragment) ParticipantViewPageFragment.pageAdapter.fragments.get(0);
        if (contractListFragment != null) {
            contractListFragment.displayContracts(contracts);
        } else
            System.out.println("NULL contract list fragment");

        return totalValue;
    }


    /* Receive the list of contracts per company and display it */
    public void receiveCompanyDetails(JSONObject response) {

        /* Set total value of the contracts */
        try {
            totalValue = Double.parseDouble(response.getString("ordersSum"));
            DecimalFormat dm = new DecimalFormat("###,###.###");
            TextView tv = (TextView) findViewById(R.id.textTotalValue);
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
        TextView tv = (TextView) findViewById(R.id.textTotalValue);
        tv.setText(String.valueOf(dm.format(totalValue) + " EUR"));
    }


    /* Receive the list of firms for a buyer/authority */
    public void receiveFirmsForBuyer(JSONObject response) {

        System.out.println("Receiving firms for buyer");

        try {
            JSONArray firmsJSON = response.getJSONArray("firms");

            for (int i = 0; i < firmsJSON.length(); i++) {
                JSONObject firmJSON = firmsJSON.getJSONObject(i);

                companies.add(firmJSON.getString("name"));
                System.out.println("Received firm " + firmJSON.getString("name"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Fill the company list fragment */
        companyListFragment = (CompanyListFragment) ParticipantViewPageFragment.pageAdapter.fragments.get(1);
        if (companyListFragment != null) {
            companyListFragment.displayCompanies(companies);
        } else
            System.out.println("NULL company list fragment");
    }


    /* Receive the list of buyers for a firm/company */
    public void receiveBuyersForFirm(JSONObject response) {

        System.out.println("Receiving buyers for firm");

        try {
            JSONArray buyersJSON = response.getJSONArray("buyers");

            for (int i = 0; i < buyersJSON.length(); i++) {
                JSONObject buyerJSON = buyersJSON.getJSONObject(i);

                buyers.add(buyerJSON.getString("name"));
                System.out.println("Received buyer " + buyerJSON.getString("name"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Fill the company list fragment */
        buyerListFragment = (BuyerListFragment) ParticipantViewPageFragment.pageAdapter.fragments.get(1);
        if (buyerListFragment != null) {
            buyerListFragment.displayBuyers(buyers);
        } else
            System.out.println("NULL buyer list fragment");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant);

        System.out.println("On create ParticipanActivity");
        ParticipantViewPageFragment viewPageFragment = (ParticipantViewPageFragment)
                getSupportFragmentManager().findFragmentById(R.id.entity_info_fragment);
        if (viewPageFragment != null)
            System.out.println("Ok fragment");
        else
            System.out.println("Not ok fragment");


        /* Set header text views */
        initUI();

        /* Get Contract List */
        if (type == CONTRACT_LIST_FOR_COMPANY) {
            viewPageFragment.setViewPager(CONTRACT_LIST_FOR_COMPANY);
            CommManager.requestCompanyDetails(this, name);
            CommManager.requestBuyersForFirm(this, name);
        } else {
            viewPageFragment.setViewPager(CONTRACT_LIST_FOR_BUYER);
            CommManager.requestBuyerDetails(this, name);
            CommManager.requestFirmsForBuyer(this, name);
        }
    }
}