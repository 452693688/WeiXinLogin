package com.tywl.myt.net.manager;


import android.os.Message;

import com.tywl.myt.net.source.WxUserData;
import com.tywl.myt.net.source.WxUserNetSource;

import api.common.net.able.RequestBack;
import api.common.net.source.AbstractSourceHandler;


/**
 * Created by Administrator on 2016/1/15.
 */
//获取微信Token
public class WxUserManage extends AbstractSourceHandler<WxUserData> {
    public static final int CODE_WHAT_SUCCEED = 100;
    public static final int CODE_WHAT_FAILED = CODE_WHAT_SUCCEED + 1;
    private WxUserNetSource netSource;

    private DataManagerListener dataManagerListener = new DataManagerListener() {
        @Override
        public Message onDealSucceed(int what, WxUserData codeData, String msg) {
            return super.onDealSucceed(CODE_WHAT_SUCCEED, codeData, msg);
        }

        @Override
        public Message onDealFailed(int what, WxUserData codeData, String msg) {
            return super.onDealFailed(CODE_WHAT_FAILED, codeData, msg);
        }
    };

    public WxUserManage(RequestBack requestBack) {
        super(requestBack);
        netSource = new WxUserNetSource();
        netSource.setRequsetListener(dataManagerListener);
    }

    public void setTokenReq(String access_token, String openid, String lang) {
        netSource.access_token = access_token;
        netSource.openid = openid;
        netSource.lang = lang;
    }

    public void doRequest() {
        netSource.doRequest();
    }
}
