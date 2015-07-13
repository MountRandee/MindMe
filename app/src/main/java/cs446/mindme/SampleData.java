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
        if (MainActivity.getActivity() != null) {
            ConnectionData.saveAllSharedReminders(MainActivity.getActivity());
        }
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

    public static Date getRandomDate() {
        Random rand = new Random();
        Date today = new Date();
        return new Date(today.getYear(), rand.nextInt(today.getMonth()), rand.nextInt(28), rand.nextInt(24), rand.nextInt(60));
    }

    public static void populateSampleData()
    {
        ReminderDataHolder r1 = new ReminderDataHolder(ReminderDataHolder.reminderType.RECEIVED,
                "Bring phone for demo", new MainActivity.Friend("Emily Na", "1001165806602425"), getRandomDate(), ReminderDataHolder.reminderStatus.ACTIVE);
        ReminderDataHolder r2 = new ReminderDataHolder(ReminderDataHolder.reminderType.RECEIVED,
                "You owe me $10", new MainActivity.Friend("Richard Fa", "442120862658124"), getRandomDate(), ReminderDataHolder.reminderStatus.ACTIVE);
        ReminderDataHolder r3 = new ReminderDataHolder(ReminderDataHolder.reminderType.RECEIVED,
                "I need my t-shirt back", new MainActivity.Friend("Richard Fa", "442120862658124"), getRandomDate(), ReminderDataHolder.reminderStatus.ACTIVE);
        ReminderDataHolder r4 = new ReminderDataHolder(ReminderDataHolder.reminderType.RECEIVED,
                "Finish Proposal", new MainActivity.Friend("Emily Na", "1001165806602425"), getRandomDate(), ReminderDataHolder.reminderStatus.ACTIVE);
        receivedList.add(r1);
        receivedList.add(r2);
        receivedList.add(r3);
        receivedList.add(r4);

        ReminderDataHolder s1 = new ReminderDataHolder(ReminderDataHolder.reminderType.SENT,
                "Bring my umbrella", new MainActivity.Friend("Richard Fa", "442120862658124"), getRandomDate(), ReminderDataHolder.reminderStatus.ACTIVE);
        ReminderDataHolder s2 = new ReminderDataHolder(ReminderDataHolder.reminderType.SENT,
                "Don't forget Watcard", new MainActivity.Friend("Emily Na", "1001165806602425"), getRandomDate(), ReminderDataHolder.reminderStatus.ACTIVE);
        ReminderDataHolder s3 = new ReminderDataHolder(ReminderDataHolder.reminderType.SENT,
                "Buy ski tickets", new MainActivity.Friend("Randy Cheung", "10152979228163161"), getRandomDate(), ReminderDataHolder.reminderStatus.ACTIVE);
        ReminderDataHolder s4 = new ReminderDataHolder(ReminderDataHolder.reminderType.SENT,
                "Pick up my laptop", new MainActivity.Friend("Richard Fa", "442120862658124"), getRandomDate(), ReminderDataHolder.reminderStatus.ACTIVE);
        sentList.add(s1);
        sentList.add(s2);
        sentList.add(s3);
        sentList.add(s4);

        sortLists();
        if (MainActivity.getActivity() != null) {
            ConnectionData.saveAllSharedReminders(MainActivity.getActivity());
        }
    }

}
