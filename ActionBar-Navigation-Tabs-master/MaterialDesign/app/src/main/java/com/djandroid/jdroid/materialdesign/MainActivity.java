package com.djandroid.jdroid.materialdesign;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.djandroid.jdroid.materialdesign.ClientLibrary.EauditingClient;
import com.djandroid.jdroid.materialdesign.ClientLibrary.HttpModel.AndroidTaskService.TaskInformation;
import com.djandroid.jdroid.materialdesign.ClientLibrary.Parameter.AuditStatus;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    List<TaskInformation> listfromserver;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout drawer_layout;
    ActionBar actionBar;
    Toolbar toolbar;
    GetProjectTask getprojecttask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mNavigationDrawerFragment = (NavigationDrawFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        listfromserver = new ArrayList<TaskInformation>();
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawer_layout, R.string.drawer_open, R.string.drawer_close) {
            //** Called when a drawer has settled in a completely open state. *//*
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            //** Called when a drawer has settled in a completely closed state. *//*
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };


        mDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer_layout.setDrawerListener(mDrawerToggle);
    }
    public class GetProjectTask extends AsyncTask<Void, Void, List<TaskInformation>> {
        GetProjectTask() {
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected List<TaskInformation> doInBackground(Void... params) {
            // TODO: attempt authentication against projectdetail network service.
            // Simulate network access.
            return EauditingClient.GetTaskList("admin", AuditStatus.None);
        }
        @Override
        protected void onPostExecute(final List<TaskInformation> success) {
            listfromserver.clear();
            for(int i=0; i < success.size();i++)
            {
                listfromserver.add(success.get(i));
                Log.d("projecttasklist",listfromserver.get(i).projectName);
                Log.d("projecttasklist",listfromserver.get(i).subcontractor);
                Log.d("projecttasklist",listfromserver.get(i).idTask);
                Log.d("projecttasklist",listfromserver.get(i).siteid);
                Log.d("projecttasklist",listfromserver.get(i).sitename);
                Log.d("projecttasklist",listfromserver.get(i).siteaddress);
                Log.d("projecttasklist",listfromserver.get(i).region);
                Log.d("projecttasklist",listfromserver.get(i).Categories);
            }
            //android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //getSupportFragmentManager().beginTransaction()
            //       .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
            //      .commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new ProjectRecycleFragment(listfromserver))
                    .commit();
            //transaction.addToBackStack(null);
            //transaction.commit();
            onSectionAttached(1);
        }
        @Override
        protected void onCancelled() {
        }
    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        switch (position) {
            case NavigationDrawFragment.NAVDRAWER_ITEM_FLOATING_ACTION:
                getprojecttask = new GetProjectTask();
                getprojecttask.execute((Void) null);
                break;
            case NavigationDrawFragment.NAVDRAWER_ITEM_TAB:

                onSectionAttached(2);
                break;
            case NavigationDrawFragment.NAVDRAWER_ITEM_CARD:
                onSectionAttached(6);
                break;
            case NavigationDrawFragment.NAVDRAWER_ITEM_DJ:
                onSectionAttached(3);
                String url = "http://www.dj-android.blogspot.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case NavigationDrawFragment.NAVDRAWER_ITEM_ABOUT:
                onSectionAttached(4);
                Intent i1 = new Intent(MainActivity.this, ProjectDetailActivity.class);
                startActivity(i1);
                break;
            case NavigationDrawFragment.NAVDRAWER_ITEM_SOCIAL:
                onSectionAttached(5);
                Intent i2 = new Intent(MainActivity.this, SocialActivity.class);
                startActivity(i2);
                break;

            default:

                break;
        }
    }

    public void onSectionAttached(int number) {
        Log.e("number", "--->" + number);
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
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                break;
        }
    }

    public void restoreActionBar() {

        actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mDrawerToggle.syncState();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
   public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            if(MyApplication.isLollipop()) {
                 rootView = inflater.inflate(R.layout.fragment_main, container, false);

            }else{
                rootView = inflater.inflate(R.layout.fragment_main_pre, container, false);
            }

            ListView list = (ListView) rootView.findViewById(R.id.listView);
            ListViewAdapter listAdapter = new ListViewAdapter(getActivity().getApplicationContext(),
                    getResources().getStringArray(R.array.android_version));
            list.setAdapter(listAdapter);

            if(MyApplication.isLollipop()) {
                FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_2);
                fab.attachToListView(list, new ScrollDirectionListener() {
                    @Override
                    public void onScrollDown() {
                        Log.d("ListViewFragment", "zhou_onScrollDown()");
                    }

                    @Override
                    public void onScrollUp() {
                        Log.d("ListViewFragment", "onScrollUp()");
                    }
                }, new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                        Log.d("ListViewFragment", "zhou_onScrollStateChanged()");
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        Log.d("ListViewFragment", "onScroll()");
                    }
                });
            }
            return rootView;
        }
        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            /*((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));*/
        }
    }
}
