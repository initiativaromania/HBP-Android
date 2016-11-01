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
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.initiativaromania.hartabanilorpublici.IRData.CommManager;
import com.initiativaromania.hartabanilorpublici.IRData.ICommManagerResponse;
import com.initiativaromania.hartabanilorpublici.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by claudiu on 2/27/16.
 */
public class InfoActivity extends Activity {

    public static final String TAG=InfoActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        /* IR home button */
        ImageButton ir = (ImageButton) findViewById(R.id.imageViewContract);
        if (ir != null) {
            ir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                     /* Go to the homepage */
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.putExtra(MainActivity.EXTRA_DISPLAY_INFOGRAPHIC, false);
                    startActivity(intent);
                }
            });
        }

        /* Get data for infographic from server*/
        CommManager.requestInitData(new ICommManagerResponse() {
            @Override
            public void processResponse(JSONObject response) {

                try {
                    mapResponseToView(response);
                } catch (JSONException e) {
                    Log.e(TAG,"Problems while mapping response",e);
                    e.printStackTrace();
                }
            }

            private void mapResponseToView(JSONObject response) throws JSONException {

                TextView tv = (TextView) findViewById(R.id.textViewNrBuyers);
                if (tv != null)
                    tv.setText(response.getString("buyers"));

                tv = (TextView) findViewById(R.id.textViewNrCompanies);
                if (tv != null)
                    tv.setText(response.getString("companies"));

                tv = (TextView) findViewById(R.id.textViewNrContracts);
                if (tv != null)
                    tv.setText(response.getString("contracts"));

                tv = (TextView) findViewById(R.id.textViewValueContracts);
                if (tv != null) {
                    double totalValue = Double.parseDouble(response.getString("sumprice"));
                    totalValue /= 1000000;
                    tv.setText(String.format("%.2f", totalValue));
                }

                tv = (TextView) findViewById(R.id.textViewNrJustifies);
                if (tv != null)
                    tv.setText(response.getString("justifies"));
            }

            @Override
            public void onErrorOccurred(String errorMsg) {
                Toast.makeText(InfoActivity.this, "Eroare conectare la server",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}
