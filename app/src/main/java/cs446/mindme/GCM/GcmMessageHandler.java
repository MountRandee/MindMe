package cs446.mindme.GCM;

import com.google.android.gms.gcm.GcmListenerService;
import android.os.Bundle;
import android.util.Log;

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
    }
}