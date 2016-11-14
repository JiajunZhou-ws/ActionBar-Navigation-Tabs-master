package com.djandroid.jdroid.Eauditing;

import android.app.Activity;
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
import android.widget.Toast;

import com.djandroid.jdroid.Eauditing.ClientLibrary.EauditingClient;
import com.djandroid.jdroid.Eauditing.ClientLibrary.HttpModel.AndroidTaskService.TaskDetailResponse;
import com.djandroid.jdroid.Eauditing.ClientLibrary.HttpModel.AndroidTaskService.TaskInformation;
import com.google.gson.Gson;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ProjectDetailActivity extends AppCompatActivity
   {

    private CharSequence mTitle;
    private TaskDetailResponse taskdetailresponse;
    private List<String> needdownloadpicture;
    private int needdownloadpicturenumber;
    public static String taskid;
    public static int savepicturenum;
    public static List<String> newpicid = new ArrayList<String>();  //read from file in this activity
    Toolbar toolbar;
    TextView projecttitle,projectname,projectarea,projectsiteid,projectsitename,projectsiteaddress,projectsitecontractor,projectcatagory;
    Button detailbutton;

    GetProjectDetail getprojectdetail;
    GetPicture getpicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        detailbutton = (Button) findViewById(R.id.detailbutton);
        Intent intent = getIntent();
        final TaskInformation temp = new Gson().fromJson(intent.getStringExtra("TaskInfomation"),TaskInformation.class);
        taskid = temp.idTask;
        projecttitle.setText(temp.projectName);
        projectname.setText("项目名称："+temp.projectName);
        projectarea.setText("区域："+temp.region);
        projectsiteid.setText("站点ID："+temp.siteid);
        projectsitename.setText("站点名称："+temp.sitename);
        projectsiteaddress.setText("站点地址："+temp.siteaddress);
        projectsitecontractor.setText("分包商："+temp.subcontractor);
        projectcatagory.setText("模块："+temp.Categories);

        taskdetailresponse = new TaskDetailResponse();
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
                getprojectdetail = new GetProjectDetail(temp.idTask);
                getprojectdetail.execute((Void) null);
                //save();

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

               //getprojectdetail = new GetProjectDetail(temp.idTask);
               //getprojectdetail.execute((Void) null);
               taskdetailresponse = success;
               getAllPictureName();
               if(needdownloadpicture.size() == 0)
               {
                   GotoCategoryActivity();
               }
               for (int i = 0; i < needdownloadpicture.size(); i++) {
                   getpicture = new GetPicture(needdownloadpicture.get(i));
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
           intent.putExtra("projectdetail", new Gson().toJson(taskdetailresponse));
           startActivity(intent);
       }
       private void getAllPictureName()
       {
           for(int i = 0 ; i < taskdetailresponse.taskCategoryList.size(); i++)

               for(int j = 0 ; j < taskdetailresponse.taskCategoryList.get(i).taskItemList.size();j++) {

                   if(taskdetailresponse.taskCategoryList.get(i).taskItemList.get(j).getPicturePathList()!=null)
                   {
                       for (int k = 0; k < taskdetailresponse.taskCategoryList.get(i).taskItemList.get(j).getPicturePathList().size(); k++) {
                           if (!fileIsExists(taskdetailresponse.taskCategoryList.get(i).taskItemList.get(j).getPicturePathList().get(k))) {
                               //getpicture = new GetPicture(success.taskCategoryList.get(i).taskItemList.get(j).getPicturePathList().get(k));
                               //getpicture.execute((Void) null);
                               needdownloadpicture.add(taskdetailresponse.taskCategoryList.get(i).taskItemList.get(j).getPicturePathList().get(k));
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
               Log.v("SAVEPICTURE","保存图片成功" + String.valueOf(savepicturenum));
               if(savepicturenum == needdownloadpicturenumber) {
                   GotoCategoryActivity();
               }
               //Toast.makeText(this, "保存图片成功", Toast.LENGTH_SHORT).show();
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }

       public class GetPicture extends AsyncTask<Void, Void, String> {
           String picturename;
           GetPicture(String picturename) {
               this.picturename = picturename;
           }
           @Override
           protected void onPreExecute() {
           }
           @Override
           protected String doInBackground(Void... params) {
               // TODO: attempt authentication against projectdetail network service.
               // Simulate network access.
               return EauditingClient.PictureGet(picturename);
           }
           @Override
           protected void onPostExecute(final String success) {
               String newpic = success.replace("\n","");
               savePicture(picturename,newpic);
               //Log.v("Main",success);
           }
           @Override
           protected void onCancelled() {
           }
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
                   //Toast.makeText(this, String.valueOf(temp.get(2)), Toast.LENGTH_SHORT).show();
                   fin.close();
               }
               else {
                        //Toast.makeText(this, "未读取到缓存文件", Toast.LENGTH_SHORT).show();
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

           }
           catch (Exception e)
           {
               return false;
           }

           return true;
       }
   }

