package com.qttx.kedouhulian.utils;


/**
 * Created by huangyr
 * on 2017/11/10.
 * 事件分发bean
 */

public class EventFilterBean {

    public EventStatus type;

    public String tag;

    public Object value;

    public EventFilterBean(EventStatus type, Object value) {
        this.type = type;
        this.value = value;
    }

    public EventFilterBean(EventStatus type, String tag, Object value) {
        this.type = type;
        this.tag = tag;
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
