package initiativaromania.hartabanilorpublici.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import initiativaromania.hartabanilorpublici.data.CompanyListItem;
import initiativaromania.hartabanilorpublici.data.InstitutionListItem;

/**
 * Created by claudiu on 25/11/17.
 */

public class InstitutionListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private final FragmentActivity context;
    private List<InstitutionListItem> pis;
    private int parentID;

    public InstitutionListAdapter(FragmentActivity context, int parentID,
                                  List<InstitutionListItem> companies) {
        this.context = context;
        this.parentID = parentID;
        this.pis = companies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.institution_list_item, null);
        }

        TextView piName = (TextView) convertView.findViewById(R.id.institutionListName);
        piName.setText((position + 1) + ". " + pis.get(position).name);

        convertView.setTag(pis.get(position));

        return convertView;
    }

    @Override
    public int getCount() {
        return pis.size();
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

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        InstitutionListItem piItem = (InstitutionListItem) v.getTag();

        System.out.println("Click on pi " + piItem.id + " name " + piItem.name);

        System.out.println("Test1 " + context.getComponentName() + " " + context.getLocalClassName());
        Fragment piFragment = new InstitutionFragment();
        System.out.println("Test2");
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        //FragmentManager fragmentManager = frag.getChildFragmentManager();

        System.out.println("Test3");
        /* Build Fragment Arguments */
        Bundle bundle = new Bundle();
        bundle.putInt(CommManager.BUNDLE_PI_ID, piItem.id);
        bundle.putString(CommManager.BUNDLE_PI_NAME, piItem.name);
        bundle.putInt(CommManager.BUNDLE_INST_TYPE,
                InstitutionFragment.CONTRACT_LIST_FOR_PUBLIC_INSTITUTION);

        System.out.println("Test6");
        piFragment.setArguments(bundle);

        System.out.println("Test7");
        /* Got the Company Fragment */
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        System.out.println("Test8 " + parentID + " " + R.id.fragment_institution_layout);
        transaction.add(parentID, piFragment)
                .addToBackStack(piFragment.getClass().getName())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }
}
