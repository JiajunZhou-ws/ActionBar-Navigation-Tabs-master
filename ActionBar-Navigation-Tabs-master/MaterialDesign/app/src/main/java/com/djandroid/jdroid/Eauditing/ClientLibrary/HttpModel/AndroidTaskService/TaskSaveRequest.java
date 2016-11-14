package com.djandroid.jdroid.Eauditing.ClientLibrary.HttpModel.AndroidTaskService;

import com.djandroid.jdroid.Eauditing.ClientLibrary.Parameter.Task.TaskItem;

import java.util.List;

/**
 * Created by Jimmy on 2016/11/13.
 */
public class TaskSaveRequest {
    public String taskid;
    public String categoryid;
    public List<TaskItem> itemList;
}
