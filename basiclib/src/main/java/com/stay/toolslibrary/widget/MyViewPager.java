package com.stay.toolslibrary.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by huang on 2017/3/24.
 */


public class MyViewPager extends ViewPager {


    public void setParent(ViewGroup parent) {
        this.parent = parent;
    }

    private ViewGroup parent;

    public MyViewPager(Context context) {
        super(context);
        parent= (ViewGroup) getParent();
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        parent= (ViewGroup) getParent();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (parent != null) {
                    //禁止上一层的View不处理该事件,屏蔽父组件的事件
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (parent != null) {
                    //拦截
                    parent.requestDisallowInterceptTouchEvent(false);
                }
                break;

            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}