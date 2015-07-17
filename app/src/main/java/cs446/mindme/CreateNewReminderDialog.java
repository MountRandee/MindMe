package cs446.mindme;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import cs446.mindme.Adapters.FriendsAdapter;
import cs446.mindme.DataHolders.ReminderDataHolder;

/**
 * Created by richardfa on 15-07-10.
 */
public class CreateNewReminderDialog extends Dialog implements
        android.view.View.OnClickListener {

    public static CreateNewReminderDialog dialog = null;

    public Activity c;
    public Dialog d;
    public Button cancel, send, refresh;
    public String message;
    public TextView reminding;

    public MainActivity.Friend selectedFriend;
    public ArrayList<MainActivity.Friend> friendsCopy;

    FriendsAdapter adapter;

    public CreateNewReminderDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_new_reminder_dialog);

        if (friendsCopy == null) {
            friendsCopy = new ArrayList<MainActivity.Friend>();
        } else if (!friendsCopy.isEmpty()) {
            friendsCopy.clear();
        }
        if (MainActivity.friends != null) {
            friendsCopy.addAll(MainActivity.friends);
        }

        reminding = (TextView) findViewById(R.id.remindingText);

        adapter = new FriendsAdapter(c, friendsCopy);
        ListView friendsList = (ListView) findViewById(R.id.friendslist);
        friendsList.setAdapter(adapter);
        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectedFriend == null || !selectedFriend.id.equals(friendsCopy.get(position).id)) {
                    selectedFriend = friendsCopy.get(position);
                    adapter.clearSelected();
                    view.setBackgroundColor(Color.parseColor("#B4CDAE"));
                    //adapter.setSelected(position);
                } else {
                    selectedFriend = null;
                    adapter.clearSelected();
                }
                updateReminding();
                updateButton();
            }
        });
        cancel = (Button) findViewById(R.id.cancelnewreminder);
        send = (Button) findViewById(R.id.sendnewreminder);
        refresh = (Button) findViewById(R.id.refreshFriendsButtonBtn);
        cancel.setOnClickListener(this);
        send.setOnClickListener(this);
        refresh.setOnClickListener(this);
        final EditText editText = (EditText) findViewById(R.id.newReminderMessage);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                message = s.toString();
                updateButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        updateReminding();
        updateButton();
        setRefreshButton(true);
    }

    public void updateReminding() {
        reminding.setText("REMINDING: ");
        if (selectedFriend != null) {
            reminding.append(selectedFriend.name);
        }
    }

    public void updateButton() {
        if (selectedFriend == null) {
            send.setEnabled(false);
            return;
        }
        if (message == null || message.isEmpty()) {
            if (send != null) {
                send.setEnabled(false);
            }
        } else {
            if (send != null) {
                send.setEnabled(true);
            }
        }
    }

    public void setRefreshButton(boolean enabled) {
        refresh.setEnabled(enabled);
        if (enabled) {
            refresh.setText("Refresh");
        } else {
            refresh.setText("Loading...");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelnewreminder:
                dismiss();
                break;
            case R.id.sendnewreminder:
                Date date = new Date();
                if (message.isEmpty() || selectedFriend == null) {
                    return;
                }
                SampleData.getSentList().add(new ReminderDataHolder(ReminderDataHolder.reminderType.SENT, message, selectedFriend, date, ReminderDataHolder.reminderStatus.ACTIVE, "0"));
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("message", message);
                params.put("author_id", AccessToken.getCurrentAccessToken().getUserId());
                params.put("assignee_id", selectedFriend.id);
                ConnectionData.post("/api/v1/reminder/create/", params, true);
                dismiss();
                break;
            case R.id.refreshFriendsButtonBtn:
                ConnectionData.setupProfile(getContext());
                setRefreshButton(false);
                break;
            default:
                break;
        }
    }

    public void completeRefreshList() {
        if (friendsCopy == null) {
            friendsCopy = new ArrayList<MainActivity.Friend>();
        } else if (!friendsCopy.isEmpty()) {
            friendsCopy.clear();
        }
        if (MainActivity.friends != null) {
            friendsCopy.addAll(MainActivity.friends);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        setRefreshButton(true);
    }
}
