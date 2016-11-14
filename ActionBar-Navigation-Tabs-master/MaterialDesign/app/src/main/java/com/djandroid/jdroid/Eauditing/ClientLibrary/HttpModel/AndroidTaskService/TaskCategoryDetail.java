package com.djandroid.jdroid.Eauditing.ClientLibrary.HttpModel.AndroidTaskService;

import java.util.List;

import com.djandroid.jdroid.Eauditing.ClientLibrary.Parameter.Task.TaskItem;

/**
 * Created by Jimmy on 2016/11/2.
 */
public class TaskCategoryDetail {
    public String CategoryName;
    public String CategoryId;  //taskid + categoryid
    public String FieldName;
    public List<TaskItem> taskItemList;
}
