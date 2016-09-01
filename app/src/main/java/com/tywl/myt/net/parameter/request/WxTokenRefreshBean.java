package com.tywl.myt.net.parameter.request;

/**
 * Created by Administrator on 2016/9/1.
 */
public class WxTokenRefreshBean {
    //是	应用唯一标识
    public String appid;
    //是	填refresh_token
    public String grant_type;
    //是	填写通过access_token获取到的refresh_token参数
    public String refresh_token;
}
