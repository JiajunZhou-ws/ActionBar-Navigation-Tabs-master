package com.djandroid.jdroid.Eab;

public class Question {
    public long id;
    public String itemid;
    public String question, description;
    public String comment;
    public int checkedId = -1;
    public int score;
    public boolean isScore;
    public boolean isdescriptionvisible;

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", checkedId=" + checkedId +
                ", comment=" + comment +
                '}';
    }
}