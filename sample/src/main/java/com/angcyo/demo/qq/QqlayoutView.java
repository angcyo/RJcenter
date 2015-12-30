package com.angcyo.demo.qq;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by angcyo on 15-12-30-030.
 */
public class QqlayoutView extends View {
    public QqlayoutView(Context context) {
        super(context);
    }

    public QqlayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QqlayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }
}
