package com.djandroid.jdroid.Eab;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Helper.TaskCategoryDetail;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.ItemDetail;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.ScoreType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.djandroid.jdroid.Eab.QuestionActivity.questions;
import static com.djandroid.jdroid.Eab.QuestionActivity.readfromlocal;

/**
 * Created by krishna on 9/1/16.
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder>{

    private static final String TAG = "QuestionAdapter";
    private LayoutInflater inflater;
    //private List<Question> questions;
    private String mapfilename;
    TaskCategoryDetail taskcategorydetail;
    private Context context;
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
        holder.mysettext(current.isdescriptionvisible,holder.textDescription,current.description);
        if(current.checkedId == 0)
            holder.textViewQuestion.setTextColor(Color.RED);
        holder.textViewQuestion.setTag(position);
        holder.goodcamera.setTag(position);
        holder.badcamera.setTag(position);
        holder.commentlistener.updatePosition(holder.getAdapterPosition(),holder.textcomment);
        holder.textcomment.setText(questions.get(holder.getAdapterPosition()).comment);
        if(readfromlocal.containsKey(questions.get(holder.getAdapterPosition()).itemid)) {
            if(!readfromlocal.get(questions.get(holder.getAdapterPosition()).itemid).goodPictureList.isEmpty())
                holder.goodpicnum.setText("√照片" + readfromlocal.get(questions.get(holder.getAdapterPosition()).itemid).goodPictureList.size() + "张");
            else
                holder.goodpicnum.setText("√照片"+"0张");
            if(!readfromlocal.get(questions.get(holder.getAdapterPosition()).itemid).badPictureList.isEmpty())
                holder.badpicnum.setText("×照片"+readfromlocal.get(questions.get(holder.getAdapterPosition()).itemid).badPictureList.size() + "张");
            else
                holder.badpicnum.setText("×照片" + "0张");
        }
        else {
            holder.goodpicnum.setText("√照片" + "0张");
            holder.badpicnum.setText("×照片" + "0张");
        }

        holder.goodpicnum.setTextColor(Color.GREEN);

        holder.badpicnum.setTextColor(Color.RED);
        holder.auditlistener.updatePosition(holder.getAdapterPosition());
        holder.auditscore.setText(String.valueOf(questions.get(holder.getAdapterPosition()).score));
        holder.setOptions(holder,current, position);
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
        private TextView textViewQuestion, textDescription, textScore;
        private TextView goodpicnum,badpicnum;
        private EditText textcomment,auditscore;
        private RadioGroup radioGroupOptions;
        private RadioButton radioButtonOption1, radioButtonOption2;
        private RadioButton radioButtonOption3, radioButtonOption4;
        private ImageView goodcamera;
        private ImageView badcamera;
        Context  context;
        public MyCustomEditTextListener commentlistener;
        public MyCustomAuditScoreListener auditlistener;
        public ViewHolder(final View itemView, MyCustomEditTextListener commentlistner,MyCustomAuditScoreListener auditlistener, Context maincontext) {
            super(itemView);
            linearLayoutContainer = (LinearLayout) itemView.findViewById(R.id.linear_layout_container);
            textViewQuestion = (TextView) itemView.findViewById(R.id.text_view_question);
            textDescription = (TextView) itemView.findViewById(R.id.text_view_explanation);
            textScore = (TextView) itemView.findViewById(R.id.text_view_score);
            goodpicnum = (TextView) itemView.findViewById(R.id.goodpicnum);
            badpicnum = (TextView) itemView.findViewById(R.id.badpicnum);

            textViewQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) textViewQuestion.getTag();
                    if(QuestionActivity.questions.get(position).isdescriptionvisible == false)
                        QuestionActivity.questions.get(position).isdescriptionvisible = true;
                    else
                        QuestionActivity.questions.get(position).isdescriptionvisible = false;
                    mysettext(QuestionActivity.questions.get(position).isdescriptionvisible,textDescription,QuestionActivity.questions.get(position).description);
                    //Toast.makeText(context,"zhegeshi::" + String.valueOf(position),Toast.LENGTH_SHORT).show();
                }
            });

            textcomment = (EditText) itemView.findViewById(R.id.editcomment);
            this.commentlistener = commentlistner;
            textcomment.addTextChangedListener(commentlistner);

            radioGroupOptions = (RadioGroup) itemView.findViewById(R.id.radioGroup);
            radioButtonOption1 = (RadioButton) itemView.findViewById(R.id.radioButton1);
            radioButtonOption2 = (RadioButton) itemView.findViewById(R.id.radioButton2);
            radioButtonOption3 = (RadioButton) itemView.findViewById(R.id.radioButton3);
            radioButtonOption4 = (RadioButton) itemView.findViewById(R.id.radioButton4);

            auditscore = (EditText) itemView.findViewById(R.id.auditscore);
            this.auditlistener = auditlistener;
            auditscore.addTextChangedListener(auditlistener);


            textScore.setVisibility(View.GONE);
            auditscore.setVisibility(View.GONE);

            goodcamera = (ImageView)itemView.findViewById(R.id.camera);
            this.context = maincontext;
            goodcamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position =(int) goodcamera.getTag();
                    QuestionActivity.rememberposition = position;
                    Log.v("zhoujiajun", String.valueOf(position));
                    Intent intent = new Intent(context,PhotoActivity.class);
                    intent.putExtra("itemid",questions.get(position).itemid);
                    intent.putExtra("cameratype","good");
                    context.startActivity(intent);
                }
            });

            badcamera = (ImageView)itemView.findViewById(R.id.camera1);
            badcamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position =(int) badcamera.getTag();
                    QuestionActivity.rememberposition = position;
                    Log.v("zhoujiajun", String.valueOf(position));
                    Intent intent = new Intent(context,PhotoActivity.class);
                    intent.putExtra("itemid",questions.get(position).itemid);
                    intent.putExtra("cameratype","bad");
                    context.startActivity(intent);
                }
            });
        }

        public void setQuestion(String question) {
            textViewQuestion.setText(question);
        }

        public void mysettext(Boolean temp,TextView description, String content)
        {
            if(temp) {
                description.setVisibility(View.VISIBLE);
                description.setText(content);
            }
            else
            {
                description.setVisibility(View.GONE);
            }
        }
        public void setOptions(final ViewHolder viewholder, Question question, int position) {
            radioGroupOptions.setTag(position);
            Log.e(TAG, position + " :setOptions: " + question.toString());
            radioGroupOptions.check(checkIDtoRealid(question.checkedId));

            //score part
            if(question.checkedId == 4) {
                viewholder.auditscore.setFocusableInTouchMode(true);
                viewholder.textScore.setTextColor(Color.BLACK);
            }
            else {
                viewholder.auditscore.clearFocus();
                viewholder.auditscore.setFocusableInTouchMode(false);
                viewholder.textScore.setTextColor(Color.RED);
            }

            radioGroupOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int pos = (int) group.getTag();
                    Question que = questions.get(pos);
                    que.checkedId = RealidtocheckID(checkedId);
                    viewholder.textViewQuestion.setTextColor(Color.BLACK);

                    if(que.checkedId == 4) {
                        viewholder.auditscore.setFocusableInTouchMode(true);
                        viewholder.textScore.setTextColor(Color.BLACK);
                    }
                    else {
                        viewholder.auditscore.clearFocus();
                        viewholder.auditscore.setFocusableInTouchMode(false);
                        viewholder.textScore.setTextColor(Color.RED);
                    }

                    if(readfromlocal.containsKey(questions.get(pos).itemid))
                    {
                        readfromlocal.get(questions.get(pos).itemid).scoreType = RealidtoScoretype(que.checkedId);
                    }
                    else {
                        ItemDetail temp;
                        int itemindex = 0; //find the index from taskcategorydetail
                        for(int j = 0 ; j < taskcategorydetail.taskItemList.size() ; j++)
                        {
                            if (taskcategorydetail.taskItemList.get(j).itemId.equals(questions.get(pos).itemid)) {
                                itemindex = j;
                                break;
                            }
                        }
                        temp = taskcategorydetail.taskItemList.get(itemindex);
                        temp.scoreType = RealidtoScoretype(questions.get(pos).checkedId);
                        readfromlocal.put(questions.get(pos).itemid, temp);
                    }
                    Log.e(TAG, pos + " :onCheckedChanged: " + que.toString());
                }
            });
        }
        public int checkIDtoRealid(int num)
        {
            switch (num)
            {
                case 0:
                    return -1;
                case 1:
                    return radioButtonOption1.getId();
                case 2:
                    return radioButtonOption2.getId();
                case 3:
                    return radioButtonOption3.getId();
                case 4:
                    return radioButtonOption4.getId();
                default:
                    return -1;
            }
        }
        public int RealidtocheckID(int num)
        {
            if(num == radioButtonOption1.getId())
                return 1;
            else if(num == radioButtonOption2.getId())
                return 2;
            else if(num == radioButtonOption3.getId())
                return 3;
            else if(num == radioButtonOption4.getId())
                return 4;
            return -1;
        }
        public ScoreType RealidtoScoretype(int num)
        {
            switch (num)
            {
                case 1:
                    return ScoreType.Pass;
                case 2:
                    return ScoreType.NoPass;
                case 3:
                    return ScoreType.NA;
                case 4:
                    return ScoreType.Score;
            }
            return ScoreType.None;
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
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            questions.get(position).comment = charSequence.toString();
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
            if(!charSequence.toString().equals("") && m.matches()) {
                questions.get(position).score = Integer.valueOf(charSequence.toString());
                if (readfromlocal.containsKey(questions.get(position).itemid)) {
                    readfromlocal.get(questions.get(position).itemid).scoreValue = questions.get(position).score;
                } else {
                    ItemDetail temp;
                    int itemindex = 0; //find the index from taskcategorydetail
                    for (int j = 0; j < taskcategorydetail.taskItemList.size(); j++) {
                        if (taskcategorydetail.taskItemList.get(j).itemId.equals(questions.get(position).itemid)) {
                            itemindex = j;
                            break;
                        }
                    }
                    temp = taskcategorydetail.taskItemList.get(itemindex);
                    temp.scoreValue = questions.get(position).score;
                    readfromlocal.put(questions.get(position).itemid, temp);
                }
                Log.v("auditscore", charSequence.toString());
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}