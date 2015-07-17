package cs446.mindme;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;
import com.facebook.AccessToken;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import cs446.mindme.Adapters.FriendsAdapter;
import cs446.mindme.DataHolders.ReminderDataHolder;

public class CreateNewReminderDialog extends Dialog implements
        android.view.View.OnClickListener {

    public static CreateNewReminderDialog dialog = null;

    private boolean useContext;
    private Context _context;
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

    public CreateNewReminderDialog(Context context, String msg) {
        super(context);
        this._context = context;
        this.message = msg;
        this.useContext = true;
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
        if (useContext) {
            adapter = new FriendsAdapter(_context, friendsCopy);
        } else {
            adapter = new FriendsAdapter(c, friendsCopy);
        }

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
        if (useContext) {
            editText.setSingleLine(false);
            editText.setMaxLines(10);
            editText.setHorizontalScrollBarEnabled(false);
            editText.setScroller(new Scroller(_context));
            editText.setVerticalScrollBarEnabled(true);
            editText.setMovementMethod(new ScrollingMovementMethod());
        }
        if (message != null ) {
            editText.setText(message);
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                message = s.toString().replace("&","");
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
