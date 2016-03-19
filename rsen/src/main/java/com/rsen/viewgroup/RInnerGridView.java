package com.rsen.viewgroup;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by angcyo on 16-03-02-002.
 */
public class RInnerGridView extends RGridView {
    public RInnerGridView(Context context) {
        super(context);
    }

    public RInnerGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RInnerGridView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
