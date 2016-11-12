package com.djandroid.jdroid.materialdesign;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.djandroid.jdroid.materialdesign.ClientLibrary.HttpModel.AndroidTaskService.TaskCategoryDetail;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {

    private RecyclerView recyclerViewQuestions;
    TaskCategoryDetail temp;
    private List<Question> questions = new ArrayList<>();
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
                finish();
            }
        });
        Intent intent = getIntent();
        TaskCategoryDetail temp = new Gson().fromJson(intent.getStringExtra("projectitems"),TaskCategoryDetail.class);
        prepareQuestions();
        initQuestionsAdapter();
    }

    private void prepareQuestions() {

        for (int i = 0; i < temp.taskItemList.size(); i++) {
            Question question = new Question();

            question.id = i;
            question.question = "#" + (i+1) + ":" + temp.taskItemList.get(i).getItem();
            question.description = temp.taskItemList.get(i).getExplanation();
            question.comment = "";
            //question.option1 = "通过";
            //question.option2 = "不通过";
            //question.option3 = "N/A";
            question.correctOption = new Random().nextInt(3);
            //question.answer = "备注";

            questions.add(question);
        }
    }

    private void initQuestionsAdapter() {

        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(this));
        QuestionAdapter questionAdapter = new QuestionAdapter(this, questions);
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
             Toast.makeText(this,"savetofile",Toast.LENGTH_SHORT).show();
              return true;
         }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if(data!=null){
                    Bundle extras = data.getExtras();
                    Bitmap bmp = (Bitmap) extras.get("data");
                   // imageView.setImageBitmap(bmp);  //设置照片现实在界面上
                }
                break;
        }
    }
}