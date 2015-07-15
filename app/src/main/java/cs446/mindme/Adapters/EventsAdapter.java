package cs446.mindme.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import cs446.mindme.DataHolders.EventDataHolder;
import cs446.mindme.R;

public class EventsAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private ArrayList<EventDataHolder> _eventList;

    public EventsAdapter(Context context, ArrayList<EventDataHolder> eventList) {
        this._context = context;
        this._eventList = eventList;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.event_item, null);
        }

        // Each event item should have a title and a date/timestamp
        TextView textViewTitle = (TextView) convertView.findViewById(R.id.text_event_title);
        TextView textViewDate = (TextView) convertView.findViewById(R.id.text_event_date);
        String eventTitle = getGroup(groupPosition).get_title();
        String eventDate = getGroup(groupPosition).get_startTimes().get(0);
        textViewTitle.setText(eventTitle);
        textViewDate.setText(eventDate);
        return convertView;
    }

    @Override
    public EventDataHolder getGroup(int groupPosition) {
        return this._eventList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._eventList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.event_actions, null);
        }

        // More info action
        // Send reminder action
        Button buttonInfo = (Button) convertView.findViewById(R.id.button_event_info);
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(_context);
                dialogBuilder.setMessage(getGroup(groupPosition).get_description());
                dialogBuilder.setTitle(getGroup(groupPosition).get_title());
                // final TextView dialogText = new TextView(_context);
                dialogBuilder.show();


            }
        });

        // Create reminder action
        Button buttonCreateReminder = (Button) convertView.findViewById(R.id.button_event_create_reminder);
        buttonCreateReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Send reminder action
        Button buttonSendReminder = (Button) convertView.findViewById(R.id.button_event_send_reminder);
        buttonSendReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


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
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
