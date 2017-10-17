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

import java.util.List;

import initiativaromania.hartabanilorpublici.R;
import initiativaromania.hartabanilorpublici.comm.CommManager;
import initiativaromania.hartabanilorpublici.data.ContractListItem;

/**
 * Created by claudiu on 9/12/17.
 */

public class ContractListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private final FragmentActivity context;
    private List<ContractListItem> contracts;

    public ContractListAdapter(FragmentActivity context, List<ContractListItem> contracts) {
        this.context = context;
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

        convertView.setTag(contracts.get(position).id);

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
        System.out.println("Click on contract");

        Fragment contractFragment = new ContractFragment();
        FragmentManager fragmentManager = context.getSupportFragmentManager();

        /* Build Fragment Arguments */
//        Bundle bundle = new Bundle();
//        bundle.putInt(CommManager.BUNDLE_PI_ID, clickedPI.id);
//        bundle.putInt(CommManager.BUNDLE_INST_TYPE, InstitutionFragment.CONTRACT_LIST_FOR_PUBLIC_INSTITUTION);
//        bundle.putString(CommManager.BUNDLE_PI_NAME, clickedPI.name);
//        bundle.putInt(CommManager.BUNDLE_PI_ACQS, clickedPI.directAcqs);
//        bundle.putInt(CommManager.BUNDLE_PI_TENDERS, clickedPI.tenders);
//        contractFragment.setArguments(bundle);

        /* Got the Contract Fragment */
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_institution_layout, contractFragment).addToBackStack("TAG")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }
}
