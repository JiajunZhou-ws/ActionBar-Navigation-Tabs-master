package com.djandroid.jdroid.Eauditing;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kundan on 10/26/2015.
 */
public class ItemRecylerViewHolder extends RecyclerView.ViewHolder {

    TextView tv1,tv2;
    ImageView imageView;

    public ItemRecylerViewHolder(View itemView) {
        super(itemView);
        tv1= (TextView) itemView.findViewById(R.id.listitem_title);
        imageView= (ImageView) itemView.findViewById(R.id.listitem_avatar);
    }
}
