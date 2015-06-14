package cs446.mindme;

import java.util.ArrayList;

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
                "Message1", "Randy Cheung", "12:00", ReminderDataHolder.reminderStatus.ACTIVE);
        ReminderDataHolder r2 = new ReminderDataHolder(ReminderDataHolder.reminderType.RECEIVED,
                "Message2", "Emily Na", "12:00", ReminderDataHolder.reminderStatus.ACTIVE);
        ReminderDataHolder r3 = new ReminderDataHolder(ReminderDataHolder.reminderType.RECEIVED,
                "Message3", "Arthur Jen", "12:00", ReminderDataHolder.reminderStatus.ACTIVE);
        ReminderDataHolder r4 = new ReminderDataHolder(ReminderDataHolder.reminderType.RECEIVED,
                "Message4", "Richard Fa", "12:00", ReminderDataHolder.reminderStatus.ACTIVE);
        receivedList.add(r1);
        receivedList.add(r2);
        receivedList.add(r3);
        receivedList.add(r4);

        ReminderDataHolder s1 = new ReminderDataHolder(ReminderDataHolder.reminderType.SENT,
                "Message1", "Randy Cheung", "12:00", ReminderDataHolder.reminderStatus.ACTIVE);
        ReminderDataHolder s2 = new ReminderDataHolder(ReminderDataHolder.reminderType.SENT,
                "Message2", "Emily Na", "12:00", ReminderDataHolder.reminderStatus.ACTIVE);
        ReminderDataHolder s3 = new ReminderDataHolder(ReminderDataHolder.reminderType.SENT,
                "Message3", "Arthur Jen", "12:00", ReminderDataHolder.reminderStatus.ACTIVE);
        ReminderDataHolder s4 = new ReminderDataHolder(ReminderDataHolder.reminderType.SENT,
                "Message4", "Richard Fa", "12:00", ReminderDataHolder.reminderStatus.ACTIVE);
        sentList.add(s1);
        sentList.add(s2);
        sentList.add(s3);
        sentList.add(s4);
    }

}
