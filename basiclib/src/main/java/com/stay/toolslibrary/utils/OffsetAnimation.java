package com.stay.toolslibrary.utils;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by huang on 2017/2/15.
 */

public class OffsetAnimation {

    private LinearLayout mOffsetll;

    private int mHeight;

    public OffsetAnimation(LinearLayout mOffsetll, int height) {

        this.mOffsetll = mOffsetll;

        mHeight = height;

    }

    public void startShowAnimate() {

        ViewWrapper wrapper = new ViewWrapper(mOffsetll);

        ObjectAnimator.ofInt(wrapper, "bottomMargin", mHeight).setDuration(300).start();

    }

    public void startHideAnimateFast() {

        ViewWrapper wrapper = new ViewWrapper(mOffsetll);

        ObjectAnimator.ofInt(wrapper, "bottomMargin", 0).setDuration(0).start();
    }

    public void startHideAnimate() {

        ViewWrapper wrapper = new ViewWrapper(mOffsetll);

        ObjectAnimator.ofInt(wrapper, "bottomMargin", 0).setDuration(300).start();

    }

    public void startShowAnimateTop() {

        ViewWrapper wrapper = new ViewWrapper(mOffsetll);

        ObjectAnimator.ofInt(wrapper, "bottomMargin", mHeight).setDuration(300).start();

    }

    public void startHideAnimateFastTop() {

        ViewWrapper wrapper = new ViewWrapper(mOffsetll);

        ObjectAnimator.ofInt(wrapper, "bottomMargin", 0).setDuration(0).start();
    }

    public void startHideAnimateTop() {
        ViewWrapper wrapper = new ViewWrapper(mOffsetll);
        ObjectAnimator.ofInt(wrapper, "bottomMargin", 0).setDuration(300).start();

    }

    public class ViewWrapper {

        private LinearLayout ll;

        public int getBottomMargin() {

            return ll.getLayoutParams().height;

        }

        public void setBottomMargin(int bottomMargin) {

            ll.getLayoutParams().height = bottomMargin;

            ll.requestLayout();

        }

        public ViewWrapper(LinearLayout view) {

            this.ll = view;

        }

    }


    /**
     * 从控件所在位置移动到控件的底部
     * **正常 to下方
     *
     * @return
     */
    public static TranslateAnimation moveToViewBottom() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(250);
        return mHiddenAction;
    }

    /**
     * 从控件所在位置快速移动到控件的底部
     * *正常 to下方
     *
     * @return
     */
    public static TranslateAnimation moveToViewBottomFast() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(0);
        return mHiddenAction;
    }

    /**
     * 从控件所在位置移动到控件的头部
     * 正常 to上方
     *
     * @return
     */
    public static TranslateAnimation moveToViewTop() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    /**
     * 从控件所在位置移动到控件的头部
     * 正常 to上方
     *
     * @return
     */
    public static TranslateAnimation moveToViewTopNow() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        mHiddenAction.setDuration(100);
        return mHiddenAction;
    }

    /**
     * 从控件的头部移动到控件所在位置==》上方to正常
     *
     * @return
     */
    public static TranslateAnimation move1To3View(int number) {
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f,0 , number);
        animation.setDuration(1);
        animation.setDuration(900);
        animation.setFillAfter(true);
        return animation;
    }

    /**
     * 从控件的头部移动到控件所在位置==》上方to正常
     *
     * @return
     */
    public static TranslateAnimation move3To1View(int number) {

        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, number, 0);
        animation.setDuration(1);
        return animation;
    }

    /**
     * 从控件所在位置移动到控件的头部
     * 正常 to上方
     *
     * @return
     */
    public static TranslateAnimation moveToViewTop4() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    /**
     * 从控件的底部移动到控件所在位置
     * 下方to正常
     *
     * @return
     */
    public static TranslateAnimation moveToViewLocation() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    /**
     * 从控件的头部移动到控件所在位置==》上方to正常
     *
     * @return
     */
    public static TranslateAnimation moveToView() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

}
