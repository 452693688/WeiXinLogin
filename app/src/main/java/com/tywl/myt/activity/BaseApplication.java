package com.tywl.myt.activity;

import android.app.Application;

/**
 * Created by Administrator on 2016/9/1.
 */
public class BaseApplication extends Application{
    public static BaseApplication baseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
    }
}
