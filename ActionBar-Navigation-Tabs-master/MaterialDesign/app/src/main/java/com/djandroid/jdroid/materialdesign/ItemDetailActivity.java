package com.djandroid.jdroid.materialdesign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by s684373 on 2016/10/27 0027.
 */
public class ItemDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemdetail);
        TextView projectname;
        TextView projectname1;
        TextView projectnum;
        Button photobutton;
        projectname = (TextView) findViewById(R.id.itemtitle);
        projectname1 = (TextView) findViewById(R.id.itemsmalltitle);
        projectnum = (TextView) findViewById(R.id.textView3);
        photobutton = (Button) findViewById(R.id.photobutton);
        Intent intent=getIntent();
        String projectname_from_MainActivity=intent.getStringExtra("projectname");
        String customername_from_MainActivity=intent.getStringExtra("customername");
        projectname.setText(projectname_from_MainActivity);
        projectname1.setText("item名称:" + customername_from_MainActivity);


    }
}
