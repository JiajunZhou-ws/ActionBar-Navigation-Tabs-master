package com.djandroid.jdroid.materialdesign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.djandroid.jdroid.materialdesign.ClientLibrary.HttpModel.AndroidTaskService.TaskDetailResponse;
import com.google.gson.Gson;

public class ProjectItemActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Toolbar toolbar;
    ItemRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemrecylerview);
        Intent intent = getIntent();
        TaskDetailResponse temp = new Gson().fromJson(intent.getStringExtra("projectdetail"),TaskDetailResponse.class);
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
              Toast.makeText(this,"upload succeed!",Toast.LENGTH_SHORT).show();
              return true;
         }

        return super.onOptionsItemSelected(item);
    }
}
