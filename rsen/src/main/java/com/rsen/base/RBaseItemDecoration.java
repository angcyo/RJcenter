package com.rsen.base;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by angcyo on 2016-10-16.
 */

public class RBaseItemDecoration extends RecyclerView.ItemDecoration {
    static int dividerColor = Color.parseColor("#333333");//分割线的颜色
    float mDividerSize;
    Drawable mDividerDrawableV;//垂直方向绘制的Drawable
    Drawable mDividerDrawableH;//水平方向绘制的Drawable
    /**
     * LinearLayoutManager中有效
     * VERTICAL 方向: padding 的是top 和 bottom
     * HORIZONTAL 方向: padding 的是left 和 right
     */
    int mMarginStart = 0, mMarginEnd = 0;


    public RBaseItemDecoration() {
        this(1);
    }

    public RBaseItemDecoration(int dividerSize) {
        this(dividerSize, dividerColor);
    }

    public RBaseItemDecoration(Drawable drawable) {
        this(drawable, 1);
    }

    public RBaseItemDecoration(int dividerSize, int dividerColor) {
        mDividerSize = dividerSize;
        mDividerDrawableV = new ColorDrawable(dividerColor);
        mDividerDrawableH = mDividerDrawableV;
    }

    public RBaseItemDecoration(Drawable drawable, int dividerSize) {
        mDividerSize = dividerSize;
        mDividerDrawableV = drawable;
        mDividerDrawableH = drawable;
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        final RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager.getItemCount() <= 1) {
            //如果只有1个item, 直接返回;
            return;
        }
        if (manager instanceof GridLayoutManager) {
            //网格布局
            drawGrid(c, manager);
        } else if (manager instanceof StaggeredGridLayoutManager) {
            //暂不支持...
        } else {
            //线性布局
            final LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
            final int firstItem = layoutManager.findFirstVisibleItemPosition();
            for (int i = 0; i < layoutManager.getChildCount(); i++) {
                final View view = layoutManager.findViewByPosition(firstItem + i);
                if (view != null) {
                    if (layoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                        //水平
                        drawDrawableV(c, view);
                    } else {
                        //垂直
                        drawDrawableH(c, view);
                    }
                }
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();//布局管理器
        final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        final int viewLayoutPosition = layoutParams.getViewLayoutPosition();//布局时当前View的位置

        if (layoutManager.getItemCount() <= 1) {
            //如果只有1个item, 直接返回;
            return;
        }

        if (layoutManager instanceof GridLayoutManager) {
            //请注意,这里的分割线 并不包括边框边上的分割线, 只处理不含边框的内部之间的分割线.
            offsetsOfGrid(outRect, ((GridLayoutManager) layoutManager), viewLayoutPosition);
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            //暂时不支持...以后再写
        } else {
            //线性布局 就简单了
            LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) layoutManager);
            final int itemCount = layoutManager.getItemCount();
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                //水平方向
                if (viewLayoutPosition == 0) {
                    //这里可以决定,第一个item的分割线
                    outRect.set(0, 0, (int) mDividerSize, 0);//默认只有右边有分割线, 你也可以把左边的分割线添加出来
                } else if (viewLayoutPosition == itemCount - 1) {
                    //这里可以决定, 最后一个item的分割线
                    outRect.set(0, 0, 0, 0);//默认, 最后一个item不需要分割线
                } else {
                    //中间的item,默认只有右边的分割线
                    outRect.set(0, 0, (int) mDividerSize, 0);
                }
            } else {
                //垂直方向
                if (viewLayoutPosition == 0) {
                    //这里可以决定,第一个item的分割线
                    outRect.set(0, 0, 0, (int) mDividerSize);//默认只有右边有分割线, 你也可以把左边的分割线添加出来
                } else if (viewLayoutPosition == itemCount - 1) {
                    //这里可以决定, 最后一个item的分割线
                    outRect.set(0, 0, 0, 0);//默认, 最后一个item不需要分割线
                } else {
                    //中间的item,默认只有右边的分割线
                    outRect.set(0, 0, 0, (int) mDividerSize);
                }
            }
        }
    }

    //------------------------------------------公共方法---------------------------------

    public static void setDividerColor(int dividerColor) {
        RBaseItemDecoration.dividerColor = dividerColor;
    }

    public void setDividerSize(float dividerSize) {
        mDividerSize = dividerSize;
    }

    public void setDividerDrawableV(Drawable dividerDrawableV) {
        mDividerDrawableV = dividerDrawableV;
    }

    public void setDividerDrawableH(Drawable dividerDrawableH) {
        mDividerDrawableH = dividerDrawableH;
    }

    public void setMarginStart(int marginStart) {
        mMarginStart = marginStart;
    }

    public void setMarginEnd(int marginEnd) {
        mMarginEnd = marginEnd;
    }


    //------------------------------------------私有方法---------------------------------

    /**
     * GridLayoutManager 布局, 计算每个Item, 应该留出的空间(用来绘制分割线的空间)
     */
    private void offsetsOfGrid(Rect outRect, GridLayoutManager layoutManager, int viewLayoutPosition) {
        final int spanCount = layoutManager.getSpanCount();

        if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            //垂直方向
            if (isLastOfGrid(layoutManager.getItemCount(), viewLayoutPosition, spanCount)/*判断是否是最后一排*/) {
                //最后一排的item, 不添加底部分割线,为了美观
                outRect.set(0, 0, (int) mDividerSize, 0);
            } else {
                if (isEndOfGrid(viewLayoutPosition, spanCount)/*判断是否是最靠右的一排*/) {
                    //最靠右的一排,不添加右边的分割线, 为了美观
                    outRect.set(0, 0, 0, (int) mDividerSize);
                } else {
                    outRect.set(0, 0, (int) mDividerSize, (int) mDividerSize);
                }
            }
        } else {
            //水平方向
            if (isLastOfGrid(layoutManager.getItemCount(), viewLayoutPosition, spanCount)) {
                outRect.set(0, 0, 0, (int) mDividerSize);
            } else {
                if (isEndOfGrid(viewLayoutPosition, spanCount)) {
                    outRect.set(0, 0, (int) mDividerSize, 0);
                } else {
                    outRect.set(0, 0, (int) mDividerSize, (int) mDividerSize);
                }
            }
        }

    }

    /**
     * 判断 viewLayoutPosition 是否是一排的结束位置 (垂直水平通用)
     */
    private boolean isEndOfGrid(int viewLayoutPosition, int spanCount) {
        return viewLayoutPosition % spanCount == spanCount - 1;
    }

    /**
     * 判断 viewLayoutPosition 所在的位置,是否是最后一排(垂直水平通用)
     */
    private boolean isLastOfGrid(int itemCount, int viewLayoutPosition, int spanCount) {
        boolean result = false;
        final double ceil = Math.ceil(((float) itemCount) / spanCount);
        if (viewLayoutPosition >= ceil * spanCount - spanCount) {
            result = true;
        }
        return result;
    }

    private void drawGrid(Canvas c, RecyclerView.LayoutManager manager) {
        final GridLayoutManager layoutManager = (GridLayoutManager) manager;

        final int firstItem = layoutManager.findFirstVisibleItemPosition();
        for (int i = 0; i < layoutManager.getChildCount(); i++) {
            final View view = layoutManager.findViewByPosition(firstItem + i);
            if (view != null) {
                final int spanCount = layoutManager.getSpanCount();
                final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                final int viewLayoutPosition = layoutParams.getViewLayoutPosition();//布局时当前View的位置

                if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                    //垂直方向
                    if (isLastOfGrid(layoutManager.getItemCount(), viewLayoutPosition, spanCount)/*判断是否是最后一排*/) {
                        //最后一排的item, 不添加底部分割线,为了美观
                        if (viewLayoutPosition != layoutManager.getItemCount() - 1) {
                            //如果不是最后一个,过滤掉最后一个.最后一item不滑分割线
                            drawDrawableV(c, view);
                        }
                    } else {
                        if (isEndOfGrid(viewLayoutPosition, spanCount)/*判断是否是最靠右的一排*/) {
                            //最靠右的一排,不添加右边的分割线, 为了美观
                            drawDrawableH(c, view);
                        } else {
                            drawDrawableH(c, view);
                            drawDrawableV(c, view);
                        }
                    }
                } else {
                    //水平方向
                    if (isLastOfGrid(layoutManager.getItemCount(), viewLayoutPosition, spanCount)) {
                        if (viewLayoutPosition != layoutManager.getItemCount() - 1) {
                            //如果不是最后一个,过滤掉最后一个.最后一item不滑分割线
                            drawDrawableH(c, view);
                        }
                    } else {
                        if (isEndOfGrid(viewLayoutPosition, spanCount)) {
                            drawDrawableV(c, view);
                        } else {
                            drawDrawableH(c, view);
                            drawDrawableV(c, view);
                        }
                    }
                }
            }
        }
    }

    /**
     * 绘制view对应垂直方向的分割线
     */
    private void drawDrawableV(Canvas c, View view) {
        drawDrawable(c, view, mDividerDrawableV);
    }

    /**
     * 绘制view对应水平方向的分割线
     */
    private void drawDrawableH(Canvas c, View view) {
        drawDrawable(c, view, mDividerDrawableH);
    }

    private void drawDrawable(Canvas c, View view, Drawable drawable) {
        final RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
        drawable.setBounds(
                view.getLeft() + mMarginStart,
                view.getBottom() + p.bottomMargin,
                view.getRight() - mMarginEnd,
                (int) (view.getBottom() + p.bottomMargin + mDividerSize));
        drawable.draw(c);
    }
}
