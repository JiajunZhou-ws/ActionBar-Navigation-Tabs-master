package com.djandroid.jdroid.Eauditing;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.djandroid.jdroid.Eauditing.ClientLibrary.HttpModel.AndroidTaskService.TaskCategoryDetail;
import com.djandroid.jdroid.Eauditing.ClientLibrary.Parameter.Task.TaskItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.djandroid.jdroid.Eauditing.QuestionActivity.questions;
import static com.djandroid.jdroid.Eauditing.QuestionActivity.readfromlocal;

/**
 * Created by krishna on 9/1/16.
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder>{

    private static final String TAG = "QuestionAdapter";
    private LayoutInflater inflater;
    //private List<Question> questions;
    private String mapfilename;
    TaskCategoryDetail taskcategorydetail;
    //private String[] mDataset;
    private Context context;
    public static boolean onbind;
    public static boolean needfocus;
    public QuestionAdapter(Context context, String mapfilename, TaskCategoryDetail temp) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.mapfilename = mapfilename;
        this.taskcategorydetail = temp;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_question, parent, false);
        ViewHolder vh = new ViewHolder(view, new MyCustomEditTextListener(),new MyCustomAuditScoreListener(),context);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Question current = questions.get(position);
        holder.setQuestion(current.question);
        holder.textDescription.setText(current.description);
        //holder.textcomment.setTag(position);
        holder.imagetest.setTag(position);
       // onbind = true;
        holder.commentlistener.updatePosition(holder.getAdapterPosition(),holder.textcomment);
        holder.textcomment.setText(questions.get(holder.getAdapterPosition()).comment);
       // if(needfocus) {
        //    holder.textcomment.requestFocus();
      //      holder.textcomment.setSelection(1);
      //  }
       // onbind = false;
        holder.auditlistener.updatePosition(holder.getAdapterPosition());
        holder.auditscore.setText(String.valueOf(questions.get(holder.getAdapterPosition()).score));
        holder.setOptions(current, position);
        Log.e(TAG, position + " :onBindViewHolder: " + current.toString());
    }


    @Override
    public int getItemCount() {
        if (questions == null) {
            return 0;
        } else {
            return questions.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout linearLayoutContainer;
        private TextView textViewQuestion, textDescription;
        private EditText textcomment,auditscore;
        private RadioGroup radioGroupOptions;
        private RadioButton radioButtonOption1, radioButtonOption2;
        private RadioButton radioButtonOption3, radioButtonOption4;
        private ImageView imagetest;
        Context  context;
        public MyCustomEditTextListener commentlistener;
        public MyCustomAuditScoreListener auditlistener;
        public ViewHolder(final View itemView, MyCustomEditTextListener commentlistner,MyCustomAuditScoreListener auditlistener, Context maincontext) {
            super(itemView);
            linearLayoutContainer = (LinearLayout) itemView.findViewById(R.id.linear_layout_container);
            textViewQuestion = (TextView) itemView.findViewById(R.id.text_view_question);
            textDescription = (TextView) itemView.findViewById(R.id.text_view_explanation);

            textcomment = (EditText) itemView.findViewById(R.id.editcomment);
            this.commentlistener = commentlistner;
            textcomment.addTextChangedListener(commentlistner);

            auditscore = (EditText) itemView.findViewById(R.id.auditscore);
            this.auditlistener = auditlistener;
            auditscore.addTextChangedListener(auditlistener);

            radioGroupOptions = (RadioGroup) itemView.findViewById(R.id.radioGroup);
            radioButtonOption1 = (RadioButton) itemView.findViewById(R.id.radioButton1);
            radioButtonOption2 = (RadioButton) itemView.findViewById(R.id.radioButton2);
            radioButtonOption3 = (RadioButton) itemView.findViewById(R.id.radioButton3);
            radioButtonOption4 = (RadioButton) itemView.findViewById(R.id.radioButton4);

            imagetest = (ImageView)itemView.findViewById(R.id.camera);
            this.context = maincontext;
            imagetest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position =(int) imagetest.getTag();
                    Log.v("zhoujiajun", String.valueOf(position));
                    //Toast.makeText(itemView.getContext(),"click picture" + String.valueOf(position),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context,PhotoActivity.class);
                    if(null != readfromlocal.get(questions.get(position).itemid).getPicturePathList()
                            && QuestionActivity.readfromlocal.get(questions.get(position).itemid).getPicturePathList().size() > 0)
                        intent.putExtra("imagelist",new Gson().toJson(QuestionActivity.readfromlocal.get(questions.get(position).itemid).getPicturePathList()));
                    else if(QuestionActivity.readfromlocal.get(questions.get(position).itemid).getPicturePathList() == null)
                    {
                        List<String> temp = new ArrayList<String>();
                        intent.putExtra("imagelist",new Gson().toJson(temp));
                    }
                    intent.putExtra("itemid",questions.get(position).itemid);
                    context.startActivity(intent);
                    //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //((Activity) context).startActivityForResult(intent,1);
                }
            });


            //textcomment.addTextChangedListener(new TextWatcher() {
            //     public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            //     public void afterTextChanged(Editable editable) {}
            //      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //           if(textcomment.getTag()!=null){
            //               questions.get((int)textcomment.getTag()).comment =charSequence.toString();
            //notifyDataSetChanged();
            //                Log.v("zhoujiajun", questions.get((int)textcomment.getTag()).toString());
            //           }
            //        }
            //    });
        }


        public void setQuestion(String question) {
            textViewQuestion.setText(question);
        }

        public void setOptions(Question question, int position) {
            radioGroupOptions.setTag(position);
            //radioButtonOption1.setText(question.option1);
            //radioButtonOption2.setText(question.option2);
            //radioButtonOption3.setText(question.option3);
            Log.e(TAG, position + " :setOptions: " + question.toString());
            radioGroupOptions.check(question.checkedId);
            radioGroupOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int pos = (int) group.getTag();
                    Question que = questions.get(pos);
                    que.checkedId = checkedId;
                    Log.e(TAG, pos + " :onCheckedChanged: " + que.toString());
                }
            });
        }
    }



    private class MyCustomEditTextListener implements TextWatcher {
        private int position;
        private EditText textcomment;
        private boolean ischanged;
        public void updatePosition(int position, EditText textcomment) {
            this.textcomment = textcomment;
            this.position = position;
        }
        public void updateui(final int position)
        {
            if(!onbind && !ischanged) {
                notifyItemChanged(position);
                //textcomment.setText("aa");
                needfocus = true;
            }
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            questions.get(position).comment = charSequence.toString();
            if(readfromlocal.containsKey(questions.get(position).itemid))
            {
                readfromlocal.get(questions.get(position).itemid).setRemark(questions.get(position).comment);
            }
            else {
                TaskItem temp;
                int itemindex = 0; //find the index from taskcategorydetail
                for(int j = 0 ; j < taskcategorydetail.taskItemList.size() ; j++)
                {
                    if (taskcategorydetail.taskItemList.get(j).getItemId().equals(questions.get(position).itemid)) {
                        itemindex = j;
                        break;
                    }
                }
                temp = taskcategorydetail.taskItemList.get(itemindex);
                temp.setRemark(questions.get(position).comment);
                readfromlocal.put(questions.get(position).itemid, temp);
            }
          //  if (!charSequence.equals(""))
          //  {
            //    ischanged = false;
              //  updateui(position);
             //   ischanged = true;
          //  }
            Log.v("zhoujiajun", questions.get(position).toString());
        }
        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private class MyCustomAuditScoreListener implements TextWatcher {
        private int position;
        public void updatePosition(int position) {
            this.position = position;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            Pattern p = Pattern.compile("[0-9]*");
            Matcher m = p.matcher(charSequence.toString());
            if(m.matches() && !charSequence.toString().equals(""))
            {
                questions.get(position).score = Integer.valueOf(charSequence.toString());
                if(readfromlocal.containsKey(questions.get(position).itemid))
                {
                    readfromlocal.get(questions.get(position).itemid).setScore(questions.get(position).score);
                }
                else {
                    TaskItem temp;
                    int itemindex = 0; //find the index from taskcategorydetail
                    for(int j = 0 ; j < taskcategorydetail.taskItemList.size() ; j++)
                    {
                        if (taskcategorydetail.taskItemList.get(j).getItemId().equals(questions.get(position).itemid)) {
                            itemindex = j;
                            break;
                        }
                    }

                    temp = taskcategorydetail.taskItemList.get(itemindex);
                    temp.setScore(questions.get(position).score);
                    readfromlocal.put(questions.get(position).itemid, temp);
                }
            }

            Log.v("auditscore", charSequence.toString());
        }
        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}