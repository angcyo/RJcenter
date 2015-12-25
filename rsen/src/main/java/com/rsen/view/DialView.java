package com.rsen.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * 大转盘
 * Created by angcyo on 15-12-18 018 15:09 下午.
 */
public class DialView extends View {
    private static final int col1 = Color.parseColor("#FFF6DB");//很淡的黄色
    private static final int col2 = Color.parseColor("#FFFFFF");//白色
    private static final int col3 = Color.parseColor("#E13F4F");//红色
    float degreeStep;//每次重绘,添加的角度
    long invalidateTime = 20;//每隔多长时间,重绘一次
    int dialNum = 3;//至少需要转多少圈;
    float startAngle, endAngle, targetAngle;//开始结束,目标的角度
    private float curDegreeStep = 20;//每次重绘,添加的角度
    private int[] mColors = new int[]{col1, col2, col1, col2, col1, col2, col1, col2};//转盘每个块区域对应的颜色
    private String[] mTexts = new String[]{"一等奖", "二等奖", "三等奖", "四等奖", "五等奖", "六等奖"};//转盘每个块区域对应的文本
    private Bitmap[] mIcons;//转盘每个块区域对应的图标
    private float mTextSize;
    private float[] mRatios = new float[]{1, 1, 1, 1, 1, 1};//转盘每个块区域对应的大小比例
    private TextPaint mTextPaint;//文本画笔
    private Paint mPaint;//画笔
    //转换之后的角度
    private float[] mAngles;
    //可绘制区域,转盘总区域
    private Rect mDialRect;
    //去掉外圈所有圆,转盘的区域
    private RectF mArcRectF;
    //文本绘制偏移比例
    private float mTextOffset = 0.1f;
    //图标绘制偏移比例
    private float mIconOffset = 0.6f;
    private float mDialCurrentDegree = 0f;// 转盘当前的角度,
    private float mDialOffsetDegree = 30f;// 转盘偏移的角度,用于决定开始时角度
    private boolean mDialStart = false;//是否开始了
    private boolean mDialEnd = true;//是否结束了
    private int mTextColor = col3;
    private Runnable endAction;

    //新增 2015-12-24
    private int mOutCircleCol = Color.parseColor("#D22C1F");//最外圈 颜色
    private float mOutCircleWidth = 6;//dp 最外圈 宽度
    private int mInnerCircleCol = Color.parseColor("#FEDC6B");//最内圈 颜色
    private float mInnerCircleWidth = 6;//dp 最内圈 宽度
    private int mMidCircleCol = Color.parseColor("#FFBE03");//中间圈 颜色
    private float mMidCircleWidth = 20;//dp 中间圈 宽度

    private int mLittleCircleCol = Color.parseColor("#FEFEFA");//中间圈 上,小圆的 颜色
    private float mLittleCircleRadius = 6;//dp 中间圈 上,小圆的 半径

    private int mCenterCircleCol = Color.parseColor("#F3F3F3");//转盘中心圆的颜色
    private float mCenterCircleRadius = 50;//dp 转盘中心圆的半径
    private int paddingBottom;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;


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

    /**
     * 测量文本范围
     */
    public static void getTextBounds(Paint paint, String text, Rect bounds) {
        Rect textRound = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRound);
        float width = paint.measureText(text);
        bounds.set(textRound.left, textRound.top, (int) (textRound.left + width), textRound.bottom);
    }

    /**
     * @see com.rsen.view.DialView#getTextBounds(Paint, String, Rect)
     */
    public static Rect getTextBounds(Paint paint, String text) {
        Rect textRound = new Rect();
        getTextBounds(paint, text, textRound);
        return textRound;
    }

    public static Bitmap rotateBitmap(Bitmap srcBmp, float degrees) {
        int width, height;
        width = srcBmp.getWidth();
        height = srcBmp.getHeight();
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degrees, width / 2, height / 2);

        Bitmap ret = Bitmap.createBitmap(srcBmp, 0, 0, width, height, matrix, true);
        return ret;
    }

    /**
     * 角度(0-360) 转换 弧度(0-π)
     */
    public static double applyAngleToRadian(double angle) {
        return (angle * Math.PI) / 180f;
    }

    public static float pxToDp(Resources res, float px) {
        float dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, res.getDisplayMetrics());

        return dp;
    }

    private void init() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mTextPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mTextPaint.setStrokeJoin(Paint.Join.ROUND);

//        mTextPaint.setTextAlign(Paint.Align.CENTER);
        setTextSize(22f);
        setmOutCircleWidth(mOutCircleWidth);
        setmMidCircleWidth(mMidCircleWidth);
        setmInnerCircleWidth(mInnerCircleWidth);
        setmLittleCircleRadius(mLittleCircleRadius);
        setmCenterCircleRadius(mCenterCircleRadius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(widthSize, heightSize);

        paddingBottom = getPaddingBottom();
        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();

        //固定大小
        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(size, size);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void start(int index, Runnable endAction) {//从0开始的索引
        if (index < 0 || index >= mRatios.length) {
            throw new IllegalArgumentException("index is invalid");
        }

        if (mDialStart) {
            return;
        }

        this.endAction = endAction;
        initTargetDegree(index);
        mDialStart = true;
        invalidate();
    }

    public boolean isStart() {
        return mDialStart;
    }

    public boolean isEnd() {
        return mDialEnd;
    }

    /**
     * 控制速度,控制结束位置
     */
    private void smoothSlow() {
        float ratio = mDialCurrentDegree / targetAngle;//已经旋转了多少角度的比例
        if (ratio < 0.5) {
            degreeStep = curDegreeStep;
        } else {
            degreeStep -= 1;
        }

        if (ratio < 0.5) {
            degreeStep = Math.max(degreeStep, curDegreeStep);
        } else if (ratio < 0.8) {
            degreeStep = Math.max(degreeStep, 5);
        } else if (ratio < 0.97) {
            degreeStep = Math.max(degreeStep, 3);
        } else if (ratio < 0.99) {
            degreeStep = Math.max(degreeStep, 2);
        } else if (ratio < 2) {
            degreeStep = Math.max(degreeStep, 1);
        }

        if ((mDialCurrentDegree + degreeStep) > targetAngle) {
            degreeStep = 1;
        }

        if (mDialCurrentDegree >= targetAngle) {
            mDialStart = false;
            mDialEnd = true;
            mDialCurrentDegree %= 360;
            if (endAction != null) {
                endAction.run();
            }
        }
    }

    /***
     * 获取目标需要旋转的角度
     */
    private void initTargetDegree(int index) {

        float offsetAngle = 360 * dialNum + 270;//+270 是让正上方为 指针位置;
        mDialCurrentDegree = 0;

        float startAngle = 0, curAngle = 0;
        for (int i = 0; i < mAngles.length; i++) {
            curAngle = mAngles[i];
            startAngle += curAngle;

            if (index == i) {
                break;
            }
        }

        this.startAngle = offsetAngle + 360 - startAngle;
        this.endAngle = this.startAngle + curAngle;
        this.targetAngle = (float) (this.startAngle + ((1 - Math.random()) * 0.8 * curAngle) - mDialCurrentDegree - mDialOffsetDegree);

//        Log.e("tag", "startAngle:" + this.startAngle + "  endAngle:" + this.endAngle + "  targetAngle:" + this.targetAngle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        testDraw(canvas);

//        mTextPaint.setColor(mColors[0]);
//        Rect mDialRect = new Rect();
//        getDialRect(mDialRect);
//        canvas.drawRect(mDialRect, mTextPaint);
        //测量一些值
        mAngles = rationsToAngle();
        mDialRect = getDialRect();

        //开始时有一个角度
        tranToCenter(canvas, mDialRect);
        //12点方向的标志线
//        mTextPaint.setColor(Color.WHITE);
//        mTextPaint.setStrokeWidth(50f);
//        canvas.drawLine(0, -getMeasuredHeight(), 0, 0, mTextPaint);

        canvas.rotate(mDialOffsetDegree + mDialCurrentDegree);

        //绘制外圈
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mOutCircleWidth);
        mPaint.setColor(mOutCircleCol);
        canvas.drawCircle(0, 0, Math.round(mDialRect.width() / 2 - mOutCircleWidth / 2), mPaint);
        //中间圈
        mPaint.setStrokeWidth(mMidCircleWidth);
        mPaint.setColor(mMidCircleCol);
        canvas.drawCircle(0, 0, Math.round(mDialRect.width() / 2 - mOutCircleWidth - mMidCircleWidth / 2), mPaint);
        //内圈
        mPaint.setStrokeWidth(mInnerCircleWidth);
        mPaint.setColor(mInnerCircleCol);
        canvas.drawCircle(0, 0, Math.round(mDialRect.width() / 2 - mOutCircleWidth - mMidCircleWidth - mInnerCircleWidth / 2), mPaint);

        //绘制色块区域
        drawDialArea(canvas);
//        drawDialText(canvas);
        //绘制图标
        drawDialIco(canvas);

        //绘制中心圆
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCenterCircleCol);
        canvas.drawCircle(0, 0, mCenterCircleRadius, mPaint);

        if (mDialStart) {
            smoothSlow();
            mDialCurrentDegree += degreeStep;//闪的很快
            postInvalidateDelayed(invalidateTime);//转的很快
        }
    }

    private void drawDialIco(Canvas canvas) {
        //绘制图标

        if (mIcons == null || mIcons.length < 1) {
            return;
        }

        canvas.save();
        float angle = 0;//需要旋转的角度
        mPaint.setColor(mLittleCircleCol);
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < mIcons.length; i++) {
            Bitmap bitmap = mIcons[i];
            angle = mAngles[i] / 2;

            //绘制中间圈上的小圆
            canvas.drawCircle(mDialRect.width() / 2 - mOutCircleWidth - mMidCircleWidth / 2, 0, mLittleCircleRadius, mPaint);

            canvas.rotate(angle);

            //绘制中间圈上的小圆
            canvas.drawCircle(mDialRect.width() / 2 - mOutCircleWidth - mMidCircleWidth / 2, 0, mLittleCircleRadius, mPaint);

            canvas.save();
            canvas.translate(mArcRectF.width() / 2 * mIconOffset, 0);//画布平移,让图片靠外圈显示
            canvas.drawBitmap(rotateBitmap(bitmap, 90), -bitmap.getWidth() / 2, -bitmap.getHeight() / 2, mPaint);
            canvas.restore();
            canvas.rotate(angle);
        }

        canvas.restore();
    }

    private void drawDialText(Canvas canvas, float angle, String text, Path path) {
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStyle(Paint.Style.FILL);
        float radius = mArcRectF.width() / 2f;//半径
        float textOffsetY = radius / 2 * mTextOffset;//文本偏移之后的横向中心坐标
//        double textOffsetX = Math.sin(applyAngleToRadian(angle / 2)) * (float) radius;//文本横向偏移量
        float textOffsetX = (float) (2 * Math.PI * radius);//圆的周长
        textOffsetX = angle * textOffsetX / 360f / 2f;//多少角度对应多少长度// 文本横向偏移量

        Rect textBound = getTextBounds(mTextPaint, text);
//        canvas.drawTextOnPath(text, path, (float) (textOffsetX), textBound.height() + textOffsetY, mTextPaint);
        canvas.drawTextOnPath(text, path, (textOffsetX - textBound.width() / 2), textBound.height() + textOffsetY, mTextPaint);

//        canvas.drawPath(path, mTextPaint);
    }

    private void drawDialText(Canvas canvas) {
        //绘制色块上对应的文本
        canvas.save();
        mTextPaint.setColor(Color.WHITE);
//        mTextPaint.setTextAlign(Paint.Align.CENTER);//居中绘制文本

        int radius = mDialRect.width() / 2;//半径
        float textCenterX = radius / 2 + radius / 2 * mTextOffset;//文本偏移之后的横向中心坐标

        float angle = 0;//需要旋转的角度
        for (int i = 0; i < mTexts.length; i++) {
            angle = mAngles[i] / 2;
            canvas.rotate(angle);
            String text = mTexts[i];
            Rect textBound = getTextBounds(mTextPaint, text);
            canvas.drawText(text, textCenterX, textBound.height() / 2, mTextPaint);
            canvas.rotate(angle);
        }
        canvas.restore();
    }

    private void drawDialArea(Canvas canvas) {
        //绘制色块区域
        canvas.save();

        float allCircleWidth = mOutCircleWidth + mMidCircleWidth + mInnerCircleWidth;
        //扇形绘制区域
        mArcRectF = new RectF(-mDialRect.width() / 2 + allCircleWidth + paddingLeft, //左
                -mDialRect.height() / 2 + allCircleWidth + paddingTop,//上
                mDialRect.width() / 2 - allCircleWidth - paddingRight, //右
                mDialRect.height() / 2 - allCircleWidth - paddingBottom);//下
        float startAngle = 0, endAngle;
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < mAngles.length; i++) {
            endAngle = mAngles[i];
            mPaint.setColor(mColors[i]);
            canvas.drawArc(mArcRectF, startAngle, endAngle, true, mPaint);
            Path path = new Path();
            path.addArc(mArcRectF, startAngle, endAngle);
            drawDialText(canvas, endAngle, mTexts[i], path);
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
        float sum = 0;
        float avg = 0;
        for (float ratio : mRatios) {
            sum += ratio;
        }
        avg = 360f / sum;

        int len = mRatios.length;
        float[] angles = new float[len];
        for (int i = 0; i < len; i++) {
            angles[i] = mRatios[i] * avg;
        }

        return angles;
    }

    private Rect getDialRect() {
        //获取转盘可绘制rect
        int width, height;
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        return new Rect(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom);
    }

    /**
     * 设置每个色块的颜色
     *
     * @param colors the colors
     */
    public void setColors(int[] colors) {
        this.mColors = colors;
    }

    /**
     * 设置每个色块对应的文本
     *
     * @param texts the texts
     */
    public void setTexts(String[] texts) {
        this.mTexts = texts;
    }

    /**
     * 设置每个色块大小对应的比例
     *
     * @param ratios the ratios
     */
    public void setRatios(float[] ratios) {
        this.mRatios = ratios;
    }

    /**
     * 转盘显示偏移的角度
     *
     * @param offsetAngle the offset angle
     */
    public void setStartOffsetAngle(float offsetAngle) {
        this.mDialOffsetDegree = offsetAngle;
    }

    /**
     * 抽奖开始之后,旋转的圈数
     *
     * @param num the num
     */
    public void setMinRotateNum(int num) {
        if (num > 1) {
            this.dialNum = num - 1;
        }
    }

    public void setTextSize(float size) {
        mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, getResources().getDisplayMetrics());
        mTextPaint.setTextSize(mTextSize);
    }

    /**
     * 文本向外的偏移比例 0-1f, 从圆边开始的偏移
     *
     * @param offset the offset
     */
    public void setTextOffset(float offset) {
        mTextOffset = offset;
    }

    /**
     * 图标的偏移比例 0-1f ,从圆心开始的偏移
     *
     * @param offset the offset
     */
    public void setIconOffset(float offset) {
        mIconOffset = offset;
    }

    public void setIcons(Bitmap[] bitmaps) {
        this.mIcons = bitmaps;
    }

    public String getText(int index) {
        return mTexts[index];
    }

    public int getDialNum() {
        return mRatios.length;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public void setmOutCircleCol(int mOutCircleCol) {
        this.mOutCircleCol = mOutCircleCol;
    }

    public void setmOutCircleWidth(float mOutCircleWidth) {
        mOutCircleWidth = pxToDp(getResources(), mOutCircleWidth);
        this.mOutCircleWidth = mOutCircleWidth;
    }

    public void setmInnerCircleCol(int mInnerCircleCol) {
        this.mInnerCircleCol = mInnerCircleCol;
    }

    public void setmInnerCircleWidth(float mInnerCircleWidth) {
        mInnerCircleWidth = pxToDp(getResources(), mInnerCircleWidth);
        this.mInnerCircleWidth = mInnerCircleWidth;
    }

    public void setmMidCircleCol(int mMidCircleCol) {
        this.mMidCircleCol = mMidCircleCol;
    }

    public void setmMidCircleWidth(float mMidCircleWidth) {
        mMidCircleWidth = pxToDp(getResources(), mMidCircleWidth);

        this.mMidCircleWidth = mMidCircleWidth;
    }

    public void setmLittleCircleCol(int mLittleCircleCol) {
        this.mLittleCircleCol = mLittleCircleCol;
    }

    public void setmLittleCircleRadius(float mLittleCircleRadius) {
        mLittleCircleRadius = pxToDp(getResources(), mLittleCircleRadius);
        this.mLittleCircleRadius = mLittleCircleRadius;
    }

    public void setmCenterCircleCol(int mCenterCircleCol) {
        this.mCenterCircleCol = mCenterCircleCol;
    }

    public void setmCenterCircleRadius(float mCenterCircleRadius) {
        mCenterCircleRadius = pxToDp(getResources(), mCenterCircleRadius);
        this.mCenterCircleRadius = mCenterCircleRadius;
    }

    public void setDialDegree(int degree) {
        if (!isStart() && isEnd()) {
            mDialOffsetDegree = degree;
            mDialCurrentDegree = 0;
            postInvalidate();
        }
    }
}
