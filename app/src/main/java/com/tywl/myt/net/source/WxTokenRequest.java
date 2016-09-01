package com.tywl.myt.net.source;


import com.tywl.myt.net.parameter.request.WxTokenBean;

import api.common.net.source.AbstractRequestData;

/**
 * Created by Administrator on 2016/1/15.
 */
public class WxTokenRequest extends AbstractRequestData<WxTokenBean> {

    public WxTokenBean req = new WxTokenBean();

    @Override
    protected WxTokenBean getData() {
        return req;
    }

    private String path = "https://api.weixin.qq.com/sns/oauth2/access_token";

    @Override
    protected String getRequestUrl() {
        path += "?appid=" + req.appId + "&secret=" + req.secret
                + "&code=" + req.code + "&grant_type=" + req.grant_type;
        return path;
    }

    @Override
    protected String postRequestUrl() {
        return path;
    }
}
