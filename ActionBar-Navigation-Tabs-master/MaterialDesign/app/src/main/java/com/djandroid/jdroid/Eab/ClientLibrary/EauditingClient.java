package com.djandroid.jdroid.Eab.ClientLibrary;

import com.djandroid.jdroid.Eab.ClientLibrary.Common.ClientConfiguration;
import com.djandroid.jdroid.Eab.ClientLibrary.Common.NetworkException;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.AuditorService.Request.AuditorLoginRequest;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.AuditorService.Response.AuditorLoginResponse;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.PictureService.Request.GetPictureRequest;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.PictureService.Request.SavePictureRequest;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.PictureService.Response.GetPictureResponse;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.PictureService.Response.SavePictureResponse;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Request.TaskItemForAuditorRequest;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Request.TaskItemSaveForAuditorRequest;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Request.TaskListForAuditorRequest;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Request.TaskSubmitForAuditorRequest;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Response.TaskItemForAuditorResponse;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Response.TaskItemSaveForAuditorResponse;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Response.TaskListForAuditorResponse;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Response.TaskSubmitForAuditorResponse;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.AuditStatus;
import com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.ItemDetail;
import com.google.gson.Gson;

import org.apache.http.HttpStatus;

import java.util.List;

import com.djandroid.jdroid.Eab.ClientLibrary.Common.EAuditingHttpResponse;
import com.djandroid.jdroid.Eab.ClientLibrary.Common.HttpWrapper;


/**
 * Created by Jimmy on 2016/10/25.
 */
public class EauditingClient {

    public static Gson gson = new Gson();

    public static AuditorLoginResponse UserLogin(String auditorName, String password) throws NetworkException
    {
        AuditorLoginRequest request = new AuditorLoginRequest();
        request.auditorName = auditorName;
        request.auditorPassword = HttpWrapper.SHA256(password);
        request.version = ClientConfiguration.Version;

        EAuditingHttpResponse response = HttpWrapper.HttpPost("/auditor/login", gson.toJson(request));
        if (response.httpStatus != HttpStatus.SC_OK) throw new NetworkException();
        return gson.fromJson(response.response, AuditorLoginResponse.class);
    }

    public static TaskListForAuditorResponse GetTaskList(String auditorName, AuditStatus auditStatus) throws NetworkException
    {
        TaskListForAuditorRequest request = new TaskListForAuditorRequest();
        request.auditorName = auditorName;
        request.auditStatus = auditStatus;
        request.version = ClientConfiguration.Version;

        EAuditingHttpResponse response = HttpWrapper.HttpPost("/task/list", gson.toJson(request));
        if (response.httpStatus != HttpStatus.SC_OK) throw new NetworkException();
        return gson.fromJson(response.response, TaskListForAuditorResponse.class);
    }

    public static TaskItemForAuditorResponse GetTaskDetail(String auditorName, String taskId) throws NetworkException
    {
        TaskItemForAuditorRequest request = new TaskItemForAuditorRequest();
        request.auditorName = auditorName;
        request.taskId = taskId;
        request.version = ClientConfiguration.Version;

        EAuditingHttpResponse response = HttpWrapper.HttpPost("/task/item/get", gson.toJson(request));
        if (response.httpStatus != HttpStatus.SC_OK) throw new NetworkException();
        return gson.fromJson(response.response, TaskItemForAuditorResponse.class);
    }

    public static TaskItemSaveForAuditorResponse SaveTaskDetail(String auditorName, String tabId, String categoryId, List<ItemDetail> itemList) throws NetworkException
    {
        TaskItemSaveForAuditorRequest request = new TaskItemSaveForAuditorRequest();
        request.auditorName = auditorName;
        request.categoryId = categoryId;
        request.tabId = tabId;
        request.itemList = itemList;

        EAuditingHttpResponse response = HttpWrapper.HttpPost("/task/item/save", gson.toJson(request));
        if (response.httpStatus != HttpStatus.SC_OK) throw new NetworkException();
        return gson.fromJson(response.response, TaskItemSaveForAuditorResponse.class);
    }

    public static TaskSubmitForAuditorResponse SubmitTask(String auditorName, String taskId) throws NetworkException
    {
        TaskSubmitForAuditorRequest request = new TaskSubmitForAuditorRequest();
        request.auditorName = auditorName;
        request.taskId = taskId;
        request.version = ClientConfiguration.Version;

        EAuditingHttpResponse response = HttpWrapper.HttpPost("/task/submit", gson.toJson(request));
        if (response.httpStatus != HttpStatus.SC_OK) throw new NetworkException();
        return gson.fromJson(response.response, TaskSubmitForAuditorResponse.class);
    }

    public static GetPictureResponse PictureGet(String pictureId, String taskId) throws NetworkException
    {
        GetPictureRequest request = new GetPictureRequest();
        request.pictureId = pictureId;
        request.taskId = taskId;
        request.version = ClientConfiguration.Version;

        EAuditingHttpResponse response = HttpWrapper.HttpPost("/picture/get", gson.toJson(request));
        if (response.httpStatus != HttpStatus.SC_OK) throw new NetworkException();
        return gson.fromJson(response.response, GetPictureResponse.class);
    }

    public static SavePictureResponse PictureSave(String pictureId, String taskId, String pictureBase64) throws NetworkException
    {
        SavePictureRequest request = new SavePictureRequest();
        request.pictureBase64 = pictureBase64;
        request.pictureId = pictureId;
        request.taskId = taskId;

        EAuditingHttpResponse response = HttpWrapper.HttpPost("/picture/save", gson.toJson(request));
        if (response.httpStatus != HttpStatus.SC_OK) throw new NetworkException();
        return gson.fromJson(response.response, SavePictureResponse.class);
    }
}
