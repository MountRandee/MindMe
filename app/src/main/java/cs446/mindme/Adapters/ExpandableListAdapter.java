package cs446.mindme.Adapters;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
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
    public void removeGroup(int groupPosition, View parent)
    {
        _reminderList.remove(groupPosition);
        notifyDataSetChanged();

        // NotifyDataSetChanged does not update views, must collapse child with the groupPosition
        if (groupPosition < getGroupCount() + 1) {
            // Parent is the R.id.received_list/sent_list
            ExpandableListView v = (ExpandableListView) parent;
            v.collapseGroup(groupPosition);
        }
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, final ViewGroup parent) {
        ReminderDataHolder.reminderType rType = getGroup(groupPosition).getType();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (ReminderDataHolder.reminderType.RECEIVED == rType) {
                convertView = infalInflater.inflate(R.layout.reminder_actions_for_received, null);
            } else if (ReminderDataHolder.reminderType.SENT == rType) {
                convertView = infalInflater.inflate(R.layout.reminder_actions_for_sent, null);
            } else {
                convertView = infalInflater.inflate(R.layout.empty_layout, null);
            }
        }

        // When the Complete/Decline/Cancel button is clicked, it should remove the reminder.
        // When the Edit button is clicked, user should be able to edit the reminder.
        // TODO: The reminder should be added to HISTORY after complete/decline/cancel.
        // TODO: History View has no reminder actions temporarily.
        if (ReminderDataHolder.reminderType.HISTORY != rType) {

            // Received and Sent reminders have an Edit button
            Button buttonEdit = (Button) convertView.findViewById(R.id.button_edit);
            final AlertDialog.Builder editableDialog = new AlertDialog.Builder(_context);
            final EditText editText = new EditText(_context);
            editableDialog.setMessage(getGroup(groupPosition).getMessage());
            editableDialog.setTitle("Edit");
            editableDialog.setView(editText);
            editableDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO: change the group
                }
            });
            editableDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    // Do nothing
                }
            });
            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Getting weird error:
                    // The specified child already has a parent. You must call removeView() on the child's parent first.
                    // Reproduce: Click Edit, Click Ok/Cancel, Click Edit again
                    editableDialog.show();
                }
            });

            if (ReminderDataHolder.reminderType.RECEIVED == rType) {
                Button buttonComplete = (Button) convertView.findViewById(R.id.button_complete);
                Button buttonDecline = (Button) convertView.findViewById(R.id.button_decline);
                // Received reminders also have Complete and Decline buttons
                buttonComplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeGroup(groupPosition, parent);
                    }
                });
                buttonDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeGroup(groupPosition, parent);
                    }
                });

            } else {
                // Sent reminders also have Cancel buttons
                Button buttonCancel = (Button) convertView.findViewById(R.id.button_cancel);
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        removeGroup(groupPosition, parent);
                    }
                });
            }
        }

        return convertView;
    }

    // TODO: Need to implement the buttons as child somehow
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return null;
    }

    // Note: 1 represents the entire xml, so 2 creates 6 buttons.
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