package com.angcyo.sample.viewdrag;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.rsen.util.ResUtil;


/**
 * Created by lsx on 16/5/4.
 * https://github.com/luoshixin/taobaopage
 */
public class DragLayout extends ViewGroup {

    //child的移动超过此速率就正向移动，否则反向
    private final float BOUND_VALOCITY = 1500f;
    //child的移动超过此距离就正向移动，否则反向
    private int BOUND_DISTANCE = (int) ResUtil.dpToPx(getContext().getResources(), 120);

    private View mTopChild, mBottomChild;
    private ViewDragHelper mDragHelper;

    public DragLayout(Context context) {
        super(context);
        init();
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mDragHelper = ViewDragHelper.create(this, 1f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mTopChild || child == mBottomChild;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                int finalTop = child.getTop() + dy / 2;
                if (child == mTopChild && top > 0 || child == mBottomChild && top < 0) {
                    finalTop = 0;
                }
                return finalTop;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                changeOtherViewPos(changedView, top);
            }

            //只有此方法大于0时候才能正常捕获
            @Override
            public int getViewVerticalDragRange(View child) {
                return child.getMeasuredHeight();
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                goBackTopOrBottom(releasedChild, yvel);
            }
        });

//        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM);
    }

    private void changeOtherViewPos(View changedView, int top) {
        if (changedView == mTopChild) {
            mBottomChild.layout(0, mTopChild.getMeasuredHeight() + top, mBottomChild.getMeasuredWidth(), mTopChild.getMeasuredHeight() + top + mBottomChild.getMeasuredHeight());
        } else if (changedView == mBottomChild) {
            mTopChild.layout(0, top - mTopChild.getMeasuredHeight(), mTopChild.getMeasuredWidth(), top);
        }
    }

    private void goBackTopOrBottom(View child, float yvel) {

        if (child == mTopChild) {
            if (-yvel > BOUND_VALOCITY || -mTopChild.getTop() > BOUND_DISTANCE) {
                mDragHelper.smoothSlideViewTo(child, 0, -mTopChild.getMeasuredHeight());
            } else {
                mDragHelper.smoothSlideViewTo(child, 0, 0);
            }

        } else if (child == mBottomChild) {
            if (yvel > BOUND_VALOCITY || mBottomChild.getTop() > BOUND_DISTANCE) {
                mDragHelper.smoothSlideViewTo(child, 0, mTopChild.getMeasuredHeight());
            } else {
                mDragHelper.smoothSlideViewTo(child, 0, 0);
            }

        }
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            postInvalidateOnAnimation();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTopChild = getChildAt(0);
        mBottomChild = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        mTopChild.measure(widthMeasureSpec, heightMeasureSpec);
        mBottomChild.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mTopChild.layout(0, 0, mTopChild.getMeasuredWidth(), mTopChild.getMeasuredHeight());
        mBottomChild.layout(0, mTopChild.getMeasuredHeight(), mBottomChild.getMeasuredWidth(), mTopChild.getMeasuredHeight() + mBottomChild.getMeasuredHeight());
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = mDragHelper.shouldInterceptTouchEvent(ev);
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            mDragHelper.processTouchEvent(event);
        } catch (Exception e) {
        }
        return true;
    }


}
