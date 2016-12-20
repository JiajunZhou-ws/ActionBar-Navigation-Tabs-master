package com.djandroid.jdroid.Eab;

import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.djandroid.jdroid.Eab.ClientLibrary.Common.NetworkException;
import com.djandroid.jdroid.Eab.ClientLibrary.EauditingClient;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.PictureService.Response.SavePictureResponse;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Response.TaskItemSaveForAuditorResponse;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.ItemDetail;
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
import java.util.Map;

public class ProjectItemActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Toolbar toolbar;
    ItemRecyclerAdapter adapter;
    String status = "";
    PictureUpload picupload;
    TaskItemUpload taskupload;
    LinkedHashMap<String,ItemDetail> uploadmap = new LinkedHashMap<>();
    private int uploadpicturenumber = 0;
    public static List<Integer> categorycolor = new ArrayList<>();
    public static int categorypotion = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemrecylerview);
        Intent intent = getIntent();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("" + ProjectDetailActivity.taskdetailresponse.taskSiteName);
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
                 uploadpicturenumber = 0;
                 uploadpicture();
                 uploadtask();
             } catch (IOException e) {
                 e.printStackTrace();
             }
             //ProjectDetailActivity.newpicid.clear();
             savePictureNewList();  //clear local cache of picturenewlist
             return true;
         }
        else if(id == R.id.uploadallfile)
         {
             dialog();
             //recyclerView.setVisibility(View.INVISIBLE);
         }

        return super.onOptionsItemSelected(item);
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认强制上传吗，这会使用的大量的流量，建议不要轻易使用，除非数据不够同步？");

        builder.setTitle("提示");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    prepareallpicture();
                    uploadpicturenumber = 0;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    uploadpicture();
                    uploadtask();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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

    private void prepareallpicture() throws IOException
    {
        ProjectDetailActivity.newpicid.clear();
        for(int i = 0 ; i < ProjectDetailActivity.taskdetailresponse.taskCategoryList.size(); i++)
        {
            if(readfromlocalmap(ProjectDetailActivity.taskid + ProjectDetailActivity.taskdetailresponse.taskCategoryList.get(i).categoryId))
            {
                for(Map.Entry<String,ItemDetail>entry:uploadmap.entrySet())
                {
                    ItemDetail tempitem = entry.getValue();
                    if(null != tempitem.goodPictureList && tempitem.goodPictureList.size() != 0)
                    {
                        for(int j = 0 ; j < tempitem.goodPictureList.size(); j++)
                        {
                            ProjectDetailActivity.newpicid.add(tempitem.goodPictureList.get(j).pictureName);
                        }
                    }
                    if(null != tempitem.badPictureList && tempitem.badPictureList.size() != 0)
                    {
                        for(int j = 0 ; j < tempitem.badPictureList.size(); j++)
                        {
                            ProjectDetailActivity.newpicid.add(tempitem.badPictureList.get(j).pictureName);
                        }
                    }
                }
            }
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
        List<ItemDetail> templist = new ArrayList<>();
        int number = 0;
        for(int i = 0 ; i < ProjectDetailActivity.taskdetailresponse.taskCategoryList.size(); i++)
        {
            templist.clear();
            uploadmap.clear();
            if(readfromlocalmap(ProjectDetailActivity.taskid + ProjectDetailActivity.taskdetailresponse.taskCategoryList.get(i).categoryId))
            {
                for(Map.Entry<String,ItemDetail>entry:uploadmap.entrySet())
                {
                    templist.add(entry.getValue());
                }
                taskupload = new TaskItemUpload(ProjectDetailActivity.taskdetailresponse.taskCategoryList.get(i).categoryId,
                        ProjectDetailActivity.taskdetailresponse.taskCategoryList.get(i).tabId,templist);
                taskupload.execute((Void) null);
                Log.v("upload Task" + ProjectDetailActivity.taskdetailresponse.taskCategoryList.get(i).categoryName, status);
            }

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
                Type listType = new TypeToken<LinkedHashMap<String,ItemDetail>>(){}.getType();
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

    public class PictureUpload extends AsyncTask<Void, Void, SavePictureResponse> {
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
        protected SavePictureResponse doInBackground(Void... params) {
            // TODO: attempt authentication against projectdetail network service.
            // Simulate network access.
            try {
                return EauditingClient.PictureSave(picture,ProjectDetailActivity.taskid,picturecontent);
            } catch (NetworkException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(final SavePictureResponse success) {
                if(success == SavePictureResponse.Failed)
                    Toast.makeText(getBaseContext(),"上传图片" + "失败",Toast.LENGTH_LONG).show();
                else
                    uploadpicturenumber++;
                if( uploadpicturenumber == ProjectDetailActivity.newpicid.size())
                {
                    Toast.makeText(getApplicationContext(), "所有图片上传成功", Toast.LENGTH_SHORT).show();
                    ProjectDetailActivity.newpicid.clear();
                }

        }
        @Override
        protected void onCancelled() {
        }
    }


    public class TaskItemUpload extends AsyncTask<Void, Void, TaskItemSaveForAuditorResponse> {
        String categoryid;
        String tabid;
        List<ItemDetail> itemList = new ArrayList<>();
        TaskItemUpload(String categoryid,String tabid, List<ItemDetail> itemlist) {
            this.categoryid = categoryid;
            this.tabid = tabid;
            for(int i = 0 ; i < itemlist.size() ; i++)
                this.itemList.add(itemlist.get(i));
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected TaskItemSaveForAuditorResponse doInBackground(Void... params) {
            // TODO: attempt authentication against projectdetail network service.
            // Simulate network access.
            try {
                return EauditingClient.SaveTaskDetail(MainActivity.username,tabid,categoryid,itemList);
            } catch (NetworkException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(final TaskItemSaveForAuditorResponse success) {
            if(success == TaskItemSaveForAuditorResponse.Success)
                Toast.makeText(getBaseContext(),"upload" + success,Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getBaseContext(),"upload" + success,Toast.LENGTH_LONG).show();
        }
        @Override
        protected void onCancelled() {
        }
    }
}
