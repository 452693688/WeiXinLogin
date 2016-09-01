package com.tywl.myt.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.ui.activity.R;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tywl.myt.bean.WxCodeBean;
import com.tywl.myt.dialogs.CustomWaitingDialog;
import com.tywl.myt.net.manager.WxTokenCheckManage;
import com.tywl.myt.net.manager.WxTokenManage;
import com.tywl.myt.net.manager.WxTokenRefreshManage;
import com.tywl.myt.net.manager.WxUserManage;
import com.tywl.myt.net.parameter.request.WxTokenBean;
import com.tywl.myt.net.parameter.result.WxTokenRefreshRes;
import com.tywl.myt.net.parameter.result.WxTokenRes;
import com.tywl.myt.net.parameter.result.WxUserRes;
import com.tywl.myt.net.source.WxTokenCheckData;
import com.tywl.myt.net.source.WxTokenData;
import com.tywl.myt.net.source.WxTokenRefreshData;
import com.tywl.myt.net.source.WxUserData;
import com.tywl.myt.receiver.NetStateReceiver;
import com.tywl.myt.utile.DLog;
import com.tywl.myt.utile.DataSaveUtile;
import com.tywl.myt.utile.ToastUtile;

import api.common.net.able.RequestBack;
import api.common.net.source.AbstractResponseData;

public class WXEntryActivity extends Activity implements View.OnClickListener,
        IWXAPIEventHandler, RequestBack {

    private WxTokenManage wxTokenManage;
    private WxUserManage wxUserManage;
    private CustomWaitingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_entry);
        findViewById(R.id.main_register_btn).setOnClickListener(this);
        findViewById(R.id.main_check_btn).setOnClickListener(this);
        findViewById(R.id.main_token_btn).setOnClickListener(this);
        findViewById(R.id.main_user_btn).setOnClickListener(this);
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.handleIntent(getIntent(), this);
        //必须将应用注册到微信
        api.registerApp(APP_ID);
        wxTokenManage = new WxTokenManage(this);
        wxUserManage = new WxUserManage(this);
        dialog = new CustomWaitingDialog(this);
    }

    private String APP_ID = "wx6b376d1ef44c8d93";// 微信开放平台申请到的app_id
    private String APP_SECRET = "bebba5a1e25fa10b6ba46cf002412131";//微信平台申请的app secret
    private String scope = "snsapi_userinfo";
    private IWXAPI api;
    private WxCodeBean wxCodeBean;
    private String state = "myt" + System.currentTimeMillis();

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_register_btn:
                //获取微信code  即授权
                if (api != null && api.isWXAppInstalled()) {
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = scope;
                    req.state = state;
                    api.sendReq(req);
                } else {
                    Toast.makeText(this, "用户未安装微信", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.main_check_btn:
                //检查toklen是否有效
                tokenCheck();
                break;
            case R.id.main_token_btn:
                //刷新token
                tokenRefresh();
                break;
            case R.id.main_user_btn:
                //重新获取个人信息
                getUser();
                dialog.show();
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        DLog.e("onNewIntent", "===============");
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetStateReceiver.inittNetWork(this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        DLog.e("微信请求发送", baseReq.toString());
    }

    @Override
    public void onResp(BaseResp baseResp) {
        DLog.e("微信请求结果", baseResp.toString());
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //发送成功
                SendAuth.Resp sendResp = (SendAuth.Resp) baseResp;
                if (sendResp == null) {
                    break;
                }
                if (!state.equals(sendResp.state)) {
                    DLog.e("state不正确", "========");
                    break;
                }
                DLog.e("state", sendResp.state);
                wxCodeBean = new WxCodeBean();
                wxCodeBean.code = sendResp.code;
                wxCodeBean.ErrCode = sendResp.errCode;
                wxCodeBean.country = sendResp.country;
                wxCodeBean.lang = sendResp.lang;
                wxCodeBean.state = sendResp.state;
                getToken();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //发送被拒绝
                break;
            default:
                //发送返回
                break;
        }
    }

    private void getToken() {
        WxTokenBean wxTokenReq = new WxTokenBean();
        wxTokenReq.appId = APP_ID;
        wxTokenReq.secret = APP_SECRET;
        wxTokenReq.grant_type = "authorization_code";
        wxTokenReq.code = wxCodeBean.code;
        wxTokenManage.setTokenReq(wxTokenReq);
        wxTokenManage.doRequest();
        dialog.show();
    }

    public void getUser() {
        if (wxTokenRes == null) {
            wxTokenRes = (WxTokenRes) DataSaveUtile.getObjectFromData(this, DataSaveUtile.WX_TOKEN);
        }
        if (wxTokenRes == null) {
            ToastUtile.showToast("未获取到微信Token");
            return;
        }
        String token = wxTokenRes.getAccess_token();
        String openId = wxTokenRes.getOpenid();
        wxUserManage.setTokenReq(token, openId, "");
        wxUserManage.doRequest();
    }

    private WxTokenCheckManage wxTokenCheckManage;

    public void tokenCheck() {
        wxTokenRes = (WxTokenRes) DataSaveUtile.getObjectFromData(this, DataSaveUtile.WX_TOKEN);
        if (wxTokenRes == null) {
            ToastUtile.showToast("未获取到微信Token");
            return;
        }
        if (wxTokenCheckManage == null) {
            wxTokenCheckManage = new WxTokenCheckManage(this);
        }
        wxTokenCheckManage.setTokenReq(wxTokenRes.getAccess_token(),
                wxTokenRes.getOpenid());
        wxTokenCheckManage.doRequest();
        dialog.show();
    }

    private WxTokenRefreshManage wxTokenRefreshManage;

    public void tokenRefresh() {
        wxTokenRes = (WxTokenRes) DataSaveUtile.getObjectFromData(this, DataSaveUtile.WX_TOKEN);
        if (wxTokenRes == null) {
            ToastUtile.showToast("未获取到微信Token");
            return;
        }
        if (wxTokenRefreshManage == null) {
            wxTokenRefreshManage = new WxTokenRefreshManage(this);
        }
        wxTokenRefreshManage.setTokenReq(APP_ID, "refresh_token", wxTokenRes.getRefresh_token());
        wxTokenRefreshManage.doRequest();
        dialog.show();
    }

    //微信token
    private WxTokenRes wxTokenRes;
    private WxUserRes wxUserRes;

    @Override
    public void OnBack(int what, Object obj, String msg, String other) {
        switch (what) {
            case WxTokenManage.CODE_WHAT_SUCCEED:
                //获取微信token成功
                WxTokenData data = (WxTokenData) obj;
                wxTokenRes = data.result;
                wxTokenRes.time = System.currentTimeMillis();
                DataSaveUtile.saveObjToData(this, wxTokenRes, DataSaveUtile.WX_TOKEN);
                getUser();
                break;
            case WxTokenManage.CODE_WHAT_FAILED:
                if (obj != null) {
                    msg = ((AbstractResponseData) obj).msg;
                }
                ToastUtile.showToast(msg);
                dialog.dismiss();
                break;
            case WxUserManage.CODE_WHAT_SUCCEED:
                //获取微信个人信息成功
                WxUserData wxUserData = (WxUserData) obj;
                wxUserRes = wxUserData.result;
                DataSaveUtile.saveObjToData(this, wxUserRes, DataSaveUtile.WX_USER);
                dialog.dismiss();
                break;
            case WxUserManage.CODE_WHAT_FAILED:
                if (obj != null) {
                    msg = ((AbstractResponseData) obj).msg;
                }
                ToastUtile.showToast(msg);
                dialog.dismiss();
                break;
            case WxTokenCheckManage.CODE_WHAT_SUCCEED:
                //检查微信token是否有效
                WxTokenCheckData wxTokenCheckData = (WxTokenCheckData) obj;
                ToastUtile.showToast("Token:" + wxTokenCheckData.msg);
                dialog.dismiss();
                break;
            case WxTokenCheckManage.CODE_WHAT_FAILED:
                if (obj != null) {
                    msg = ((AbstractResponseData) obj).msg;
                }
                ToastUtile.showToast(msg);
                dialog.dismiss();
                break;
            case WxTokenRefreshManage.CODE_WHAT_SUCCEED:
                //刷新token成功
                WxTokenRefreshData wxTokenRefreshData = (WxTokenRefreshData) obj;
                WxTokenRefreshRes wxTokenRefreshRes = wxTokenRefreshData.result;
                //wxTokenRes = (WxTokenRes) DataSaveUtile.getObjectFromData(this, DataSaveUtile.WX_TOKEN);
                wxTokenRes.setRefresh_token(wxTokenRefreshRes.getRefresh_token());
                wxTokenRes.setAccess_token(wxTokenRefreshRes.getAccess_token());
                wxTokenRes.setOpenid(wxTokenRefreshRes.getOpenid());
                wxTokenRes.setScope(wxTokenRefreshRes.getScope());
                wxTokenRes.time = System.currentTimeMillis();
                DataSaveUtile.saveObjToData(this, wxTokenRes, DataSaveUtile.WX_TOKEN);

                dialog.dismiss();
                ToastUtile.showToast("toaken已刷新");
                break;
            case WxTokenRefreshManage.CODE_WHAT_FAILED:
                if (obj != null) {
                    msg = ((AbstractResponseData) obj).msg;
                }
                ToastUtile.showToast(msg);
                dialog.dismiss();
                break;
        }
    }
}
