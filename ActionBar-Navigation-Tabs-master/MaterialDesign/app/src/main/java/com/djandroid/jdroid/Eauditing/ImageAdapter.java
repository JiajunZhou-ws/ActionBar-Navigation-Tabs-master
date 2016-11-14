package com.djandroid.jdroid.Eauditing;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.LinkedHashMap;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
        Picture = new LinkedHashMap<>();
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public void setimage(int i , Bitmap temp)
    {
        //mThumbIds[i] = picture;
        Picture.put(i,temp);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        if (Picture.containsKey(position))
            imageView.setImageBitmap(Picture.get(position));
        return imageView;
    }
    private LinkedHashMap<Integer,Bitmap> Picture;
    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.a,R.drawable.a,R.drawable.a,R.drawable.a,R.drawable.a,R.drawable.a,R.drawable.a,R.drawable.a,R.drawable.a
    };
}