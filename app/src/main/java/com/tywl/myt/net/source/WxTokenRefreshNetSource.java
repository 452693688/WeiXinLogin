package com.tywl.myt.net.source;


import com.tywl.myt.net.parameter.result.WxTokenRefreshRes;
import com.tywl.myt.utile.DLog;

import api.common.json.JSONUtile;
import api.common.net.source.AbstractSourceRTS;

/**
 * Created by Administrator on 2016/1/15.
 */
public class WxTokenRefreshNetSource extends AbstractSourceRTS<WxTokenRefreshData, WxTokenRefreshRequest> {
    private final String TAG = "WxTokenRefreshNetSource";
    //是	应用唯一标识
    public String appid;
    //是	填refresh_token
    public String grant_type;
    //是	填写通过access_token获取到的refresh_token参数
    public String refresh_token;

    @Override
    protected WxTokenRefreshRequest getRequest() {
        WxTokenRefreshRequest r = new WxTokenRefreshRequest();
        r.req.appid = appid;
        r.req.grant_type = grant_type;
        r.req.refresh_token = refresh_token;
        return r;
    }


    @Override
    protected WxTokenRefreshData parseResp(byte[] bytes, byte[] request) {
        String json = new String(bytes);
        DLog.e(TAG, "-----" + json);
        WxTokenRefreshRes result = (WxTokenRefreshRes) JSONUtile.json2Obj(json, WxTokenRefreshRes.class);
        WxTokenRefreshData data = null;
        if (result != null) {
            data = new WxTokenRefreshData();
            data.result = result;
            data.code = result.getErrcode();
            data.msg = result.getErrmsg();
        }
        return data;
    }
}
