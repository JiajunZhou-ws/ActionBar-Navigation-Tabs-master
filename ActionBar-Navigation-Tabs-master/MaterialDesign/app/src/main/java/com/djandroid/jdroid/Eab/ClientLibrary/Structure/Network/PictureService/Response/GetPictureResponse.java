package com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.PictureService.Response;

import com.djandroid.jdroid.Eab.ClientLibrary.Structure.Network.VersionCheckResult;

/**
 * Created by Jimmy on 2016/12/9.
 */
public class GetPictureResponse {
    public VersionCheckResult versionStatus;
    public String pictureBase64;
    public String pictureId;
    public String taskId;

}