package com.example.claudiu.investitiipublice.IRUserInterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.example.claudiu.initiativaromania.R;
import com.example.claudiu.investitiipublice.IRObjects.Category;
import com.example.claudiu.investitiipublice.IRObjects.CommManager;
import com.example.claudiu.investitiipublice.IRObjects.Company;
import com.example.claudiu.investitiipublice.IRObjects.Contract;
import com.example.claudiu.investitiipublice.IRObjects.Primarie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by claudiu on 2/10/16.
 */
public class ContractActivity extends Activity {
    public static final String EXTRA_CONTRACT_ID    = "com.example.claudiu.investitiipublice.IRObjects.Contract";
    private Contract contract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);

        /* Get the contract only with the id from the intent */
        Intent intent = getIntent();
        contract = (Contract)intent.getSerializableExtra(EXTRA_CONTRACT_ID);

        System.out.println("Got contract " + contract.id);

        /* Send request to server to get all the contract details */
        CommManager.requestContract(this,contract.id);
    }


    /* Show contract details in the contract view */
    private void displayContract() {

        /* Show contract title */
        TextView tv = (TextView)findViewById(R.id.textViewTitluContract);
        if (tv != null)
            tv.setText(contract.title);

        /* Show Contract number */
        tv = (TextView)findViewById(R.id.textContractNr);
        if (tv != null)
            tv.setText(contract.number);

        /* Show company */
        tv = (TextView)findViewById(R.id.textCompany);
        if (tv != null) {
            tv.setText(contract.company.name);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Click on company");

                    /* Start a separate view for a company */
                    Intent intent = new Intent(getBaseContext(), ContractListActivity.class);
                    intent.putExtra(ContractListActivity.CONTRACT_LIST_TYPE, ContractListActivity.CONTRACT_LIST_FOR_COMPANY);
                    intent.putExtra(ContractListActivity.CONTRACT_LIST_EXTRA, contract.company);
                    startActivity(intent);
                }
            });
        }

        /* Show the mayor office */
        tv = (TextView)findViewById(R.id.textPrimarie);
        if (tv != null) {
            contract.primarie.name = "Primaria Mun. Bucuresti";
            tv.setText(contract.primarie.name);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Click on primarie");

                    /* Start a separate view for a company */
                    Intent intent = new Intent(getBaseContext(), ContractListActivity.class);
                    intent.putExtra(ContractListActivity.CONTRACT_LIST_TYPE, ContractListActivity.CONTRACT_LIST_FOR_MAYORY);
                    intent.putExtra(ContractListActivity.CONTRACT_LIST_EXTRA, contract.primarie);
                    startActivity(intent);
                }
            });
        }

        /* Show the date of the contract */
        tv = (TextView)findViewById(R.id.textData);
        if (tv != null)
            tv.setText(contract.date);

        /* Show the value of the contract */
        tv = (TextView)findViewById(R.id.textValue);
        if (tv != null)
            tv.setText(String.valueOf(contract.valueEUR) + " EUR");

        /* Show the contract's CPV code */
        tv = (TextView)findViewById(R.id.textCPV);
        if (tv != null)
            tv.setText(contract.CPVCode);

        /* Show the contract's categories */
        tv = (TextView)findViewById(R.id.textCategory);
        if (tv != null)
            tv.setText(contract.categories.getFirst().name);
    }


    /* Receive the contract details from the server and display them */
    public void receiveContract(JSONObject response) {

        try {
            contract.CPVCode = response.getString("CPVCode");
            contract.address = response.getString("address");
            contract.company = new Company();
            contract.primarie = new Primarie();
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
    }
}
