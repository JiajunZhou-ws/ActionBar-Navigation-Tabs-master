package com.djandroid.jdroid.Eauditing;

public class Question {
    public long id;
    public String itemid;
    public String question, description;
    public String option1, option2, option3,option4;
    public String comment;
    public int checkedId = -1;
    public int score;
    public boolean isScore;

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