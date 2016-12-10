package com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Response;

import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Helper.TaskCategoryDetail;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.VersionCheckResult;

import java.util.List;

/**
 * Created by Jimmy on 2016/12/7.
 */

public class TaskItemForAuditorResponse{
    public VersionCheckResult versionStatus;
    public String taskSiteid;
    public String taskSiteName;
    public List<TaskCategoryDetail> taskCategoryList;
}