package initiativaromania.hartabanilorpublici;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by adriana on 30.08.2017.
 */

public class ExpandableSearchListAdapter implements ExpandableListAdapter {
    private Context context;
    private List<String> listHeader;
    private HashMap<String, List<String>> listItems;
    private static final String TAG = "MyActivity";

    public ExpandableSearchListAdapter(Context searchContext, List<String> searchHeaders,
                                       HashMap<String, List<String>> searchItems) {
        this.context    = searchContext;
        this.listHeader = searchHeaders;
        this.listItems  = searchItems;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getGroupCount() {
        return this.listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupNo) {
        return this.listItems.get(this.listHeader.get(groupNo)).size();
    }

    @Override
    public Object getGroup(int groupNo) {
        return this.listHeader.get(groupNo);
    }

    @Override
    public Object getChild(int groupNo, int childNo) {
        return this.listItems.get(this.listHeader.get(groupNo)).get(childNo);
    }

    @Override
    public long getGroupId(int groupNo) {
        return groupNo;
    }

    @Override
    public long getChildId(int groupNo, int childNo) {
        return childNo;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupNo, boolean isExpanded, View view, ViewGroup parentView) {
        Log.d(TAG, " List ADAPTER=== get group: " + getGroup(groupNo).toString());

        String headerTitle = (String) getGroup(groupNo);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView listName = (TextView) view.findViewById(R.id.listName);
        listName.setTypeface(null, Typeface.BOLD);
        listName.setText(headerTitle);

        return view;
    }

    @Override
    public View getChildView(int groupNo, final int childNo,
                             boolean isLastChild, View view, ViewGroup parentView) {

        final String childText = (String) getChild(groupNo, childNo);

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) view
                .findViewById(R.id.listItem);

        txtListChild.setText(childText);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int i) {

    }

    @Override
    public void onGroupCollapsed(int i) {

    }

    @Override
    public long getCombinedChildId(long l, long l1) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long l) {
        return 0;
    }
}
