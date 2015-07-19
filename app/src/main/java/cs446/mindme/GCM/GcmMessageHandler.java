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
        Log.e("GCM", data.toString());

        try {
            String reminderID = data.getBundle("results").getString("id");
            ConnectionData.showNotification(reminderID, MainActivity.getActivity());
        } catch (Exception e) {
            ConnectionData.showToast(e.getLocalizedMessage(), ConnectionData.callType.GCM);
        }
    }
}