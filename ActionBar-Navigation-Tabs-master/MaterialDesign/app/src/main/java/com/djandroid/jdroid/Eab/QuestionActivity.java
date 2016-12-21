package com.djandroid.jdroid.Eab;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Helper.TaskCategoryDetail;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.ItemDetail;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.ScoreType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

public class QuestionActivity extends AppCompatActivity {

    private RecyclerView recyclerViewQuestions;
    TaskCategoryDetail taskcategorydetail;
    public static List<Question> questions = new ArrayList<>();
    public static String catogoryid;
    public static LinkedHashMap<String,ItemDetail> readfromlocal = new LinkedHashMap<>();;
    QuestionAdapter questionAdapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        recyclerViewQuestions = (RecyclerView) findViewById(R.id.my_recycler_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("" + "项目条目");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMaptofile();
                QuestionActivity.readfromlocal.clear();
                finish();
            }
        });

        Intent intent = getIntent();
        taskcategorydetail = new Gson().fromJson(intent.getStringExtra("projectitems"),TaskCategoryDetail.class);
        catogoryid = taskcategorydetail.categoryId;


        readfromlocal.clear(); //clear the static cache
        try {
            readfromlocalmap(ProjectDetailActivity.taskid + taskcategorydetail.categoryId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        questions.clear();
        prepareQuestions();
        initQuestionsAdapter();
    }

    private void saveMaptofile() {
        try {
            FileOutputStream outputStream = openFileOutput(ProjectDetailActivity.taskid + taskcategorydetail.categoryId, Activity.MODE_PRIVATE);
            outputStream.write(new GsonBuilder().serializeNulls().create().toJson(QuestionActivity.readfromlocal).getBytes());
            outputStream.flush();
            outputStream.close();
            Toast.makeText(this, taskcategorydetail.categoryName + " 保存成功", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, taskcategorydetail.categoryName + " 保存成功", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, taskcategorydetail.categoryName + " 保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void prepareQuestions() {

        for (int i = 0; i < taskcategorydetail.taskItemList .size(); i++) {
            Question question = new Question();
            question.id = i;
            question.itemid = taskcategorydetail.taskItemList.get(i).itemId;
            question.question = "#" + (i + 1) + ":" + taskcategorydetail.taskItemList.get(i).itemDetail;
            question.description = taskcategorydetail.taskItemList.get(i).itemExplanation;

            if(readfromlocal.containsKey(taskcategorydetail.taskItemList.get(i).itemId))
            {
                //if(readfromlocal.get(taskcategorydetail.taskItemList.get(i).itemId). != null)
                 //   question.comment = readfromlocal.get(taskcategorydetail.taskItemList.get(i).getItemId()).getRemark();
                //else
                //    question.comment = taskcategorydetail.taskItemList.get(i).getRemark();
                if(readfromlocal.get(taskcategorydetail.taskItemList.get(i).itemId).scoreValue > 0)
                    question.score = readfromlocal.get(taskcategorydetail.taskItemList.get(i).itemId).scoreValue;
                else
                    question.score = taskcategorydetail.taskItemList.get(i).scoreValue;

                question.checkedId = readfromlocal.get(taskcategorydetail.taskItemList.get(i).itemId).scoreType.ordinal();

                if(readfromlocal.get(taskcategorydetail.taskItemList.get(i).itemId).goodPictureList == null)
                    readfromlocal.get(taskcategorydetail.taskItemList.get(i).itemId).goodPictureList = taskcategorydetail.taskItemList.get(i).goodPictureList;
               // else if(readfromlocal.get(taskcategorydetail.taskItemList.get(i).itemId).goodPictureList.size() == 0)
            }
            else {
                if(taskcategorydetail.taskItemList.get(i).scoreType == ScoreType.Score)
                    question.score = taskcategorydetail.taskItemList.get(i).scoreValue;
                question.checkedId = taskcategorydetail.taskItemList.get(i).scoreType.ordinal();
            }
            questions.add(question);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        questionAdapter.notifyDataSetChanged();
        //Toast.makeText(this,"ahouojdfio",Toast.LENGTH_SHORT).show();
    }


    private void initQuestionsAdapter() {
        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(this));
        questionAdapter = new QuestionAdapter(this,ProjectDetailActivity.taskid + taskcategorydetail.categoryId, taskcategorydetail);
        recyclerViewQuestions.setAdapter(questionAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
         if (id == R.id.savetofile) {
             saveMaptofile();
             //Toast.makeText(this,"savetofile",Toast.LENGTH_SHORT).show();
             return true;
         }

        if  (id == R.id.otherfinding)
        {
            Intent intent = new Intent();
            intent.setClass(this, OtherFindingActivity.class);
            this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    public void readfromlocalmap(String fileName) throws IOException {
        String res="";
        try{
            if(fileIsExists(fileName)) {
                FileInputStream fin = openFileInput(fileName);
                int length = fin.available();
                byte[] buffer = new byte[length];
                fin.read(buffer);
                res = EncodingUtils.getString(buffer, "UTF-8");
                //List<String> taskcategorydetail = new Gson().fromJson(res, List.class);
                Type listType = new TypeToken<LinkedHashMap<String,ItemDetail>>(){}.getType();
                readfromlocal = new Gson().fromJson(res,listType);
                //Toast.makeText(this,"yijingduqudao"+fileName,Toast.LENGTH_SHORT).show();
                //Log.d("Main",res.toString());
                fin.close();
            }
            else {
             //   Toast.makeText(this, "未读取到" + fileName, Toast.LENGTH_SHORT).show();
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
            Log.d("aaa",this.getFilesDir().getPath().toString());
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