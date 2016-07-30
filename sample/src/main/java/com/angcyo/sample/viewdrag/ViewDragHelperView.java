package com.angcyo.sample.viewdrag;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by angcyo on 2016-01-18.
 */
public class ViewDragHelperView extends ViewGroup {

    private ViewDragHelper viewDragHelper;


    public ViewDragHelperView(Context context) {
        super(context);
    }

    public ViewDragHelperView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewDragHelperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void initView() {
        viewDragHelper = ViewDragHelper.create(this, new DragCallback());
        //0.5 是灵敏度, 比如: 实际滑动10像素, 但是只处理 10*0.5 像素, 未验证
        //viewDragHelper = ViewDragHelper.create(this, 0.5f, new DragCallback());

        //edge 边缘拖动
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);
    }

    @Override
    public void computeScroll() {
        //固定写法
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //固定写法
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //固定写法
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    public class DragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //是否可以 抓起 这个child
            return false;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            //当 放下 child的时候, xvel yvel x和y的速度
            super.onViewReleased(releasedChild, xvel, yvel);

            //方式1:
            //滑动child到目标300,300
            //viewDragHelper.smoothSlideViewTo(releasedChild, 300, 300);
            //ViewCompat.postInvalidateOnAnimation(ViewDragHelperView.this);

            //方式2:
            //应该是和 fling 有关
            //viewDragHelper.flingCapturedView(0,0,
            //        viewDragHelper.getCapturedView().getMeasuredWidth(), viewDragHelper.getCapturedView().getMeasuredHeight());

            //方式3:
            //直接设置Child在100,200的位置
            //viewDragHelper.settleCapturedViewAt(100, 200);

        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //水平方向 child 视图滚动到 left 位置, 距离上一次的距离 dx

            return super.clampViewPositionHorizontal(child, left, dx);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            //竖直方向 child 视图滚动到 top 位置, 距离上一次的距离 dy

            return super.clampViewPositionVertical(child, top, dy);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            switch (state) {
                case ViewDragHelper.STATE_DRAGGING:  // 正在被拖动
                    break;
                case ViewDragHelper.STATE_IDLE:  // view没有被拖拽或者 正在进行fling/snap
                    break;
                case ViewDragHelper.STATE_SETTLING: // fling完毕后被放置到一个位置
                    break;
            }
        }

        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);
        }
    }

    /**
     * 参考文章:
     * http://www.cnphp6.com/archives/87727
     *
     * */
}
