package com.initiativaromania.hartabanilorpublici.IRUserInterface.objects;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.initiativaromania.hartabanilorpublici.IRUserInterface.activities.ParticipantActivity;
import com.initiativaromania.hartabanilorpublici.R;

import java.util.List;

/**
 * Created by claudiu on 4/3/16.
 */
public class BuyerListAdapter  extends BaseAdapter implements AdapterView.OnItemClickListener {
    private final Context context;
    private List<String> buyerNames;

    public BuyerListAdapter(Context context, List<String> buyerNames) {
        this.context = context;
        this.buyerNames = buyerNames;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contract_list_item, null);
        }

        TextView buyerName = (TextView) convertView.findViewById(R.id.statistics_around_row_contractName);
        buyerName.setText((position + 1) + ". " + buyerNames.get(position));
        convertView.setTag(buyerNames.get(position));

        return convertView;
    }

    @Override
    public int getCount() {
        return buyerNames.size();
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
        Intent intent = new Intent(context, ParticipantActivity.class);
        String buyerName = (String)v.getTag();

        intent.putExtra(ParticipantActivity.CONTRACT_LIST_TYPE, ParticipantActivity.CONTRACT_LIST_FOR_BUYER);
        intent.putExtra(ParticipantActivity.CONTRACT_LIST_EXTRA, buyerName);
        context.startActivity(intent);
    }
}
