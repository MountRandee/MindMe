package cs446.mindme;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import cs446.mindme.Adapters.ReminderPagerAdapter;
import cs446.mindme.DataHolders.ReminderDataHolder;
import cs446.mindme.Views.ViewEmpty;
import cs446.mindme.Views.ViewEvent;
import cs446.mindme.Views.ViewSidePanelMenu;
import cs446.mindme.Views.ViewMisc;

public class MainActivity extends FragmentActivity implements ViewSidePanelMenu.NavigationDrawerCallbacks {

    private static MainActivity activity;
    public static MainActivity getActivity() { return activity; }

    public static ArrayList<Friend> friends;

    public static class Friend {
        public String name;
        public String id;
        public boolean isSelected = false;

        public Friend(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public static Friend getFriend(String id) {
            for (Friend friend : friends) {
                if (id.equals(friend.id)) {
                    return friend;
                }
            }
            return new Friend("Unknown", id);
        }
    }

    ActionBar actionBar;
    ReminderPagerAdapter reminderPagerAdapter;
    ViewPager viewPager;
    boolean populateOnce = false;

    Timer timer;
    TimerTask timerTask;

    private ViewSidePanelMenu mNavigationDrawerFragment;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("creating main activity");
        super.onCreate(savedInstanceState);


        /*SharedPreferences prefs = getSharedPreferences(ConnectionData.MINDME_SHARED_PREF, Context.MODE_PRIVATE);
        String sharedGCM = prefs.getString(ConnectionData.SHARED_GCM_ID, "");

        if (sharedGCM.isEmpty()) {
            if (ConnectionData.gcm == null) {
                ConnectionData.gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
            }
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            //instanceID.deleteInstanceID ();

            try {
                ConnectionData.regid = instanceID.getToken(ConnectionData.PROJECT_NUMBER,
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ConnectionData.msg = "Device registered, registration ID=" + ConnectionData.regid;
            Log.i("GCM", ConnectionData.msg);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(ConnectionData.SHARED_GCM_ID, ConnectionData.regid);
            editor.apply();
        }

        String sharedToken = prefs.getString(ConnectionData.SHARED_TOKEN, "");
        String sharedFB = prefs.getString(ConnectionData.SHARED_FB_ID, "");

        if (sharedToken.isEmpty() || !sharedToken.equals(AccessToken.getCurrentAccessToken().getToken())
                || sharedFB.isEmpty() || !sharedFB.equals(AccessToken.getCurrentAccessToken().getUserId())) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("/api/v1/user/login/");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("token",
                        AccessToken.getCurrentAccessToken().getToken()));
                nameValuePairs.add(new BasicNameValuePair("expiration",
                        AccessToken.getCurrentAccessToken().getExpires().toString()));
                nameValuePairs.add(new BasicNameValuePair("fb_id",
                        AccessToken.getCurrentAccessToken().getUserId()));
                nameValuePairs.add(new BasicNameValuePair("gcm_id",
                        ConnectionData.regid));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = client.execute(post);
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    System.out.println(line);
                }

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(ConnectionData.SHARED_FB_ID, AccessToken.getCurrentAccessToken().getUserId());
                editor.putString(ConnectionData.SHARED_TOKEN, AccessToken.getCurrentAccessToken().getToken());
                editor.apply();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/


        activity = this;
        if(timer != null){
            timer.cancel();
        }
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                ConnectionData.updateAction();
            }
        };
        timer.schedule(timerTask, 10000, 10000);

        startService(new Intent(this, WidgetService.class));

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (ViewSidePanelMenu)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // TODO: remove this
        SampleData.populateSampleData();
        ConnectionData.applyAllSharedReminders(getApplicationContext());
        if (SampleData.receivedList.isEmpty() && SampleData.sentList.isEmpty() && SampleData.historyList.isEmpty()) {
            SampleData.populateSampleData();
        }

        actionBar = getActionBar();
        reminderPagerAdapter = new ReminderPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOnPageChangeListener(
            new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    // When swiping between pages, select the corresponding tab.
                    getActionBar().setSelectedNavigationItem(position);
                    // System.out.println("onPageSelected: " + position);
                }
            }
        );
        viewPager.setAdapter(reminderPagerAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // show the given tab
                viewPager.setCurrentItem(tab.getPosition());
                 // System.out.println("onTabSelected: " + tab.getPosition());
            }
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        // Add 3 tabs, specifying the tab's text and TabListener
        actionBar.addTab(actionBar.newTab().setText(ReminderDataHolder.reminderType.RECEIVED.toString())
                .setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(ReminderDataHolder.reminderType.SENT.toString())
                .setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(ReminderDataHolder.reminderType.HISTORY.toString())
                .setTabListener(tabListener));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (WidgetService.getWidgetService() != null) {
            WidgetService.getWidgetService().toggleVisibility(false);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(position) {
            default:
            case 0:
                if (viewPager != null) {
                    viewPager.setVisibility(View.VISIBLE);
                    fragment = new ViewEmpty();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    // viewPager.getRootView().setVisibility(View.VISIBLE);
                }

                break;
            case 1:
                fragment = new ViewEvent();
                viewPager.setVisibility(View.INVISIBLE);;
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setTitle(mTitle);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;
            case 2:
                fragment = new ViewMisc();
                viewPager.setVisibility(View.INVISIBLE);;
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setTitle(mTitle);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       /* getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;*/
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.

            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.new_reminder_icon) {
            CreateNewReminderDialog dialog = new CreateNewReminderDialog(this);
            dialog.show();
            return true;
        } else if (id == R.id.logout_settings) {
            LoginManager.getInstance().logOut();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (timer != null){
            timer.cancel();
            timer = null;
        }*/
    }
     /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 0:
                if(resultCode == RESULT_OK) {
                    //Do something useful with data
                }
                break;
        }
    }*/

}