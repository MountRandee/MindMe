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

    public static void loadReminders() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (isLoadingReminders) {
                    return;
                }
                isLoadingReminders = true;
                Log.e("loadReminders", "loading");
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet(DOMAIN + "/api/v1/user/get/" + "4" + "/");
                    HttpResponse response = client.execute(request);

                    // Get the response
                    BufferedReader rd = new BufferedReader
                            (new InputStreamReader(response.getEntity().getContent()));

                    String line = "";
                    StringBuffer error = new StringBuffer();
                    while ((line = rd.readLine()) != null) {
                        error.append(line);
                    }

                    JSONObject obj = new JSONObject(error.toString());

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
                            reminder.set_type(ReminderDataHolder.reminderType.RECEIVED);
                            if (!SampleData.getReceivedList().contains(reminder)) {
                                SampleData.getReceivedList().add(reminder);
                            }
                        } else {
                            reminder.set_type(ReminderDataHolder.reminderType.HISTORY);
                            if (!SampleData.getHistoryList().contains(reminder)) {
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
                            reminder.set_type(ReminderDataHolder.reminderType.SENT);
                            if (!SampleData.getSentList().contains(reminder)) {
                                SampleData.getSentList().add(reminder);
                            }
                        } else {
                            reminder.set_type(ReminderDataHolder.reminderType.HISTORY);
                            if (!SampleData.getHistoryList().contains(reminder)) {
                                SampleData.getHistoryList().add(reminder);
                            }
                        }
                    }
                    SampleData.sortLists();
                    if (ViewReceived.getViewReceived() != null) {
                        ViewReceived.getViewReceived().notifyDataSetChanged();
                    }
                    if (ViewSent.getViewSent() != null) {
                        ViewSent.getViewSent().notifyDataSetChanged();
                    }
                    if (ViewHistory.getViewHistory() != null) {
                        ViewHistory.getViewHistory().notifyDataSetChanged();
                    }
                    isLoadingReminders = false;
                    Log.e("loadReminders", "loaded");
                    if (MainActivity.getActivity() != null) {
                        //saveAllSharedReminders(MainActivity.getActivity());
                    }
                } catch (IOException e) {
                    isLoadingReminders = false;
                    Log.e("loadReminders", "IOException: " + e);
                } catch (JSONException e) {
                    isLoadingReminders = false;
                    Log.e("loadReminders", "JSONException: " + e);
                } catch (ParseException e) {
                    isLoadingReminders = false;
                    Log.e("loadReminders", "ParseException: " + e);
                }
            }
        });
        thread.start();
    }

    public static void post(final String appendURL, final HashMap<String, String>params, final boolean shouldReloadReminders) {
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
                    if (shouldReloadReminders) {
                        loadReminders();
                    }
                } catch (IOException e) {
                    Log.e("post", "IOException: " + e);
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
                    post("/api/v1/user/login/", params, true);

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(SHARED_FB_ID, AccessToken.getCurrentAccessToken().getUserId());
                    editor.putString(SHARED_TOKEN, AccessToken.getCurrentAccessToken().getToken());
                    editor.apply();
                }catch (IOException e) {
                    Log.e("startGCM", "IOException: " + e);
                } catch (Exception e) {
                    Log.e("startGCM", "Exception: " + e);
                }
            }
        });
        thread.start();
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
                        JSONObject obj = response.getJSONObject();
                        JSONArray array = null;
                        try {
                            array = obj.getJSONArray("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (MainActivity.friends == null) {
                            MainActivity.friends = new ArrayList<MainActivity.Friend>();
                        } else if (!MainActivity.friends.isEmpty()) {
                            MainActivity.friends.clear();
                        }

                        if (array != null) {
                            for (int i=0;i<array.length();i++){
                                try {
                                    MainActivity.friends.add(new MainActivity.Friend(array.getJSONObject(i).getString("name"),
                                            array.getJSONObject(i).getString("id")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (ViewReceived.getViewReceived() != null) {
                                    ViewReceived.getViewReceived().notifyDataSetChanged();
                                }
                                if (ViewSent.getViewSent() != null) {
                                    ViewSent.getViewSent().notifyDataSetChanged();
                                }
                                if (ViewHistory.getViewHistory() != null) {
                                    ViewHistory.getViewHistory().notifyDataSetChanged();
                                }
                            }
                        }, 1000);
                        /*if (SampleData.getReceivedList().isEmpty() && SampleData.getSentList().isEmpty()
                                && SampleData.getHistoryList().isEmpty()) {
                            loadReminders();
                        }*/
                        if (CreateNewReminderDialog.dialog != null && CreateNewReminderDialog.dialog.isShowing()) {
                            CreateNewReminderDialog.dialog.completeRefreshList();
                        }
                    }
                }
        ).executeAsync();
    }

    public static void showNotification(String sender, String eventtext, Context context) {
        // Set the icon, scrolling text and timestamp

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.widget,
                eventtext, System.currentTimeMillis());

        Intent notificationIntent = new Intent(context, MainActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // The PendingIntent to launch our activity if the user selects this
        // notification
        PendingIntent intent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);

        // Set the info for the views that show in the notification panel.
        String title = sender + " sent you a reminder";

        notification.setLatestEventInfo(context, title, eventtext,
                intent);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Send the notification.
        notificationManager.notify("MindMe", 0, notification);

        if (!isAppOpen(context)) {
            WidgetService.getWidgetService().incrementNumber();
        }
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
