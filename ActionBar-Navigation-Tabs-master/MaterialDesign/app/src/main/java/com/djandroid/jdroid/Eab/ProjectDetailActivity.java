package com.djandroid.jdroid.Eab;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.djandroid.jdroid.Eab.ClientLibrary.Common.NetworkException;
import com.djandroid.jdroid.Eab.ClientLibrary.EauditingClient;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.PictureService.Response.GetPictureResponse;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Helper.TaskInformation;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Response.TaskItemForAuditorResponse;
import com.google.gson.Gson;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class ProjectDetailActivity extends AppCompatActivity
   {

    private CharSequence mTitle;
    public  static TaskItemForAuditorResponse taskdetailresponse;
    private List<String> needdownloadpicture;
    private int needdownloadpicturenumber;
    private String lastmodifiedtime;
    public static String taskid;
    public static String taskname;
    public static int savepicturenum;
    public static List<String> newpicid = new ArrayList<String>();  //read from file in this activity
    Toolbar toolbar;
    ProgressBar progressBar;
    TextView progresstext,projecttitle,projectname,projectarea,projectsiteid,projectsitename,projectsiteaddress,projectsitecontractor,projectcatagory;
    TextView projectdelegate,areasize,areatype,pmodelegate,projecttaskdate;
    Button detailbutton,updatebutton;

    GetProjectDetail getprojectdetail;
    GetPicture getpicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_PROGRESS);
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_projectdetail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("" + getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
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
        projectdelegate = (TextView) findViewById(R.id.textView9);
        areasize = (TextView) findViewById(R.id.textView10);
        areatype = (TextView) findViewById(R.id.textView11);
        pmodelegate = (TextView) findViewById(R.id.textView12);
        projecttaskdate = (TextView) findViewById(R.id.textView13);

        progresstext = (TextView) findViewById(R.id.progresstext);
        progressBar = (ProgressBar) findViewById(R.id.progressbar); //progressbar
        progressBar.setVisibility(View.GONE);
        progresstext.setVisibility(View.GONE);
        detailbutton = (Button) findViewById(R.id.detailbutton);
        updatebutton = (Button) findViewById(R.id.updatebutton);
        Intent intent = getIntent();
        final TaskInformation temp = new Gson().fromJson(intent.getStringExtra("TaskInfomation"), TaskInformation.class);

        taskid = temp.taskId;
        taskname = temp.projectName;
        if (temp.projectName.length() == 0)
            projecttitle.setVisibility(View.GONE);
        else {
            projecttitle.setVisibility(View.VISIBLE);
            projecttitle.setText(temp.projectName);
        }
        if (temp.projectName.length() == 0)
            projectname.setVisibility(View.GONE);
        else {
            projectname.setVisibility(View.VISIBLE);
            projectname.setText("项目名称：" + temp.projectName);
        }
        if (temp.taskRegion.length() == 0)
            projectarea.setVisibility(View.GONE);
        else {
            projectarea.setVisibility(View.VISIBLE);
            projectarea.setText("区域：" + temp.taskRegion);
        }
        if (temp.taskSiteId.length() == 0)
            projectsiteid.setVisibility(View.GONE);
        else {
            projectsiteid.setVisibility(View.VISIBLE);
            projectsiteid.setText("站点ID：" + temp.taskSiteId);
        }
        if (temp.taskSiteName.length() == 0)
            projectsitename.setVisibility(View.GONE);
        else
        {
            projectsitename.setVisibility(View.VISIBLE);
            projectsitename.setText("站点名称："+temp.taskSiteName);
        }
        if(temp.taskSiteAddress.length() == 0)
            projectsiteaddress.setVisibility(View.GONE);
        else
        {
            projectsiteaddress.setVisibility(View.VISIBLE);
            projectsiteaddress.setText("站点地址："+temp.taskSiteAddress);
        }
        if(temp.taskSubcontractor.length() == 0)
            projectsitecontractor.setVisibility(View.GONE);
        else
        {
            projectsitecontractor.setVisibility(View.VISIBLE);
            projectsitecontractor.setText("分包商："+temp.taskSubcontractor);
        }
        if(temp.taskTabs.length() == 0)
            projectcatagory.setVisibility(View.GONE);
        else
        {
            projectcatagory.setVisibility(View.VISIBLE);
            projectcatagory.setText("模块："+temp.taskTabs);
        }
        if(temp.taskRepresent.length() == 0)
            projectdelegate.setVisibility(View.GONE);
        else {
            projectdelegate.setVisibility(View.VISIBLE);
            projectdelegate.setText("代表处:" + temp.taskRepresent);
        }
        if(temp.taskBuildingArea.length() == 0)
            areasize.setVisibility(View.GONE);
        else
        {
            areasize.setVisibility(View.VISIBLE);
            areasize.setText("建筑面积:"+temp.taskBuildingArea);
        }
        if(temp.taskBuildingArea.length() == 0)
            areatype.setVisibility(View.GONE);
        else
        {
            areatype.setVisibility(View.VISIBLE);
            areatype.setText("建筑类型:"+temp.taskBuildingType);
        }
        if(temp.taskPmo.length() == 0)
            pmodelegate.setVisibility(View.GONE);
        else
        {
            pmodelegate.setVisibility(View.VISIBLE);
            pmodelegate.setText("PMO代表:" + temp.taskPmo);
        }
        if(temp.taskDate.length() == 0)
            projecttaskdate.setVisibility(View.GONE);
        else
        {
            projecttaskdate.setVisibility(View.VISIBLE);
            projecttaskdate.setText("任务日期:"+temp.taskDate);
        }


        taskdetailresponse = new TaskItemForAuditorResponse();
        needdownloadpicture = new ArrayList<>();
        needdownloadpicturenumber = 0;
        newpicid.clear(); //清空newpicid之后在读取
        savepicturenum = 0;

        try {
            readPictureNewList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        detailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    readtask(temp.taskId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        if(fileIsExists(taskid + "task")) {
            updatebutton.setEnabled(false);
        }
        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "正在下载", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.VISIBLE);
                progresstext.setVisibility(View.VISIBLE);
                setProgressBarVisibility(true);
                //setProgressBarIndeterminate(true);
                setProgress(0);
                getprojectdetail = new GetProjectDetail(temp.taskId);
                getprojectdetail.execute((Void) null);
            }
        });

    }

       private void saveTask(String taskid) {
           try {
               FileOutputStream outputStream = openFileOutput(taskid+"task",
                       Activity.MODE_PRIVATE);
               outputStream.write(new Gson().toJson(taskdetailresponse).getBytes());
               outputStream.flush();
               outputStream.close();
               Toast.makeText(this, "脱机任务保存成功", Toast.LENGTH_LONG).show();
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }

       public void readtask(String taskid) throws IOException{
           String res="";
           try{
               if(fileIsExists(taskid + "task")) {
                   FileInputStream fin = openFileInput(taskid + "task");
                   int length = fin.available();
                   byte[] buffer = new byte[length];
                   fin.read(buffer);
                   res = EncodingUtils.getString(buffer, "UTF-8");
                   taskdetailresponse = new Gson().fromJson(res, TaskItemForAuditorResponse.class);
                   Toast.makeText(this,"使用来自"+lastmodifiedtime+"下载的脱机版本",Toast.LENGTH_SHORT).show();
                   fin.close();
                   GotoCategoryActivity();
               }
               else {
                   Toast.makeText(getApplicationContext(), "未下载脱机模式资料", Toast.LENGTH_LONG).show();
               }
           }
           catch(Exception e){
               e.printStackTrace();
           }
           // return res;
       }

       public class GetProjectDetail extends AsyncTask<Void, Void, TaskItemForAuditorResponse> {
           String taskid;
           GetProjectDetail(String taskid) {
            this.taskid = taskid;
           }
           @Override
           protected void onPreExecute() {
           }
           @Override
           protected TaskItemForAuditorResponse doInBackground(Void... params) {
               // TODO: attempt authentication against projectdetail network service.
               // Simulate network access.
               try {
                   return EauditingClient.GetTaskDetail(MainActivity.username,taskid);
               } catch (NetworkException e) {
                   e.printStackTrace();
               }
               return null;
           }
           @Override
           protected void onPostExecute(final TaskItemForAuditorResponse success) {
               taskdetailresponse = success;
               saveTask(taskid);
               getAllPictureName();
               if(needdownloadpicture.size() == 0)
               {
                   GotoCategoryActivity();
               }
               for (int i = 0; i < needdownloadpicture.size(); i++) {
                   getpicture = new GetPicture(needdownloadpicture.get(i),taskid);
                   getpicture.execute((Void) null);
               }
           }
           @Override
           protected void onCancelled() {
           }
       }
       private void GotoCategoryActivity()
       {
           Intent intent = new Intent(ProjectDetailActivity.this, ProjectItemActivity.class);
           startActivity(intent);
       }
       private void getAllPictureName()
       {
           for(int i = 0 ; i < taskdetailresponse.taskCategoryList.size(); i++)

               for(int j = 0 ; j < taskdetailresponse.taskCategoryList.get(i).taskItemList.size();j++) {

                   if(taskdetailresponse.taskCategoryList.get(i).taskItemList.get(j).goodPictureList!=null)
                   {
                       for (int k = 0; k < taskdetailresponse.taskCategoryList.get(i).taskItemList.get(j).goodPictureList.size(); k++) {
                           if (!fileIsExists(taskdetailresponse.taskCategoryList.get(i).taskItemList.get(j).goodPictureList.get(k).pictureName)) {
                               needdownloadpicture.add(taskdetailresponse.taskCategoryList.get(i).taskItemList.get(j).goodPictureList.get(k).pictureName);
                           }
                       }
                   }

                   if(taskdetailresponse.taskCategoryList.get(i).taskItemList.get(j).badPictureList!=null)
                   {
                       for (int k = 0; k < taskdetailresponse.taskCategoryList.get(i).taskItemList.get(j).badPictureList.size(); k++) {
                           if (!fileIsExists(taskdetailresponse.taskCategoryList.get(i).taskItemList.get(j).badPictureList.get(k).pictureName)) {
                               needdownloadpicture.add(taskdetailresponse.taskCategoryList.get(i).taskItemList.get(j).badPictureList.get(k).pictureName);
                           }
                       }
                   }
               }
           needdownloadpicturenumber = needdownloadpicture.size();
       }
       private void savePicture(String filename , String content) {
           try {
               FileOutputStream outputStream = openFileOutput(filename,
                       Activity.MODE_PRIVATE);
               outputStream.write(content.getBytes());
               outputStream.flush();
               outputStream.close();
               savepicturenum++;
               progressBar.setProgress(savepicturenum * 100 / needdownloadpicturenumber);
               progresstext.setText("已经下载："+String.valueOf(savepicturenum * 100 / needdownloadpicturenumber)+"%");
               if(savepicturenum == needdownloadpicturenumber) {
                   GotoCategoryActivity();
               }
               Log.v("SAVEPICTURE","下载图片"+String.valueOf(savepicturenum)+"成功" + String.valueOf(savepicturenum));
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }

       public class GetPicture extends AsyncTask<Void, Void, GetPictureResponse> {
           String picturename;
           String taskid;
           GetPicture(String picturename,String taskid) {
               this.picturename = picturename;
               this.taskid=taskid;
           }
           @Override
           protected void onPreExecute() {
           }
           @Override
           protected GetPictureResponse doInBackground(Void... params) {
               // TODO: attempt authentication against projectdetail network service.
               // Simulate network access.
               try {
                   return EauditingClient.PictureGet(picturename,taskid);
               } catch (NetworkException e) {
                   e.printStackTrace();
               }
               return null;
           }
           @Override
           protected void onPostExecute(final GetPictureResponse success) {
               String newpic = success.pictureBase64.replace("\n","");
               savePicture(picturename,newpic);
               //Log.v("Main",success);
           }
           @Override
           protected void onCancelled() {
           }
       }

       @Override
       protected void onResume() {
           super.onResume();
           if(fileIsExists(taskid + "task")) {
               updatebutton.setEnabled(false);
           }
           progressBar.setVisibility(View.GONE);
           progresstext.setVisibility(View.GONE);
       }

       public void readPictureNewList() throws IOException{
           String res="";
           try{
               if(fileIsExists(ProjectDetailActivity.taskid)) {
                   FileInputStream fin = openFileInput(ProjectDetailActivity.taskid);
                   int length = fin.available();
                   byte[] buffer = new byte[length];
                   fin.read(buffer);
                   res = EncodingUtils.getString(buffer, "UTF-8");
                   newpicid = new Gson().fromJson(res, List.class);
                   Log.d("PictureListSize:",String.valueOf(newpicid.size()));
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
   }

