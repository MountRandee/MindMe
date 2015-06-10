package cs446.mindme;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                // Received reminders
                return new cs446.mindme.Received.ViewReceived();
            case 1:
                // Sent reminders
                return new cs446.mindme.Sent.ViewSent();
            case 2:
                // History of reminders
                return new cs446.mindme.History.ViewHistory();
        }
        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
