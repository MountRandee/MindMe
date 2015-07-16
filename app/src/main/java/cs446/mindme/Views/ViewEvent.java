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
        System.out.println("Creating Events Fragment!");
        final View rootView = inflater.inflate(R.layout.view_events, container, false);
        expListView = (ExpandableListView) rootView.findViewById(R.id.event_list);
        setHasOptionsMenu(true);

        // Prepare the data
        String jsonAddress = EventRequest.buildAddress(null, -1);
        eventList = new ArrayList<EventDataHolder>();
        EventRequest eventConnection = new EventRequest();
        try {
            System.out.println("getting response string ...");
            String responseString = eventConnection.execute(jsonAddress).get();
            eventList = eventConnection.buildEventList(responseString);
            if (eventList.size() != 0) {
                for (int i = 0; i < eventList.size(); i++) {
                    System.out.println(eventList.get(i).get_title());
                }
            } else {
                System.out.println("eventList has size 0");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // System.out.println(responseString);
        // eventList = EventRequest.parseJSON(EventRequest.getJSON(jsonAddress));
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
        // TODO: Remove toast later
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int prevPosition = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                /*Toast.makeText(rootView.getContext().getApplicationContext(),
                        reminderList.get(groupPosition).getMessage()
                                + " Expanded:" + groupPosition
                                + " Prev:" + prevPosition,
                        Toast.LENGTH_SHORT).show();*/
                if (prevPosition != groupPosition) {
                   /* Toast.makeText(rootView.getContext().getApplicationContext(),
                            reminderList.get(groupPosition).getMessage()
                                    *//*+ " prevCount:" + prevCount*//*
                                    + " currCount:" + listAdapter.getGroupCount(),
                            Toast.LENGTH_SHORT).show();*/
                    // This may be executed even if position doesn't exit.
                    // Initially didn't work when removing reminders, had to compare with list count.
                    // TODO: Why?
                    expListView.collapseGroup(prevPosition);
                }
                prevPosition = groupPosition;
            }
        });

        // TODO: Remove if not needed
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
               /* Toast.makeText(rootView.getContext().getApplicationContext(),
                        reminderList.get(groupPosition).getMessage() + " Collapsed",
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        // Note: Event actions are implemented in the adapter.
        // TODO: Remove if unnecessary.
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