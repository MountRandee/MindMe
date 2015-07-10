package cs446.mindme.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cs446.mindme.MainActivity;
import cs446.mindme.R;

/**
 * Created by richardfa on 15-07-10.
 */
public class FriendsAdapter extends ArrayAdapter<MainActivity.Friend> {
    public FriendsAdapter(Context context, ArrayList<MainActivity.Friend> friends) {
        super(context, 0, friends);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MainActivity.Friend friend = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_friend, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.friendname);
        TextView id = (TextView) convertView.findViewById(R.id.friendid);
        // Populate the data into the template view using the data object
        name.setText(friend.name);
        id.setText(friend.id);
        // Return the completed view to render on screen
        return convertView;
    }
}
