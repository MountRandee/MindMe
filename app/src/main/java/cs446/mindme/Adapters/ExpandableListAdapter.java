package cs446.mindme.Adapters;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
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

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
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

        // Only display the status if it's a HISTORY view, and
        // Only display the status if it's not ACTIVE, i.e. COMPLETED or DECLINED
        // TODO: Edge case when a reminder in HISTORY has ACTIVE status, not likely to happen
        // TODO: There may be a better way to do this
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
    public ReminderDataHolder getGroup(int groupPosition) {
        return this._reminderList.get(groupPosition);
    }

    @Override
    public int getGroupCount() { return this._reminderList.size(); }

    @Override
    public long getGroupId(int groupPosition) { return groupPosition; }

    // Removes the reminder from the list
    public void removeGroup(int groupPosition)
    {
        _reminderList.remove(groupPosition);
        notifyDataSetChanged();
    }

    // TODO: Need to implement the buttons as child somehow
    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.reminder_action, null);
        }

        if (ReminderDataHolder.reminderType.HISTORY != getGroup(groupPosition).getType()) {
            Button buttonCompleted = (Button) convertView.findViewById(R.id.button_finished);
            buttonCompleted.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeGroup(groupPosition);
                }
            });
        }

        return convertView;
    }

    // TODO: Need to implement the buttons as child somehow
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return null;
    }

    // Note: 1 represents the entire reminder_action.xml, so 2 creates 6 buttons.
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) { return childPosition; }

    @Override
    public boolean hasStableIds() {return false; }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }
}