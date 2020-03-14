package com.qttx.kedouhulian.utils;


import org.greenrobot.eventbus.EventBus;

/**
 * Created by huangyr
 * on 2017/11/10.
 */

public class EventUtils {


    public static void postEvent(EventStatus type) {
        postEvent(type, 0);
    }

    public static void postEvent(EventStatus type, int value) {
        EventBus.getDefault().post(new EventFilterBean(type, value));
    }

    public static void postEvent(EventStatus type, Object value) {
        EventBus.getDefault().post(new EventFilterBean(type, value));
    }
}
