package com.example.claudiu.investitiipublice.IRUserInterface.statistics;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.claudiu.initiativaromania.R;
import com.example.claudiu.investitiipublice.IRObjects.Contract;
import com.example.claudiu.investitiipublice.IRUserInterface.ContractActivity;

import java.util.List;

public class StatisticsContractRowAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private final Context context;
    private List<StatisticsOrderDetails> orders;

    public StatisticsContractRowAdapter(Context context, List<StatisticsOrderDetails> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.statistics_around_fragment_contract_list_row, null);
        }

        TextView contractName = (TextView) convertView.findViewById(R.id.statistics_around_row_contractName);
        contractName.setText((position + 1) +". " + orders.get(position).title);
        convertView.setTag(orders.get(position).id);

        return convertView;
    }

    @Override
    public int getCount() {
        return orders.size();
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
        Intent intent = new Intent(context, ContractActivity.class);
        Contract contract = new Contract();
        contract.id = (int) v.getTag();

        intent.putExtra(ContractActivity.EXTRA_CONTRACT_ID, contract);
        context.startActivity(intent);
    }
}
