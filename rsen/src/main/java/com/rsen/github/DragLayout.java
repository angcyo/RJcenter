package com.rsen.github;

import android.content.Context;
import android.support.v4.view.ViewCompat;
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
    OnViewChangedListener mOnViewChangedListener;
    //child的移动超过此距离就正向移动，否则反向
    private int BOUND_DISTANCE = (int) ResUtil.dpToPx(getContext().getResources(), 120);
    private View mTopChild, mBottomChild;
    private ViewDragHelper mDragHelper;

    private boolean isShowTop = true;


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
                if (child == mTopChild && !innerCanChildScrollVertically(child, 1)) {
                    //如果是top view, 并且 不能向上滑动
                    return true;
                }
                if (child == mBottomChild && !innerCanChildScrollVertically(child, -1)) {
                    //如果是bottom view, 并且 不能向下滑动
                    return true;
                }
                return false;
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

    private boolean innerCanChildScrollVertically(View view, int direction) {
        if (view instanceof ViewGroup) {
            final ViewGroup vGroup = (ViewGroup) view;
            View child;
            boolean result;
            for (int i = 0; i < vGroup.getChildCount(); i++) {
                child = vGroup.getChildAt(i);
                if (child instanceof View) {
                    result = ViewCompat.canScrollVertically(child, direction);
                } else {
                    result = innerCanChildScrollVertically(child, direction);
                }

                if (result) {
                    return true;
                }
            }
        }

        return ViewCompat.canScrollVertically(view, direction);
    }

    private void changeOtherViewPos(View changedView, int top) {
        if (changedView == mTopChild) {
            mBottomChild.layout(0, mTopChild.getMeasuredHeight() + top, mBottomChild.getMeasuredWidth(),
                    mTopChild.getMeasuredHeight() + top + mBottomChild.getMeasuredHeight());
        } else if (changedView == mBottomChild) {
            mTopChild.layout(0, top - mTopChild.getMeasuredHeight(),
                    mTopChild.getMeasuredWidth(), top);
        }
    }

    private void goBackTopOrBottom(View child, float yvel) {

        if (child == mTopChild) {
            if (-yvel > BOUND_VALOCITY || -mTopChild.getTop() > BOUND_DISTANCE) {
                mDragHelper.smoothSlideViewTo(child, 0, -mTopChild.getMeasuredHeight());
                onViewChanged(false);
            } else {
                mDragHelper.smoothSlideViewTo(child, 0, 0);
                onViewChanged(true);
            }

        } else if (child == mBottomChild) {
            if (yvel > BOUND_VALOCITY || mBottomChild.getTop() > BOUND_DISTANCE) {
                mDragHelper.smoothSlideViewTo(child, 0, mTopChild.getMeasuredHeight());
                onViewChanged(true);
            } else {
                mDragHelper.smoothSlideViewTo(child, 0, 0);
                onViewChanged(false);
            }

        }
        postInvalidate();
    }

    private void onViewChanged(boolean showTop) {
        isShowTop = showTop;
        if (mOnViewChangedListener != null) {
            mOnViewChangedListener.onViewChanged(showTop);
        }
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            postInvalidate();
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
        if (isShowTop) {
            mTopChild.layout(0, 0, mTopChild.getMeasuredWidth(), mTopChild.getMeasuredHeight());
            mBottomChild.layout(0, mTopChild.getMeasuredHeight(), mBottomChild.getMeasuredWidth(), mTopChild.getMeasuredHeight() + mBottomChild.getMeasuredHeight());
        } else {
            mTopChild.layout(0, -mTopChild.getMeasuredHeight(), mTopChild.getMeasuredWidth(), 0);
            mBottomChild.layout(0, 0, mBottomChild.getMeasuredWidth(), mBottomChild.getMeasuredHeight());
        }
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

    public DragLayout setOnViewChangedListener(OnViewChangedListener listener) {
        this.mOnViewChangedListener = listener;
        return this;
    }

    public interface OnViewChangedListener {
        void onViewChanged(boolean showTop);
    }

}
