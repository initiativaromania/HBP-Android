package com.example.claudiu.investitiipublice.IRUserInterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.example.claudiu.initiativaromania.R;
import com.example.claudiu.investitiipublice.IRObjects.Contract;

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

        Intent intent = getIntent();
        contract = (Contract)intent.getSerializableExtra(EXTRA_CONTRACT_ID);

        System.out.println("Got contract " + contract.id);


        /* Set content based on the contract */
        TextView tv = (TextView)findViewById(R.id.textContractNr);
        if (tv != null)
            tv.setText(String.valueOf(contract.id));

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

        tv = (TextView)findViewById(R.id.textPrimarie);
        if (tv != null) {
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


        tv = (TextView)findViewById(R.id.textValue);
        if (tv != null)
            tv.setText(String.valueOf(contract.valueEUR) + " EUR");

        tv = (TextView)findViewById(R.id.textCategory);
        if (tv != null)
            tv.setText(contract.categories.getFirst().name);

        tv = (TextView)findViewById(R.id.textDescription);
        if (tv != null) {
            tv.setText(contract.description);
            tv.setMovementMethod(new ScrollingMovementMethod());
        }

    }
}
