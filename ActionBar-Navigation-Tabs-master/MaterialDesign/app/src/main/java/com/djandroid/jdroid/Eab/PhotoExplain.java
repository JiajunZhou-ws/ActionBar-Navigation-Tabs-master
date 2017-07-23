package com.djandroid.jdroid.Eab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.PictureDetail;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.ViolationLevel;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoExplain extends AppCompatActivity {
    ImageView image;
    Toolbar toolbar;
    RadioGroup radioGroupOption;
    EditText editcomment,editcomment1,editcomment2;
    RadioButton buttonhigh,buttonmedium,buttonlow,buttonnone;
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;
    int position;
    String cameratype;
    MyCustomEditTextListener editcommentlistener;
    MyCustomEditTextListener1 editcomment1listener;
    MyCustomEditTextListener2 editcomment2listener;
    List<PictureDetail> imagelist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoexplain);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("" + getResources().getString(R.string.app_name));
        image = (ImageView)findViewById(R.id.detailimageview);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        radioGroupOption = (RadioGroup)findViewById(R.id.RiskRadioGroup1);
        buttonhigh = (RadioButton)findViewById(R.id.high1);
        buttonmedium = (RadioButton)findViewById(R.id.middle1);
        buttonlow = (RadioButton)findViewById(R.id.low1);
        buttonnone = (RadioButton)findViewById(R.id.norisk1);
        editcomment = (EditText)findViewById(R.id.editcomment1);
        editcomment1 = (EditText)findViewById(R.id.editcomment2);
        editcomment2 = (EditText)findViewById(R.id.editcomment3);

        Intent intent = getIntent();
        final String picturename = intent.getStringExtra("picturename");
        cameratype = intent.getStringExtra("cameratype");
        final String itemid = intent.getStringExtra("itemid");
        position = intent.getIntExtra("pictureindex",1);
        Log.v("index",String.valueOf(position));

        if(cameratype.equals("good")) {
            imagelist = QuestionActivity.readfromlocal.get(itemid).goodPictureList;
            radioGroupOption.setVisibility(View.GONE);
        }
        else
        {
            imagelist = QuestionActivity.readfromlocal.get(itemid).badPictureList;
            radioGroupOption.setVisibility(View.VISIBLE);
            if(imagelist.get(position).pictureViolation == ViolationLevel.Critical)
                radioGroupOption.check(buttonhigh.getId());
            else if(imagelist.get(position).pictureViolation == ViolationLevel.Major)
                radioGroupOption.check(buttonmedium.getId());
            else if(imagelist.get(position).pictureViolation == ViolationLevel.Minor)
                radioGroupOption.check(buttonlow.getId());
            else
                radioGroupOption.check(buttonnone.getId());
            radioGroupOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkid) {
                    View viewById = radioGroupOption.findViewById(checkid);
                    if (!viewById.isPressed()){
                        return;
                    }
                    if(checkid == buttonhigh.getId())
                        imagelist.get(position).pictureViolation = ViolationLevel.Critical;
                    else if(checkid == buttonmedium.getId())
                        imagelist.get(position).pictureViolation = ViolationLevel.Major;
                    else if(checkid == buttonlow.getId())
                        imagelist.get(position).pictureViolation = ViolationLevel.Minor;
                    else
                        imagelist.get(position).pictureViolation = ViolationLevel.None;
                }
            });
        }
        editcomment.setText(imagelist.get(position).pictureExplanation);
        editcomment1.setText(imagelist.get(position).pictureConsequence);
        editcomment2.setText(imagelist.get(position).pictureSuggestion);
        editcommentlistener = new MyCustomEditTextListener(position);
        editcomment1listener = new MyCustomEditTextListener1(position);
        editcomment2listener = new MyCustomEditTextListener2(position);
        editcomment.addTextChangedListener(editcommentlistener);
        editcomment1.addTextChangedListener(editcomment1listener);
        editcomment2.addTextChangedListener(editcomment2listener);
        try {
            readfromlocalpictrue(picturename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoExplain.this,PreviewActivity.class);
                intent.putExtra("picturename",picturename);
                PhotoExplain.this.startActivity(intent);
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            if(y1 - y2 > 40) {
                if(editcomment2.hasFocus())
                {
                    editcomment2.clearFocus();
                    editcomment1.requestFocus();
                }
                else if(editcomment1.hasFocus())
                {
                    editcomment1.clearFocus();
                    editcomment.requestFocus();
                }
            } else if(y2 - y1 > 50) {
                //Toast.makeText(this, "向下滑", Toast.LENGTH_SHORT).show();
                if(editcomment.hasFocus()) {
                    editcomment.clearFocus();
                    editcomment1.requestFocus();
                }
                else if(editcomment1.hasFocus())
                {
                    editcomment1.clearFocus();
                    editcomment2.requestFocus();
                }
            } else if(x1 - x2 > 50) {
                if(position - 1 >= 0)
                {
                    position--;
                    try {
                        readfromlocalpictrue(imagelist.get(position).pictureName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    editcomment.removeTextChangedListener(editcommentlistener);
                    editcomment1.removeTextChangedListener(editcomment1listener);
                    editcomment2.removeTextChangedListener(editcomment2listener);
                    editcomment.setText(imagelist.get(position).pictureExplanation);
                    editcomment1.setText(imagelist.get(position).pictureConsequence);
                    editcomment2.setText(imagelist.get(position).pictureSuggestion);
                    editcommentlistener.setposition(position);
                    editcomment1listener.setposition(position);
                    editcomment2listener.setposition(position);
                    editcomment.addTextChangedListener(editcommentlistener);
                    editcomment1.addTextChangedListener(editcomment1listener);
                    editcomment2.addTextChangedListener(editcomment2listener);
                    if(cameratype.equals("bad")) {
                        if (imagelist.get(position).pictureViolation == ViolationLevel.Critical)
                            radioGroupOption.check(buttonhigh.getId());
                        else if (imagelist.get(position).pictureViolation == ViolationLevel.Major)
                            radioGroupOption.check(buttonmedium.getId());
                        else if (imagelist.get(position).pictureViolation == ViolationLevel.Minor)
                            radioGroupOption.check(buttonlow.getId());
                        else
                            radioGroupOption.check(buttonnone.getId());
                    }

                }
                else
                {
                    Toast.makeText(this, "已经是第一张照片", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "向左滑", Toast.LENGTH_SHORT).show();
            } else if(x2 - x1 > 50) {
                if(imagelist.size() > position + 1)
                {
                    position++;
                    try {
                        readfromlocalpictrue(imagelist.get(position).pictureName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    editcomment.removeTextChangedListener(editcommentlistener);
                    editcomment1.removeTextChangedListener(editcomment1listener);
                    editcomment2.removeTextChangedListener(editcomment2listener);
                    editcomment.setText(imagelist.get(position).pictureExplanation);
                    editcomment1.setText(imagelist.get(position).pictureConsequence);
                    editcomment2.setText(imagelist.get(position).pictureSuggestion);
                    editcommentlistener.setposition(position);
                    editcomment1listener.setposition(position);
                    editcomment2listener.setposition(position);
                    editcomment.addTextChangedListener(editcommentlistener);
                    editcomment1.addTextChangedListener(editcomment1listener);
                    editcomment2.addTextChangedListener(editcomment2listener);
                    if(cameratype.equals("bad")) {
                        if (imagelist.get(position).pictureViolation == ViolationLevel.Critical)
                            radioGroupOption.check(buttonhigh.getId());
                        else if (imagelist.get(position).pictureViolation == ViolationLevel.Major)
                            radioGroupOption.check(buttonmedium.getId());
                        else if (imagelist.get(position).pictureViolation == ViolationLevel.Minor)
                            radioGroupOption.check(buttonlow.getId());
                        else
                            radioGroupOption.check(buttonnone.getId());
                    }
                }
                else
                {
                    Toast.makeText(this, "已经是最后一张照片", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private class MyCustomEditTextListener implements TextWatcher {
        int position;
        MyCustomEditTextListener(int position)
        {
            this.position = position;
        }
        public void setposition(int num)
        {
            this.position = num;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            //Toast.makeText(getBaseContext(),String.valueOf(position),Toast.LENGTH_SHORT).show();
            imagelist.get(position).pictureExplanation = charSequence.toString();
        }
        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
    private class MyCustomEditTextListener1 implements TextWatcher {
        int position;
        MyCustomEditTextListener1(int position)
        {
            this.position = position;
        }
        public void setposition(int n)
        {
            this.position = n;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            //Toast.makeText(getBaseContext(),String.valueOf(position),Toast.LENGTH_SHORT).show();
            imagelist.get(position).pictureConsequence = charSequence.toString();
        }
        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
    private class MyCustomEditTextListener2 implements TextWatcher {
        int position;
        public void setposition(int n)
        {
            this.position = n;
        }
        MyCustomEditTextListener2(int position)
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
            imagelist.get(position).pictureSuggestion = charSequence.toString();
        }
        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
    public void readfromlocalpictrue(String fileName) throws IOException {
        String res="";
        try{
            if(fileIsExists(fileName+"bitmap"))
            {
                FileInputStream fis = new FileInputStream(this.getFilesDir().getPath() + "/" + fileName+"bitmap");
                Bitmap bitmap  = BitmapFactory.decodeStream(fis);
                Log.v("photobitmap",String.valueOf(bitmap.getRowBytes() * bitmap.getHeight()));
                image.setImageBitmap(bitmap);
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
                    image.setImageBitmap(Base64Util.base64ToBitmap(res));
                    fin.close();
                }
                else
                    Toast.makeText(this,"????"+fileName,Toast.LENGTH_SHORT).show();
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
}

