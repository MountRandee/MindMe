package cs446.mindme.DataHolders;

import java.util.List;

public class EventDataHolder {

    // See https://github.com/uWaterloo/api-documentation/blob/master/v2/events/events.md
    // Other event details will be requested when user clicks on More Info
    private int _id;
    private String _site;
    private String _title;
    private List<String> _startTimes;

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


