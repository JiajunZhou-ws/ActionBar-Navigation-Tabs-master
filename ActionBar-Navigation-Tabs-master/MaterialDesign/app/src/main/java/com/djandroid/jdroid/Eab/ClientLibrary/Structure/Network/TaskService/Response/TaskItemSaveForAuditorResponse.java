package com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Response;

/**
 * Created by Jimmy on 2016/12/9.
 */
public enum TaskItemSaveForAuditorResponse{
    Success,
    SaveFailed, //服务器上该任务已经不存在
    WrongAuditor, //您不是该任务有效的上传者
    WrongStatus, //已提交条目无法上传
    NetWorkError //网络传输错误
}