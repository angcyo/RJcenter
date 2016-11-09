package com.rsen.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2016/10/25 12:37
 * 修改人员：Robi
 * 修改时间：2016/10/25 12:37
 * 修改备注：
 * Version: 1.0.0
 */
public class TranBehavior extends CoordinatorLayout.Behavior {

    private View mTargetView;
    private int mTopOffset;
    private int mTranOffset;
    private int lastTop = 0;

    public TranBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        child.setTop(dependency.getBottom());
        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (TextUtils.equals((CharSequence) dependency.getTag(), "target_tran")) {
            mTargetView = dependency;
            mTopOffset = mTargetView.getMeasuredHeight();
            mTargetView.setTop(lastTop);
            mTargetView.setBottom(lastTop + mTopOffset);
            mTranOffset = ((ViewGroup) mTargetView).getChildAt(0).getMeasuredHeight();
            return true;
        }
        return false;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        if (mTargetView == null) {
            return false;
        }
        child.layout(child.getLeft(), mTargetView.getBottom(), child.getMeasuredWidth(), parent.getBottom());
        return true;
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        if (mTargetView == null) {
            return false;
        }

        child.measure(View.MeasureSpec.makeMeasureSpec(parent.getMeasuredWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(parent.getMeasuredHeight() - mTargetView.getMeasuredHeight(), View.MeasureSpec.EXACTLY));
        return true;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (!ViewCompat.canScrollVertically(target, 1) && !ViewCompat.canScrollVertically(target, -1)) {
            return;////1 表示 视图下滚, 相当于手指 上滑
        }

        int top = mTargetView.getTop();
        int offset = 0;
        if (dy > 0) {
            //上滑
            offset = Math.min(dy, mTranOffset + top);
        } else if (dy < 0) {
            //下滑
            offset = Math.max(dy, top);
        }
        ViewCompat.offsetTopAndBottom(mTargetView, -offset);
        consumed[1] = offset;
        lastTop = mTargetView.getTop();
    }
}
