package com.djandroid.jdroid.materialdesign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by dhawal sodha parmar on 5/4/2015.
 */
public class ProjectRecycleFragment extends Fragment {

    RecyclerView recList;

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
        recList.setAdapter(new MyAdapter(getActivity()));
        recList.setLayoutManager(llm);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public class MyAdapter extends RecyclerView.Adapter<RecylerViewHolder> {
        String [] project_name={"AX001-项目1","AX002-项目2","AX003-项目3","AX004-项目4",
                "AX005-项目5","AX006-项目6","AX007-项目7","AX008-项目8","AX009-项目9","AX010-项目10","AX011-项目11"};
        String [] customer_name={"华为审计","华为审计","ABB审计","平安审计",
                "XX审计","XX审计","XX审计","XX审计","XX审计","XX审计","XX审计"};
        Context context;
        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(Context context) {
            this.context = context;
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
                Intent intent = new Intent();
                intent.putExtra("projectname",project_name[position]);
                intent.setClass(context,ProjectDetailActivity.class);
                context.startActivity(intent);

            }
        };
        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final RecylerViewHolder holder, int position) {
            holder.tv1.setText(project_name[position]);
            holder.tv2.setText(customer_name[position]);
            holder.itemView.setOnClickListener(clickListener);
            holder.itemView.setTag(holder);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return project_name.length;
        }
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

    public void setupListItems() {
    }
}
