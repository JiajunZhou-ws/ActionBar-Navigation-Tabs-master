package com.djandroid.jdroid.materialdesign.ClientLibrary.Parameter.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimmy on 2016/11/1.
 */
public class TaskDetail {
    private List<TaskField> taskFieldList;

    public List<TaskField> getTaskFieldList() {
        return taskFieldList;
    }

    public void setTaskFieldList(List<TaskField> taskFieldList) {
        this.taskFieldList = taskFieldList;
    }

    public void addField(TaskField taskField)
    {
        if (this.taskFieldList == null) this.taskFieldList = new ArrayList<TaskField>();
        this.taskFieldList.add(taskField);
    }

    public void removeField(TaskField taskField)
    {
        this.taskFieldList.remove(taskField);
    }
}
