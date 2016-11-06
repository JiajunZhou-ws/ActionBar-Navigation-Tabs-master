package com.djandroid.jdroid.materialdesign.ClientLibrary.HttpModel.AndroidTaskService;

import java.util.ArrayList;
import java.util.List;

import com.djandroid.jdroid.materialdesign.ClientLibrary.Parameter.Task.TaskItem;

/**
 * Created by Jimmy on 2016/11/2.
 */
public class TaskCategoryDetail {
    public String CategoryName;
    public List<TaskItem> taskItemList;

    public void addTaskItem(TaskItem taskItem)
    {
        if (this.taskItemList == null) this.taskItemList = new ArrayList<TaskItem>();
        this.taskItemList.add(taskItem);
    }
}
