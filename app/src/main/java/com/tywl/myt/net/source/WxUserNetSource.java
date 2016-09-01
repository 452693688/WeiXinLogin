package com.tywl.myt.net.source;


import com.tywl.myt.net.parameter.result.WxUserRes;
import com.tywl.myt.utile.DLog;

import api.common.json.JSONUtile;
import api.common.net.source.AbstractSourceRTS;


/**
 * Created by Administrator on 2016/1/15.
 */
public class WxUserNetSource extends AbstractSourceRTS<WxUserData, WxUserReq> {
    private final String TAG = "WxUserNetSource";
    public String access_token;
    public String openid;
    public String lang;

    @Override
    protected WxUserReq getRequest() {
        WxUserReq req = new WxUserReq();
        req.req.access_token = access_token;
        req.req.openid = openid;
        req.req.lang = lang;
        return req;
    }

    @Override
    protected WxUserData parseResp(byte[] bytes, byte[] request) {
        String json = new String(bytes);
        DLog.e(TAG, "-----" + json);
        WxUserRes result = (WxUserRes) JSONUtile.json2Obj(json, WxUserRes.class);
        WxUserData data = null;
        if (result != null) {
            data = new WxUserData();
            data.result = result;
            data.code = result.getErrcode();
            data.msg = result.getErrmsg();
        }
        return data;
    }
}
