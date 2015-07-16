package cs446.mindme.DataHolders;

import java.util.List;

/**
 * Created by randy on 15/07/2015.
 */
public class EventDetailsDataHolder {

    private String _description;
    private List<String> _startDate;
    private List<String> _startTime;
    private List<String> _endDate;
    private List<String> _endTime;
    private String _locationName;
    private String _locationStreet;
    private String _locationCity;
    private String _locationProvince;
    private String _locationPostal;
    private String _link;

    public EventDetailsDataHolder() {
        _description = "";
        _startDate = null;
        _startTime = null;
        _endDate = null;
        _endTime = null;
        _locationName = "";
        _locationStreet = "";
        _locationCity = "";
        _locationProvince = "";
        _locationPostal = "";
        _link = "";
    }

    public EventDetailsDataHolder(String description, List<String> startDate, List<String> startTime, List<String> endDate, List<String> endTime, String locationName, String locationStreet, String locationCity, String locationProvince, String locationPostal, String link) {
        _description = description;
        _startDate = startDate;
        _startTime = startTime;
        _endDate = endDate;
        _endTime = endTime;
        _locationName = locationName;
        _locationStreet = locationStreet;
        _locationCity = locationCity;
        _locationProvince = locationProvince;
        _locationPostal = locationPostal;
        _link = link;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public List<String> get_startDate() {
        return _startDate;
    }

    public void set_startDate(List<String> _startDate) {
        this._startDate = _startDate;
    }

    public List<String> get_startTime() {
        return _startTime;
    }

    public void set_startTime(List<String> _startTime) {
        this._startTime = _startTime;
    }

    public List<String> get_endDate() {
        return _endDate;
    }

    public void set_endDate(List<String> _endDate) {
        this._endDate = _endDate;
    }

    public List<String> get_endTime() {
        return _endTime;
    }

    public void set_endTime(List<String> _endTime) {
        this._endTime = _endTime;
    }

    public String get_locationName() {
        return _locationName;
    }

    public void set_locationName(String _locationName) {
        this._locationName = _locationName;
    }

    public String get_locationStreet() {
        return _locationStreet;
    }

    public void set_locationStreet(String _locationStreet) {
        this._locationStreet = _locationStreet;
    }

    public String get_locationCity() {
        return _locationCity;
    }

    public void set_locationCity(String _locationCity) {
        this._locationCity = _locationCity;
    }

    public String get_locationProvince() {
        return _locationProvince;
    }

    public void set_locationProvince(String _locationProvince) {
        this._locationProvince = _locationProvince;
    }

    public String get_locationPostal() {
        return _locationPostal;
    }

    public void set_locationPostal(String _locationPostal) {
        this._locationPostal = _locationPostal;
    }

    public String get_link() {
        return _link;
    }

    public void set_link(String _link) {
        this._link = _link;
    }
}
