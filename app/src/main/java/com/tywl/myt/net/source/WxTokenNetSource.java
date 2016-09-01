package com.tywl.myt.net.source;


import com.tywl.myt.net.parameter.request.WxTokenBean;
import com.tywl.myt.net.parameter.result.WxTokenRes;
import com.tywl.myt.utile.DLog;

import api.common.json.JSONUtile;
import api.common.net.source.AbstractSourceRTS;

/**
 * Created by Administrator on 2016/1/15.
 */
public class WxTokenNetSource extends AbstractSourceRTS<WxTokenData, WxTokenRequest> {
    private final String TAG = "WxTokenNetSource";
    public WxTokenBean wxTokenReq;

    @Override
    protected WxTokenRequest getRequest() {
        WxTokenRequest r = new WxTokenRequest();
        r.req = wxTokenReq;
        return r;
    }


    @Override
    protected WxTokenData parseResp(byte[] bytes, byte[] request) {
        String json = new String(bytes);
        DLog.e(TAG, "-----" + json);
        WxTokenRes result = (WxTokenRes) JSONUtile.json2Obj(json, WxTokenRes.class);
        WxTokenData data = null;
        if (result != null) {
            data = new WxTokenData();
            data.result = result;
            data.code = result.getErrcode();
            data.msg = result.getErrmsg();
        }
        return data;
    }
}
