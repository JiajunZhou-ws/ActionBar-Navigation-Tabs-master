package com.djandroid.jdroid.Eab.ClientLibrary.Parameter.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimmy on 2016/10/10.
 */
public class TaskCategory {
    private String categoryName;
    private String categoryId;
    private List<TaskItem> taskItemList;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<TaskItem> getTaskItemList() {
        return taskItemList;
    }

    public void setTaskItemList(List<TaskItem> taskItemList) {
        this.taskItemList = taskItemList;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void addItem(TaskItem item)
    {
        if (this.taskItemList == null) this.taskItemList = new ArrayList<>();
        this.taskItemList.add(item);
    }

    public void removeItem(TaskItem item)
    {
        this.taskItemList.remove(item);
    }
}
