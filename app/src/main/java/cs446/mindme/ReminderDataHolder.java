package cs446.mindme;

public class ReminderDataHolder {

    private String _type;
    private String _message;
    private String _from;
    private String _time;
    private boolean _status;

    public ReminderDataHolder(String m, String f, String t) {
        this._message = m;
        this._from = f;
        this._time = t;
    }

    public ReminderDataHolder(String m, String f, String t, boolean s) {
        this._message = m;
        this._from = f;
        this._time = t;
        this._status = s;
    }

    public String getMessage() { return _message; }
    public String getFrom() { return _from; }
    public String getTime () { return _time; }
    public boolean getStatus() { return _status; }

}
