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
import java.util.concurrent.ExecutionException;

import cs446.mindme.DataHolders.EventDataHolder;
import cs446.mindme.DataHolders.EventDetailsDataHolder;
import cs446.mindme.DataRequest.EventRequest;
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
        String eventDate = getGroup(groupPosition).get_startTimes().get(0).substring(0,10);

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
                EventDetailsDataHolder eventDetails = new EventDetailsDataHolder();
                // Get the event information from a JSON response
                String jsonAddress = EventRequest.buildAddress(getGroup(groupPosition).get_site(), getGroup(groupPosition).get_id());
                EventRequest eventConnection = new EventRequest();
                try {
                    String responseString = eventConnection.execute(jsonAddress).get();
                    eventDetails = eventConnection.buildEventInfo(responseString);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                // Build the dialog
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(_context);
                // Create the date and time string
                StringBuilder dateTimeSB = new StringBuilder();
                boolean equalSize = (eventDetails.get_startDate().size() == eventDetails.get_startTime().size());
                if (equalSize) {
                    for (int i = 0; i < eventDetails.get_startDate().size(); i++) {
                        dateTimeSB.append(eventDetails.get_startDate().get(i))
                                .append(" @ ")
                                .append(eventDetails.get_startTime().get(i))
                                .append("\n");
                    }
                }
                // Create the location string
                StringBuilder locationSB = new StringBuilder();
                locationSB.append(eventDetails.get_locationName())
                        .append("\n")
                        .append(eventDetails.get_locationStreet())
                        .append("\n")
                        .append(eventDetails.get_locationCity())
                        .append("\n")
                        .append(eventDetails.get_locationProvince())
                        .append("\n")
                        .append(eventDetails.get_locationPostal())
                        .append("\n");
                // Build the dialog message
                StringBuilder dialogMessage = new StringBuilder();
                dialogMessage.append("Description:\n")
                        .append(eventDetails.get_description() + "\n\n")
                        .append("Dates and Times:\n")
                        .append(dateTimeSB.toString() + "\n")
                        .append("Location:\n")
                        .append(locationSB.toString() + "\n")
                        .append("Link:\n")
                        .append(eventDetails.get_link() + "\n");

                dialogBuilder.setMessage(dialogMessage.toString());
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
