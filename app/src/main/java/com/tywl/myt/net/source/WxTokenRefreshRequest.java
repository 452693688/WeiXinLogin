package com.tywl.myt.net.source;


import com.tywl.myt.net.parameter.request.WxTokenRefreshBean;

import api.common.net.source.AbstractRequestData;

/**
 * Created by Administrator on 2016/1/15.
 */
public class WxTokenRefreshRequest extends AbstractRequestData<WxTokenRefreshBean> {

    public WxTokenRefreshBean req = new WxTokenRefreshBean();

    @Override
    protected WxTokenRefreshBean getData() {
        return req;
    }

    private String path = "https://api.weixin.qq.com/sns/oauth2/refresh_token?";

    @Override
    protected String getRequestUrl() {
        path += "appid=" + req.appid
                + "&grant_type=" + req.grant_type
                + "&refresh_token=" + req.refresh_token;
        return path;
    }

    @Override
    protected String postRequestUrl() {
        return path;
    }
}
