package cs446.mindme.History;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import cs446.mindme.Adapters.ExpandableListAdapter;
import cs446.mindme.R;
import cs446.mindme.ReminderDataHolder;

public class ViewHistory extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    ArrayList<ReminderDataHolder> reminderList = new ArrayList<ReminderDataHolder>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.view_history, container, false);
        expListView = (ExpandableListView) rootView.findViewById(R.id.history_list);
        prepareSampleData();
        listAdapter = new ExpandableListAdapter(rootView.getContext(), reminderList);

        expListView.setAdapter(listAdapter);

        // TODO: Temporarily true to disable actions for the reminders in HISTORY
        // TODO: Decide if any actions are needed for reminders in HISTORY
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        return rootView;
    }

   private void prepareSampleData() {
        reminderList = new ArrayList<ReminderDataHolder>();
        ReminderDataHolder r1 = new ReminderDataHolder(ReminderDataHolder.reminderType.HISTORY,
                "Message1", "Randy Cheung", "12:00", ReminderDataHolder.reminderStatus.COMPLETED);
        ReminderDataHolder r2 = new ReminderDataHolder(ReminderDataHolder.reminderType.HISTORY,
                "Message2", "Emily Na", "12:00", ReminderDataHolder.reminderStatus.COMPLETED);
        ReminderDataHolder r3 = new ReminderDataHolder(ReminderDataHolder.reminderType.HISTORY,
                "Message3", "Arthur Jen", "12:00", ReminderDataHolder.reminderStatus.DECLINED);
        ReminderDataHolder r4 = new ReminderDataHolder(ReminderDataHolder.reminderType.HISTORY,
                "Message4", "Richard Fa", "12:00", ReminderDataHolder.reminderStatus.ACTIVE);
        reminderList.add(r1);
        reminderList.add(r2);
        reminderList.add(r3);
        reminderList.add(r4);
    }
}
