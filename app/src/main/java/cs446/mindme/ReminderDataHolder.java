package cs446.mindme;

public class ReminderDataHolder {

    public enum reminderType {
        RECEIVED, SENT, HISTORY
    }

    public enum reminderStatus {
        COMPLETED, DECLINED, ACTIVE
    }

    private reminderType _type;
    private String _message;
    private String _from;
    private String _time;
    private reminderStatus _status;

    public ReminderDataHolder(reminderType type, String msg, String from, String time, reminderStatus status) {
        this._type = type;
        this._message = msg;
        this._from = from;
        this._time = time;
        this._status = status;
    }

    public reminderType getType() { return _type; }
    public String getMessage() { return _message; }
    public String getFrom() { return _from; }
    public String getTime () { return _time; }
    public reminderStatus getStatus() { return _status; }

}
