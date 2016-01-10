package com.rsen.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.rsen.util.ResUtil;

/**
 * Created by angcyo on 16-01-09-009.
 */
public class GalleryLinearLayout2 extends ViewGroup {

    /**
     * 突出显示view的放大倍数, 大于1
     */
    private float mScale = 1.3f;

    /**
     * 布局之间的间隔, dp
     */
    private float mSpacing = 20;

    /**
     * 如果高度或者宽度是 WRAP_CONTENT, 那么就是这此属性
     */
    private int defaultSize = 200;

    /**
     * 布局的宽度
     */
    private int viewWidth;

    /**
     * 布局的高度
     */
    private int viewHeight;

    /**
     * 没有放大之前的高度
     */
    private float viewRawHeight;

    /**
     * 突出view的位置,默认是第一个
     */
    private int primaryIndex = 0;

    public GalleryLinearLayout2(Context context) {
        this(context, null);
    }

    public GalleryLinearLayout2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GalleryLinearLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        defaultSize = (int) ResUtil.dpToPx(getResources(), defaultSize);
        mSpacing = (int) ResUtil.dpToPx(getResources(), mSpacing);
        init();
    }

    private void init() {
        setWillNotDraw(true);
        setFocusable(true);
        setClickable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = defaultSize;
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = defaultSize;
        }

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            childView.setLayoutParams(new LayoutParams(childView.getLayoutParams().width, heightSize));//强制高度
            measureChild(childView, MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(heightSize, heightMode));
        }

        viewWidth = widthSize;
        viewRawHeight = heightSize;
        viewHeight = (int) (viewRawHeight * mScale);//要包围最大view的高度,未考虑padding属性;以后更新
        setMeasuredDimension(viewWidth, viewHeight);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int left = getLeftOffset();//偏移
        int top = (int) ((viewHeight - viewRawHeight) / 2);

        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);

            int width, height;
            width = childView.getMeasuredWidth();
            height = childView.getMeasuredHeight();

            if (i > 0) {
                left += mSpacing;//添加间隔距离
            }

            childView.layout(left, top, left + width, top + height);
            left += width;



        }
//        scaleView();
    }

    private void scaleView() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);

            //左边的view
            if (primaryIndex - 1 == i) {
                View leftView = getChildAt(primaryIndex-1);
                if (leftView != null) {
                    leftView.setScaleX(moveLeftScale);
                    leftView.setScaleY(moveLeftScale);
                }
            }

            if (primaryIndex == i) {
                childView.setScaleX(moveScale);
                childView.setScaleY(moveScale);
            }

            //右边的view
            if (primaryIndex+1 == i) {
                View rightView = getChildAt(primaryIndex+1);
                if (rightView != null) {
                    rightView.setScaleX(moveRightScale);
                    rightView.setScaleY(moveRightScale);
                }
            }
        }

    }

    private int leftOffset = 0;

    private int getLeftOffset() {
//        int primaryOffset = getPrimaryOffset();
//        View primaryView = getChildAt(primaryIndex);
//        leftOffset = (int) (primaryOffset + primaryView.getMeasuredWidth() * moveScale);

        return leftOffset;
    }

    private int getPrimaryOffset() {
        return getOffsetToPrimary(primaryIndex);
    }

    private int getOffsetToPrimary(int index) {
        //计算突出view的左偏移距离
        int count = getChildCount();
        int offset = 0;

        int leftLength = 0;
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            int childWidth = childView.getMeasuredWidth();
            if (i > 0) {
                leftLength += mSpacing;
            }

            if (index == i) {
                offset = viewWidth / 2 - leftLength - childWidth / 2;
                break;
            }

            leftLength += childWidth;
        }

        return offset;
    }

    private float downX;
    private float moveX;
    private float moveLength = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            downX = ev.getX();
        }

        if (ev.getActionMasked() == MotionEvent.ACTION_MOVE) {
            moveX = ev.getX();
            handleTouchEvent(moveX - downX);
            downX = moveX;

            requestLayout();
            return true;
        }

        if (ev.getActionMasked() == MotionEvent.ACTION_UP) {
            moveLength = 0;
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 移动偏移量,和突出view宽度的比例
     */
    private float moveScale = 0f;
    private float moveLeftScale = 0f;
    private float moveRightScale = 0f;

    private void handleTouchEvent(float offset) {
        moveLength += offset;
        leftOffset += moveLength;//

        View primaryView = getChildAt(primaryIndex);
        moveScale = moveLength / primaryView.getMeasuredWidth() / 2;
    }
}
