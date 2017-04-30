package com.djandroid.jdroid.Eab;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

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
    ImageAdapter pictureadapter;
    Integer numofpic;
    String itemid;
    String cameratype;
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
                finish();
            }
        });
        numofpic = 0;
        pictureadapter = new ImageAdapter(this);

        Intent intent = getIntent();
        itemid = intent.getStringExtra("itemid");
        cameratype = intent.getStringExtra("cameratype");
        if(cameratype.equals("good"))
            imagelist = QuestionActivity.readfromlocal.get(itemid).goodPictureList; //get the picturelist
        else
            imagelist = QuestionActivity.readfromlocal.get(itemid).badPictureList; //get the picturelist

        try {
            PreparePicture();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(pictureadapter);
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Toast.makeText(PhotoActivity.this, "short click" + String.valueOf(position), Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(PhotoActivity.this,PreviewActivity.class);
                //intent.putExtra("picturename",imagelist.get(position).pictureName);
                //PhotoActivity.this.startActivity(intent);
                Intent intent = new Intent(PhotoActivity.this,PhotoExplain.class);
                intent.putExtra("cameratype",cameratype);
                intent.putExtra("itemid",itemid);
                intent.putExtra("pictureindex",position);
                intent.putExtra("picturename",imagelist.get(position).pictureName);
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            savePictureNewList();
            saveMaptofile();
            finish();
        }

        return false;

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

    protected void dialog(final int n) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否要删除图片" + String.valueOf(n + 1));
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                imagelist.remove(n);
                pictureadapter.clearpicture();
                try {
                    PreparePicture();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pictureadapter.notifyDataSetChanged();
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
            // Toast.makeText(this, ProjectDetailActivity.taskid + QuestionActivity.catogoryid +"????", Toast.LENGTH_SHORT).show();
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
            for(int i = 0 ; i < numofpic ; i++)
            {
                readfromlocalpictrue(imagelist.get(i).pictureName , i);
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
            //Toast.makeText(this, "????", Toast.LENGTH_LONG).show();
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
    private void savebitmap(String filename , Bitmap bmp) {
        try {
            File f = new File(this.getFilesDir().getPath()+"/"+filename+"bitmap");
           // Toast.makeText(this, this.getFilesDir().getPath(), Toast.LENGTH_SHORT).show();
            FileOutputStream outbitmap = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, outbitmap);
            outbitmap.flush();
            outbitmap.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readfromlocalpictrue(String fileName, int i) throws IOException {
        String res="";
        try{
            if(fileIsExists(fileName+"bitmap"))
            {
                FileInputStream fis = new FileInputStream(this.getFilesDir().getPath() + "/" + fileName+"bitmap");
                Bitmap bitmap  = BitmapFactory.decodeStream(fis);
                Log.v("photobitmap",String.valueOf(bitmap.getRowBytes() * bitmap.getHeight()));
                pictureadapter.setimage(i,bitmap);
                fis.close();
            }
            else
            {
                if(fileIsExists(fileName))
                {
                    FileInputStream fin = openFileInput(fileName);
                    int length = fin.available();
                    byte[] buffer = new byte[length];
                    fin.read(buffer);
                    res = EncodingUtils.getString(buffer, "UTF-8");
                    pictureadapter.setimage(i,Base64Util.base64ToBitmap(res));
                    fin.close();
                }
                else
                    Toast.makeText(this,"未读取到该照片"+fileName,Toast.LENGTH_SHORT).show();
            }
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
    public static Bitmap createWatermark(Context context, Bitmap bitmap, String markText, int markBitmapId) {

        // 当水印文字与水印图片都没有的时候，返回原图
        if (TextUtils.isEmpty(markText) && markBitmapId == 0) {
            return bitmap;
        }

        // 获取图片的宽高
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        // 创建一个和图片一样大的背景图
        Bitmap bmp = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        // 画背景图
        canvas.drawBitmap(bitmap, 0, 0, null);
        //-------------开始绘制文字-------------------------------

        // 文字开始的坐标,默认为左上角
        float textX = 0;
        float textY = 0;

        if (!TextUtils.isEmpty(markText)) {
            // 创建画笔
            Paint mPaint = new Paint();
            // 文字矩阵区域
            Rect textBounds = new Rect();
            // 获取屏幕的密度，用于设置文本大小
            //float scale = context.getResources().getDisplayMetrics().density;
            // 水印的字体大小
            //mPaint.setTextSize((int) (11 * scale));
            mPaint.setTextSize(20);
            // 文字阴影
            mPaint.setShadowLayer(0.5f, 0f, 1f, Color.BLACK);
            // 抗锯齿
            mPaint.setAntiAlias(true);
            // 水印的区域
            mPaint.getTextBounds(markText, 0, markText.length(), textBounds);
            // 水印的颜色
            mPaint.setColor(Color.WHITE);

            if (textBounds.width() > bitmapWidth / 3 || textBounds.height() > bitmapHeight / 3) {
                return bitmap;
            }

            // 文字开始的坐标
            textX = bitmapWidth - textBounds.width() - 10;//这里的-10和下面的+6都是微调的结果
            textY = bitmapHeight - textBounds.height() + 6;
            // 画文字
            canvas.drawText(markText, textX, textY, mPaint);
        }

        //------------开始绘制图片-------------------------

        if (markBitmapId != 0) {
            // 载入水印图片
            Bitmap markBitmap = BitmapFactory.decodeResource(context.getResources(), markBitmapId);

            // 如果图片的大小小于水印的3倍，就不添加水印
            if (markBitmap.getWidth() > bitmapWidth / 3 || markBitmap.getHeight() > bitmapHeight / 3) {
                return bitmap;
            }

            int markBitmapWidth = markBitmap.getWidth();
            int markBitmapHeight = markBitmap.getHeight();

            // 图片开始的坐标
            float bitmapX = (float) (bitmapWidth - markBitmapWidth - 10);//这里的-10和下面的-20都是微调的结果
            float bitmapY = (float) (textY - markBitmapHeight - 20);

            // 画图
            canvas.drawBitmap(markBitmap, bitmapX, bitmapY, null);
        }

        //保存所有元素
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return bmp;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    if(numofpic <= 50) {
                        String tempid = UUID.randomUUID().toString();
                        Bitmap bmp =  getScaleBitmap(this, getTempImage().getPath(),tempid);
                        Bitmap watermarkbmp=createWatermark(this.getApplicationContext(),bmp,MainActivity.username,0);
                        savePicture(tempid,watermarkbmp);
                        ProjectDetailActivity.newpicid.add(tempid);  //???????
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
                        Toast.makeText(this,"请不要超过50张照片",Toast.LENGTH_SHORT).show();
                    }
                }
                saveMaptofile();
                savePictureNewList();
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
    public Bitmap getScaleBitmap(Context ctx, String filePath,String id) {
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
                opt.inSampleSize = 10;//bmpWidth / screenWidth;
        } else {
            if (bmpHeght > screenHeight)
                opt.inSampleSize = 10;
        }
        opt.inJustDecodeBounds = false;

        bmp = BitmapFactory.decodeFile(filePath, opt);
        savebitmap(id,bmp);
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        Log.d("TAG", "Max memory is " + maxMemory + "KB");
        Log.v("photobitmap",String.valueOf(bmp.getRowBytes() * bmp.getHeight()));
        pictureadapter.setimage(numofpic++, bmp);
        pictureadapter.notifyDataSetChanged();
        opt.inSampleSize = 4;
        bmp = BitmapFactory.decodeFile(filePath, opt);
        Log.v("photo",String.valueOf(bmp.getRowBytes() * bmp.getHeight()));
        return bmp;
    }

}
