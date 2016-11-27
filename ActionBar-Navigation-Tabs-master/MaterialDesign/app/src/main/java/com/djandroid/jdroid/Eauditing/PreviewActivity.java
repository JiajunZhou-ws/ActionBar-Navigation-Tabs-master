package com.djandroid.jdroid.Eauditing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class PreviewActivity extends ActionBarActivity implements View.OnTouchListener {
    ImageView previewimage;

    private PointF point0 = new PointF();
    private PointF pointM = new PointF();

    private final float ZOOM_MIN_SPACE = 10f;

    private final int NONE = 0;
    private final int DRAG = 1;
    private final int ZOOM = 2;
    private int mode = NONE;

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    private int displayHeight = 1920;
    private int displayWidth = 1080;

    private float minScale = 1f;
    private float maxScale = 10f;
    private float currentScale = 1f;
    private float oldDist;

    private int imgWidth;
    private int imgHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        previewimage = (ImageView) findViewById(R.id.imageView);
        previewimage.setOnTouchListener(this);

        Intent intent = getIntent();
        try {
            readfromlocalmap(intent.getStringExtra("picturename"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView imgv = (ImageView) v;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                point0.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > ZOOM_MIN_SPACE) {
                    savedMatrix.set(matrix);
                    setMidPoint(event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                whenMove(event);
                break;

        }
        imgv.setImageMatrix(matrix);
        checkView();
        return true;
    }

    private void whenMove(MotionEvent event) {
        switch (mode) {
            case DRAG:
                matrix.set(savedMatrix);
                matrix.postTranslate(event.getX() - point0.x, event.getY()
                        - point0.y);
                break;
            case ZOOM:
                float newDist = spacing(event);
                if (newDist > ZOOM_MIN_SPACE) {
                    matrix.set(savedMatrix);
                    float sxy = newDist / oldDist;
                    System.out.println(sxy + "<==放大缩小倍数");
                    matrix.postScale(sxy, sxy, pointM.x, pointM.y);
                }
                break;
        }
    }

    // 两个触点的距离
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void setMidPoint(MotionEvent event) {
        float x = event.getX(0) + event.getY(1);
        float y = event.getY(0) + event.getY(1);
        pointM.set(x / 2, y / 2);
    }

    // 图片居中
    private void center() {
        RectF  rect = new RectF(0, 0, imgWidth, imgHeight);
        matrix.mapRect(rect);
        float width = rect.width();
        float height = rect.height();
        float dx = 0;
        float dy = 0;

        if (width < displayWidth)
            dx = displayWidth / 2 - width / 2 - rect.left;
        else if (rect.left > 0)
            dx = -rect.left;
        else if (rect.right < displayWidth)
            dx = displayWidth - rect.right;

        if (height < displayHeight)
            dy = displayHeight / 2 - height / 2 - rect.top;
        else if (rect.top > 0)
            dy = -rect.top;
        else if (rect.bottom < displayHeight)
            dy = displayHeight - rect.bottom;

        matrix.postTranslate(dx, dy);
    }

    // 获取最小缩放比例
    private float getMinScale() {
        float sx = (float) displayWidth / imgWidth;
        float sy = (float) displayHeight / imgHeight;
        float scale = sx < sy ? sx : sy;
        if (scale > 1) {
            scale = 1f;
        }
        return scale;
    }

    // 检查约束条件，是否居中，空间显示是否合理
    private void checkView() {
        currentScale = getCurrentScale();
        if (mode == ZOOM) {
            if (currentScale < minScale) {
                matrix.setScale(minScale, minScale);
            }
            if (currentScale > maxScale) {
                matrix.set(savedMatrix);
            }
        }
        center();
    }

    // 图片当前的缩放比例
    private float getCurrentScale() {
        float[] values = new float[9];
        matrix.getValues(values);
        return values[Matrix.MSCALE_X];
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
                imgWidth = tempbitmap.getWidth();
                imgHeight = tempbitmap.getHeight();
                previewimage.setImageBitmap(tempbitmap);
                minScale = getMinScale();
                matrix.setScale(minScale,minScale);
                center();
                previewimage.setImageMatrix(matrix);
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
