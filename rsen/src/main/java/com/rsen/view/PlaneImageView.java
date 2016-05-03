package com.rsen.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by robi on 2016-05-03 14:49.
 * 水平对置的ImageView
 */
public class PlaneImageView extends ImageView {
    public PlaneImageView(Context context) {
        super(context);
        setScaleX(-1);
    }

    public PlaneImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleX(-1);
    }
}
