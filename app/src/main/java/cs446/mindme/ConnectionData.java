package cs446.mindme;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import cs446.mindme.DataHolders.ReminderDataHolder;
import cs446.mindme.Views.ViewHistory;
import cs446.mindme.Views.ViewReceived;
import cs446.mindme.Views.ViewSent;

/**
 * Created by richardfa on 15-07-12.
 */
public class ConnectionData {
    private static ConnectionData ourInstance = new ConnectionData();

    public static String DOMAIN = "http://10.0.2.2:5000";

    public static String MINDME_SHARED_PREF = "mindmesharedpref";

    public static String SHARED_USER_ID = "shareduserid";
    public static String SHARED_RECEIVED_REMINDERS = "sharedreceivedreminders";
    public static String SHARED_SENT_REMINDERS = "sharedsentreminders";
    public static String SHARED_HISTORY_REMINDERS = "sharedhistoryreminders";

    //gcm
    public static GoogleCloudMessaging gcm;
    public static String PROJECT_NUMBER = "936075907537";
    public static String regid;

    public static String SHARED_GCM_ID = "sharedGCMID";
    public static String SHARED_FB_ID = "sharedFBID";
    public static String SHARED_TOKEN = "sharedToken";

    public static boolean isLoadingReminders = false;

    public enum callType {
        LOAD_REMINDERS, LOGIN, LOGOUT, CREATE_REMINDER, EDIT_REMINDER, COMPLETE_REMINDER, DECLINE_REMINDER, CANCEL_REMINDER, GCM, RETRIEVE_FRIENDS, NONE
    }

    public static void loadReminders() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (isLoadingReminders) {
                    return;
                }
                try {
                    isLoadingReminders = true;
                    Log.e("loadReminders", "loading");

                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet(DOMAIN + "/api/v1/user/get/" + Profile.getCurrentProfile().getId() + "/?deref=all");
                    HttpResponse response = client.execute(request);

                    // Get the response
                    BufferedReader rd = new BufferedReader
                            (new InputStreamReader(response.getEntity().getContent()));

                    String line = "";
                    StringBuffer responseEntity = new StringBuffer();
                    while ((line = rd.readLine()) != null) {
                        responseEntity.append(line);
                    }

                    JSONObject obj = new JSONObject(responseEntity.toString());

                    SampleData.getReceivedList().clear();
                    SampleData.getSentList().clear();
                    SampleData.getHistoryList().clear();

                    JSONArray receivedMsgs = obj.getJSONObject("result").getJSONArray("received_messages");
                    JSONArray sentMsgs = obj.getJSONObject("result").getJSONArray("sent_messages");
                    for (int i = 0 ; i < receivedMsgs.length() ; i++) {
                        Log.e("receivedMsgs", receivedMsgs.getJSONObject(i).toString());
                        String status = receivedMsgs.getJSONObject(i).getString("status");
                        String id = receivedMsgs.getJSONObject(i).getString("id");
                        String message = receivedMsgs.getJSONObject(i).getString("message");
                        MainActivity.Friend from = MainActivity.Friend.getFriend(receivedMsgs.getJSONObject(i).getString("from"));
                        String last_modified_date = receivedMsgs.getJSONObject(i).getString("last_modified_date");
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                        Date date = format.parse(last_modified_date);
                        ReminderDataHolder reminder = new ReminderDataHolder(ReminderDataHolder.reminderType.RECEIVED, message, from, date, ReminderDataHolder.stringToStatus(status), id);
                        if (reminder.getStatus() == ReminderDataHolder.reminderStatus.ACTIVE) {
                            if (!SampleData.contains(SampleData.getReceivedList(), reminder)) {
                                SampleData.getReceivedList().add(reminder);
                            }
                        } else {
                            reminder.set_type(ReminderDataHolder.reminderType.HISTORY);
                            if (!SampleData.contains(SampleData.getHistoryList(), reminder)) {
                                SampleData.getHistoryList().add(reminder);
                            }
                        }
                    }
                    for (int i = 0 ; i < sentMsgs.length() ; i++) {
                        Log.e("sentMsgs", sentMsgs.getJSONObject(i).toString());
                        String status = sentMsgs.getJSONObject(i).getString("status");
                        String message = sentMsgs.getJSONObject(i).getString("message");
                        MainActivity.Friend to = MainActivity.Friend.getFriend(sentMsgs.getJSONObject(i).getString("to"));
                        String last_modified_date = sentMsgs.getJSONObject(i).getString("last_modified_date");
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                        Date date = format.parse(last_modified_date);
                        String id = sentMsgs.getJSONObject(i).getString("id");
                        ReminderDataHolder reminder = new ReminderDataHolder(ReminderDataHolder.reminderType.SENT, message, to, date, ReminderDataHolder.stringToStatus(status), id);
                        if (reminder.getStatus() == ReminderDataHolder.reminderStatus.ACTIVE) {
                            if (!SampleData.contains(SampleData.getSentList(), reminder)) {
                                SampleData.getSentList().add(reminder);
                            }
                        } else {
                            reminder.set_type(ReminderDataHolder.reminderType.HISTORY);
                            if (!SampleData.contains(SampleData.getHistoryList(), reminder)) {
                                SampleData.getHistoryList().add(reminder);
                            }
                        }
                    }
                    SampleData.sortLists();
                    notifyDataSetChanged();
                    isLoadingReminders = false;
                    MainActivity.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.getActivity(), "Finished reloading", Toast.LENGTH_LONG).show();
                            Log.e("loadReminders", "loaded");
                        }
                    });
                } catch (Exception e) {
                    isLoadingReminders = false;
                    showToast(e.getLocalizedMessage(), callType.LOAD_REMINDERS);
                }
            }
        });
        thread.start();
    }

    public static void notifyDataSetChanged() {
        if (MainActivity.getActivity() == null) {
            return;
        }
        MainActivity.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ViewReceived.getViewReceived() != null) {// && ViewReceived.getViewReceived().getUserVisibleHint()) {
                    Log.e("Visible Fragment", "Received");
                    ViewReceived.getViewReceived().notifyDataSetChanged();
                }
                if (ViewSent.getViewSent() != null) {// && ViewSent.getViewSent().getUserVisibleHint()) {
                    Log.e("Visible Fragment", "Sent");
                    ViewSent.getViewSent().notifyDataSetChanged();
                }
                if (ViewHistory.getViewHistory() != null) {// && ViewHistory.getViewHistory().getUserVisibleHint()) {
                    Log.e("Visible Fragment", "History");
                    ViewHistory.getViewHistory().notifyDataSetChanged();
                }
            }
        });
    }

    public static void post(final String appendURL, final HashMap<String, String>params, final boolean shouldReloadReminders, final callType callType) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuffer appendParams = new StringBuffer();
                    for (HashMap.Entry<String, String>param : params.entrySet()) {
                        appendParams.append("&" + param.getKey() + "=" + URLEncoder.encode(param.getValue()));
                    }
                    String url = DOMAIN + appendURL + "?" + appendParams.toString().substring(1);
                    Log.e("post", "URL: " + url);
                    HttpPost post = new HttpPost(url);
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(post);
                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String line = "";
                    StringBuffer responseEntity = new StringBuffer();
                    while ((line = rd.readLine()) != null) {
                        responseEntity.append(line);
                    }
                    Log.e("post", "response entity: " + responseEntity.toString());
                    if (response.getStatusLine().getStatusCode() != 200) {
                        showToast(response.getStatusLine().getReasonPhrase(), callType);
                    }
                    if (shouldReloadReminders) {
                        loadReminders();
                    }
                } catch (Exception e) {
                    showToast(e.getLocalizedMessage(), callType);
                }
            }
        });
        thread.start();
    }

    public static void startGCM(final Context context) {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    FacebookSdk.sdkInitialize(MainActivity.getActivity());
                    SharedPreferences prefs = context.getSharedPreferences(MINDME_SHARED_PREF, Context.MODE_PRIVATE);
                    String sharedGCM = prefs.getString(SHARED_GCM_ID, "");
                    if (sharedGCM.isEmpty()) {
                        if (gcm == null) {
                            gcm = GoogleCloudMessaging.getInstance(context);
                        }
                        InstanceID instanceID = InstanceID.getInstance(context);
                        regid = instanceID.getToken(PROJECT_NUMBER, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                        Log.e("startGCM", "registration ID: " + regid);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(SHARED_GCM_ID, regid);
                        editor.apply();
                    }
                    regid = sharedGCM;

                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("token", AccessToken.getCurrentAccessToken().getToken());
                    params.put("expiration", "123123");
                    params.put("fb_id", AccessToken.getCurrentAccessToken().getUserId());
                    params.put("gcm_id", regid);
                    post("/api/v1/user/login/", params, true, callType.LOGIN);

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(SHARED_FB_ID, AccessToken.getCurrentAccessToken().getUserId());
                    editor.putString(SHARED_TOKEN, AccessToken.getCurrentAccessToken().getToken());
                    editor.apply();
                } catch (Exception e) {
                    showToast(e.getLocalizedMessage(), callType.GCM);
                }
            }
        });
        thread.start();
    }

    public static void showToast(final String message, final callType callType) {
        if (MainActivity.getActivity() != null) {
            MainActivity.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String title;
                    switch (callType) {
                        case LOAD_REMINDERS:
                            title = "Load Reminders Error";
                            break;
                        case LOGIN:
                            title = "Login Error";
                            break;
                        case LOGOUT:
                            title = "Logout Error";
                            break;
                        case CREATE_REMINDER:
                            title = "Create Reminder Error";
                            break;
                        case DECLINE_REMINDER:
                            title = "Decline Reminder Error";
                            break;
                        case CANCEL_REMINDER:
                            title = "Cancel Reminder Error";
                            break;
                        case GCM:
                            title = "GCM Error";
                            break;
                        case RETRIEVE_FRIENDS:
                            title = "Retrieve Friends Error";
                            break;
                        default:
                            title = "Error";
                            break;
                    }
                    Toast.makeText(MainActivity.getActivity(), title + "\n" + message, Toast.LENGTH_LONG).show();
                    Log.e(title, message);
                }
            });
        }
    }

    public static void setSharedUserID(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MINDME_SHARED_PREF, Context.MODE_PRIVATE).edit();
        if (Profile.getCurrentProfile().getId() != null) {
            editor.putString(SHARED_USER_ID, Profile.getCurrentProfile().getId());
        }
        editor.apply();
    }

    public static String getSharedUserID(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MINDME_SHARED_PREF, Context.MODE_PRIVATE);
        return prefs.getString(SHARED_USER_ID, null);
    }

    public static void saveAllSharedReminders(Context context) {
        saveSharedReceivedReminders(context);
        saveSharedSentReminders(context);
        saveSharedHistoryReminders(context);
    }

    public static void saveSharedReceivedReminders(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MINDME_SHARED_PREF, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String jsonReceived = gson.toJson(SampleData.getReceivedList());
        editor.putString(SHARED_RECEIVED_REMINDERS, jsonReceived);
        editor.apply();
    }

    public static void saveSharedSentReminders(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MINDME_SHARED_PREF, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String jsonSent = gson.toJson(SampleData.getSentList());
        editor.putString(SHARED_SENT_REMINDERS, jsonSent);
        editor.apply();
    }

    public static void saveSharedHistoryReminders(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MINDME_SHARED_PREF, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String jsonHistory = gson.toJson(SampleData.getHistoryList());
        editor.putString(SHARED_HISTORY_REMINDERS, jsonHistory);
        editor.apply();
    }

    public static void applyAllSharedReminders(Context context) {
        applySharedReceivedReminders(context);
        applySharedSentReminders(context);
        applySharedHistoryReminders(context);
    }

    public static void applySharedReceivedReminders(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MINDME_SHARED_PREF, Context.MODE_PRIVATE);
        String jsonReceived = prefs.getString(SHARED_RECEIVED_REMINDERS, null);
        Type type = new TypeToken<ArrayList<ReminderDataHolder>>() {}.getType();
        ArrayList<ReminderDataHolder> received = new Gson().fromJson(jsonReceived, type);
        if (SampleData.receivedList == null) {
            SampleData.receivedList = new ArrayList<ReminderDataHolder>();
        }
        if (!SampleData.receivedList.isEmpty()) {
            SampleData.receivedList.clear();
        }
        if (received != null) {
            SampleData.receivedList.addAll(received);
        }
    }

    public static void applySharedSentReminders(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MINDME_SHARED_PREF, Context.MODE_PRIVATE);
        String jsonSent = prefs.getString(SHARED_SENT_REMINDERS, null);
        Type type = new TypeToken<ArrayList<ReminderDataHolder>>() {}.getType();
        ArrayList<ReminderDataHolder> sent = new Gson().fromJson(jsonSent, type);
        if (SampleData.sentList == null) {
            SampleData.sentList = new ArrayList<ReminderDataHolder>();
        }
        if (!SampleData.sentList.isEmpty()) {
            SampleData.sentList.clear();
        }
        if (sent != null) {
            SampleData.sentList.addAll(sent);
        }
    }

    public static void applySharedHistoryReminders(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MINDME_SHARED_PREF, Context.MODE_PRIVATE);
        String jsonHistory = prefs.getString(SHARED_HISTORY_REMINDERS, null);
        Type type = new TypeToken<ArrayList<ReminderDataHolder>>() {}.getType();
        ArrayList<ReminderDataHolder> history = new Gson().fromJson(jsonHistory, type);
        if (SampleData.historyList == null) {
            SampleData.historyList = new ArrayList<ReminderDataHolder>();
        }
        if (!SampleData.historyList.isEmpty()) {
            SampleData.historyList.clear();
        }
        if (history != null) {
            SampleData.historyList.addAll(history);
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            return false;
        }
        return true;
    }

    public static void updateAction() {
        if (MainActivity.getActivity() == null) {
            return;
        }
    }

    public static void setupProfile(Context context){
        if (!isNetworkAvailable(context)) {
            return;
        }
        setSharedUserID(context);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONObject obj = response.getJSONObject();
                            JSONArray array = null;
                            array = obj.getJSONArray("data");

                            if (MainActivity.friends == null) {
                                MainActivity.friends = new ArrayList<MainActivity.Friend>();
                            } else if (!MainActivity.friends.isEmpty()) {
                                MainActivity.friends.clear();
                            }
                            MainActivity.friends.add(new MainActivity.Friend(Profile.getCurrentProfile().getName(), Profile.getCurrentProfile().getId()));
                            if (array != null) {
                                for (int i = 0; i < array.length(); i++) {
                                    MainActivity.friends.add(new MainActivity.Friend(array.getJSONObject(i).getString("name"),
                                            array.getJSONObject(i).getString("id")));
                                }
                            }
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    notifyDataSetChanged();
                                }
                            }, 0000);
                            if (CreateNewReminderDialog.dialog != null && CreateNewReminderDialog.dialog.isShowing()) {
                                CreateNewReminderDialog.dialog.completeRefreshList();
                            }
                        } catch (Exception e) {
                            showToast(e.getLocalizedMessage(), callType.RETRIEVE_FRIENDS);
                        }
                    }
                }
        ).executeAsync();
    }

    public static void showNotification(final String reminderID, final Context context) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet(DOMAIN + "/api/v1/reminder/get/" + reminderID + "/");
                    HttpResponse response = null;
                        response = client.execute(request);

                    // Get the response
                    BufferedReader rd = new BufferedReader
                            (new InputStreamReader(response.getEntity().getContent()));

                    String line = "";
                    StringBuffer responseEntity = new StringBuffer();
                    while ((line = rd.readLine()) != null) {
                        responseEntity.append(line);
                    }

                    JSONObject obj = new JSONObject(responseEntity.toString());
                    MainActivity.Friend from = MainActivity.Friend.getFriend(obj.getJSONObject("result").getString("from"));
                    MainActivity.Friend to = MainActivity.Friend.getFriend(obj.getJSONObject("result").getString("to"));
                    String message = obj.getJSONObject("result").getString("message");
                    if (message.length() > 15) {
                        message = message.substring(0, 15);
                    }
                    ReminderDataHolder.reminderStatus status = ReminderDataHolder.stringToStatus(obj.getJSONObject("result").getString("status"));
                    String date1 = obj.getJSONObject("result").getString("created_date");
                    String date2 = obj.getJSONObject("result").getString("last_modified_date");

                    String notificationTitle;
                    String notificationMessage = "Reminder: \"" + message + "\"";
                    if (status == ReminderDataHolder.reminderStatus.COMPLETED) {
                        notificationTitle = to.name + " has completed your reminder";
                    } else if (status == ReminderDataHolder.reminderStatus.DECLINED) {
                        notificationTitle = to.name + " has declined your reminder";
                    } else if (status == ReminderDataHolder.reminderStatus.CANCELLED) {
                        notificationTitle = from.name + " has cancelled a reminder";
                    } else {
                        if (date1.equals(date2)) {
                            notificationTitle = from.name + " has sent you a reminder";
                        } else {
                            notificationTitle = "A reminder has been edited";
                        }
                    }
                    NotificationManager notificationManager = (NotificationManager) context
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notification = new Notification(R.drawable.notification_icon,
                            notificationMessage, System.currentTimeMillis());
                    Intent notificationIntent = new Intent(context, MainActivity.class);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    PendingIntent intent = PendingIntent.getActivity(context, 0,
                            notificationIntent, 0);
                    notification.setLatestEventInfo(context, notificationTitle, notificationMessage,
                            intent);
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(MINDME_SHARED_PREF, Integer.parseInt(reminderID), notification);

                    if (!isAppOpen(context)) {
                        WidgetService.getWidgetService().incrementNumber();
                    }
                } catch (Exception e) {
                    showToast(e.getLocalizedMessage(), callType.LOAD_REMINDERS);
                }
            }
        });
        thread.start();
    }

    public static boolean isAppOpen(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager
                .getRunningTasks(Integer.MAX_VALUE);
        boolean isActivityFound = false;

        if (services.get(0).topActivity.getPackageName().toString()
                .equalsIgnoreCase(context.getPackageName().toString())) {
            isActivityFound = true;
        }

        return isActivityFound;
    }

    private ConnectionData() {
    }
}
