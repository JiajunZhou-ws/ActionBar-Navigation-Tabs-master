package com.djandroid.jdroid.materialdesign;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.djandroid.jdroid.materialdesign.ClientLibrary.HttpModel.AndroidTaskService.TaskCategoryDetail;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {

    private RecyclerView recyclerViewQuestions;
    private List<Question> questions = new ArrayList<>();
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        recyclerViewQuestions = (RecyclerView) findViewById(R.id.my_recycler_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("" + "项目条目");
        TextView temp = new TextView(this);
        temp.setText("保存");
        //temp.setTextColor(0);
        //temp.setTextSize(18);
        toolbar.addView(temp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_drawer_explore);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        prepareQuestions();
        initQuestionsAdapter();
    }

    private void prepareQuestions() {
        Intent intent = getIntent();
        TaskCategoryDetail temp = new Gson().fromJson(intent.getStringExtra("projectitems"),TaskCategoryDetail.class);
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

}