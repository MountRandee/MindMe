package cs446.mindme.Views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import cs446.mindme.Adapters.ReminderListAdapter;
import cs446.mindme.ConnectionData;
import cs446.mindme.R;
import cs446.mindme.DataHolders.ReminderDataHolder;
import cs446.mindme.SampleData;

public class ViewHistory extends Fragment {

    public static ViewHistory viewHistory;
    public static ViewHistory getViewHistory() { return viewHistory; }
    ReminderListAdapter listAdapter;
    ExpandableListView expListView;
    ArrayList<ReminderDataHolder> reminderList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        System.out.println("Creating history fragment.");
        viewHistory = this;
        final View rootView = inflater.inflate(R.layout.view_history, container, false);
        expListView = (ExpandableListView) rootView.findViewById(R.id.history_list);

        reminderList = SampleData.getHistoryList();
        listAdapter = new ReminderListAdapter(rootView.getContext(), reminderList);
        expListView.setAdapter(listAdapter);
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (listAdapter != null && this.isVisible()) {
            listAdapter.notifyDataSetChanged();
        }
    }

    public void notifyDataSetChanged() {
        try {
            listAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            ConnectionData.showToast(e.getLocalizedMessage(), ConnectionData.callType.LOAD_REMINDERS);
        }
    }
}
