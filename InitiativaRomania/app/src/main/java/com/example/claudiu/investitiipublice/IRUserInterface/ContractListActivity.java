package com.example.claudiu.investitiipublice.IRUserInterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.claudiu.initiativaromania.R;
import com.example.claudiu.investitiipublice.IRObjects.Company;
import com.example.claudiu.investitiipublice.IRObjects.Contract;
import com.example.claudiu.investitiipublice.IRObjects.CommManager;
import com.example.claudiu.investitiipublice.IRObjects.Primarie;

import java.util.LinkedList;

/**
 * Created by claudiu on 2/12/16.
 */
public class ContractListActivity extends Activity {
    public static final String COMPANY_ACTIVITY_NAME    = "Companie";
    public static final String MAYOR_OFFICE_ACTIVITY_NAME = "Primarie";
    public static final String CONTRACT_LIST_TYPE       = "contract list type";
    public static final String CONTRACT_LIST_EXTRA      = "contract list extra";
    public static final int CONTRACT_LIST_FOR_COMPANY   = 1;
    public static final int CONTRACT_LIST_FOR_MAYORY    = 2;

    private int type;
    private Company company = null;
    private Primarie primarie = null;
    private LinkedList<Contract> contracts;

    private void setTextViews() {
        Intent intent = getIntent();
        this.type = intent.getIntExtra(CONTRACT_LIST_TYPE, -1);

        System.out.println("Type of Contract list is " + this.type);


        /* Determine if the activity is for a Company or for a Mayor Office */
        if (type == CONTRACT_LIST_FOR_COMPANY) {
            company = (Company) intent.getSerializableExtra(CONTRACT_LIST_EXTRA);

            TextView tv = (TextView)findViewById(R.id.textViewContractList);
            if (tv != null)
                tv.setText(COMPANY_ACTIVITY_NAME);

            tv = (TextView)findViewById(R.id.textEntityName);
            if (tv != null)
                tv.setText(company.name);

        } else {
            primarie = (Primarie) intent.getSerializableExtra(CONTRACT_LIST_EXTRA);

            TextView tv = (TextView)findViewById(R.id.textViewContractList);
            if (tv != null)
                tv.setText(MAYOR_OFFICE_ACTIVITY_NAME);

            tv = (TextView)findViewById(R.id.textEntityName);
            if (tv != null)
                tv.setText(primarie.name);
        }
    }


    /* Set the contracts on the list view */
    private void setContracts() {
        ListView listView = (ListView)findViewById(R.id.listViewContracte);
        if (listView == null)
            return;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_contracts);

        /* Set header text views */
        setTextViews();


        /* Get the contracts */
        contracts = CommManager.getEntityContractList(company, CONTRACT_LIST_FOR_COMPANY);

        setContracts();
    }
}
