package cs446.mindme.Adapters;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;

import cs446.mindme.DataHolders.EventDataHolder;
import cs446.mindme.DataHolders.ReminderDataHolder;

/**
 * Created by randy on 14/07/2015.
 */
public class EventsAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private ArrayList<EventDataHolder> _eventList;

    public EventsAdapter(Context context, ArrayList<EventDataHolder> eventList) {
        this._context = context;
        this._eventList = eventList;
    }

    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
