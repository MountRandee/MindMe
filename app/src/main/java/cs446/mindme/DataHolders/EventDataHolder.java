package cs446.mindme.DataHolders;

import java.util.Date;
import java.util.List;

public class EventDataHolder {

    // See: https://github.com/uWaterloo/api-documentation/blob/master/v2/events/events.md
    private int _id;
    private String _site;
    private String _siteName;
    private String _title;
    private List<Date> _dates;
    private List<String> _startTimes;
    private List<String> _endTimes;
    private String _link;
    private String _description;

    // Used to create a reminder
    private ReminderDataHolder.reminderStatus _reminderStatus;
    private ReminderDataHolder.reminderType _reminderType;

    public EventDataHolder() {
        this._id = 0;
        this._site = "";
        this._siteName = "";
        this._title = "";
        this._dates = null;
        this._startTimes = null;
        this._endTimes = null;
        this._link = "";
        this._description = "";
    }

    public EventDataHolder (
            int id, String site, String siteName, String title,
            List<Date> dates,List<String> startTimes, List<String> endTimes,
            String link, String description)
    {
        this._id = id;
        this._site = site;
        this._siteName = siteName;
        this._title = title;
        this._dates = dates;
        this._endTimes = endTimes;
        this._startTimes = startTimes;
        this._link = link;
        this._description = description;
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
    public String get_siteName() {
        return _siteName;
    }
    public String get_title() {
        return _title;
    }
    public List<Date> get_dates() {
        return _dates;
    }
    public List<String>get_startTimes() {
        return _startTimes;
    }
    public List<String>get_endTimes() {
        return _endTimes;
    }
    // TODO: implement
    public void get_date_string() {
        /*DateFormat dateFormat = new SimpleDateFormat("MMM d");
        if (dateFormat.format(_date).equals(dateFormat.format(new Date()))) {
            dateFormat = new SimpleDateFormat("h:mm a");
        }
        return dateFormat.format(_date);*/
    }
    public String get_link() {
        return _link;
    }
    public String get_description() {
        return _description;
    }
}


