package com.djandroid.jdroid.Eab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PhotoExplain extends AppCompatActivity {
    ImageView image;
    Toolbar toolbar;
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
        Intent intent = getIntent();
        final String picturename = intent.getStringExtra("picturename");
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
