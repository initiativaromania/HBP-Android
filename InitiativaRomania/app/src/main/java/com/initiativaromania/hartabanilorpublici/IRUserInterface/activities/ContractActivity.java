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

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import 	android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
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
import com.initiativaromania.hartabanilorpublici.IRUserInterface.fragments.ContractPageFragment;
import com.initiativaromania.hartabanilorpublici.R;
import com.initiativaromania.hartabanilorpublici.IRData.Category;
import com.initiativaromania.hartabanilorpublici.IRData.CommManager;
import com.initiativaromania.hartabanilorpublici.IRData.Company;
import com.initiativaromania.hartabanilorpublici.IRData.Contract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;


/**
 * Created by claudiu on 2/10/16.
 */
public class ContractActivity extends Activity {
    public static final String TAG=ContractActivity.class.getName();

    public static final String INITIAL_POSITION    = "initial.position";
    public static final String CONTRACTS_AROUND_IDS = "contracts.around.ids";


    private List<Integer> contractsAroundIds ;
    private Integer initialPosition;
    private Context contractContext = this;
    private ViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);

        /* Get the contract only with the id from the intent */
        Intent intent = getIntent();
        initialPosition =  intent.getIntExtra(INITIAL_POSITION,1);
        contractsAroundIds = (List<Integer>) intent.getIntegerArrayListExtra(CONTRACTS_AROUND_IDS);

        Log.i(TAG,"Got initial contract " +  initialPosition);

        setNavigateToMainActivityButtonListener();

        setContractInformationButtonListener();

        pager = (ViewPager ) findViewById(R.id.contract_pager);

        pager.setAdapter(new ContractPageAdapter(getFragmentManager()));
        pager.setCurrentItem(initialPosition);
    }

    private void setNavigateToMainActivityButtonListener() {
    /* IR home button */
        ImageButton ir = (ImageButton)findViewById(R.id.imageViewContract);
        if (ir != null) {
            ir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG,"Click IR");

                     /* Go to the homepage */
                    Intent intent = new Intent(ContractActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.EXTRA_DISPLAY_INFOGRAPHIC, false);
                    startActivity(intent);
                }
            });
        }
    }

    private void setContractInformationButtonListener() {
    /* Information button */
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButtonContract);
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
    }

    class ContractPageAdapter extends FragmentStatePagerAdapter {

        public ContractPageAdapter(FragmentManager fragmentManager ) {
            super(fragmentManager );
        }


        @Override
        public Fragment getItem(int position) {

            Log.i(TAG,"Getting contract id:" + contractsAroundIds.get(position));
            return    ContractPageFragment.newInstance(contractsAroundIds.get(position)) ;
        }

        @Override
        public int getCount() {
            return contractsAroundIds.size();
        }
    }
}
