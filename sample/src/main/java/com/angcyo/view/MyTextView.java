package com.angcyo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by angcyo on 16-01-10-010.
 */
public class MyTextView extends TextView {

    Random random;
    float mTranslate = 0f;
    private Matrix gradientMatrix;
    private Shader gradientShader;


    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWidth = getMeasuredWidth();
        mTranslate += viewWidth / 8;
        if (mTranslate > viewWidth / 2) {
            mTranslate = -viewWidth / 2;
        }
        e(mTranslate + "");
        gradientMatrix.setTranslate(mTranslate, 0);
        gradientShader.setLocalMatrix(gradientMatrix);
        postInvalidateDelayed(100);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        e("onSizeChanged");
        gradientShader = new LinearGradient(0, 0, getMeasuredWidth(), 0,
                new int[]{Color.BLUE, Color.WHITE, Color.GREEN}, null, Shader.TileMode.CLAMP);
        getPaint().setShader(gradientShader);
        gradientMatrix = new Matrix();
        random = new Random();
    }

    private void e(String msg) {
        Log.e("angcyo", msg + "");
    }
}
