package com.tywl.myt.net.source;


import com.tywl.myt.net.parameter.result.WxBaseRes;
import com.tywl.myt.utile.DLog;

import api.common.json.JSONUtile;
import api.common.net.source.AbstractSourceRTS;

/**
 * Created by Administrator on 2016/1/15.
 */
public class WxTokenCheckNetSource extends AbstractSourceRTS<WxTokenCheckData, WxTokenCheckRequest> {
    private final String TAG = "WxTokenCheckNetSource";
    //是	调用接口凭证
    public String access_token;
    //是	普通用户标识，对该公众帐号唯一
    public String openid;

    @Override
    protected WxTokenCheckRequest getRequest() {
        WxTokenCheckRequest r = new WxTokenCheckRequest();
        r.req.access_token = access_token;
        r.req.openid = openid;
        return r;
    }


    @Override
    protected WxTokenCheckData parseResp(byte[] bytes, byte[] request) {
        String json = new String(bytes);
        DLog.e(TAG, "-----" + json);
        WxBaseRes result = (WxBaseRes) JSONUtile.json2Obj(json, WxBaseRes.class);
        WxTokenCheckData data = null;
        if (result != null) {
            data = new WxTokenCheckData();
            data.code = result.getErrcode();
            data.msg = result.getErrmsg();
        }
        return data;
    }
}
