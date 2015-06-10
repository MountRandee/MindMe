package cs446.mindme.Adapters;

import java.util.ArrayList;
/*import java.util.HashMap;
import java.util.List;*/

import android.content.Context;
/*import android.graphics.Typeface;*/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import cs446.mindme.R;
import cs446.mindme.ReminderDataHolder;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<ReminderDataHolder> _reminderList;

    // child data in format of header title, child title
    /*private List<String> _listDataHeader; // header titles
    private HashMap<String, List<String>> _listDataChild;*/

    public ExpandableListAdapter(Context context, ArrayList<ReminderDataHolder> reminderList) {
        this._context = context;
        this._reminderList = reminderList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        /*return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);*/
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.reminder_action, null);
        }
        return convertView;
        /*final String childText = (String) getChild(groupPosition, childPosition);*/
       /* TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);*/

        /*txtListChild.setText(childText);*/
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        /*return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();*/
        return 1;
    }

    @Override
    public ReminderDataHolder getGroup(int groupPosition) {
        return this._reminderList.get(groupPosition);
    }

    @Override
    public int getGroupCount() { return this._reminderList.size(); }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.reminder_item, null);
        }

        TextView textViewMessage = (TextView) convertView.findViewById(R.id.reminder_text);
        String reminderMessage = (String) getGroup(groupPosition).getMessage();
        TextView textViewFrom = (TextView) convertView.findViewById(R.id.reminder_from_text);
        String reminderFrom = (String) getGroup(groupPosition).getFrom();
        TextView textViewTime = (TextView) convertView.findViewById(R.id.reminder_timestamp);
        String reminderTime = (String) getGroup(groupPosition).getTime();

        /*lblListHeader.setTypeface(null, Typeface.BOLD);*/
        textViewMessage.setText(reminderMessage);
        textViewFrom.setText(reminderFrom);
        textViewTime.setText(reminderTime);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}