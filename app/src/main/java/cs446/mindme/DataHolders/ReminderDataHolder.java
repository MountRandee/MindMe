package cs446.mindme.DataHolders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import cs446.mindme.MainActivity;

public class ReminderDataHolder implements Comparable<ReminderDataHolder> {

    @Override
    public int compareTo(ReminderDataHolder another) {
        return another.getDate().after(_time) ? 1 : -1;
    }

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
    private String _id;

    public ReminderDataHolder(reminderType type, String msg, MainActivity.Friend from, Date time, reminderStatus status, String id) {
        this._type = type;
        this._message = msg;
        this._from = from;
        this._time = time;
        this._status = status;
        this._id = id;
    }

    public reminderType getType() { return _type; }

    public String getMessage() { return _message; }

    public MainActivity.Friend getFrom() { return _from; }

    public Date getDate() { return _time; }

    public String getTime () {
        DateFormat dateFormat = new SimpleDateFormat("MMM d");
        if (dateFormat.format(_time).equals(dateFormat.format(new Date()))) {
            dateFormat = new SimpleDateFormat("h:mm a");
        }
        return dateFormat.format(_time);
    }

    public reminderStatus getStatus() { return _status; }
    public String getID() { return  _id; }

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
