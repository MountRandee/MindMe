package cs446.mindme.GCM;

import com.google.android.gms.gcm.GcmListenerService;
import android.os.Bundle;
import android.util.Log;

import cs446.mindme.ConnectionData;
import cs446.mindme.DataHolders.ReminderDataHolder;
import cs446.mindme.MainActivity;
import cs446.mindme.SampleData;

public class GcmMessageHandler extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        Log.d("GCM", "Handle incoming message");

        String sender = data.getString("sender");
        String title = data.getString("title");
        String message = data.getString("message");

        Log.d("GCM", "From: " + sender);
        Log.d("GCM", "Title: " + title);
        Log.d("GCM", "Message: " + message);

        // populate push notification
        ConnectionData.showNotification(sender, message, MainActivity.getActivity());
    }
}