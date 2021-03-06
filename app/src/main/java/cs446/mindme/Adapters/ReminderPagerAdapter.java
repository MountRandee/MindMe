package cs446.mindme.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class ReminderPagerAdapter extends FragmentPagerAdapter {

    public ReminderPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return new cs446.mindme.Views.ViewReceived();
            case 1:
                return new cs446.mindme.Views.ViewSent();
            case 2:
                return new cs446.mindme.Views.ViewHistory();
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
