package com.djandroid.jdroid.materialdesign.ClientLibrary.Parameter.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimmy on 2016/10/10.
 */
public class TaskField {
    private String fieldName;
    private List<TaskCategory> taskCategoryList;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public List<TaskCategory> getTaskCategoryList() {
        return taskCategoryList;
    }

    public void setTaskCategoryList(List<TaskCategory> taskCategoryList) {
        this.taskCategoryList = taskCategoryList;
    }

    public void addCategory(TaskCategory category)
    {
        if (this.taskCategoryList == null) this.taskCategoryList = new ArrayList<TaskCategory>();
        this.taskCategoryList.add(category);
    }

    public void removeCategory(TaskCategory category)
    {
        this.taskCategoryList.remove(category);
    }
}
