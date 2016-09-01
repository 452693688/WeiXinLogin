package com.tywl.myt.bean;

/**
 * Created by Administrator on 2016/8/31.
 */
public class WxCodeBean {
    //ERR_OK = 0(用户同意);
    // ERR_AUTH_DENIED  = -4（用户拒绝授权）;
    // ERR_USER_CANCEL  = -2（用户取消）
    public int ErrCode;
    //用户换取access_token的code，仅在ErrCode为0时有效
    //ode的超时时间为10分钟，一个code只能成功换取一次access_token即失效。
    public String code;
    //第三方程序发送时用来标识其请求的唯一性的标志，
    // 由第三方程序调用sendReq时传入，由微信终端回传，state字符串长度不能超过1K
    public String state;
    //微信客户端当前语言
    public String lang;
    //微信用户当前国家信息
    public String country;
}
