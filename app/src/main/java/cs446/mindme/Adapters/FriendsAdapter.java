package cs446.mindme.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.Iterator;

import cs446.mindme.MainActivity;
import cs446.mindme.R;

/**
 * Created by richardfa on 15-07-10.
 */
public class FriendsAdapter extends ArrayAdapter<MainActivity.Friend> {
    public ArrayList<LinearLayout>itemViews = new ArrayList<LinearLayout>();

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
        name.setText(friend.name);
        ProfilePictureView profilePictureView;
        profilePictureView = (ProfilePictureView) convertView.findViewById(R.id.friendimage);
        profilePictureView.setProfileId(friend.id);

        itemViews.add(position, (LinearLayout)convertView.findViewById(R.id.mainItemView));
        // Populate the data into the template view using the data object
        // Return the completed view to render on screen
        return convertView;
    }

    public void clearSelected() {
        Iterator<LinearLayout> itemViewsIterator = itemViews.iterator();
        while (itemViewsIterator.hasNext()) {
            itemViewsIterator.next().setBackgroundColor(Color.parseColor("#DADADA"));
        }
    }

    public void setSelected(int position) {
        itemViews.get(position).setBackgroundColor(Color.parseColor("#B4CDAE"));
    }
}
