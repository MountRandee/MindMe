package cs446.mindme;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import cs446.mindme.DataHolders.ReminderDataHolder;

/**
 * Created by richardfa on 15-07-12.
 */
public class ConnectionData {
    private static ConnectionData ourInstance = new ConnectionData();

    public static String MINDME_SHARED_PREF = "mindmesharedpref";

    public static String SHARED_USER_ID = "shareduserid";
    public static String SHARED_RECEIVED_REMINDERS = "sharedreceivedreminders";
    public static String SHARED_SENT_REMINDERS = "sharedsentreminders";
    public static String SHARED_HISTORY_REMINDERS = "sharedhistoryreminders";

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

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                // String key = new String(Base64.encodeBytes(md.digest()));
                return key;
            }
        } catch (PackageManager.NameNotFoundException e1) {
            return e1.toString();
        } catch (NoSuchAlgorithmException e) {
            return e.toString();
        } catch (Exception e) {
            return e.toString();
        }

        return key;
    }

    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isNetworkAvailable(Context context) {
        /*if (checkNetwork(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                Log.e("Network", urlc.getResponseCode() == 200 ? "True" : "False");
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e("Network", "False");
                return false;
            } catch (Exception e) {
                Log.e("Network", "False");
                return false;
            }
        }
        Log.e("Network", "False");
        return false;*/
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            /*
             * Toast.makeText(getActivity(), "No Internet connection!",
             * Toast.LENGTH_LONG).show();
             */
            return false;
        }
        return true;
        //return true;
    }

    public static boolean checkNetworkAvailable(Context context) {
        if (!isNetworkAvailable(context)) {
            Toast toast = Toast.makeText(context, "Network Unavailable", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    public static void updateAction() {
        if (MainActivity.getActivity() == null) {
            return;
        }
        if (!isNetworkAvailable(MainActivity.getActivity())) {
            Toast toast = Toast.makeText(MainActivity.getActivity(), "Network Unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static void setupProfile(Context context){
        if (!isNetworkAvailable(context)) {
            return;
        }
        //Log.e("FBProfile", "ID: " + Profile.getCurrentProfile().getId());
        //Log.e("FBProfile", "Name: " + Profile.getCurrentProfile().getName());
        ConnectionData.setSharedUserID(context);
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
                                    //Log.e("FBProfile", "Name: " + array.getJSONObject(i).getString("name") + ", ID: " + array.getJSONObject(i).getString("id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (CreateNewReminderDialog.dialog != null && CreateNewReminderDialog.dialog.isShowing()) {
                            CreateNewReminderDialog.dialog.completeRefreshList();
                        }
                    }
                }
        ).executeAsync();
    }

    public static void showNotification(String eventtext, Context ctx) {
        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.login_logo,
                eventtext, System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this
        // notification
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                new Intent(ctx, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(ctx, "MindMe: ", eventtext,
                contentIntent);

        // Send the notification.
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify("Title", 0, notification);
    }



    private ConnectionData() {
    }
}
