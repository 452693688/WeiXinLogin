package com.tywl.myt.bean;

/**
 * Created by Administrator on 2016/8/31.
 */
public class WxTokenBean {
    //接口调用凭证，有效期 2个小时
    public String access_token;
    //access_token接口调用凭证超时时间，单位（秒）
    public String expires_in;
    //有效期（30天） 用户刷新access_token
    public String refresh_token;
    //授权用户唯一标识
    public String openid;
    //用户授权的作用域，使用逗号（,）分隔
    public String scope;
    //当且仅当该移动应用已获得该用户的userinfo授权时，才会出现该字段
    public String unionid;
    //错误返回样例：{"errcode":40029,"errmsg":"invalid code"}

}
