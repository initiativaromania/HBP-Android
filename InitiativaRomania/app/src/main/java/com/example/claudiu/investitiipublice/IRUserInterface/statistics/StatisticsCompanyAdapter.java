package com.example.claudiu.investitiipublice.IRUserInterface.statistics;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.claudiu.initiativaromania.R;
import com.example.claudiu.investitiipublice.IRObjects.Contract;
import com.example.claudiu.investitiipublice.IRUserInterface.ContractActivity;
import com.example.claudiu.investitiipublice.IRUserInterface.ContractListActivity;

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