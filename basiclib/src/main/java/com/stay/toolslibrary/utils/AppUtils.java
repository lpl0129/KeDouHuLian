package com.stay.toolslibrary.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.support.annotation.NonNull;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 16/12/08
 *     desc  : Utils初始化相关
 * </pre>
 * 　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　丶
 */
public final class AppUtils {

    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;


    private AppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param app 应用
     */
    public static void init(@NonNull final Application app) {
        AppUtils.sApplication = app;
    }

    /**
     * 获取Application
     *
     * @return Application
     */
    public static Application getApp() {
        if (sApplication != null) {
            return sApplication;
        }
        throw new NullPointerException("u should init first");
    }

}
