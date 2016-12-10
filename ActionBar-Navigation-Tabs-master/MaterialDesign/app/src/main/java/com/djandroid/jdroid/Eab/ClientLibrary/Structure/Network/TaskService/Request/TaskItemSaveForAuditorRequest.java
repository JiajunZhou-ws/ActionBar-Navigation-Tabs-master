package com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.TaskService.Request;

import com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail.ItemDetail;

import java.util.List;

/**
 * Created by Jimmy on 2016/12/10.
 */
public class TaskItemSaveForAuditorRequest {
    public String tabId;
    public String categoryId;
    public String auditorName;
    public List<ItemDetail> itemList;
}