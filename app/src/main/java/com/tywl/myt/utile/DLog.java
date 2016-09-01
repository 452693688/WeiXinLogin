package com.tywl.myt.utile;

import android.util.Log;

/**
 * Created by Administrator on 2016/8/31.
 */
public class DLog {
    public static void e(Object tag, Object value) {
        Log.e(tag.toString(), value.toString());
    }
}
