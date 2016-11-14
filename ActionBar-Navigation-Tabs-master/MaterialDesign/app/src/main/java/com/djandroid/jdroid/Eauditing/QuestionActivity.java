package com.djandroid.jdroid.Eauditing;
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

import com.djandroid.jdroid.Eauditing.ClientLibrary.HttpModel.AndroidTaskService.TaskCategoryDetail;
import com.djandroid.jdroid.Eauditing.ClientLibrary.Parameter.Task.TaskItem;
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
    TaskCategoryDetail temp;
    public List<Question> questions = new ArrayList<>();
    public static String catogoryid;
    public static LinkedHashMap<String,TaskItem> readfromlocal = new LinkedHashMap<>();;
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
                finish();
            }
        });

        Intent intent = getIntent();
        temp = new Gson().fromJson(intent.getStringExtra("projectitems"),TaskCategoryDetail.class);
        catogoryid = temp.CategoryId;


        readfromlocal.clear(); //clear the static cache
        try {
            readfromlocalmap(ProjectDetailActivity.taskid + temp.CategoryId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        prepareQuestions();
        initQuestionsAdapter();
    }

    private void saveMaptofile() {
        try {
            FileOutputStream outputStream = openFileOutput(ProjectDetailActivity.taskid + temp.CategoryId, Activity.MODE_PRIVATE);
            outputStream.write(new GsonBuilder().serializeNulls().create().toJson(QuestionActivity.readfromlocal).getBytes());
            outputStream.flush();
            outputStream.close();
            QuestionActivity.readfromlocal.clear();
            //Toast.makeText(this, ProjectDetailActivity.taskid + temp.CategoryId+"保存成功", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepareQuestions() {

        for (int i = 0; i < temp.taskItemList .size(); i++) {
            Question question = new Question();
            question.id = i;
            question.itemid = temp.taskItemList.get(i).getItemId();
            question.question = "#" + (i + 1) + ":" + temp.taskItemList.get(i).getItem();
            question.description = temp.taskItemList.get(i).getExplanation();

            if(readfromlocal.containsKey(temp.taskItemList.get(i).getItemId()))
            {
                if(readfromlocal.get(temp.taskItemList.get(i).getItemId()).getRemark() != null)
                    question.comment = readfromlocal.get(temp.taskItemList.get(i).getItemId()).getRemark();
                else
                    question.comment = temp.taskItemList.get(i).getRemark();
                if(readfromlocal.get(temp.taskItemList.get(i).getItemId()).getScore() != null)
                    question.score = readfromlocal.get(temp.taskItemList.get(i).getItemId()).getScore();
                else
                    question.score = temp.taskItemList.get(i).getScore();
            }
            else {
                question.comment = temp.taskItemList.get(i).getRemark();
                question.score = temp.taskItemList.get(i).getScore();
            }
            questions.add(question);
        }
    }

    private void initQuestionsAdapter() {

        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(this));
        QuestionAdapter questionAdapter = new QuestionAdapter(this, questions, ProjectDetailActivity.taskid + temp.CategoryId,temp);
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
                //List<String> temp = new Gson().fromJson(res, List.class);
                Type listType = new TypeToken<LinkedHashMap<String,TaskItem>>(){}.getType();
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