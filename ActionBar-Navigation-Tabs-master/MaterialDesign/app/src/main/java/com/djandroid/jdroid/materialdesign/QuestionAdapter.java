package com.djandroid.jdroid.materialdesign;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

/**
 * Created by krishna on 9/1/16.
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder>{

    private static final String TAG = "QuestionAdapter";
    private LayoutInflater inflater;
    private List<Question> questions;
    private String[] mDataset;
    private Context context;
    public QuestionAdapter(Context context, List<Question> questions) {
        this.inflater = LayoutInflater.from(context);
        this.questions = questions;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_question, parent, false);
        ViewHolder vh = new ViewHolder(view, new MyCustomEditTextListener(),context);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Question current = questions.get(position);
        holder.setQuestion(current.question);
        holder.textDescription.setText(current.description);
        //holder.textcomment.setTag(position);
        holder.imagetest.setTag(position);
        holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
        holder.textcomment.setText(questions.get(holder.getAdapterPosition()).comment);
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
        private EditText textcomment;
        private RadioGroup radioGroupOptions;
        private RadioButton radioButtonOption1, radioButtonOption2;
        private RadioButton radioButtonOption3;
        private ImageView imagetest;
        Context  context;
        public MyCustomEditTextListener myCustomEditTextListener;
        public ViewHolder(final View itemView, MyCustomEditTextListener myCustomEditTextListener, Context maincontext) {
            super(itemView);
            linearLayoutContainer = (LinearLayout) itemView.findViewById(R.id.linear_layout_container);
            textViewQuestion = (TextView) itemView.findViewById(R.id.text_view_question);
            textDescription = (TextView) itemView.findViewById(R.id.text_view_explanation);
            radioGroupOptions = (RadioGroup) itemView.findViewById(R.id.radio_group_options);
            radioButtonOption1 = (RadioButton) itemView.findViewById(R.id.radio_button_option_1);
            radioButtonOption2 = (RadioButton) itemView.findViewById(R.id.radio_button_option_2);
            radioButtonOption3 = (RadioButton) itemView.findViewById(R.id.radio_button_option_3);
            imagetest = (ImageView)itemView.findViewById(R.id.imagetest);
            this.context = maincontext;
            imagetest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position =(int) imagetest.getTag();
                    Log.v("zhoujiajun", String.valueOf(position));
                    Toast.makeText(itemView.getContext(),"click picture" + String.valueOf(position),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    ((Activity) context).startActivityForResult(intent,1);
                }
            });
            textcomment = (EditText) itemView.findViewById(R.id.editcomment);
            this.myCustomEditTextListener = myCustomEditTextListener;
            textcomment.addTextChangedListener(myCustomEditTextListener);

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

            if(question.isAnswered) {
                radioGroupOptions.check(question.checkedId);
            } else {
                radioGroupOptions.check(-1);
            }
            radioGroupOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int pos = (int) group.getTag();
                    Question que = questions.get(pos);
                    que.isAnswered = true;
                    que.checkedId = checkedId;
                    Log.e(TAG, pos + " :onCheckedChanged: " + que.toString());
                }
            });
        }
    }
    private class MyCustomEditTextListener implements TextWatcher {
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
            questions.get(position).comment = charSequence.toString();
            Log.v("zhoujiajun", questions.get(position).toString());
        }
        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}