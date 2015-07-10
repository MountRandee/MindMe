package cs446.mindme;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import cs446.mindme.Adapters.FriendsAdapter;

/**
 * Created by richardfa on 15-07-10.
 */
public class CreateNewReminderDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;

    public CreateNewReminderDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_reminder_dialog);
        FriendsAdapter adapter = new FriendsAdapter(c, MainActivity.friends);
        ListView friendsList = (ListView) findViewById(R.id.friendslist);
        friendsList.setAdapter(adapter);
        //yes = (Button) findViewById(R.id.btn_yes);
        //no = (Button) findViewById(R.id.btn_no);
        //yes.setOnClickListener(this);
        //no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.btn_yes:
                c.finish();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();*/
    }
}
