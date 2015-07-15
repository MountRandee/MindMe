package cs446.mindme.DataHolders;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventDataHolder {

    // Used in the Event item
    private int _id;
    private String _title;
    private String _site;
    private Date _date;

    // Used in the Event actions
    private List<String> _times;
    private String _description;
    private String _link;

    // Storing temporarily
    private String _siteName;
    private List<String> _types;

    // Used to create a reminder
    private ReminderDataHolder.reminderStatus _reminderStatus;
    private ReminderDataHolder.reminderType _reminderType;

    public EventDataHolder() {
        this._id = 0;
        this._title = "";
        this._site = "";
        this._date = null;
        this._times = null;
        this._description = "";
        this._link = "";
        this._siteName = "";
        this._types = null;

    }
    //
    public EventDataHolder (int id, String title, String site, Date date,
                           List<String> times, String description, String link, String siteName, List<String> types) {
        this._id = id;
        this._title = title;
        this._site = site;
        this._date = date;
        this._times = times;
        this._description = description;
        this._link = link;
        this._siteName = siteName;
        this._types = types;
    }

    public void setReminderStatus(ReminderDataHolder.reminderStatus status) {
        this._reminderStatus = status;
    }

    public void set_reminderType(ReminderDataHolder.reminderType type) {
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

    public String get_title() {
        return _title;
    }

    public String get_site() {
        return _site;
    }

    public Date get_date() {
        return _date;
    }

    public String get_date_string() {
        DateFormat dateFormat = new SimpleDateFormat("MMM d");
        if (dateFormat.format(_date).equals(dateFormat.format(new Date()))) {
            dateFormat = new SimpleDateFormat("h:mm a");
        }
        return dateFormat.format(_date);
    }

    public List<String> get_times() {
        return _times;
    }

    public String get_description() {
        return _description;
    }

    public String get_link() {
        return _link;
    }

    public String get_siteName() {
        return _siteName;
    }

    public List<String> get_types() {
        return _types;
    }
}


