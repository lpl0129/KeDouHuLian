package com.stay.toolslibrary.library.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by huang on 2017/3/24.
 */

public class CoustomPtrFrameLayout extends PtrClassicFrameLayout {
    private boolean disallowInterceptTouchEvent = false;

    public CoustomPtrFrameLayout(Context context) {
        super(context);
    }

    public CoustomPtrFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoustomPtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        disallowInterceptTouchEvent = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                this.requestDisallowInterceptTouchEvent(false);
                break;
        }
        if (disallowInterceptTouchEvent) {
            return dispatchTouchEventSupper(e);
        }
        return super.dispatchTouchEvent(e);
    }
}
