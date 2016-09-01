package com.tywl.myt.net.manager;


import android.os.Message;

import com.tywl.myt.net.parameter.request.WxTokenBean;
import com.tywl.myt.net.source.WxTokenData;
import com.tywl.myt.net.source.WxTokenNetSource;

import api.common.net.able.RequestBack;
import api.common.net.source.AbstractSourceHandler;


/**
 * Created by Administrator on 2016/1/15.
 */
//获取微信Token
public class WxTokenManage extends AbstractSourceHandler<WxTokenData> {
    public static final int CODE_WHAT_SUCCEED = 500;
    public static final int CODE_WHAT_FAILED = CODE_WHAT_SUCCEED + 1;
    private WxTokenNetSource netSource;

    private DataManagerListener dataManagerListener = new DataManagerListener() {
        @Override
        public Message onDealSucceed(int what, WxTokenData codeData, String msg) {
            return super.onDealSucceed(CODE_WHAT_SUCCEED, codeData, msg);
        }

        @Override
        public Message onDealFailed(int what, WxTokenData codeData, String msg) {
            return super.onDealFailed(CODE_WHAT_FAILED, codeData, msg);
        }
    };

    public WxTokenManage(RequestBack requestBack) {
        super(requestBack);
        netSource = new WxTokenNetSource();
        netSource.setRequsetListener(dataManagerListener);
    }

    public void setTokenReq(WxTokenBean wxTokenReq) {
        netSource.wxTokenReq = wxTokenReq;
    }

    public void doRequest() {
        netSource.doRequest();
    }
}
