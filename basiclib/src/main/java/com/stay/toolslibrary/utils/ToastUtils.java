package com.stay.toolslibrary.utils;


/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/09/29
 *     desc  : 吐司相关工具类
 * </pre>
 */
public final class ToastUtils {

    public static void showShort( String content) {
        com.hjq.toast.ToastUtils.show(content);
    }

    public static void showShort( int content) {
        com.hjq.toast.ToastUtils.show(content);
    }

}
