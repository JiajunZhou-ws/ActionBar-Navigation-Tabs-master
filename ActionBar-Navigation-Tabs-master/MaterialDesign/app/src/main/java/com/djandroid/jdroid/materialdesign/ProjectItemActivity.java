package com.djandroid.jdroid.materialdesign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.djandroid.jdroid.materialdesign.ClientLibrary.HttpModel.AndroidTaskService.TaskDetailResponse;
import com.google.gson.Gson;

public class ProjectItemActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemrecylerview);
        Intent intent = getIntent();
        TaskDetailResponse temp = new Gson().fromJson(intent.getStringExtra("projectdetail"),TaskDetailResponse.class);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("" + temp.taskSiteid);
        TextView temp1 = new TextView(this);
        temp1.setText("上传");
        //temp.setTextColor(0);
        //temp.setTextSize(18);
        toolbar.addView(temp1);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_drawer_explore);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view1);
        ItemRecyclerAdapter adapter = new ItemRecyclerAdapter(this,temp);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
