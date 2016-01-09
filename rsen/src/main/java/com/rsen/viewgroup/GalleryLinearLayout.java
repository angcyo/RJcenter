package com.rsen.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by angcyo on 16-01-09-009.
 */
public class GalleryLinearLayout extends ViewGroup{
    public GalleryLinearLayout(Context context) {
        super(context);
    }

    public GalleryLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GalleryLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
