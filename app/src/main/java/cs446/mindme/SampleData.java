package cs446.mindme;

import java.util.ArrayList;

import cs446.mindme.DataHolders.ReminderDataHolder;

public class SampleData {

    static ArrayList<ReminderDataHolder> receivedList = new ArrayList<ReminderDataHolder>();
    static ArrayList<ReminderDataHolder> sentList = new ArrayList<ReminderDataHolder>();
    static ArrayList<ReminderDataHolder> historyList = new ArrayList<ReminderDataHolder>();

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
    }

    static public ArrayList<ReminderDataHolder> getReceivedList()
    {
        return receivedList;
    }

    static public ArrayList<ReminderDataHolder> getSentList()
    {
        return sentList;
    }

    static public ArrayList<ReminderDataHolder> getHistoryList()
    {
        return historyList;
    }

    public static void populateSampleData()
    {
        ReminderDataHolder r1 = new ReminderDataHolder(ReminderDataHolder.reminderType.RECEIVED,
                "Bring phone for demo", "Emily Na", "5:01 PM", ReminderDataHolder.reminderStatus.ACTIVE);
        ReminderDataHolder r2 = new ReminderDataHolder(ReminderDataHolder.reminderType.RECEIVED,
                "You owe me $10", "Richard Fa", "2d ago", ReminderDataHolder.reminderStatus.ACTIVE);
        ReminderDataHolder r3 = new ReminderDataHolder(ReminderDataHolder.reminderType.RECEIVED,
                "I need my t-shirt back", "Richard Fa", "May 14", ReminderDataHolder.reminderStatus.ACTIVE);
        ReminderDataHolder r4 = new ReminderDataHolder(ReminderDataHolder.reminderType.RECEIVED,
                "Finish Proposal", "Emily Na", "May 12", ReminderDataHolder.reminderStatus.ACTIVE);
        receivedList.add(r1);
        receivedList.add(r2);
        receivedList.add(r3);
        receivedList.add(r4);

        ReminderDataHolder s1 = new ReminderDataHolder(ReminderDataHolder.reminderType.SENT,
                "Bring my umbrella", "Richard Fa", "11:54 AM", ReminderDataHolder.reminderStatus.ACTIVE);
        ReminderDataHolder s2 = new ReminderDataHolder(ReminderDataHolder.reminderType.SENT,
                "Don't forget Watcard", "Emily Na", "1d ago", ReminderDataHolder.reminderStatus.ACTIVE);
        ReminderDataHolder s3 = new ReminderDataHolder(ReminderDataHolder.reminderType.SENT,
                "Buy ski tickets", "Randy Cheung", "3d ago", ReminderDataHolder.reminderStatus.ACTIVE);
        ReminderDataHolder s4 = new ReminderDataHolder(ReminderDataHolder.reminderType.SENT,
                "Pick up my laptop", "Richard Fa", "May 12", ReminderDataHolder.reminderStatus.ACTIVE);
        sentList.add(s1);
        sentList.add(s2);
        sentList.add(s3);
        sentList.add(s4);
    }

}
