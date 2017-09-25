package initiativaromania.hartabanilorpublici.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import initiativaromania.hartabanilorpublici.R;
import initiativaromania.hartabanilorpublici.data.ContractListItem;

/**
 * Created by claudiu on 9/12/17.
 */

public class ContractListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private final Context context;
    private List<ContractListItem> contracts;

    public ContractListAdapter(Context context, List<ContractListItem> contracts) {
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
//        Intent intent = new Intent(context, ContractActivity.class);
//        Contract contract = new Contract();
//        contract.id = (int) v.getTag();
//
//        intent.putExtra(ContractActivity.EXTRA_CONTRACT_ID, contract);
//        context.startActivity(intent);
    }
}
