package com.djandroid.jdroid.Eauditing;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.djandroid.jdroid.Eauditing.ClientLibrary.EauditingClient;
import com.djandroid.jdroid.Eauditing.ClientLibrary.HttpModel.AndroidTaskService.TaskDetailResponse;
import com.djandroid.jdroid.Eauditing.ClientLibrary.Parameter.Task.TaskItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProjectItemActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Toolbar toolbar;
    ItemRecyclerAdapter adapter;
    String status = "";
    PictureUpload picupload;
    TaskItemUpload taskupload;
    LinkedHashMap<String,TaskItem> uploadmap = new LinkedHashMap<>();
    public static List<Integer> categorycolor = new ArrayList<>();
    public static int categorypotion = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemrecylerview);
        Intent intent = getIntent();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("" + ProjectDetailActivity.taskdetailresponse.taskSiteid);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view1);

        categorycolor.clear();

        adapter = new ItemRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
         if (id == R.id.uploadfile) {
             try {
                 uploadpicture();
                 uploadtask();
             } catch (IOException e) {
                 e.printStackTrace();
             }


             ProjectDetailActivity.newpicid.clear();
             savePictureNewList();  //clear local cache of picturenewlist

             return true;
         }

        return super.onOptionsItemSelected(item);
    }

    private void savePictureNewList() {
        try {
            FileOutputStream outputStream = openFileOutput(ProjectDetailActivity.taskid,
                    Activity.MODE_PRIVATE);
            outputStream.write(new Gson().toJson(ProjectDetailActivity.newpicid).getBytes());
            outputStream.flush();
            outputStream.close();
            //Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadpicture() throws IOException {
        int trytime = 3; //try 3 time
        for(int i = 0; i < ProjectDetailActivity.newpicid.size(); i++)
        {
            String picname = ProjectDetailActivity.newpicid.get(i);
            String piccontent = readfromlocalpicture(picname);
            picupload = new PictureUpload(picname,piccontent);
            picupload.execute((Void) null);
            Log.d("Main","上传图片" + picname + status);
        }
    }

    private void uploadtask() throws IOException {
        List<TaskItem> templist = new ArrayList<>();
        int number = 0;
        for(int i = 0 ; i < ProjectDetailActivity.taskdetailresponse.taskCategoryList.size(); i++)
        {
            templist.clear();
            uploadmap.clear();
            if(readfromlocalmap(ProjectDetailActivity.taskid + ProjectDetailActivity.taskdetailresponse.taskCategoryList.get(i).CategoryId))
            {

                for(Map.Entry<String,TaskItem>entry:uploadmap.entrySet())
                {
                    templist.add(entry.getValue());
                }

                taskupload = new TaskItemUpload(ProjectDetailActivity.taskdetailresponse.taskCategoryList.get(i).CategoryId,templist);
                taskupload.execute((Void) null);
            }
            Log.v("upload Task" + ProjectDetailActivity.taskdetailresponse.taskCategoryList.get(i).CategoryName, status);
        }

    }

    public Boolean readfromlocalmap(String fileName) throws IOException {
        String res="";
        try{
            if(fileIsExists(fileName)) {
                FileInputStream fin = openFileInput(fileName);
                int length = fin.available();
                byte[] buffer = new byte[length];
                fin.read(buffer);
                res = EncodingUtils.getString(buffer, "UTF-8");
                Type listType = new TypeToken<LinkedHashMap<String,TaskItem>>(){}.getType();
                uploadmap = new Gson().fromJson(res,listType);
                fin.close();
                Log.d("ProjectitemActivy","read from local map" + fileName + "succeed!");
                return true;
            }
            else {
                //Toast.makeText(this, "未读取到" + fileName, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public String readfromlocalpicture(String fileName) throws IOException {
        String res="";
        try{
            if(fileIsExists(fileName)) {
                FileInputStream fin = openFileInput(fileName);
                int length = fin.available();
                byte[] buffer = new byte[length];
                fin.read(buffer);
                res = EncodingUtils.getString(buffer, "UTF-8");
                Log.d("ProjectitemActivy","read from local picture" + fileName + "succeed!");
                fin.close();
            }
            else {
               // Toast.makeText(this, "未读取到" + fileName, Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
         return res;
    }

    public boolean fileIsExists(String strFile)
    {
        try
        {
            File f=new File(this.getFilesDir().getPath() + "/" + strFile);
           //Log.d("aaa",this.getFilesDir().getPath().toString());
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

    public class PictureUpload extends AsyncTask<Void, Void, String> {
        String picture;
        String picturecontent;
        PictureUpload(String temp, String tempcontent) {
            picture = temp;
            picturecontent = tempcontent;
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against projectdetail network service.
            // Simulate network access.
            return EauditingClient.PictureSave(picture,picturecontent);
        }
        @Override
        protected void onPostExecute(final String success) {
                status = success;
                if(status != "success")
                    Toast.makeText(getBaseContext(),"上传图片" + success,Toast.LENGTH_LONG).show();

        }
        @Override
        protected void onCancelled() {
        }
    }


    public class TaskItemUpload extends AsyncTask<Void, Void, String> {
        String categoryid;
        List<TaskItem> itemList = new ArrayList<>();
        TaskItemUpload(String categoryid, List<TaskItem> itemlist) {
            this.categoryid = categoryid;
            for(int i = 0 ; i < itemlist.size() ; i++)
                this.itemList.add(itemlist.get(i));
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against projectdetail network service.
            // Simulate network access.
            return EauditingClient.TaskSave(ProjectDetailActivity.taskid,categoryid,itemList);
        }
        @Override
        protected void onPostExecute(final String success) {
            status = success;
            if(status == "success")
                Toast.makeText(getBaseContext(),"upload" + success,Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getBaseContext(),"upload" + success,Toast.LENGTH_LONG).show();
        }
        @Override
        protected void onCancelled() {
        }
    }
}
