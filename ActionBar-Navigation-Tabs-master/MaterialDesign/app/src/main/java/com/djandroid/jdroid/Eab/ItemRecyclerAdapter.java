package com.djandroid.jdroid.Eab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Helper.TaskCategoryDetail;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.ItemDetail;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.ScoreType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.djandroid.jdroid.Eab.QuestionActivity.readfromlocal;

public class ItemRecyclerAdapter extends  RecyclerView.Adapter<ItemRecylerViewHolder> {
    Context context;
    LayoutInflater inflater;
    //TaskDetailResponse projectdetail;
    public ItemRecyclerAdapter(Context context) {
        this.context=context;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public ItemRecylerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.item_list, parent, false);
        ItemRecylerViewHolder viewHolder=new ItemRecylerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemRecylerViewHolder holder, int position) {
        try {
            holder.tv1.setTextColor(itemcolor(ProjectDetailActivity.taskdetailresponse.taskCategoryList.get(position)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //holder.tv1.setText(ProjectDetailActivity.taskdetailresponse.taskCategoryList.get(position).categoryName +
        //"\n" + ProjectDetailActivity.taskdetailresponse.taskCategoryList.get(position).tabName);
        holder.tv1.setText(ProjectDetailActivity.taskdetailresponse.taskCategoryList.get(position).categoryName);
        //holder.tv1.setTextColor(Color.RED);
        holder.itemView.setOnClickListener(clickListener);
        holder.itemView.setTag(holder);
    }


    public int itemcolor(TaskCategoryDetail temp) throws IOException {
        readfromlocal.clear();
        readfromlocalmap(ProjectDetailActivity.taskid + temp.categoryId);
        for(int i = 0 ; i < temp.taskItemList.size() ; i++)
        {
            if(readfromlocal.containsKey(temp.taskItemList.get(i).itemId))
            {
                if(readfromlocal.get(temp.taskItemList.get(i).itemId).scoreType == ScoreType.None)
                    return Color.RED;
            }
            else if(temp.taskItemList.get(i).scoreType == ScoreType.None)
                return Color.RED;
        }
        for(Map.Entry<String,ItemDetail>entry:readfromlocal.entrySet())
        {
            if(entry.getValue().scoreType == ScoreType.None)
                return Color.RED;
        }
        return Color.BLACK;
    }
    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ItemRecylerViewHolder vholder = (ItemRecylerViewHolder) v.getTag();
            int position = vholder.getPosition();
            ProjectItemActivity.rememberposition=position;
            //Toast.makeText(context,"This is position "+position,Toast.LENGTH_SHORT ).show();
            Intent intent=new Intent();
            intent.putExtra("projectitems", new Gson().toJson(ProjectDetailActivity.taskdetailresponse.taskCategoryList.get(position)));
            ProjectItemActivity.categorypotion = position;
            intent.setClass(context, QuestionActivity.class);
            context.startActivity(intent);
        }
    };

    public void readfromlocalmap(String fileName) throws IOException {
        String res="";
        try{
            if(fileIsExists(fileName)) {
                FileInputStream fin = context.openFileInput(fileName);
                int length = fin.available();
                byte[] buffer = new byte[length];
                fin.read(buffer);
                res = EncodingUtils.getString(buffer, "UTF-8");
                Type listType = new TypeToken<LinkedHashMap<String,ItemDetail>>(){}.getType();
                readfromlocal = new Gson().fromJson(res,listType);
                fin.close();
            }
            else {
                //   Toast.makeText(this, "未读取到" + fileName, Toast.LENGTH_SHORT).show();
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
            File f=new File(context.getFilesDir().getPath() + "/" + strFile);
            Log.d("aaa",context.getFilesDir().getPath().toString());
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
    @Override
    public int getItemCount() {
        return ProjectDetailActivity.taskdetailresponse.taskCategoryList.size();
    }

}