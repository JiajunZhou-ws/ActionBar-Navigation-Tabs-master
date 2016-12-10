package com.djandroid.jdroid.Eab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
            Toast.makeText(this,"savefile succeed!",Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
