package com.djandroid.jdroid.materialdesign;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.djandroid.jdroid.materialdesign.ClientLibrary.EauditingClient;
import com.djandroid.jdroid.materialdesign.ClientLibrary.HttpModel.AndroidTaskService.TaskDetailResponse;
import com.djandroid.jdroid.materialdesign.ClientLibrary.HttpModel.AndroidTaskService.TaskInformation;
import com.djandroid.jdroid.materialdesign.ClientLibrary.Parameter.AuditStatus;
import com.google.gson.Gson;

import java.util.List;


public class ProjectDetailActivity extends AppCompatActivity
   {

    private CharSequence mTitle;
    Toolbar toolbar;
    TextView projecttitle,projectname,projectarea,projectsiteid,projectsitename,projectsiteaddress,projectsitecontractor,projectcatagory;
    Button detailbutton;
    GetProjectDetail getprojectdetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectdetail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("" + getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_drawer_explore);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        projecttitle = (TextView) findViewById(R.id.textView1);
        projectname = (TextView) findViewById(R.id.textView2);
        projectarea = (TextView) findViewById(R.id.textView3);
        projectsiteid = (TextView) findViewById(R.id.textView4);
        projectsitename = (TextView) findViewById(R.id.textView5);
        projectsiteaddress = (TextView) findViewById(R.id.textView6);
        projectsitecontractor = (TextView) findViewById(R.id.textView7);
        projectcatagory = (TextView) findViewById(R.id.textView8);

        detailbutton = (Button) findViewById(R.id.detailbutton);
        Intent intent = getIntent();
        final TaskInformation temp = new Gson().fromJson(intent.getStringExtra("TaskInfomation"),TaskInformation.class);
        projecttitle.setText(temp.projectName);
        projectname.setText("项目名称："+temp.projectName);
        projectarea.setText("区域："+temp.region);
        projectsiteid.setText("站点ID："+temp.siteid);
        projectsitename.setText("站点名称："+temp.sitename);
        projectsiteaddress.setText("站点地址："+temp.siteaddress);
        projectsitecontractor.setText("分包商："+temp.subcontractor);
        projectcatagory.setText("模块："+temp.Categories);



        detailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getprojectdetail = new GetProjectDetail(temp.idTask);
                getprojectdetail.execute((Void) null);

                //Intent intent = new Intent(ProjectDetailActivity.this,ProjectItemActivity.class);
               // startActivity(intent);
            }
        });
    }
       public class GetProjectDetail extends AsyncTask<Void, Void, TaskDetailResponse> {
           String taskid;
           GetProjectDetail(String taskid) {
            this.taskid = taskid;
           }
           @Override
           protected void onPreExecute() {
           }
           @Override
           protected TaskDetailResponse doInBackground(Void... params) {
               // TODO: attempt authentication against projectdetail network service.
               // Simulate network access.
               return EauditingClient.GetTaskDetail(taskid);
           }
           @Override
           protected void onPostExecute(final TaskDetailResponse success) {
               Intent intent = new Intent(ProjectDetailActivity.this,ProjectItemActivity.class);
               intent.putExtra("projectdetail",new Gson().toJson(success));
               startActivity(intent);
           }
           @Override
           protected void onCancelled() {
           }
       }
       @Override
       public boolean onCreateOptionsMenu(Menu menu) {
               getMenuInflater().inflate(R.menu.main, menu);
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

           return super.onOptionsItemSelected(item);
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

}
