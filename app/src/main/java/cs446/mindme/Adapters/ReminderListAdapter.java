package cs446.mindme.Adapters;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.widget.Toast;
import java.util.ArrayList;
import com.facebook.login.widget.ProfilePictureView;

import cs446.mindme.ConnectionData;
import cs446.mindme.R;
import cs446.mindme.DataHolders.ReminderDataHolder;
import cs446.mindme.SampleData;

public class ReminderListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<ReminderDataHolder> _reminderList;

    public ReminderListAdapter(Context context, ArrayList<ReminderDataHolder> reminderList) {
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
        TextView textViewFrom = (TextView) convertView.findViewById(R.id.reminder_from_text);
        TextView textViewTime = (TextView) convertView.findViewById(R.id.reminder_timestamp);
        String reminderMessage =  getGroup(groupPosition).getMessage();
        String reminderFrom =  getGroup(groupPosition).getFrom().name;
        String reminderTime = getGroup(groupPosition).getTime();
        textViewMessage.setText(reminderMessage);
        textViewFrom.setText(reminderFrom);
        textViewTime.setText(reminderTime);
        ProfilePictureView profilePictureView;
        profilePictureView = (ProfilePictureView) convertView.findViewById(R.id.user_icon);
        profilePictureView.setProfileId(getGroup(groupPosition).getFrom().id);

        // Only display the status if it's a HISTORY view, and
        // Only display the status if it's not ACTIVE, i.e. COMPLETED or DECLINED
        // TODO: Edge case when a reminder in HISTORY has ACTIVE status, not likely to happen
        // TODO: There may be a better way to do this
        if (ReminderDataHolder.reminderType.HISTORY == getGroup(groupPosition).getType() &&
                ReminderDataHolder.reminderStatus.ACTIVE != getGroup(groupPosition).getStatus())
        {
            ReminderDataHolder.reminderStatus reminderStatus = getGroup(groupPosition).getStatus();
            TextView textViewStatus = (TextView) convertView.findViewById(R.id.reminder_status);
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

    // TODO: Implementation with API Call
    // Removes the reminder from the list
    public void removeGroup(int groupPosition, ReminderDataHolder.reminderStatus status, View parent)
    {
        if (getGroup(groupPosition).getType() == ReminderDataHolder.reminderType.RECEIVED) {
            SampleData.addToHistory(0, status, groupPosition);
        } else if (getGroup(groupPosition).getType() == ReminderDataHolder.reminderType.SENT) {
            SampleData.addToHistory(1, status, groupPosition);
        }
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

        // The reminder type determines the child view
        ReminderDataHolder.reminderType rType = getGroup(groupPosition).getType();

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (ReminderDataHolder.reminderType.RECEIVED == rType) {
                convertView = layoutInflater.inflate(R.layout.reminder_actions_for_received, null);
            } else if (ReminderDataHolder.reminderType.SENT == rType) {
                convertView = layoutInflater.inflate(R.layout.reminder_actions_for_sent, null);
            } else {
                convertView = layoutInflater.inflate(R.layout.empty_layout, null);
            }
        }

        // When the Complete/Decline/Cancel button is clicked, it should remove the reminder.
        // When the Edit button is clicked, user should be able to edit the reminder.
        if (ReminderDataHolder.reminderType.HISTORY != rType) {

            // Received and Sent reminders have an Edit button
            Button buttonEdit = (Button) convertView.findViewById(R.id.button_edit);
            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Must always create a new dialog
                    final AlertDialog.Builder editableDialog = new AlertDialog.Builder(_context);
                    editableDialog.setMessage(getGroup(groupPosition).getMessage());
                    editableDialog.setTitle("Edit");
                    final EditText editText = new EditText(_context);
                    editText.setText(getGroup(groupPosition).getMessage());
                    editableDialog.setView(editText);
                    editableDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        // Check if the new reminder message is empty
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String changedMessage = editText.getText().toString();
                            if (changedMessage.isEmpty()) {
                                Toast.makeText(_context, "Reminder message empty !", Toast.LENGTH_SHORT).show();
                            } else {
                                getGroup(groupPosition).set_message(changedMessage);
                                notifyDataSetChanged();
                                HashMap<String, String> params = new HashMap<String, String>();
                                params.put("message_id", getGroup(groupPosition).getID());
                                params.put("new_status", "active");
                                params.put("message", changedMessage);
                                ConnectionData.post("/api/v1/reminder/update/", params, false);
                            }
                        }
                    });
                    editableDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            // Do nothing
                        }
                    });
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
                        removeGroup(groupPosition, ReminderDataHolder.reminderStatus.COMPLETED, parent);
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("message_id", getGroup(groupPosition).getID());
                        params.put("new_status", "complete");
                        params.put("message", getGroup(groupPosition).getMessage());
                        ConnectionData.post("/api/v1/reminder/update/", params, false);
                    }
                });
                buttonDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeGroup(groupPosition, ReminderDataHolder.reminderStatus.DECLINED, parent);
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("message_id", getGroup(groupPosition).getID());
                        params.put("new_status", "decline");
                        params.put("message", getGroup(groupPosition).getMessage());
                        ConnectionData.post("/api/v1/reminder/update/", params, false);
                    }
                });

            } else {
                // Sent reminders also have Cancel buttons
                Button buttonCancel = (Button) convertView.findViewById(R.id.button_cancel);
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        removeGroup(groupPosition, ReminderDataHolder.reminderStatus.CANCELLED, parent);
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("message_id", getGroup(groupPosition).getID());
                        params.put("new_status", "canceled");
                        params.put("message", getGroup(groupPosition).getMessage());
                        ConnectionData.post("/api/v1/reminder/update/", params, false);
                    }
                });
            }
        }

        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
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