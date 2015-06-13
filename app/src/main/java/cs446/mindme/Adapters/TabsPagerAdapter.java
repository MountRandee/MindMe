package cs446.mindme.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        System.out.println(index);
        switch (index) {
            case 0:
                // Received reminders view
                return new cs446.mindme.Received.ViewReceived();
            case 1:
                // Sent reminders view
                return new cs446.mindme.Sent.ViewSent();
            case 2:
                // History of reminders view
                return new cs446.mindme.History.ViewHistory();
        }
        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Note: For some reason views were being destroyed and recreated again,
        // e.g. going from deleting from received, going to history, coming back to received,
        // reminders reappear in received when they shouldn't
        // TODO: Figure out why
    }
}
