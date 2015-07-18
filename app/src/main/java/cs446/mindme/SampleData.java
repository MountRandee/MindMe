package cs446.mindme;

import android.text.format.DateUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import cs446.mindme.DataHolders.ReminderDataHolder;

public class SampleData {

    static ArrayList<ReminderDataHolder> receivedList = new ArrayList<ReminderDataHolder>();
    static ArrayList<ReminderDataHolder> sentList = new ArrayList<ReminderDataHolder>();
    static ArrayList<ReminderDataHolder> historyList = new ArrayList<ReminderDataHolder>();

    static public void sortLists() {
        Collections.sort(receivedList);
        Collections.sort(sentList);
        Collections.sort(historyList);
    }

    static public void removeItem(int listId, int pos) {
        switch (listId) {
            case 0:
                receivedList.remove(pos);
                break;
            case 1:
                sentList.remove(pos);
                break;
            case 2:
                historyList.remove(pos);
                break;
        }
    }

    static public void addToHistory(int listId, ReminderDataHolder.reminderStatus status, int pos) {
        switch (listId) {
            case 0:
                ReminderDataHolder temp = receivedList.get(pos);
                temp.set_type(ReminderDataHolder.reminderType.HISTORY);
                temp.set_status(status);
                historyList.add(temp);
                break;
            case 1:
                ReminderDataHolder temp2 = sentList.get(pos);
                temp2.set_type(ReminderDataHolder.reminderType.HISTORY);
                temp2.set_status(status);
                historyList.add(temp2);
                break;
        }
        sortLists();
        /*if (MainActivity.getActivity() != null) {
            ConnectionData.saveAllSharedReminders(MainActivity.getActivity());
        }*/
    }

    static public ArrayList<ReminderDataHolder> getReceivedList()
    {
        sortLists();
        return receivedList;
    }

    static public ArrayList<ReminderDataHolder> getSentList()
    {
        sortLists();
        return sentList;
    }

    static public ArrayList<ReminderDataHolder> getHistoryList()
    {
        sortLists();
        return historyList;
    }

    public static boolean contains(ArrayList<ReminderDataHolder> list, ReminderDataHolder reminder) {
        for (ReminderDataHolder reminder2 : list) {
            if (reminder.getID() == reminder2.getID()) {
                return true;
            }
        }
        return false;
    }

}
