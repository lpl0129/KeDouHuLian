package com.qttx.kedouhulian.weight;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.stay.toolslibrary.utils.ScreenUtils;

public class DragLinearLayout extends LinearLayout {

    private float mLastTouchX = 0;
    private float mLastTouchY= 0;

    private float mMoveX = 0;
    private float mMoveY = 0;

    private float mLeft;
    private float mTop;
    private float mRight;
    private float mBottom;

    private int mDx;
    private int mDy;
    private boolean isLeft = false;
    boolean moveRight = false;
    boolean moveLeft = false;

    //屏幕宽度
    private static final int screenWidth = ScreenUtils.getScreenWidth();
    //屏幕高度
    private static final int screenHeight = ScreenUtils.getScreenHeight();

    public DragLinearLayout(Context context) {
        super(context);
    }

    public DragLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DragLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchX = ev.getX();
                mLastTouchY= ev.getY();
                return false;
            case MotionEvent.ACTION_MOVE:
                mMoveX = ev.getX();
                mMoveY = ev.getY();
                //移动很小的一段距离也视为点击
                if(Math.abs(mMoveX - mLastTouchX) < 5 || Math.abs(mMoveY - mLastTouchY) < 5)
                    //不进行事件拦截
                    return false;
                else
                    return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                moveRight = false;
                moveLeft = false;
                final float x = ev.getX();
                final float y = ev.getY();
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;
                mLeft = getLeft() + dx;
                mTop = getTop() + dy;
                mRight = getRight() + dx;
                mBottom = getBottom() + dy;
                if(mLeft < 0){
                    moveLeft = true;
                    mLeft = 0;
                    mRight = mLeft + getWidth();
                }
                if(mRight > screenWidth){
                    moveRight = true;
                    mRight = screenWidth;
                    mLeft = mRight - getWidth();
                }
                if(mTop < 0){
                    mTop = 0;
                    mBottom = mTop + getHeight();
                }
                if(mBottom > screenHeight){
                    mBottom = screenHeight;
                    mTop = mBottom - getHeight();
                }
                mDx += dx;
                mDy += dy;
                offsetLeftAndRight((int)dx);
                offsetTopAndBottom((int)dy);
                if(moveLeft){
                    offsetLeftAndRight(-getLeft());
                }
                if(moveRight){
                    offsetLeftAndRight(screenWidth-getRight());
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                int upX = (int) ev.getRawX();
                if (upX > (screenWidth / 2)) {
                    isLeft = false;
                    offsetLeftAndRight(screenWidth-getRight());
                    invalidate();
                } else {
                    isLeft = true;
                    offsetLeftAndRight(-getLeft());
                    invalidate();
                }
                if(getTop()<0){
                    mDy += -getTop();
                    offsetTopAndBottom(-getTop());
                }
                if(getBottom()>screenHeight){
                    mDy += screenHeight-getBottom();
                    offsetTopAndBottom(screenHeight-getBottom());
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                break;
            }
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        offsetTopAndBottom(mDy);
        if(isLeft){
            offsetLeftAndRight(-getLeft());
        }else {
            offsetLeftAndRight(screenWidth-getRight());
        }
    }
}