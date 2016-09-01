package com.tywl.myt.net.manager;


import android.os.Message;

import com.tywl.myt.net.source.WxTokenRefreshData;
import com.tywl.myt.net.source.WxTokenRefreshNetSource;

import api.common.net.able.RequestBack;
import api.common.net.source.AbstractSourceHandler;


/**
 * Created by Administrator on 2016/1/15.
 */
//刷新微信Token
public class WxTokenRefreshManage extends AbstractSourceHandler<WxTokenRefreshData> {
    public static final int CODE_WHAT_SUCCEED = 400;
    public static final int CODE_WHAT_FAILED = CODE_WHAT_SUCCEED + 1;
    private WxTokenRefreshNetSource netSource;

    private DataManagerListener dataManagerListener = new DataManagerListener() {
        @Override
        public Message onDealSucceed(int what, WxTokenRefreshData codeData, String msg) {
            return super.onDealSucceed(CODE_WHAT_SUCCEED, codeData, msg);
        }

        @Override
        public Message onDealFailed(int what, WxTokenRefreshData codeData, String msg) {
            return super.onDealFailed(CODE_WHAT_FAILED, codeData, msg);
        }
    };

    public WxTokenRefreshManage(RequestBack requestBack) {
        super(requestBack);
        netSource = new WxTokenRefreshNetSource();
        netSource.setRequsetListener(dataManagerListener);
    }

    public void setTokenReq(String appid, String grant_type, String refresh_token) {
        netSource.appid = appid;
        netSource.grant_type = grant_type;
        netSource.refresh_token = refresh_token;
    }

    public void doRequest() {
        netSource.doRequest();
    }
}
