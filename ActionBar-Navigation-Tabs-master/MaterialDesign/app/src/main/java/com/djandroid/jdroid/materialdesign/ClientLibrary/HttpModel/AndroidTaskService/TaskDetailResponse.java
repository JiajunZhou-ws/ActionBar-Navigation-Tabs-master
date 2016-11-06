package com.djandroid.jdroid.materialdesign.ClientLibrary.HttpModel.AndroidTaskService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimmy on 2016/11/2.
 */
public class TaskDetailResponse {
    public String taskSiteid;
    public List<TaskCategoryDetail> taskCategoryList;

    public void addTaskCategoryDetail(TaskCategoryDetail taskCategoryDetail)
    {
        if (this.taskCategoryList == null) this.taskCategoryList = new ArrayList<TaskCategoryDetail>();
        this.taskCategoryList.add(taskCategoryDetail);
    }
}
