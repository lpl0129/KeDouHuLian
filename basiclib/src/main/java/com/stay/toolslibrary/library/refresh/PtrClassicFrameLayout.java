package com.stay.toolslibrary.library.refresh;

import android.content.Context;
import android.util.AttributeSet;

import com.stay.toolslibrary.library.refresh.loadmore.DefaultLoadMoreViewFooter;
import com.wang.avi.AVLoadingIndicatorView;


public class PtrClassicFrameLayout extends PtrFrameLayout {

    private static final String headerProgessStyle = "BallClipRotateIndicator";
    private static final String footerProgessStyle = "BallPulseRiseIndicator";

    private PtrClassicDefaultHeader mPtrClassicHeader;
    private DefaultLoadMoreViewFooter loadMoreViewFactory;

    public PtrClassicFrameLayout(Context context) {
        this(context, null);
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
        mPtrClassicHeader = new PtrClassicDefaultHeader(getContext());
        setHeaderProgessStyle();
        setHeaderView(mPtrClassicHeader);
        addPtrUIHandler(mPtrClassicHeader);
        setLoadingMinTime(500);
        loadMoreViewFactory = new DefaultLoadMoreViewFooter();
        setFooterView(loadMoreViewFactory);
    }


    public void setHeaderProgessStyle() {
        AVLoadingIndicatorView simpleViewSwitcher = mPtrClassicHeader.getProgess();
        if (simpleViewSwitcher != null) {
            simpleViewSwitcher.setIndicator(headerProgessStyle);
            simpleViewSwitcher.setIndicatorColor(0xffB5B5B5);
        }
    }

    public void setFooterProgessStyle() {
        if (loadMoreViewFactory.getILoadMoreView() != null) {
            AVLoadingIndicatorView simpleViewSwitcher = loadMoreViewFactory.getILoadMoreView().getProgess();
            if (simpleViewSwitcher != null) {
                simpleViewSwitcher.setIndicator(footerProgessStyle);
                simpleViewSwitcher.setIndicatorColor(0xffB5B5B5);
            }
        }
    }

    public PtrClassicDefaultHeader getHeader() {
        return mPtrClassicHeader;
    }

    /**
     * Specify the last update time by this key string
     *
     * @param key
     */
    public void setLastUpdateTimeKey(String key) {
        if (mPtrClassicHeader != null) {
            mPtrClassicHeader.setLastUpdateTimeKey(key);
        }
    }

    /**
     * Using an object to specify the last update time.
     *
     * @param object
     */
    public void setLastUpdateTimeRelateObject(Object object) {
        if (mPtrClassicHeader != null) {
            mPtrClassicHeader.setLastUpdateTimeRelateObject(object);
        }
    }


}
