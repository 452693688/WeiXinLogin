package com.tywl.myt.net.parameter.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/1.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WxBaseRes implements Serializable {
    private String errcode;
    private String errmsg;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
