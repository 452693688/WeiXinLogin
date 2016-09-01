package com.tywl.myt.utile;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 私有数据：data目录
 */
public class DataSaveUtile {
    private final static String NAME = "data";
    private static SharedPreferences shared;
    public static final String WX_USER = "wx_user";
    public static final String WX_TOKEN = "wx_token";

    /**
     * 保存数据
     */
    public static void saveData(Context context, String name, Object value) {
        if (shared == null) {
            shared = context.getSharedPreferences(NAME, 0);
        }
        shared.edit().putString(name, String.valueOf(value)).commit();
        shared = null;
    }

    /**
     * 读取数据
     */
    public static String readData(Context context, String name) {
        if (shared == null) {
            shared = context.getSharedPreferences(NAME, 0);
        }
        return shared.getString(name, "");
    }

    /**
     * 将obj 存储到data目录
     *
     * @param obj
     * @param fileName
     */
    public static void saveObjToData(Context context, Object obj, String fileName) {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fis = context.openFileOutput(
                    fileName, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fis);
            oos.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 取Object
     *
     * @param fileName
     * @return
     */
    public static Object getObjectFromData(Context context, String fileName) {
        Object obj = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(
                    context.openFileInput(fileName));
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
