package cs446.mindme;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
        SampleData.receivedList.addAll(received);
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
        SampleData.sentList.addAll(sent);
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
        SampleData.historyList.addAll(history);
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

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showNoNetworkToast(Context context) {
        CharSequence text = "Network Unavailable";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private ConnectionData() {
    }
}
