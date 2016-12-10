package com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Response;

import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Helper.TaskInformation;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.VersionCheckResult;

import java.util.List;

/**
 * Created by Jimmy on 2016/12/7.
 */

public class TaskListForAuditorResponse{
    public VersionCheckResult versionStatus;
    public List<TaskInformation> taskList;
}