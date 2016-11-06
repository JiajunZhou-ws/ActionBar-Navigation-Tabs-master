package com.djandroid.jdroid.materialdesign;

public class Question {
    public long id;
    public String question, answer;
    public String option1, option2, option3;
    public String comment;
    public int checkedId = -1;
    public int correctOption;
    public boolean isAnswered;
    public boolean isComment;

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", checkedId=" + checkedId +
                ", correctOption=" + correctOption +
                ", comment=" + comment +
                '}';
    }
}