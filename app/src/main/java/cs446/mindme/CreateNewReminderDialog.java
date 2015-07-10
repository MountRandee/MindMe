package cs446.mindme;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import cs446.mindme.Adapters.FriendsAdapter;

/**
 * Created by richardfa on 15-07-10.
 */
public class CreateNewReminderDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button cancel, send;
    public TextView reminding;

    public MainActivity.Friend selectedFriend;
    public ArrayList<MainActivity.Friend> friendsCopy;

    public CreateNewReminderDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_new_reminder_dialog);

        if (friendsCopy == null) {
            friendsCopy = new ArrayList<MainActivity.Friend>();
        } else if (!friendsCopy.isEmpty()) {
            friendsCopy.clear();
        }

        friendsCopy.addAll(MainActivity.friends);
        for (int i = 0 ; i < 10 ; i++) {
            Random rand = new Random();
            int rand1 = rand.nextInt((50000 - 10000) + 1) + 10000;
            int rand2 = rand.nextInt((99999 - 10000) + 1) + 10000;
            int rand3 = rand.nextInt((99999 - 10000) + 1) + 10000;
            friendsCopy.add(new MainActivity.Friend("Test Account " + i, "" + rand1 + rand2 + rand3));
        }

        reminding = (TextView) findViewById(R.id.remindingText);

        final FriendsAdapter adapter = new FriendsAdapter(c, friendsCopy);
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
            }
        });
        cancel = (Button) findViewById(R.id.cancelnewreminder);
        send = (Button) findViewById(R.id.sendnewreminder);
        cancel.setOnClickListener(this);
        send.setOnClickListener(this);
        updateReminding();
    }

    public void updateReminding() {
        reminding.setText("REMINDING: ");
        if (selectedFriend != null) {
            reminding.append(selectedFriend.name);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelnewreminder:
                //c.finish();
                dismiss();
                break;
            case R.id.sendnewreminder:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
