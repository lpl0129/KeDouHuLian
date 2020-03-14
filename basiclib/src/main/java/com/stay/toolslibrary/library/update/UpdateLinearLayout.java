package com.stay.toolslibrary.library.update;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 软件更新布局容器，宽高比456:620的LinearLayout
 */
public class UpdateLinearLayout extends LinearLayout {

    public UpdateLinearLayout(Context context, AttributeSet attrs,
                              int defStyle) {
        super(context, attrs, defStyle);
    }

    public UpdateLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UpdateLinearLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, widthMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        // int childHeightSize = getMeasuredHeight();
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,
                MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize * 620 / 456,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
