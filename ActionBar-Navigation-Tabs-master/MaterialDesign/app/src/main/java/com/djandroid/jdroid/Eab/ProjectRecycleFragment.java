package com.djandroid.jdroid.Eab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Helper.TaskInformation;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.AuditStatus;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.AuditStatus.*;


/**
 * Created by Tonny Zhou on 12/11/2016.
 */
@SuppressLint("ValidFragment")
public class ProjectRecycleFragment extends Fragment {

    RecyclerView recList;
    AuditStatus taskstatus;
    Boolean isget;
    List<TaskInformation> listfromserver;
    public ProjectRecycleFragment()
    {}
    public ProjectRecycleFragment(List<TaskInformation> temp, AuditStatus tempstatus)
    {
        listfromserver = temp;
        taskstatus = tempstatus;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupListItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_project_recylerview, container, false);
        RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.project_recylerview);
        //recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setAdapter(new MyAdapter(getActivity(), listfromserver));
        recList.setLayoutManager(llm);
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public class MyAdapter extends RecyclerView.Adapter<RecylerViewHolder> {
        List<TaskInformation> projectlist;
        Context context;
        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(Context context,List<TaskInformation> listfromserver) {

            this.context = context;
            this.projectlist = listfromserver;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public RecylerViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.project_recyler_list, parent, false);
            // set the view's size, margins, paddings and layout parameters
            RecylerViewHolder vh = new RecylerViewHolder(v);
            return vh;
        }
        View.OnClickListener clickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecylerViewHolder vholder = (RecylerViewHolder) v.getTag();
                int position = vholder.getPosition();
                 //Toast.makeText(context,"This is position "+position,Toast.LENGTH_SHORT ).show();
                dialog(position);

            }
        };
        protected void dialog(final int n) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("是否使用脱机模式" + String.valueOf(n + 1));
            builder.setTitle("提示");
            builder.setPositiveButton("脱机", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.APPSTATUS = 1;
                    Intent intent = new Intent();
                    intent.putExtra("TaskInfomation", new Gson().toJson(projectlist.get(n)));
                    intent.setClass(context,ProjectDetailActivity.class);
                    context.startActivity(intent);
                }
            });
            builder.setNegativeButton("在线", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.APPSTATUS = 0;
                    Intent intent = new Intent();
                    intent.putExtra("TaskInfomation", new Gson().toJson(projectlist.get(n)));
                    intent.setClass(context,ProjectDetailActivity.class);
                    context.startActivity(intent);
                }
            });
            builder.create().show();
        }
        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final RecylerViewHolder holder, int position) {
            String textstatus = "";
            //projectlist.get(position).projectName = "周周周周周1周周周周周2周周周周周3周周周周周";
            //if(projectlist.get(position).projectName.length() >= 14)
            //    holder.tv1.setText(projectlist.get(position).taskSiteId+ "\n" + projectlist.get(position).projectName.substring(0,14));
           // else
            holder.tv1.setText(projectlist.get(position).taskSiteId+ "\n" + projectlist.get(position).projectName);
            switch (taskstatus)
            {
                case None:
                    textstatus = "未完成";
                    break;
                case NoPass:
                    textstatus = "审核未通过";
                    break;
                case Pass:
                    textstatus = "已完成";
                    break;
                case Waiting:
                    textstatus = "待审核";
                    break;
            }
            if(!taskiddate(projectlist.get(position).taskId + "task").equals(""))
                 holder.tv2.setText(textstatus + " 脱机版本" + taskiddate(projectlist.get(position).taskId + "task"));
            else
                 holder.tv2.setText(textstatus + " 无脱机版本");
            holder.itemView.setOnClickListener(clickListener);
            holder.itemView.setTag(holder);
            Log.d("zhoujiajun",String.valueOf(projectlist.size()));
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return projectlist.size();}
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class RecylerViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView tv1,tv2;
        ImageView imageView;

        public RecylerViewHolder(View v) {
            super(v);
            tv1= (TextView) itemView.findViewById(R.id.list_title);
            tv2= (TextView) itemView.findViewById(R.id.list_desc);
            imageView= (ImageView) itemView.findViewById(R.id.list_avatar);
        }
    }

    public String taskiddate(String strFile)
    {
        try
        {
            File f=new File(getActivity().getFilesDir().getPath() + "/" + strFile);
            if(!f.exists())
            {
                return "";
            }
            long time=f.lastModified();
            SimpleDateFormat formatter = new
                    SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(time);
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public void setupListItems() {
    }
}
