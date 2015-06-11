package cs446.mindme.Sent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;

import cs446.mindme.Adapters.ExpandableListAdapter;
import cs446.mindme.R;
import cs446.mindme.ReminderDataHolder;

public class ViewSent extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    ArrayList<ReminderDataHolder> reminderList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.view_sent, container, false);
        expListView = (ExpandableListView) rootView.findViewById(R.id.sent_list);

        prepareSampleData();

        listAdapter = new ExpandableListAdapter(rootView.getContext(), reminderList);

        expListView.setAdapter(listAdapter);

        // TODO: Figure out why it must be false to show child
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        // TODO: Remove if not needed
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int prevPosition = -1;
            int prevCount = listAdapter.getGroupCount();
            @Override
            public void onGroupExpand(int groupPosition) {
                /*Toast.makeText(rootView.getContext().getApplicationContext(),
                        reminderList.get(groupPosition).getMessage()
                                + " Expanded:" + groupPosition
                                + " Prev:" + prevPosition,
                        Toast.LENGTH_SHORT).show();*/
                // Allow only one reminder action to be view at a time
                if (prevPosition != groupPosition && prevPosition >= 0) {
                    /*Toast.makeText(rootView.getContext().getApplicationContext(),
                            reminderList.get(groupPosition).getMessage()
                                    + " prevCount:" + prevCount
                                    + " currCount:" + listAdapter.getGroupCount(),
                            Toast.LENGTH_SHORT).show();*/
                    if (prevCount == listAdapter.getGroupCount()) {
                        expListView.collapseGroup(prevPosition);
                    }
                    else {
                        prevCount = listAdapter.getGroupCount();
                    }
                }
                prevPosition = groupPosition;
            }
        });

        // When the child view collapses
        // TODO: Remove if not needed
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(rootView.getContext().getApplicationContext(),
                        reminderList.get(groupPosition).getMessage() + " Collapsed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // TODO: Child click listener - when the buttons are pressed, reminders should go away, etc
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return true;
            }});

        return rootView;
    }

    private void prepareSampleData() {
        reminderList = new ArrayList<ReminderDataHolder>();
        ReminderDataHolder r1 = new ReminderDataHolder(ReminderDataHolder.reminderType.SENT,
                "Message1", "Randy Cheung", "12:00", ReminderDataHolder.reminderStatus.ACTIVE);
        ReminderDataHolder r2 = new ReminderDataHolder(ReminderDataHolder.reminderType.SENT,
                "Message2", "Emily Na", "12:00", ReminderDataHolder.reminderStatus.COMPLETED);
        ReminderDataHolder r3 = new ReminderDataHolder(ReminderDataHolder.reminderType.SENT,
                "Message3", "Arthur Jen", "12:00", ReminderDataHolder.reminderStatus.DECLINED);
        ReminderDataHolder r4 = new ReminderDataHolder(ReminderDataHolder.reminderType.RECEIVED,
                "Message4", "Richard Fa", "12:00", ReminderDataHolder.reminderStatus.ACTIVE);
        reminderList.add(r1);
        reminderList.add(r2);
        reminderList.add(r3);
        reminderList.add(r4);
    }
}
