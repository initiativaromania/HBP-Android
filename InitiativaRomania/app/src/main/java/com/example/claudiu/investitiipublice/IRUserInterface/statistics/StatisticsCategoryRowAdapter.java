package com.example.claudiu.investitiipublice.IRUserInterface.statistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.claudiu.initiativaromania.R;

import java.util.List;

public class StatisticsCategoryRowAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private final Context context;
    private List<String> names;

    public StatisticsCategoryRowAdapter(Context context, List<String> names) {
        this.context = context;
        this.names = names;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.statistics_around_fragment_category_list_row, null);
        }

        TextView firmName = (TextView) convertView.findViewById(R.id.statistics_around_row_categoryName);
        firmName.setText(names.get(position));
        convertView.setTag(names.get(position));
        return convertView;
    }

    @Override
    public int getCount() {
        return names.size();
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
        Toast.makeText(context, v.getTag().toString(), Toast.LENGTH_SHORT).show();
    }

}
