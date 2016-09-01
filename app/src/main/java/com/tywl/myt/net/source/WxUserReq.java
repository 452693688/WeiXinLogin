package com.tywl.myt.net.source;


import com.tywl.myt.net.parameter.request.WxUserBean;

import api.common.net.source.AbstractRequestData;


/**
 * Created by Administrator on 2016/1/15.
 */
public class WxUserReq extends AbstractRequestData<WxUserBean> {

    public WxUserBean req = new WxUserBean();

    @Override
    protected WxUserBean getData() {
        return req;
    }

    String path = "https://api.weixin.qq.com/sns/userinfo";

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
