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

package com.initiativaromania.hartabanilorpublici.IRUserInterface.statistics;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.initiativaromania.hartabanilorpublici.R;
import com.initiativaromania.hartabanilorpublici.IRUserInterface.ContractListActivity;

import java.util.List;

public class StatisticsCompanyAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private final Context context;
    private List<String> companyNames;

    public StatisticsCompanyAdapter(Context context, List<String> companyNames) {
        this.context = context;
        this.companyNames = companyNames;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.statistics_around_fragment_contract_list_row, null);
        }

        TextView companyName = (TextView) convertView.findViewById(R.id.statistics_around_row_contractName);
        companyName.setText((position + 1) + ". " + companyNames.get(position));
        convertView.setTag(companyNames.get(position));

        return convertView;
    }

    @Override
    public int getCount() {
        return companyNames.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Intent intent = new Intent(context, ContractListActivity.class);
        String companyName = (String)v.getTag();

        intent.putExtra(ContractListActivity.CONTRACT_LIST_TYPE, ContractListActivity.CONTRACT_LIST_FOR_COMPANY);
        intent.putExtra(ContractListActivity.CONTRACT_LIST_EXTRA, companyName);
        context.startActivity(intent);
    }
}