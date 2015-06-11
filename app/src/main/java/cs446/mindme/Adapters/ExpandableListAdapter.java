package cs446.mindme.Adapters;

import java.util.ArrayList;
import android.content.Context;
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

    public ExpandableListAdapter(Context context, ArrayList<ReminderDataHolder> reminderList) {
        this._context = context;
        this._reminderList = reminderList;
    }

    // TODO: Need to implement the buttons as child somehow
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // TODO: Need to implement the buttons as child somehow
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.reminder_action, null);
        }
        return convertView;
    }

    // TODO: Should have 3 child for RECEIVED, 2 for SENT, # for HISTORY
    @Override
    public int getChildrenCount(int groupPosition) {
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

        // Each reminder item should have a message, whom its from, and the timestamp
        TextView textViewMessage = (TextView) convertView.findViewById(R.id.reminder_text);
        String reminderMessage = (String) getGroup(groupPosition).getMessage();
        TextView textViewFrom = (TextView) convertView.findViewById(R.id.reminder_from_text);
        String reminderFrom = (String) getGroup(groupPosition).getFrom();
        TextView textViewTime = (TextView) convertView.findViewById(R.id.reminder_timestamp);
        String reminderTime = (String) getGroup(groupPosition).getTime();
        textViewMessage.setText(reminderMessage);
        textViewFrom.setText(reminderFrom);
        textViewTime.setText(reminderTime);

        // Only display the status if it's a HISTORY view
        // Only display the status if it's not ACTIVE, i.e. COMPLETED or DECLINED
        // TODO: Edge case when a reminder in HISTORY has ACTIVE status, not likely to happen
        // TODO: There may be a better to render this
        if (ReminderDataHolder.reminderType.HISTORY == getGroup(groupPosition).getType() &&
                ReminderDataHolder.reminderStatus.ACTIVE != getGroup(groupPosition).getStatus())
        {
            TextView textViewStatus = (TextView) convertView.findViewById(R.id.reminder_status);
            ReminderDataHolder.reminderStatus reminderStatus = (ReminderDataHolder.reminderStatus) getGroup(groupPosition).getStatus();
            textViewStatus.setText(reminderStatus.toString());
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {return false; }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }
}