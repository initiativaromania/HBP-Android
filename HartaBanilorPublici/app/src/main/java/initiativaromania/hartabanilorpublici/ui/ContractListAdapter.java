package initiativaromania.hartabanilorpublici.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import initiativaromania.hartabanilorpublici.R;
import initiativaromania.hartabanilorpublici.comm.CommManager;
import initiativaromania.hartabanilorpublici.data.Contract;
import initiativaromania.hartabanilorpublici.data.ContractListItem;

/**
 * Created by claudiu on 9/12/17.
 */

public class ContractListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private final FragmentActivity context;
    private List<ContractListItem> contracts;
    private int parentID;

    public ContractListAdapter(FragmentActivity context, int parentID,
                               List<ContractListItem> contracts) {
        this.context = context;
        this.parentID = parentID;
        this.contracts = contracts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contract_list_item, null);
        }

        TextView contractName = (TextView) convertView.findViewById(R.id.listTitle);
        contractName.setText((position + 1) +". " + contracts.get(position).title);

        TextView contractValue = (TextView) convertView.findViewById(R.id.listPrice);
        contractValue.setText(contracts.get(position).price + " RON");

        TextView contractYear = (TextView) convertView.findViewById(R.id.listDate);
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date contractDate = null;
        try {
            if (contracts.get(position).date != null &&
                    contracts.get(position).date.equals("-") == false) {
                contractDate = parser.parse(contracts.get(position).date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(contractDate);
                contractYear.setText(calendar.get(Calendar.YEAR) + "");
            } else
                contractYear.setText("-");
        } catch (ParseException e) {
            contractYear.setText("-");
            e.printStackTrace();
        }


        convertView.setTag(contracts.get(position));

        return convertView;
    }

    @Override
    public int getCount() {
        return contracts.size();
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
        ContractListItem contractItem = (ContractListItem) v.getTag();

        System.out.println("Click on contract " + contractItem.id + " type " + contractItem.type);

        ContractFragment contractFragment = new ContractFragment();
        contractFragment.setParentID(parentID);

        final FragmentManager fragmentManager = context.getSupportFragmentManager();

        /* Build Fragment Arguments */
        Bundle bundle = new Bundle();
        bundle.putInt(CommManager.BUNDLE_CONTRACT_ID, contractItem.id);
        bundle.putInt(CommManager.BUNDLE_CONTRACT_TYPE, contractItem.type);
        bundle.putInt(CommManager.BUNDLE_CONTRACT_PI_ID, contractItem.pi != null ?
            contractItem.pi.id : 0);
        bundle.putString(CommManager.BUNDLE_CONTRACT_PI_NAME, contractItem.pi != null ?
                contractItem.pi.name : "");
        bundle.putInt(CommManager.BUNDLE_CONTRACT_COMP_ID, contractItem.company != null ?
                contractItem.company.id : 0);
        bundle.putString(CommManager.BUNDLE_CONTRACT_COMP_NAME, contractItem.company != null ?
                contractItem.company.name : "");
        contractFragment.setArguments(bundle);


        /* Got the Contract Fragment */
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(parentID, contractFragment)
                .addToBackStack(contractFragment.getClass().getName())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }
}
