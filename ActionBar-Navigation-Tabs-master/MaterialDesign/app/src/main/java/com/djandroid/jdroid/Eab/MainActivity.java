package com.djandroid.jdroid.Eab;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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

import com.djandroid.jdroid.Eab.ClientLibrary.Common.ClientConfiguration;
import com.djandroid.jdroid.Eab.ClientLibrary.Common.NetworkException;
import com.djandroid.jdroid.Eab.ClientLibrary.EauditingClient;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Helper.TaskInformation;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.TaskOtherInformation;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Response.TaskListForAuditorResponse;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.AuditStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
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
    private String lastmodifiedtime;
    public static String username;
    //public static int APPSTATUS; //0 is online, 1 is offline
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

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer_layout.setDrawerListener(mDrawerToggle);
    }
    public class GetProjectTask extends AsyncTask<Void, Void, TaskListForAuditorResponse> {
        AuditStatus status;
        GetProjectTask(AuditStatus temp) {
            this.status = temp;
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected TaskListForAuditorResponse doInBackground(Void... params) {
            // TODO: attempt authentication against projectdetail network service.
            // Simulate network access.
            try {
                return EauditingClient.GetTaskList(username, status);
            } catch (NetworkException e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(final TaskListForAuditorResponse success) {
            listfromserver.clear();
            Log.d("MainActivity","Get the Task List succeed!");
            if(success != null)
            {
                for(int i=0; i < success.taskList.size();i++)
                {
                    listfromserver.add(success.taskList.get(i));
                }
                saveTaskList();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new ProjectRecycleFragment(listfromserver,status))
                        .commit();
            }
        }
        @Override
        protected void onCancelled() {
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position){
        // update the main content by replacing fragments
        switch (position) {
            case NavigationDrawFragment.NOTDONE:
                if(isNetworkAvailable(getApplicationContext())) {
                    getprojecttask = new GetProjectTask(AuditStatus.None);
                    getprojecttask.execute((Void) null);
                }
                else
                {
                    try {
                        readtaskList();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new ProjectRecycleFragment(listfromserver,AuditStatus.None))
                            .commit();

                }
                onSectionAttached(1);
                break;
            case NavigationDrawFragment.WAITAUDIT:
                getprojecttask = new GetProjectTask(AuditStatus.Waiting);
                getprojecttask.execute((Void) null);
                onSectionAttached(2);
                break;
            case NavigationDrawFragment.HAVEPASS:
                getprojecttask = new GetProjectTask(AuditStatus.Pass);
                getprojecttask.execute((Void) null);
                onSectionAttached(3);
                break;
            case NavigationDrawFragment.SETTING:
                //onSectionAttached(4);
                Toast.makeText(this,"当前版本是"+String.valueOf(ClientConfiguration.Version)+"2017/01/08",Toast.LENGTH_SHORT).show();
                break;
            case NavigationDrawFragment.QUIT:
                //onSectionAttached(4);
                filedelete(getString(R.string.UserCache) + String.valueOf(ClientConfiguration.Version));
                Toast.makeText(this,"登出成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                break;
        }
    }

    public boolean filedelete(String strFile)
    {
        try
        {
            File f=new File(this.getFilesDir().getPath() + "/" + strFile);
            if(f.exists()) f.delete();
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    //保存任务列表
    private void saveTaskList() {
        try {
            FileOutputStream outputStream = openFileOutput(MainActivity.username + "tasklist",
                    Activity.MODE_PRIVATE);
            outputStream.write(new Gson().toJson(listfromserver).getBytes());
            outputStream.flush();
            outputStream.close();
            //Toast.makeText(this, "tasklist保存成功", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取任务列表
    public void readtaskList() throws IOException{
        String res="";
        try{
            if(fileIsExists(MainActivity.username+"tasklist")) {
                FileInputStream fin = openFileInput(MainActivity.username+"tasklist");
                int length = fin.available();
                byte[] buffer = new byte[length];
                fin.read(buffer);
                res = EncodingUtils.getString(buffer, "UTF-8");
                Type listType = new TypeToken<ArrayList<TaskInformation>>(){}.getType();
                listfromserver = new Gson().fromJson(res, listType);
                Log.d("readtasklist:",String.valueOf(listfromserver.size()));
                //Toast.makeText(this, String.valueOf(taskcategorydetail.get(2)), Toast.LENGTH_SHORT).show();
                fin.close();
            }
            else {
                Log.d("ProjectDetail","未读取到新图片列表缓存文件");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        // return res;
    }
    public boolean fileIsExists(String strFile)
    {
        try
        {
            File f=new File(this.getFilesDir().getPath() + "/" + strFile);
            if(!f.exists())
            {
                return false;
            }
            long time=f.lastModified();
            SimpleDateFormat formatter = new
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            lastmodifiedtime=formatter.format(time);
        }
        catch (Exception e)
        {
            return false;
        }


        return true;
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
            //getMenuInflater().inflate(R.menu.main, menu);
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
      //  if (id == R.id.action_settings) {
     //       return true;
     //   }

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
