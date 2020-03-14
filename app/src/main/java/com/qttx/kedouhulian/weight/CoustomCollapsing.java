package com.qttx.kedouhulian.weight;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.AttributeSet;

/**
 * Created by huang on 2017/9/13.
 */

public class CoustomCollapsing extends CollapsingToolbarLayout {

    public CoustomCollapsing(Context context) {
        super(context);
    }

    public CoustomCollapsing(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoustomCollapsing(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        getStatusBarScrim().setBounds(0,0,-getWidth(),50);
    }

    @Override
    public void setScrimsShown(boolean shown) {
    }

    @Override
    public void setScrimsShown(boolean shown, boolean animate) {

    }
//
//    @Override
//    public void draw(Canvas canvas) {
//        super.draw(canvas);
////        getStatusBarScrim().setBounds(0,0,-getWidth(),50);
////        getStatusBarScrim().draw(canvas);
//    }
}
