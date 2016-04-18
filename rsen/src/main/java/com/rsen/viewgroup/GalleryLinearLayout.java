package com.rsen.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;

import com.rsen.util.ResUtil;

/**
 * Created by angcyo on 16-01-09-009.
 */
public class GalleryLinearLayout extends ViewGroup {

    int mScrollX, mScrollY;
    /**
     * 突出显示view的放大倍数, 大于1
     */
    private float mScale = 1.5f;
    /**
     * 布局之间的间隔, dp
     */
    private float mSpacing = 50;
    /**
     * 如果高度或者宽度是 WRAP_CONTENT, 那么就是这此属性
     */
    private int defaultSize = 200;
    /**
     * 布局的宽度
     */
    private int viewWidth;
    private int allViewWidth;
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
    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private float downX;
    private float moveX;
    private float moveLength = 0;
    private boolean isBeginFling = false;
    private float primaryIndexOffset;

    public GalleryLinearLayout(Context context) {
        this(context, null);
    }

    public GalleryLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public GalleryLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        defaultSize = (int) ResUtil.dpToPx(getResources(), defaultSize);
        mSpacing = (int) ResUtil.dpToPx(getResources(), mSpacing);
        init();
    }

    private void init() {
        setWillNotDraw(true);
        setFocusable(true);
        setClickable(true);

        mScroller = new OverScroller(getContext());

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
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
            int childWidth = childView.getLayoutParams().width;

            if (childWidth == LayoutParams.MATCH_PARENT) {
                childWidth = (int) (widthSize / mScale - 2 * mSpacing);
            }

            childView.setLayoutParams(new LayoutParams(childWidth, heightSize));//强制高度
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
        int left = 0;
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
            allViewWidth = left;
        }

        smoothToIndex(primaryIndex);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        initVelocityTrackerIfNotExists();

        int actionMasked = ev.getActionMasked();

        if (actionMasked == MotionEvent.ACTION_DOWN) {
            downX = ev.getX();

            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
        }

        if (actionMasked == MotionEvent.ACTION_MOVE) {
            mVelocityTracker.addMovement(ev);

            moveX = ev.getX();
            handleTouchEvent(moveX - downX);
            downX = moveX;
            return true;
        }

        if (actionMasked == MotionEvent.ACTION_UP || actionMasked == MotionEvent.ACTION_CANCEL) {
            mVelocityTracker.addMovement(ev);

            if (moveLength != 0) {
                endDrag();
                moveLength = 0;
            }

            recycleVelocityTracker();
        }

        return super.dispatchTouchEvent(ev);
    }

    private void endDrag() {
        final VelocityTracker velocityTracker = mVelocityTracker;
        velocityTracker.computeCurrentVelocity(1000, mMinimumVelocity * 40);
        //计算速率
        int velocityX = (int) velocityTracker.getXVelocity();
        e("moveLength-->" + moveLength + " velocityX-->" + velocityX +
                "  mMinimumVelocity-->" + mMinimumVelocity + " mMaximumVelocity-->" + mMaximumVelocity);

        if (Math.abs(velocityX) > 3 * mMinimumVelocity) {
            isBeginFling = true;
            int offset = allViewWidth - viewWidth;
            int scrollX = getScrollX();
            e("getScrollX-->" + scrollX + " allViewWidth-viewWidth-->" + offset);

            mScroller.fling(scrollX, 0, -velocityX, 0, 0, offset, 0, 0, viewWidth / 2, 0);

        } else {
            smoothToIndex(primaryIndex);
        }
    }

    private void smoothToIndex(int index) {
        if (index > getChildCount()) {
            return;
        }

        int dx;
        View childAt = getChildAt(index);
        int left = childAt.getLeft();
        int width = childAt.getMeasuredWidth();
        int scrollX = getScrollX();


        dx = left + width / 2 - viewWidth / 2 - scrollX;

        e("primaryIndex-->" + primaryIndex + " scrollX-->" + scrollX + "  dx-->" + dx);
        mScroller.startScroll(scrollX, 0, dx, 0, 500);
        invalidate();
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void handleTouchEvent(float offset) {
        moveLength += offset;
        scrollBy(-(int) offset, 0);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
//        e("l-->" + l + " t-->" + t + " oldl-->" + oldl + " oldt-->" + oldt);
        scaleView();
    }

    private void scaleView() {
        int count = getChildCount();
        int curScrollX = getScrollX();//滚动为负值, 向右偏移
        primaryIndexOffset = Float.MAX_VALUE;
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            int childLeft = childView.getLeft();//
            int childWidth = childView.getMeasuredWidth();
            int childNeedOffset = getOffsetToPrimary(i);//越向右,值越小

            float scale;
            float rawLength = viewWidth / 2 + curScrollX - childLeft - childWidth / 2;//离中心的距离
            float length = Math.abs(rawLength);//离中心的距离
            if (length < primaryIndexOffset) {//保存距离中心位置最近view的索引
                primaryIndex = i;
                primaryIndexOffset = rawLength;

                e("primaryIndex-->" + primaryIndex);
            }

//            float length = Math.abs(Math.abs(childNeedOffset) - curScrollX);//离中心的距离
            scale = Math.min(1, length / childWidth);

//            e("index-->" + i + " curScrollX-->" + curScrollX + " childLeft-->" + childLeft + " childWidth-->" +
//                    childWidth + " childNeedOffset-->" + childNeedOffset + " length-->" + length + " scale-->" + scale);

            scale = Math.max(1, mScale * (1 - scale));
            childView.setScaleX(scale);
            childView.setScaleY(scale);

        }

    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int oldX = mScrollX;
            int oldY = mScrollY;
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            if (isBeginFling) {
                e("XUtil-->" + x);
            }
            scrollTo(x, 0);
            invalidate();
        } else {
            if (isBeginFling) {
                smoothToIndex(primaryIndex);
                isBeginFling = false;
            }
        }
    }

    /**
     * 当前凸出的item索引
     */
    public int getPrimaryIndex() {
        return primaryIndex;
    }

    /**
     * 当前凸出的item索引
     */
    public View getPrimaryView() {
        return getChildAt(primaryIndex);
    }

    /**
     * 缩放比例
     */
    public float getScale() {
        return mScale;
    }

    private void e(String msg) {
//        Log.e("angcyo", msg + "");
    }
}
