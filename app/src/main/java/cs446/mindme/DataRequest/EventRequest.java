package cs446.mindme.DataRequest;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cs446.mindme.DataHolders.EventDataHolder;
import cs446.mindme.DataHolders.EventDetailsDataHolder;

public class EventRequest extends AsyncTask<String, Void, String> {

    // Refer to https://github.com/uWaterloo/api-documentation
    private final static String apiLink = "https://api.uwaterloo.ca/v2/";
    private final static String strEvent = "events";
    private final static String strJSON = ".json";
    private final static String keyData = "data";

    // Keys for the event list
    private final static String keyEventID = "id";
    private final static String keySite = "site";
    private final static String keyTitle = "title";
    private final static String keyTimes = "times";
    private final static String keyStart = "start";

    // Keys for the event{site}{id}
    private final static String keyDescription = "description";
    private final static String keyStartDate  = "start_date";
    private final static String keyStartTime = "start_time";
    private final static String keyEndDate  = "end_date";
    private final static String keyEndTime = "end_time";
    private final static String keyLocation = "location";
    private final static String keyLocationName = "name";
    private final static String keyLocationStreet = "street";
    private final static String keyLocationCity = "city";
    private final static String keyLocationProvince = "province";
    private final static String keyLocationPostal = "postal_code";
    private final static String keyLink = "link";

    // Builds the address for JSON request
    public static String buildAddress(String site, int eventID) {

        // Example:
        // https://api.uwaterloo.ca/v2/events.json?key=435c0cf289fbfebc934d29e8c924b323
        // https://api.uwaterloo.ca/v2/events/engineering/1701.json?key=435c0cf289fbfebc934d29e8c924b323

        String jsonAddress = apiLink;
        if (site != null && eventID >= 0){
            jsonAddress += strEvent + "/" + site + "/" + eventID + strJSON + apiKey;
        } else {
            jsonAddress += strEvent+ strJSON + apiKey;
        }
        System.out.println(jsonAddress);
        return jsonAddress;
    }

    // Gets the JSON response string from the url passed in
    @Override
    protected String doInBackground(String ... params) {
        URL url;
        HttpURLConnection urlConnection = null;
        String responseString = "";
        try {
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                responseString = readStream(urlConnection.getInputStream());
                System.out.println(responseString);
            } else {
                Log.v("EventRequest", "Response code:" + responseCode);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       return responseString;
    }

    // Builds the event list based on the JSON request /events.json
    public ArrayList<EventDataHolder> buildEventList(String responseString) {
        ArrayList<EventDataHolder> eventList = new ArrayList<EventDataHolder>();
        try {

            JSONObject responseObject = new JSONObject(responseString);
            JSONArray dataArray = responseObject.getJSONArray(keyData);
            System.out.println("dataArray length:" + dataArray.length());

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject eventObject = dataArray.getJSONObject(i);
                JSONArray timesArray = eventObject.getJSONArray(keyTimes);
                List<String> startTimes = new ArrayList<String>();
                for (int j = 0; j < timesArray.length(); j++) {
                    startTimes.add(timesArray.getJSONObject(j).getString(keyStart));
                }

                // Create an event holder and add to the list
                EventDataHolder eventHolder = new EventDataHolder(
                        Integer.parseInt(eventObject.getString(keyEventID)),
                        eventObject.getString(keySite),
                        eventObject.getString(keyTitle),
                        startTimes
                );
                eventList.add(eventHolder);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return eventList;
    }

    public EventDetailsDataHolder buildEventInfo(String responseString) {
        EventDetailsDataHolder eventDetails = new EventDetailsDataHolder();
        try {
            JSONObject responseObject = new JSONObject(responseString).getJSONObject(keyData);

            // get the event times
            JSONArray timesArray = responseObject.getJSONArray(keyTimes);
            List<String> startDate = new ArrayList<String>();
            List<String> startTime = new ArrayList<String>();
            List<String> endDate = new ArrayList<String>();
            List<String> endTime = new ArrayList<String>();
            for (int i = 0; i < timesArray.length(); i++) {
                startDate.add(timesArray.getJSONObject(i).getString(keyStartDate));
                startTime.add(timesArray.getJSONObject(i).getString(keyStartTime));
                endDate.add(timesArray.getJSONObject(i).getString(keyEndDate));
                endTime.add(timesArray.getJSONObject(i).getString(keyEndTime));
            }

            // get the location object
            JSONObject locationObject = responseObject.getJSONObject(keyLocation);

            // store the description
            eventDetails.set_description(responseObject.getString(keyDescription));

            // store the time information
            eventDetails.set_startDate(startDate);
            eventDetails.set_startTime(startTime);
            eventDetails.set_endDate(endDate);
            eventDetails.set_endTime(endTime);

            // store the location information
            eventDetails.set_locationName(locationObject.getString(keyLocationName));
            eventDetails.set_locationStreet(locationObject.getString(keyLocationStreet));
            eventDetails.set_locationCity(locationObject.getString(keyLocationCity));
            eventDetails.set_locationProvince(locationObject.getString(keyLocationProvince));
            eventDetails.set_locationPostal(locationObject.getString(keyLocationPostal));

            // store the link
            eventDetails.set_link(responseObject.getString(keyLink));

        } catch (JSONException e) {
                e.printStackTrace();
        }
        return eventDetails;
    }

    private static String readStream(InputStream in) {
        BufferedReader bufferedReader = null;
        StringBuffer responseBuffer = new StringBuffer();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                responseBuffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return responseBuffer.toString();
    }
}
