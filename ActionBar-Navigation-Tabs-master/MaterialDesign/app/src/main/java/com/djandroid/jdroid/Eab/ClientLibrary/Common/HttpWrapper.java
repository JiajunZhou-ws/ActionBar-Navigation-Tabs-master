package com.djandroid.jdroid.Eab.ClientLibrary.Common;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Jimmy on 2016/10/25.
 */
public class HttpWrapper {
    public static EAuditingHttpResponse HttpGet(String url)
    {
        EAuditingHttpResponse response = EAuditingHttpResponse.ErrorResponse;
        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            String fullURL = ClientConfiguration.ServiceUrl + url;

            HttpGet httpGet = new HttpGet(fullURL);
            HttpResponse response1 = httpclient.execute(httpGet);

            String responseString = EntityUtils.toString(response1.getEntity());
            response.response = responseString;
            response.httpStatus = HttpStatus.SC_OK;
        }
        catch(Exception e)
        {
            System.out.println("Error occured in HttpGet:" + url + "\n" + e.toString() + "\n----------------------------");
            e.printStackTrace();
        }
        return response;
    }


    public static EAuditingHttpResponse HttpPost(String url, String requestBody)
    {
        EAuditingHttpResponse response = EAuditingHttpResponse.ErrorResponse;
        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            String fullURL = ClientConfiguration.ServiceUrl + url;

            HttpPost httpPost = new HttpPost(fullURL);
            httpPost.setEntity(new StringEntity(requestBody, HTTP.UTF_8));
            httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
            HttpResponse response1 = httpclient.execute(httpPost);
            String responseString = EntityUtils.toString(response1.getEntity());
            response.response = responseString;
            response.httpStatus = HttpStatus.SC_OK;
        }
        catch(Exception e)
        {
            System.out.println("Error occured in HttpPost:" + url + "\n" + e.toString() + "\n----------------------------");
            e.printStackTrace();
        }
        return response;
    }

    public static String SHA256(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }


}
