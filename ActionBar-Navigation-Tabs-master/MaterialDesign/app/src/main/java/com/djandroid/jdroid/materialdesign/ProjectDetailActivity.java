package com.djandroid.jdroid.materialdesign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ProjectDetailActivity extends AppCompatActivity
   {

    private CharSequence mTitle;
    Toolbar toolbar;
    TextView projectname;
    TextView projectname1;
    TextView projectnum;
    Button detailbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectdetail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("" + getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_drawer_explore);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        projectname = (TextView) findViewById(R.id.textView1);
        projectname1 = (TextView) findViewById(R.id.textView2);
        projectnum = (TextView) findViewById(R.id.textView3);
        detailbutton = (Button) findViewById(R.id.detailbutton);
        Intent intent = getIntent();
        String projectname_from_MainActivity = intent.getStringExtra("projectname");
        projectname.setText(projectname_from_MainActivity);
        projectname1.setText("项目名称:" + projectname_from_MainActivity);
        projectnum.setText("项目代码:" + projectname_from_MainActivity.substring(0,5));

        detailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectDetailActivity.this,ProjectItemActivity.class);
                startActivity(intent);
            }
        });
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
           if (id == R.id.action_settings) {
               return true;
           }

           return super.onOptionsItemSelected(item);
       }


    public void onSectionAttached(int number) {
        Log.e("number", "--->" + number);
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                break;
        }
    }

}
