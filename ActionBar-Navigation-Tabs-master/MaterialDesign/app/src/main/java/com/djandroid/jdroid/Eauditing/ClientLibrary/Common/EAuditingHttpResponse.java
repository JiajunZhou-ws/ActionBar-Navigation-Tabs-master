package com.djandroid.jdroid.Eauditing.ClientLibrary.Common;


import org.apache.http.HttpStatus;

/**
 * Created by Jimmy on 2016/10/25.
 */
public class EAuditingHttpResponse {
    public int httpStatus;
    public String response;

    public EAuditingHttpResponse(){}

    public EAuditingHttpResponse(int _httpStatus, String _response)
    {
        httpStatus = _httpStatus;
        response = _response;
    }

    public static EAuditingHttpResponse ErrorResponse = new EAuditingHttpResponse(HttpStatus.SC_BAD_REQUEST, "");
}
