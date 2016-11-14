package com.djandroid.jdroid.Eauditing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v7.widget.Toolbar;

import com.djandroid.jdroid.Eauditing.ClientLibrary.Parameter.Task.TaskItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PhotoActivity extends AppCompatActivity {
    /** Called when the activity is first created. */
    Toolbar toolbar;
    ImageAdapter temp;
    Integer numofpic;
    private List<String> imagelist = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("" + getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePictureNewList();
                saveMaptofile();
               // Toast.makeText(view.getContext(),"picturelistsaved",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        numofpic = 0;
        temp = new ImageAdapter(this);

        try {
            PreparePicture();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(temp);
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
               // Toast.makeText(PhotoActivity.this, "" + String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveMaptofile() {
        try {
            FileOutputStream outputStream = openFileOutput(ProjectDetailActivity.taskid + QuestionActivity.catogoryid, Activity.MODE_PRIVATE);
            outputStream.write(new GsonBuilder().serializeNulls().create().toJson(QuestionActivity.readfromlocal).getBytes());
            outputStream.flush();
            outputStream.close();
           // Toast.makeText(this, ProjectDetailActivity.taskid + QuestionActivity.catogoryid +"保存成功", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addphoto, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.addphoto) {
           // Toast.makeText(this,"addphoto",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri imageUri = Uri.fromFile(getTempImage());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void PreparePicture() throws IOException {
        Intent intent = getIntent();
        imagelist = new Gson().fromJson(intent.getStringExtra("imagelist"),List.class);
        if(imagelist != null && imagelist.size() > 0)
        {
            numofpic = imagelist.size();
            for(int i = 0 ; i < imagelist.size() ; i++)
            {
                readfromlocalmap(imagelist.get(i) , i);
            }
        }


    }
    private void savePictureNewList() {
        try {
            FileOutputStream outputStream = openFileOutput(ProjectDetailActivity.taskid,
                    Activity.MODE_PRIVATE);
            outputStream.write(new Gson().toJson(ProjectDetailActivity.newpicid).getBytes());
            outputStream.flush();
            outputStream.close();
            //Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void savePicture(String filename , Bitmap bmp) {
        try {
            FileOutputStream outputStream = openFileOutput(filename,
                    Activity.MODE_PRIVATE);
            outputStream.write(Base64Util.bitmapToBase64(bmp).getBytes());
            outputStream.flush();
            outputStream.close();
            Toast.makeText(this, "保存图片成功", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readfromlocalmap(String fileName, int i) throws IOException {
        String res="";
        try{
            if(fileIsExists(fileName)) {
                FileInputStream fin = openFileInput(fileName);
                int length = fin.available();
                byte[] buffer = new byte[length];
                fin.read(buffer);
                res = EncodingUtils.getString(buffer, "UTF-8");
                //List<String> temp = new Gson().fromJson(res, List.class);
                temp.setimage(i,Base64Util.base64ToBitmap(res));
                //readfromlocal = new Gson().fromJson(res,listType);
                //Toast.makeText(this,"yijingduqudao"+fileName,Toast.LENGTH_SHORT).show();
                //Log.d("Main",res.toString());
                fin.close();
            }
            else
                Toast.makeText(this,"未读取到"+fileName,Toast.LENGTH_SHORT).show();
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
            //Log.d("aaa",this.getFilesDir().getPath().toString());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bitmap bmp =  getScaleBitmap(this, getTempImage().getPath());

                    if(numofpic <= 8) {
                        temp.setimage(numofpic++, bmp);
                        temp.notifyDataSetChanged();
                        String tempid = UUID.randomUUID().toString();
                        savePicture(tempid,bmp);
                        ProjectDetailActivity.newpicid.add(tempid);
                        Intent intent = getIntent();
                        String itemid = intent.getStringExtra("itemid");
                        if (QuestionActivity.readfromlocal.containsKey(itemid))
                        {
                            if(QuestionActivity.readfromlocal.get(itemid).getPicturePathList() == null) {
                                List<String> templist = new ArrayList<>();
                                templist.add(tempid); //add uuid
                                QuestionActivity.readfromlocal.get(itemid).setPicturePathList(templist); //add uuid}
                            }
                            else
                            {
                                QuestionActivity.readfromlocal.get(itemid).getPicturePathList().add(tempid);
                            }
                        }
                        else
                        {
                            TaskItem temp = new TaskItem();
                            temp.getPicturePathList().add(tempid);
                            QuestionActivity.readfromlocal.put(itemid, temp);
                        }
                    }
                    else
                    {
                        Toast.makeText(this,"最多支持9张照片",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
    public static File getTempImage() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            File tempFile = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
            Log.v("photo",String.valueOf(Environment.getExternalStorageDirectory().getUsableSpace()));
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tempFile;
        }
        return null;
    }
    public static Bitmap getScaleBitmap(Context ctx, String filePath) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, opt);

        int bmpWidth = opt.outWidth;
        int bmpHeght = opt.outHeight;

        Log.v("photo",String.valueOf(bmpHeght * bmpHeght));

        WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        opt.inSampleSize = 1;
        if (bmpWidth > bmpHeght) {
            if (bmpWidth > screenWidth)
                opt.inSampleSize = 3;//bmpWidth / screenWidth;
        } else {
            if (bmpHeght > screenHeight)
                opt.inSampleSize = 3;
        }
        opt.inJustDecodeBounds = false;

        bmp = BitmapFactory.decodeFile(filePath, opt);
        Log.v("photo",String.valueOf(bmp.getRowBytes() * bmp.getHeight()));
        return bmp;
    }

}