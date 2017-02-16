package com.djandroid.jdroid.Eab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import me.kareluo.intensify.image.IntensifyImageView;

public class PreviewActivity extends ActionBarActivity {
    IntensifyImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        imageView = (IntensifyImageView) findViewById(R.id.imageview);

        Intent intent = getIntent();
        try {
            readfromlocalmap(intent.getStringExtra("picturename"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void readfromlocalmap(String fileName) throws IOException {
        String res="";
        try{
            if(fileIsExists(fileName)) {
                FileInputStream fin = openFileInput(fileName);
                int length = fin.available();
                byte[] buffer = new byte[length];
                fin.read(buffer);
                res = EncodingUtils.getString(buffer, "UTF-8");
                Bitmap tempbitmap = Base64Util.base64ToBitmap(res);
                File file = new File(this.getFilesDir().getPath() + "/"  + "temp.jpg");
                FileOutputStream out = new FileOutputStream(file);
                if(tempbitmap.compress(Bitmap.CompressFormat.JPEG, 100, out))
                {
                    out.flush();
                    out.close();
                }
                //previewimage.setImageBitmap(tempbitmap);
                imageView.setImage(this.getFilesDir().getPath() + "/" + "temp.jpg");
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
}
