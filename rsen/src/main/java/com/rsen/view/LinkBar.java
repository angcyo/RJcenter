package com.rsen.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.angcyo.rsen.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robi on 2016-05-26 10:24.
 */
public class LinkBar extends View {

    /**
     * 圆圈的半径
     */
    private float mCircleRadius = dpToPx(10f);

    /**
     * 线的宽度
     */
    private float mLineWidth = dpToPx(2f);

    /**
     * 非填充状态时,圆的宽度
     */
    private float mCircleWidth = dpToPx(2f);

    /**
     * 包含的文本,也决定了圆圈的个数
     */
    private List<String> mTexts;

    /**
     * 圆圈的颜色
     */
    @ColorInt
    private int mCircleColor = Color.BLUE;

    /**
     * 线条的颜色
     */
    @ColorInt
    private int mLineColor = Color.GRAY;


    /**
     * 文本的颜色
     */
    @ColorInt
    private int mTextColor = Color.BLACK;

    private int DEFAULT_SPACE = (int) dpToPx(40);

    /**
     * 圆圈之间的间隙
     */
    private int mLineSpace = DEFAULT_SPACE;


    /**
     * 圆圈与文字之间的间隙
     */
    private int mLineTextSpace = (int) dpToPx(4);

    /**
     * 文字的大小
     */
    private float mTextSize = spToPx(12f);

    /**
     * 当前的进度, 表示第几个圆圈之前都是实心的
     */
    private int mLinkIndex = -1;

    private Paint mPaint;

    private float FACTOR_STEP = 0.1f;
    private boolean isAnim = true;
    private float factor = FACTOR_STEP;//一次动画放大的因子
    private long animTime = 16;//100毫秒绘制一次动画
    private boolean isAnimEnd = false;//动画是否结束

    public LinkBar(Context context) {
        super(context);
        initView();
    }

    public LinkBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LinkBar);
        mCircleRadius = typedArray.getDimension(R.styleable.LinkBar_circle_radius, mCircleRadius);
        mLineWidth = typedArray.getDimension(R.styleable.LinkBar_line_width, mLineWidth);
        mCircleWidth = typedArray.getDimension(R.styleable.LinkBar_circle_width, mCircleWidth);
        mLineSpace = (int) typedArray.getDimension(R.styleable.LinkBar_line_space, mLineSpace);
        mLineTextSpace = (int) typedArray.getDimension(R.styleable.LinkBar_line_text_space, mLineTextSpace);
        mLinkIndex = typedArray.getInt(R.styleable.LinkBar_link_index, mLinkIndex);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.LinkBar_text_size, (int) mTextSize);

        mCircleColor = typedArray.getColor(R.styleable.LinkBar_circle_color, mCircleColor);
        mLineColor = typedArray.getColor(R.styleable.LinkBar_line_color, mLineColor);
        mTextColor = typedArray.getColor(R.styleable.LinkBar_text_color, mTextColor);

        typedArray.recycle();
        initView();
    }

    public static void getTextRect(Paint paint, String text, Rect rect) {
        paint.getTextBounds(text, 0, text.length(), rect);
    }

    private void initView() {
        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTexts = new ArrayList<>();
//        mCirclePoint = new ArrayList<>();
//        mTextRect = new ArrayList<>();

        mPaint.setTextSize(mTextSize);
    }

    public void setTextSize(float size) {
        mTextSize = size;
        mPaint.setTextSize(size);
    }

    public void setTexts(List<String> texts) {
        mTexts = texts;
    }

    public int getLinkIndex() {
        return mLinkIndex;
    }

    public void setLinkIndex(int linkIndex) {
        mLinkIndex = linkIndex;
        if (mLinkIndex >= getCircleCount()) {
            mLinkIndex -= 1;
        }
        if (mLinkIndex < 0) {
            mLinkIndex = -1;
        }
        factor = FACTOR_STEP;
        isAnimEnd = false;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width, height;
        if (widthMode == MeasureSpec.AT_MOST) {
            mLineSpace = DEFAULT_SPACE;
            width = measureWidth();
        } else {
            width = MeasureSpec.getSize(widthMeasureSpec);
            mLineSpace = measureLineSpace(width);
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            height = measureHeight();
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }

        setMeasuredDimension(width, height);
    }

    private int getCircleCount() {
        if (mTexts == null) {
            return 0;
        }

        return mTexts.size();
    }

    private int measureLineSpace(int maxWidth) {
        int width = maxWidth - getPaddingRight() - getPaddingLeft();
        width -= getCircleCount() * (mCircleRadius + mCircleWidth / 2) * 2;
        int count = getCircleCount();
        if (count > 1) {
            width /= (getCircleCount() - 1);
        }
        return width;
    }

    private int measureWidth() {
        int width = getMinimumWidth();
        int w = 0;
        for (int i = 0; i < getCircleCount(); i++) {
            w += (mCircleRadius + mCircleWidth / 2) * 2;
            if (i > 0) {
                w += mLineSpace;
            }
        }
        w += getPaddingLeft();
        w += getPaddingRight();
        return Math.max(width, w);
    }

    private int measureHeight() {
        int height = getMinimumHeight();
        int h = 0;
        h += mCircleRadius * 2;
        h += mLineTextSpace;
        h += getPaddingTop();
        h += getPaddingBottom();
        Rect rect = new Rect();
        getTextRect(rect);
        h += rect.height();
        h += mCircleWidth * 2;
        return Math.max(height, h);
    }

    private void getTextRect(Rect rect) {
        if (mTexts == null || mTexts.size() == 0) {
            rect.set(0, 0, 0, 0);
        }

        for (String text : mTexts) {
            if (!TextUtils.isEmpty(text)) {
                getTextRect(mPaint, text, rect);
                return;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        measureDraw(canvas);
    }

    private void measureDraw(Canvas canvas) {
        float cx = 0f, cy = 0f;

        float animFactor = checkAnim();
        for (int i = 0; i < getCircleCount(); i++) {
            if (i > 0) {
                drawLine(canvas, cx + mCircleRadius + mCircleWidth / 2, cy, cx + mCircleRadius + mLineSpace, cy);
            }

            cx = getPaddingLeft() + mCircleRadius + mCircleWidth / 2 + i * (2 * mCircleRadius + mLineSpace + mCircleWidth / 2);
            cy = getPaddingTop() + mCircleRadius + mCircleWidth / 2;

            if (isAnim && i == mLinkIndex) {
                drawCircle(canvas, cx, cy, mCircleRadius * animFactor, true);
            } else {
                drawCircle(canvas, cx, cy, mCircleRadius, i <= mLinkIndex);
            }

            Rect rect = new Rect();
            getTextRect(mPaint, mTexts.get(i), rect);

            drawText(canvas, mTexts.get(i), cx - rect.width() / 2, cy + mCircleRadius + mLineTextSpace + rect.height());
        }

        if (isAnim && !isAnimEnd) {
            postInvalidateDelayed(animTime);
        }
    }

    private float checkAnim() {
        if (factor >= 1f) {
            isAnimEnd = true;
        }
        factor += FACTOR_STEP;
        return Math.min(1f, factor);
    }

    private void drawLine(Canvas canvas, float startx, float starty, float stopx, float stopy) {
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setColor(mLineColor);
        canvas.drawLine(startx, starty, stopx, stopy, mPaint);
    }

    private void drawCircle(Canvas canvas, float cx, float cy, float radius, boolean isFill) {
        if (isFill) {
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        } else {
            mPaint.setStyle(Paint.Style.STROKE);
        }
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setColor(mCircleColor);

        canvas.drawCircle(cx, cy, radius, mPaint);
    }

    private void drawText(Canvas canvas, String text, float x, float y) {
        mPaint.setColor(mTextColor);
        mPaint.setStrokeWidth(1f);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, x, y, mPaint);
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private float spToPx(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }
}
