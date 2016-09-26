package com.rsen.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by angcyo on 2016-09-26 16:32.
 */
public class PointLoadView extends View {
    static final String TAG = "point_load_view";
    Paint mPaint;

    public PointLoadView(Context context) {
        super(context);
        initView();
    }

    public PointLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    //------------------公有的方法----------------------------//

    //------------------重载的方法---------------------------//

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, 20, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            width = 100;
            Log.d(TAG, "onMeasure: width");
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            height = 100;
            Log.d(TAG, "onMeasure: height");
        }

        setMeasuredDimension(width, height);
    }

    //--------------------私有的方法------------------------//

    private void initView() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GREEN);
    }

    private void initAnimator() {

    }
}
