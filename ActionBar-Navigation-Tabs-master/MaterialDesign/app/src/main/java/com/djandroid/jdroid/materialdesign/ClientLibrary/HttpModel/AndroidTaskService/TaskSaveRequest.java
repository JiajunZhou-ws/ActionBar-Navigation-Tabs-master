package com.djandroid.jdroid.materialdesign.ClientLibrary.HttpModel.AndroidTaskService;

import com.djandroid.jdroid.materialdesign.ClientLibrary.Parameter.Task.TaskItem;

import java.util.List;

/**
 * Created by Jimmy on 2016/11/13.
 */
public class TaskSaveRequest {
    public String taskid;
    public String categoryid;
    public List<TaskItem> itemList;
}
