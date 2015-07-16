package cs446.mindme.DataHolders;

import java.util.List;

public class EventDataHolder {

    // See https://github.com/uWaterloo/api-documentation/blob/master/v2/events/events.md
    // Other event details will be requested when user clicks on Info
    private int _id;
    private String _site;
    private String _title;
    private List<String> _startTimes;

    // Used to create a reminder
    private ReminderDataHolder.reminderStatus _reminderStatus;
    private ReminderDataHolder.reminderType _reminderType;

    public EventDataHolder() {
        this._id = 0;
        this._site = "";
        this._title = "";
        this._startTimes = null;
    }

    public EventDataHolder (int id, String site, String title, List<String> startTimes)
    {
        this._id = id;
        this._site = site;
        this._title = title;
        this._startTimes = startTimes;
    }

    public void setReminderStatus(ReminderDataHolder.reminderStatus status) {
        this._reminderStatus = status;
    }

    public void setReminderType(ReminderDataHolder.reminderType type) {
        this._reminderType = type;
    }

    public ReminderDataHolder.reminderStatus getReminderStatus() {
        return this._reminderStatus;
    }

    public ReminderDataHolder.reminderType getReminderType() {
        return this._reminderType;
    }

    public int get_id() {
        return _id;
    }

    public String get_site() {
        return _site;
    }

    public String get_title() {
        return _title;
    }

    public List<String>get_startTimes() {
        return _startTimes;
    }
}


