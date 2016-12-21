package com.djandroid.jdroid.Eab;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v7.widget.Toolbar;

import com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.ItemDetail;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.PictureDetail;
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
    String itemid;
    String cameratype;
    TextView[] textcommentview = new TextView[10];
    EditText[] editcomment = new EditText[10];
    private List<PictureDetail> imagelist = new ArrayList<>();
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

        Intent intent = getIntent();
        itemid = intent.getStringExtra("itemid");
        cameratype = intent.getStringExtra("cameratype");
        if(cameratype.equals("good"))
            imagelist = QuestionActivity.readfromlocal.get(itemid).goodPictureList; //get the picturelist
        else
            imagelist = QuestionActivity.readfromlocal.get(itemid).badPictureList; //get the picturelist

        initializetextview();
        try {
            PreparePicture();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(temp);
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(PhotoActivity.this, "short click" + String.valueOf(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PhotoActivity.this,PreviewActivity.class);
                if(cameratype.equals("good"))
                    intent.putExtra("picturename",QuestionActivity.readfromlocal.get(itemid).goodPictureList.get(position).pictureName);
                else
                    intent.putExtra("picturename",QuestionActivity.readfromlocal.get(itemid).badPictureList.get(position).pictureName);
                PhotoActivity.this.startActivity(intent);
            }
        });
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(PhotoActivity.this, "long click" + String.valueOf(position), Toast.LENGTH_SHORT).show();
                dialog(position);
                return true;
            }
        });
    }

    private class MyCustomEditTextListener implements TextWatcher{
        int position;
        MyCustomEditTextListener(int position)
        {
            this.position = position;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            //Toast.makeText(getBaseContext(),String.valueOf(position),Toast.LENGTH_SHORT).show();
            if(cameratype.equals("good"))
                QuestionActivity.readfromlocal.get(itemid).goodPictureList.get(position - 1).pictureExplanation = charSequence.toString();
            else
                QuestionActivity.readfromlocal.get(itemid).badPictureList.get(position - 1).pictureExplanation = charSequence.toString();
        }
        @Override
        public void afterTextChanged(Editable editable) {

        }
    }


    public void initializetextview()
    {
        textcommentview[1] = (TextView) findViewById(R.id.text_view_answer1);
        textcommentview[2] = (TextView) findViewById(R.id.text_view_answer2);
        textcommentview[3] = (TextView) findViewById(R.id.text_view_answer3);
        textcommentview[4] = (TextView) findViewById(R.id.text_view_answer4);
        textcommentview[5] = (TextView) findViewById(R.id.text_view_answer5);
        textcommentview[6] = (TextView) findViewById(R.id.text_view_answer6);
        textcommentview[7] = (TextView) findViewById(R.id.text_view_answer7);
        textcommentview[8] = (TextView) findViewById(R.id.text_view_answer8);
        textcommentview[9] = (TextView) findViewById(R.id.text_view_answer9);
        editcomment[1] = (EditText) findViewById(R.id.editcomment1);
        editcomment[2] = (EditText) findViewById(R.id.editcomment2);
        editcomment[3] = (EditText) findViewById(R.id.editcomment3);
        editcomment[4] = (EditText) findViewById(R.id.editcomment4);
        editcomment[5] = (EditText) findViewById(R.id.editcomment5);
        editcomment[6] = (EditText) findViewById(R.id.editcomment6);
        editcomment[7] = (EditText) findViewById(R.id.editcomment7);
        editcomment[8] = (EditText) findViewById(R.id.editcomment8);
        editcomment[9] = (EditText) findViewById(R.id.editcomment9);
    }

    public void showcomment(int num)
    {
        for(int i = 1 ; i <= num ; i++)
        {
            textcommentview[i].setVisibility(View.VISIBLE);
            editcomment[i].setVisibility(View.VISIBLE);
            editcomment[i].setText(imagelist.get(i-1).pictureExplanation);
            editcomment[i].setTag(i);
            editcomment[i].addTextChangedListener(new MyCustomEditTextListener(i));
        }
        for(int i = num + 1; i <= 9 ; i++)
        {
            textcommentview[i].setVisibility(View.GONE);
            editcomment[i].setVisibility(View.GONE);
        }
    }

    protected void dialog(final int n) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否要删除照片" + String.valueOf(n + 1));
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(cameratype.equals("good"))
                    QuestionActivity.readfromlocal.get(itemid).goodPictureList.remove(n);
                else
                    QuestionActivity.readfromlocal.get(itemid).badPictureList.remove(n);
                temp.clearpicture();
                try {
                    PreparePicture();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                temp.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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
        if(imagelist != null && imagelist.size() > 0)
        {
            numofpic = imagelist.size();
            for(int i = 0 ; i < imagelist.size() ; i++)
            {
                readfromlocalmap(imagelist.get(i).pictureName , i);
            }
        }
        showcomment(imagelist.size());
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
                temp.setimage(i,Base64Util.base64ToBitmap(res));
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
                        ProjectDetailActivity.newpicid.add(tempid);  //更新新图片列表
                        PictureDetail temppicture = new PictureDetail();
                        temppicture.pictureName = tempid;
                        if (QuestionActivity.readfromlocal.containsKey(itemid))
                        {
                            if(cameratype.equals("good")) {
                                if (QuestionActivity.readfromlocal.get(itemid).goodPictureList == null) {
                                    List<PictureDetail> templist = new ArrayList<>();
                                    templist.add(temppicture); //add uuid
                                    QuestionActivity.readfromlocal.get(itemid).goodPictureList = templist; //add uuid}
                                    imagelist = templist;
                                } else {
                                    QuestionActivity.readfromlocal.get(itemid).goodPictureList.add(temppicture);
                                }
                            }
                            else
                            {
                                if (QuestionActivity.readfromlocal.get(itemid).badPictureList == null) {
                                    List<PictureDetail> templist = new ArrayList<>();
                                    templist.add(temppicture); //add uuid
                                    QuestionActivity.readfromlocal.get(itemid).badPictureList = templist; //add uuid}
                                    imagelist = templist;
                                } else {
                                    QuestionActivity.readfromlocal.get(itemid).badPictureList.add(temppicture);
                                }
                            }
                        }
                        else
                        {
                            ItemDetail temp = new ItemDetail();
                            if(cameratype.equals("good")) {
                                temp.goodPictureList.add(temppicture);
                                imagelist = temp.goodPictureList;
                            }
                            else {
                                temp.badPictureList.add(temppicture);
                                imagelist = temp.badPictureList;
                            }
                            QuestionActivity.readfromlocal.put(itemid, temp);

                        }
                    }
                    else
                    {
                        Toast.makeText(this,"最多支持9张照片",Toast.LENGTH_SHORT).show();
                    }
                }
                showcomment(imagelist.size());
                break;
        }
    }
    public static File getTempImage() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            File tempFile = new File(Environment.getExternalStorageDirectory(), "taskcategorydetail.jpg");
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
