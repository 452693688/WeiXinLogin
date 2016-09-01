package com.tywl.myt.net.source;


import com.tywl.myt.net.parameter.request.WxTokenCheckBean;

import api.common.net.source.AbstractRequestData;

/**
 * Created by Administrator on 2016/1/15.
 */
public class WxTokenCheckRequest extends AbstractRequestData<WxTokenCheckBean> {

    public WxTokenCheckBean req = new WxTokenCheckBean();

    @Override
    protected WxTokenCheckBean getData() {
        return req;
    }

    private String path = "https://api.weixin.qq.com/sns/auth";
    @Override
    protected String getRequestUrl() {
        path += "?access_token=" + req.access_token + "&openid=" + req.openid;
        return path;
    }

    @Override
    protected String postRequestUrl() {
        return path;
    }
}
