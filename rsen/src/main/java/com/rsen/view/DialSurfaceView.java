package com.rsen.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 大转盘
 * Created by angcyo on 15-12-18 018 15:09 下午.
 */
public class DialSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private static final int col1 = Color.parseColor("#FFF6DB");//很淡的黄色
    private static final int col2 = Color.parseColor("#FFFFFF");//白色
    private static final int col3 = Color.parseColor("#E13F4F");//红色
    /**
     * The Degree step.
     */
    float degreeStep;//每次重绘,添加的角度
    /**
     * The Invalidate time.
     */
    long invalidateTime = 20;//每隔多长时间,重绘一次
    /**
     * The Dial num.
     */
    int dialNum = 3;//至少需要转多少圈;
    /**
     * The Start angle.
     */
    float startAngle, /**
     * The End angle.
     */
    endAngle, /**
     * The Target angle.
     */
    targetAngle;//开始结束,目标的角度
    boolean isCreated = false;
    private float curDegreeStep = 20;//每次重绘,添加的角度
    private int[] mColors = new int[]{col1, col2, col1, col2, col1, col2, col1, col2};//转盘每个块区域对应的颜色
    private String[] mTexts = new String[]{"一等奖", "二等奖", "三等奖", "四等奖", "五等奖", "六等奖"};//转盘每个块区域对应的文本
    private Bitmap[] mIcons;//转盘每个块区域对应的图标
    private float mTextSize;
    private float[] mRatios = new float[]{1, 1, 1, 1, 1, 1};//转盘每个块区域对应的大小比例, isMean = false 时,有效
    private boolean isMean = true;//是否平均分配,色块的区域
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
    //新增 2015-12-29
    private float icoWidth = 50f;//dp 图标的宽度
    private float icoHeight = 50f;//dp 图标的高度
    private SurfaceHolder surfaceHolder;
    private Thread drawThread;
    private Handler handler = new Handler(Looper.getMainLooper());

    /**
     * Instantiates a new Dial view.
     *
     * @param context the context
     */
    public DialSurfaceView(Context context) {
        this(context, null);
    }

    /**
     * Instantiates a new Dial view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public DialSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Instantiates a new Dial view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public DialSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    /**
     * 测量文本范围
     *
     * @param paint  the paint
     * @param text   the text
     * @param bounds the bounds
     */
    public static void getTextBounds(Paint paint, String text, Rect bounds) {
        Rect textRound = new Rect(0, 0, 0, 0);
        float width = 0f;
        if (text != null) {
            paint.getTextBounds(text, 0, text.length(), textRound);
            width = paint.measureText(text);
        }
        bounds.set(textRound.left, textRound.top, (int) (textRound.left + width), textRound.bottom);
    }

    /**
     * Gets text bounds.
     *
     * @param paint the paint
     * @param text  the text
     * @return the text bounds
     * @see DialSurfaceView#getTextBounds(Paint, String, Rect) DialView#getTextBounds(Paint, String, Rect)
     */
    public static Rect getTextBounds(Paint paint, String text) {
        Rect textRound = new Rect();
        getTextBounds(paint, text, textRound);
        return textRound;
    }

    /**
     * Rotate bitmap bitmap.
     *
     * @param srcBmp  the src bmp
     * @param degrees the degrees
     * @return the bitmap
     */
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
     *
     * @param angle the angle
     * @return the double
     */
    public static double applyAngleToRadian(double angle) {
        return (angle * Math.PI) / 180f;
    }

    /**
     * Px to dp float.
     *
     * @param res the res
     * @param px  the px
     * @return the float
     */
    public static float pxToDp(Resources res, float px) {
        float dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, res.getDisplayMetrics());

        return dp;
    }

    public float getIcoWidth() {
        return icoWidth;
    }

    public void setIcoWidth(float icoWidth) {
        this.icoWidth = pxToDp(getResources(), icoWidth);
    }

    public float getIcoHeight() {
        return icoHeight;
    }

    public void setIcoHeight(float icoHeight) {
        this.icoHeight = pxToDp(getResources(), icoHeight);
    }

    private void init() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        this.setZOrderOnTop(true);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mTextPaint.setStrokeCap(Paint.Cap.ROUND);
//        mPaint.setStrokeJoin(Paint.Join.ROUND);
//        mTextPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setFilterBitmap(true);
        mTextPaint.setFilterBitmap(true);

//        mTextPaint.setTextAlign(Paint.Align.CENTER);
        setTextSize(22f);
        setmOutCircleWidth(mOutCircleWidth);
        setmMidCircleWidth(mMidCircleWidth);
        setmInnerCircleWidth(mInnerCircleWidth);
        setmLittleCircleRadius(mLittleCircleRadius);
        setmCenterCircleRadius(mCenterCircleRadius);
        setIcoWidth(icoWidth);
        setIcoHeight(icoHeight);
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

    /**
     * Start.
     *
     * @param index     the index
     * @param endAction the end action
     */
    public void start(int index, Runnable endAction) {//从0开始的索引
        if (mRatios == null) {
            return;
        }

        if (index < 0 || index >= mRatios.length) {
            throw new IllegalArgumentException("index is invalid");
        }

        if (mDialStart) {
            return;
        }

        this.endAction = endAction;
        initTargetDegree(index);
        mDialStart = true;

        startThreadDraw();
    }

    /**
     * Is start boolean.
     *
     * @return the boolean
     */
    public boolean isStart() {
        return mDialStart;
    }

    /**
     * Is end boolean.
     *
     * @return the boolean
     */
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
        } else if (ratio < 0.98) {
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
                handler.post(endAction);//2016-1-4 ui线程执行
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
        float offset = 10f;//角度 偏移范围,距离2变的值
        this.targetAngle = (float) (this.startAngle +
                curAngle / offset + ((1 - Math.random()) * 0.8 * (curAngle - 2 * curAngle / offset))
                - mDialCurrentDegree - mDialOffsetDegree);

//        Log.e("tag", "startAngle:" + this.startAngle + "  endAngle:" + this.endAngle + "  targetAngle:" + this.targetAngle);
    }

    protected void onDrawing(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

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
//            postInvalidateDelayed(invalidateTime);//转的很快
            try {
                Thread.sleep(invalidateTime);
            } catch (InterruptedException e) {
            }

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

        if (mIcons != null && mAngles != null) {
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
                if (bitmap != null) {
//                    canvas.drawBitmap(rotateBitmap(bitmap, 90), -bitmap.getWidth() / 2, -bitmap.getHeight() / 2, mPaint);
                    int bmpWidth, bmpHeight;
                    bmpWidth = bitmap.getWidth();
                    bmpHeight = bitmap.getHeight();
                    Rect src = new Rect(0, 0, bmpWidth, bmpHeight);
                    float w, h;
                    w = icoWidth / 2;
                    h = icoHeight / 2;
                    RectF det = new RectF(-w, -h, w, h);
                    canvas.drawBitmap(rotateBitmap(bitmap, 90), src, det, mPaint);
                }
                canvas.restore();
                canvas.rotate(angle);
            }
        }

        canvas.restore();
    }

    private void drawDialText(Canvas canvas, float angle, String text, Path path) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
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
        mArcRectF = new RectF(-mDialRect.width() / 2f + allCircleWidth, //左
                -mDialRect.height() / 2f + allCircleWidth,//上
                mDialRect.width() / 2f - allCircleWidth, //右
                mDialRect.height() / 2f - allCircleWidth);//下
        float startAngle = 0, endAngle;
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(0f);

        if (mAngles != null) {
            for (int i = 0; i < mAngles.length; i++) {
                endAngle = mAngles[i];
                mPaint.setColor(mColors[i]);
                canvas.drawArc(mArcRectF, startAngle, endAngle, true, mPaint);
                Path path = new Path();
                path.addArc(mArcRectF, startAngle, endAngle);
                if (mTexts != null && mTexts.length > i) {
                    drawDialText(canvas, endAngle, mTexts[i], path);
                }
                startAngle += endAngle;
            }
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
        float[] angles;
        int len;
        if (isMean) {
            if (mTexts == null) {
                return null;
            }

            len = mTexts.length;
            mRatios = new float[len];
            for (int i = 0; i < len; i++) {
                mRatios[i] = 1f;
            }
        }
        if (mRatios == null) {
            return null;
        }

        //讲色块比例,转换成 角度
        float sum = 0;
        float avg = 0;
        for (float ratio : mRatios) {
            sum += ratio;
        }
        avg = 360f / sum;

        len = mRatios.length;
        angles = new float[len];
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
        threadDraw();
    }

    /**
     * 设置每个色块大小对应的比例
     *
     * @param ratios the ratios
     */
    public void setRatios(float[] ratios) {
        this.mRatios = ratios;
        this.isMean = false;
        threadDraw();
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
        if (num > 0) {
            this.dialNum = num - 1;
        }
    }

    /**
     * Sets text size.
     *
     * @param size the size
     */
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

    /**
     * Sets icons.
     *
     * @param bitmaps the bitmaps
     */
    public void setIcons(Bitmap[] bitmaps) {
        this.mIcons = bitmaps;
        threadDraw();
    }

    /**
     * Sets icon at.
     *
     * @param index the index
     * @param bmp   the bmp
     */
    public void setIconAt(int index, Bitmap bmp) {
        if (mIcons != null && mIcons.length > index) {
            mIcons[index] = bmp;
            threadDraw();
        }
    }

    /**
     * Gets text.
     *
     * @param index the index
     * @return the text
     */
    public String getText(int index) {
        return mTexts[index];
    }

    /**
     * Gets dial num.
     *
     * @return the dial num
     */
    public int getDialNum() {
        return mRatios.length;
    }

    /**
     * Sets text color.
     *
     * @param textColor the text color
     */
    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    /**
     * Sets out circle col.
     *
     * @param mOutCircleCol the m out circle col
     */
    public void setmOutCircleCol(int mOutCircleCol) {
        this.mOutCircleCol = mOutCircleCol;
    }

    /**
     * Sets out circle width.
     *
     * @param mOutCircleWidth the m out circle width
     */
    public void setmOutCircleWidth(float mOutCircleWidth) {
        mOutCircleWidth = pxToDp(getResources(), mOutCircleWidth);
        this.mOutCircleWidth = mOutCircleWidth;
    }

    /**
     * Sets inner circle col.
     *
     * @param mInnerCircleCol the m inner circle col
     */
    public void setmInnerCircleCol(int mInnerCircleCol) {
        this.mInnerCircleCol = mInnerCircleCol;
    }

    /**
     * Sets inner circle width.
     *
     * @param mInnerCircleWidth the m inner circle width
     */
    public void setmInnerCircleWidth(float mInnerCircleWidth) {
        mInnerCircleWidth = pxToDp(getResources(), mInnerCircleWidth);
        this.mInnerCircleWidth = mInnerCircleWidth;
    }

    /**
     * Sets mid circle col.
     *
     * @param mMidCircleCol the m mid circle col
     */
    public void setmMidCircleCol(int mMidCircleCol) {
        this.mMidCircleCol = mMidCircleCol;
    }

    /**
     * Sets mid circle width.
     *
     * @param mMidCircleWidth the m mid circle width
     */
    public void setmMidCircleWidth(float mMidCircleWidth) {
        mMidCircleWidth = pxToDp(getResources(), mMidCircleWidth);

        this.mMidCircleWidth = mMidCircleWidth;
    }

    /**
     * Sets little circle col.
     *
     * @param mLittleCircleCol the m little circle col
     */
    public void setmLittleCircleCol(int mLittleCircleCol) {
        this.mLittleCircleCol = mLittleCircleCol;
    }

    /**
     * Sets little circle radius.
     *
     * @param mLittleCircleRadius the m little circle radius
     */
    public void setmLittleCircleRadius(float mLittleCircleRadius) {
        mLittleCircleRadius = pxToDp(getResources(), mLittleCircleRadius);
        this.mLittleCircleRadius = mLittleCircleRadius;
    }

    /**
     * Sets center circle col.
     *
     * @param mCenterCircleCol the m center circle col
     */
    public void setmCenterCircleCol(int mCenterCircleCol) {
        this.mCenterCircleCol = mCenterCircleCol;
    }

    /**
     * Sets center circle radius.
     *
     * @param mCenterCircleRadius the m center circle radius
     */
    public void setmCenterCircleRadius(float mCenterCircleRadius) {
        mCenterCircleRadius = pxToDp(getResources(), mCenterCircleRadius);
        this.mCenterCircleRadius = mCenterCircleRadius;
    }

    /**
     * Sets dial degree.
     *
     * @param degree the degree
     */
    public void setDialDegree(int degree) {
        if (!isStart() && isEnd()) {
            mDialOffsetDegree = degree;
            mDialCurrentDegree = 0;
            threadDraw();
        }
    }

    /**
     * Is mean boolean.
     *
     * @return the boolean
     */
    public boolean isMean() {
        return isMean;
    }

    /**
     * Sets mean.
     *
     * @param mean the mean
     */
    public void setMean(boolean mean) {
        isMean = mean;
    }

    public void setInvalidateTime(long invalidateTime) {
        this.invalidateTime = invalidateTime;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isCreated = true;
        threadDraw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isCreated = false;
    }

    private void e(String msg) {
        Log.e("angcyo", msg + "");
    }


    @Override
    public void run() {
        while (isCreated && isStart()) {
            threadDraw();
        }
        drawThread = null;
    }

    private synchronized void startThreadDraw() {
        if (drawThread != null) {
            return;
        }
        drawThread = new Thread(this);
        drawThread.start();
    }

    private synchronized void threadDraw() {
        Canvas canvas = null;
        synchronized (this) {
            try {
                canvas = surfaceHolder.lockCanvas();
                if (canvas != null) {
                    onDrawing(canvas);
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
