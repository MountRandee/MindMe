package cs446.mindme.Views;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import cs446.mindme.Adapters.EventsAdapter;
import cs446.mindme.DataHolders.EventDataHolder;
import cs446.mindme.DataRequest.EventRequest;
import cs446.mindme.R;

public class ViewEvent extends Fragment {

    EventsAdapter listAdapter;
    ExpandableListView expListView;
    ArrayList<EventDataHolder> eventList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        System.out.println("Creating Events fragment.");

        final View rootView = inflater.inflate(R.layout.view_events, container, false);
        expListView = (ExpandableListView) rootView.findViewById(R.id.event_list);

        // Must be true to show hide reminder view's actionbar
        setHasOptionsMenu(true);

        // Prepare the data
        String jsonAddress = EventRequest.buildAddress(null, -1);
        eventList = new ArrayList<EventDataHolder>();
        EventRequest eventConnection = new EventRequest();
        try {
            System.out.println("Getting response string ...");
            String responseString = eventConnection.execute(jsonAddress).get();
            eventList = eventConnection.buildEventList(responseString);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        listAdapter = new EventsAdapter(rootView.getContext(), eventList);
        expListView.setAdapter(listAdapter);

        // Must be false to enable child views
        // TODO: Figure out why???
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        // When event expanded, collapse the previous expanded event
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int prevPosition = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if (prevPosition != groupPosition) {
                    // This may be executed even if position doesn't exit.
                    // Initially didn't work when removing reminders, had to compare with list count.
                    // TODO: Why?
                    expListView.collapseGroup(prevPosition);
                }
                prevPosition = groupPosition;
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {}
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        System.out.println("Creating events menu.");
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        getActivity().getActionBar().setDisplayShowTitleEnabled(true);
        getActivity().getActionBar().setTitle("uWaterloo Events");
        return;
    }
}