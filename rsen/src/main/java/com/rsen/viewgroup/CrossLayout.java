package com.rsen.viewgroup;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 十字架布局
 *
 * Created by robi on 2016-06-03 19:56.
 */
public class CrossLayout extends ViewGroup {
    public CrossLayout(Context context) {
        super(context);
    }

    public CrossLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        Drawable background = getBackground();

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST) {
            if (background != null) {
                setMeasuredDimension(background.getIntrinsicWidth(), background.getIntrinsicHeight());
                return;
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = getChildCount();
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        final int paddLeft = getPaddingLeft();
        final int paddRight = getPaddingRight();
        final int paddTop = getPaddingTop();
        final int paddBottom = getPaddingBottom();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                switch (i) {
                    case 0:
                        layoutTop(child, width, paddTop);
                        break;
                    case 1:
                        layoutLeft(child, height, paddLeft);
                        break;
                    case 2:
                        layoutRight(child, width, height, paddRight);
                        break;
                    case 3:
                        layoutBottom(child, width, height, paddBottom);
                        break;
                    default:
                        return;

                }
            }
        }
    }

    private void layoutBottom(View child, int width, int height, int paddBottom) {
        final int l = (width - child.getMeasuredWidth()) / 2;
        final int t = height - paddBottom - child.getMeasuredHeight();
        child.layout(l, t, l + child.getMeasuredWidth(), t + child.getMeasuredHeight());
    }

    private void layoutRight(View child, int width, int height, int paddRight) {
        final int l = width - paddRight - child.getMeasuredWidth();
        final int t = (height - child.getMeasuredHeight()) / 2;
        child.layout(l, t, l + child.getMeasuredWidth(), t + child.getMeasuredHeight());
    }

    private void layoutLeft(View child, int height, int paddLeft) {
        final int t = (height - child.getMeasuredHeight()) / 2;
        child.layout(paddLeft, t, paddLeft + child.getMeasuredWidth(), t + child.getMeasuredHeight());
    }

    private void layoutTop(View child, int width, int paddTop) {
        final int l = (width - child.getMeasuredWidth()) / 2;
        child.layout(l, paddTop, l + child.getMeasuredWidth(), paddTop + child.getMeasuredHeight());
    }
}
