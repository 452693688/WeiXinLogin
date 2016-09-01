package com.tywl.myt.net.parameter.request;

/**
 * Created by Administrator on 2016/9/1.
 */
public class WxUserBean {
    //是	调用凭证
    public String access_token;
    //是	普通用户的标识，对当前开发者帐号唯一
    public String openid;
    //否	国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语，默认为zh-CN
    public String lang;
}
