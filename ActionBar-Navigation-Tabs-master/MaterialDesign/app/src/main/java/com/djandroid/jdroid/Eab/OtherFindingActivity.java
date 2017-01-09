package com.djandroid.jdroid.Eab;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.ItemDetail;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.PictureDetail;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.ScoreType;

import java.util.ArrayList;
import java.util.UUID;

public class OtherFindingActivity extends ActionBarActivity {
    Toolbar toolbar;
    Button commitbutton;
    EditText question,questiondescription,questionscore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_finding);
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
        question = (EditText)findViewById(R.id.editquestion);
        questiondescription = (EditText)findViewById(R.id.editdescription);
        questionscore = (EditText)findViewById(R.id.editscore);
        questionscore.setVisibility(View.GONE);
        commitbutton = (Button)findViewById(R.id.otherfindingbutton);
        commitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),question.getText().toString()+questiondescription.getText().toString(),Toast.LENGTH_SHORT).show();
                if(question.getText()!=null && !question.getText().equals("")) {
                    Question questiontemp = new Question();
                    ItemDetail temp = new ItemDetail();
                    questiontemp.id = QuestionActivity.questions.size() + 1;
                    questiontemp.itemid = UUID.randomUUID().toString();
                    temp.itemId = questiontemp.itemid;
                    questiontemp.question ="#" + (questiontemp.id) + ":" + question.getText().toString();
                    temp.itemDetail = question.getText().toString();

                    if(questiondescription.getText().toString().length() !=0 ) {
                        questiontemp.description = questiondescription.getText().toString();
                        temp.itemExplanation = questiontemp.description;
                    }
                    else
                        questiontemp.description = "";
                    questiontemp.checkedId = 0;
                    temp.scoreType = ScoreType.None;
                    QuestionActivity.questions.add(questiontemp);
                    QuestionActivity.readfromlocal.put(questiontemp.itemid,temp);

                    ProjectDetailActivity.taskdetailresponse.taskCategoryList.get(ProjectItemActivity.categorypotion).taskItemList.add(temp);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.otherfinding, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.otherfinding)
        {
            Toast.makeText(this,"aaa",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
