package com.rsen.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 大转盘
 * Created by angcyo on 15-12-18 018 15:09 下午.
 */
public class DialView extends View {
    @ColorInt
    private int[] mColors = new int[]{Color.RED, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GREEN, Color.YELLOW};//转盘每个块区域对应的颜色

    private String[] mTexts = new String[]{"一等奖", "二等奖", "三等奖", "二等奖", "一等奖", "三等奖"};//转盘每个块区域对应的文本
    private float mTextSize = 60f;
    private float[] mRatios = new float[]{1, 2, 3, 2, 1, 3};//转盘每个块区域对应的大小比例

    private TextPaint mTextPaint;//文本画笔


    public DialView(Context context) {
        this(context, null);
    }

    public DialView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public static void getTextBounds(Paint paint, String text, Rect bounds) {
        Rect textRound = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRound);
        float width = paint.measureText(text);
        bounds.set(textRound.left, textRound.top, (int) (textRound.left + width), textRound.bottom);
    }

    private void init() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
//        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(widthSize, heightSize);

        //固定大小
        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(size, size);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        testDraw(canvas);

//        mTextPaint.setColor(mColors[0]);
//        Rect dialRect = new Rect();
//        getDialRect(dialRect);
//        canvas.drawRect(dialRect, mTextPaint);

        drawDialArea(canvas);
        drawDialText(canvas);
    }

    private void drawDialText(Canvas canvas) {
        //绘制色块上对应的文本
        canvas.save();
        Rect dialRect = tranToCenter(canvas);

        canvas.restore();
    }

    private void drawDialArea(Canvas canvas) {
        //绘制色块区域
        canvas.save();
        Rect dialRect = tranToCenter(canvas);

        RectF rectF = new RectF(-dialRect.width() / 2, -dialRect.height() / 2, dialRect.width() / 2, dialRect.height() / 2);//扇形绘制区域
        float[] angles = rationsToAngle();
        float startAngle = 0;
        for (int i = 0; i < angles.length; i++) {
            float endAngle = angles[i];
            mTextPaint.setColor(mColors[i]);
            canvas.drawArc(rectF, startAngle, endAngle, true, mTextPaint);
            startAngle += endAngle;
        }

        canvas.restore();
    }

    private Rect tranToCenter(Canvas canvas) {
        Rect dialRect = getDialRect();
        tranToCenter(canvas, dialRect);
        return dialRect;
    }

    private void tranToCenter(Canvas canvas, Rect dialRect) {
        //讲绘图坐标移至view的中心点
        canvas.translate(dialRect.centerX(), dialRect.centerY());
    }

    private float[] rationsToAngle() {
        //讲色块比例,转换成 角度
        int sum = 0;
        int avg = 0;
        for (float ratio : mRatios) {
            sum += ratio;
        }
        avg = Math.round(360 / sum);

        int len = mRatios.length;
        float[] angles = new float[len];
        for (int i = 0; i < len; i++) {
            angles[i] = mRatios[i] * avg;
        }

        return angles;
    }

    private void testDraw(Canvas canvas) {
        Rect textRound = new Rect();
        getTextBounds(mTextPaint, mTexts[0], textRound);

        mTextPaint.setColor(mColors[0]);
        canvas.drawRect(new Rect(100, 100 - textRound.height(), 100 + textRound.width(), 100), mTextPaint);

        canvas.translate(100, 100);
        canvas.save();
        canvas.rotate(-45f);
        mTextPaint.setColor(mColors[4]);
        canvas.drawText(mTexts[0], 100, 100, mTextPaint);

        mTextPaint.setColor(mColors[1]);
        canvas.drawCircle(100, 100, 1, mTextPaint);

        mTextPaint.setColor(mColors[2]);
        canvas.drawRect(new Rect(10, 10, textRound.width(), textRound.height()), mTextPaint);

        canvas.restore();
//        mTextPaint.setColor(Color.BLACK);
        canvas.drawRect(new Rect(0, 0, 800, 800), mTextPaint);
        mTextPaint.setColor(Color.BLACK);

        canvas.drawArc(new RectF(-400, -400, 400, 400), 0, 30, true, mTextPaint);
    }

    private Rect getDialRect() {
        int width, height;
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        return new Rect(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom);
    }


}
