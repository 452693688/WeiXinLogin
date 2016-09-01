package com.tywl.myt.net.manager;


import android.os.Message;

import com.tywl.myt.net.source.WxTokenCheckData;
import com.tywl.myt.net.source.WxTokenCheckNetSource;

import api.common.net.able.RequestBack;
import api.common.net.source.AbstractSourceHandler;


/**
 * Created by Administrator on 2016/1/15.
 */
//检查微信Token是否有效
public class WxTokenCheckManage extends AbstractSourceHandler<WxTokenCheckData> {
    public static final int CODE_WHAT_SUCCEED = 300;
    public static final int CODE_WHAT_FAILED = CODE_WHAT_SUCCEED + 1;
    private WxTokenCheckNetSource netSource;

    private DataManagerListener dataManagerListener = new DataManagerListener() {
        @Override
        public Message onDealSucceed(int what, WxTokenCheckData codeData, String msg) {
            return super.onDealSucceed(CODE_WHAT_SUCCEED, codeData, msg);
        }

        @Override
        public Message onDealFailed(int what, WxTokenCheckData codeData, String msg) {
            return super.onDealFailed(CODE_WHAT_FAILED, codeData, msg);
        }
    };

    public WxTokenCheckManage(RequestBack requestBack) {
        super(requestBack);
        netSource = new WxTokenCheckNetSource();
        netSource.setRequsetListener(dataManagerListener);
    }

    public void setTokenReq(String access_token, String openid) {
        netSource.access_token = access_token;
        netSource.openid = openid;
    }

    public void doRequest() {
        netSource.doRequest();
    }
}
