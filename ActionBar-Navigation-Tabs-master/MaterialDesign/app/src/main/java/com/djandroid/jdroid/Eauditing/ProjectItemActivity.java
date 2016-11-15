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
import java.util.LinkedHashMap;
import java.util.List;

public class ProjectItemActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Toolbar toolbar;
    ItemRecyclerAdapter adapter;
    String status = "";
    PictureUpload picupload;
    TaskItemUpload taskupload;
    TaskDetailResponse temp;
    LinkedHashMap<String,TaskItem> uploadmap = new LinkedHashMap<>();
    public static List<Integer> categorycolor = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemrecylerview);
        Intent intent = getIntent();
        temp = new Gson().fromJson(intent.getStringExtra("projectdetail"),TaskDetailResponse.class);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("" + temp.taskSiteid);
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

        adapter = new ItemRecyclerAdapter(this,temp);
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
             Toast.makeText(this,"上传服务器成功！",Toast.LENGTH_SHORT).show();

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
        for(int i = 0 ; i < temp.taskCategoryList.size(); i++)
        {
            templist.clear();
            uploadmap.clear();
            if(readfromlocalmap(ProjectDetailActivity.taskid + temp.taskCategoryList.get(i).CategoryId))
            {
                number++;
                for(int j = 0 ; j < temp.taskCategoryList.get(i).taskItemList.size(); j++) {
                    if(uploadmap.containsKey(temp.taskCategoryList.get(i).taskItemList.get(j).getItemId()))
                        templist.add(uploadmap.get(temp.taskCategoryList.get(i).taskItemList.get(j).getItemId()));
                }
                taskupload = new TaskItemUpload(temp.taskCategoryList.get(i).CategoryId,templist);
                taskupload.execute((Void) null);
            }
            Log.v("upload Task" + temp.taskCategoryList.get(i).CategoryName, status);
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
                //List<String> taskcategorydetail = new Gson().fromJson(res, List.class);
                Type listType = new TypeToken<LinkedHashMap<String,TaskItem>>(){}.getType();
                uploadmap = new Gson().fromJson(res,listType);
                Toast.makeText(this,"yijingduqudao"+fileName,Toast.LENGTH_SHORT).show();
                //Log.d("Main",res.toString());
                fin.close();
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
                //List<String> taskcategorydetail = new Gson().fromJson(res, List.class);
                //readfromlocal = new Gson().fromJson(res,listType);
                //Toast.makeText(this,"yijingduqudao"+fileName,Toast.LENGTH_SHORT).show();
                //Log.d("Main",res.toString());
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
            //status = success;
            //Toast.makeText(getBaseContext(),"upload" + success,Toast.LENGTH_SHORT).show();
        }
        @Override
        protected void onCancelled() {
        }
    }
}
