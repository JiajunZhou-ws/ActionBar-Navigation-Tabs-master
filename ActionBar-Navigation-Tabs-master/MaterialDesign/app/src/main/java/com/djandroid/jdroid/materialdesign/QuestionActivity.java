package com.djandroid.jdroid.materialdesign;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {

    private RecyclerView recyclerViewQuestions;
    private List<Question> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewQuestions = (RecyclerView) findViewById(R.id.my_recycler_view);
        prepareQuestions();
        initQuestionsAdapter();
    }

    private void prepareQuestions() {
        for (int i = 0; i < 30; i++) {
            Question question = new Question();

            question.id = i;
            question.question = "Question #" + (i+1);
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