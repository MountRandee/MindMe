package cs446.mindme.DataHolders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cs446.mindme.MainActivity;

public class ReminderDataHolder {

    public enum reminderType {
        RECEIVED, SENT, HISTORY
    }

    public enum reminderStatus {
        COMPLETED, DECLINED, CANCELLED, ACTIVE
    }

    private reminderType _type;
    private String _message;
    private MainActivity.Friend _from;
    private Date _time;
    private reminderStatus _status;

    public ReminderDataHolder(reminderType type, String msg, MainActivity.Friend from, Date time, reminderStatus status) {
        this._type = type;
        this._message = msg;
        this._from = from;
        this._time = time;
        this._status = status;
    }

    public reminderType getType() { return _type; }
    public String getMessage() { return _message; }
    public MainActivity.Friend getFrom() { return _from; }
    public String getTime () {
        DateFormat dateFormat = new SimpleDateFormat("MMM d");
        return dateFormat.format(_time);
    }
    public reminderStatus getStatus() { return _status; }

    public void set_message(String msg) {
        this._message = msg;
    }

    public void set_type(reminderType type) {
        this._type = type;
    }

    public void set_status(reminderStatus status) {
        this._status = status;
    }
}
