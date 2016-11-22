package com.rsen.viewgroup;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.rsen.animation.recyclerview.adapters.AnimationAdapter;
import com.rsen.animation.recyclerview.adapters.ScaleInAnimationAdapter;
import com.rsen.animation.recyclerview.animators.BaseItemAnimator;
import com.rsen.animation.recyclerview.animators.FadeInDownAnimator;
import com.rsen.base.RBaseAdapter;
import com.rsen.util.AnimUtil;

import java.lang.reflect.Constructor;

/**
 * 简单封装的RecyclerView
 * <p>
 * 动画样式:https://github.com/wasabeef/recyclerview-animators
 * Created by angcyo on 16-03-01-001.
 */
public class RRecyclerView extends RecyclerView {
    protected LayoutManager layoutManager;
    protected int spanCount = 2;
    protected int orientation = LinearLayout.VERTICAL;
    protected Class<? extends AnimationAdapter> animatorAdapter;
    protected RBaseAdapter mAdapterRaw;
    protected AnimationAdapter mAnimationAdapter;
    protected boolean mItemAnim = true;

    private OnScrollListener mScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Adapter adapter = getAdapterRaw();
            if (adapter != null && adapter instanceof RBaseAdapter) {
                ((RBaseAdapter) adapter).onScrollStateChanged(recyclerView, newState);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            Adapter adapter = getAdapterRaw();
            if (adapter != null && adapter instanceof RBaseAdapter) {
                ((RBaseAdapter) adapter).onScrolled(recyclerView, dx, dy);
            }
        }
    };

    public RRecyclerView(Context context) {
        this(context, null);
    }

    public RRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        String tag = (String) this.getTag();
        if (TextUtils.isEmpty(tag) || "V".equalsIgnoreCase(tag)) {
            layoutManager = new LinearLayoutManager(context, orientation, false);
            AnimUtil.applyLayoutAnimation(this);
        } else {
            //线性布局管理器
            if ("H".equalsIgnoreCase(tag)) {
                orientation = LinearLayoutManager.HORIZONTAL;
                layoutManager = new LinearLayoutManager(context, orientation, false);
            } else {
                //读取其他配置信息(数量和方向)
                final String type = tag.substring(0, 1);
                if (tag.length() >= 3) {
                    spanCount = Integer.valueOf(tag.substring(2));//数量
                }
                if (tag.length() >= 2) {
                    if ("H".equalsIgnoreCase(tag.substring(1, 2))) {
                        orientation = StaggeredGridLayoutManager.HORIZONTAL;//方向
                    }
                }

                //交错布局管理器
                if ("S".equalsIgnoreCase(type)) {
                    layoutManager = new StaggeredGridLayoutManager(spanCount, orientation);
                }
                //网格布局管理器
                else if ("G".equalsIgnoreCase(type)) {
                    layoutManager = new GridLayoutManager(context, spanCount, orientation, false);
                }
            }
        }

        this.setLayoutManager(layoutManager);
        //this.setItemAnimator(new DefaultItemAnimator());
        this.setItemAnimator(new FadeInDownAnimator());

        //clearOnScrollListeners();
        removeOnScrollListener(mScrollListener);
        //添加滚动事件监听
        addOnScrollListener(mScrollListener);
    }


    @Override
    public void setTag(Object tag) {
        super.setTag(tag);
        initView(getContext());
    }

    /**
     * 请在{@link RRecyclerView#setAdapter(Adapter)}方法之前调用
     */
    public void setItemAnim(boolean itemAnim) {
        mItemAnim = itemAnim;
        if (mItemAnim) {
            this.setItemAnimator(new FadeInDownAnimator());
        } else {
            this.setItemAnimator(new DefaultItemAnimator());
        }
    }

    //-----------获取 默认的adapter, 获取 RBaseAdapter, 获取 AnimationAdapter----------//

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof RBaseAdapter) {
            mAdapterRaw = (RBaseAdapter) adapter;
        }
        mAnimationAdapter = getAnimationAdapter(adapter);

        if (mItemAnim) {
            super.setAdapter(mAnimationAdapter);
        } else {
            super.setAdapter(adapter);
        }
    }

    public RBaseAdapter getAdapterRaw() {
        return mAdapterRaw;
    }

    public AnimationAdapter getAnimationAdapter() {
        return mAnimationAdapter;
    }

    //----------------end--------------------//

    /**
     * 设置Item 动画类, 用于 添加 和 删除 Item时候的动画
     */
    public RRecyclerView setItemAnimator(Class<? extends BaseItemAnimator> animator) {
        try {
            super.setItemAnimator(animator.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置Item滑动时的动画,用于滑动查看时的动画
     */
    public void setAnimatorAdapter(Class<? extends AnimationAdapter> animatorAdapter, Adapter adapter) {
        setAnimatorAdapter(animatorAdapter);
        setAdapter(adapter);
    }

    public RRecyclerView setAnimatorAdapter(Class<? extends AnimationAdapter> animatorAdapter) {
        this.animatorAdapter = animatorAdapter;
        return this;
    }

    /**
     * 将默认的adapter, 包裹一层动画adapter
     */
    private AnimationAdapter getAnimationAdapter(RecyclerView.Adapter adapter) {
        AnimationAdapter animationAdapter = new ScaleInAnimationAdapter(adapter);
        if (animatorAdapter != null) {
            try {
                final Constructor<? extends AnimationAdapter> constructor =
                        animatorAdapter.getDeclaredConstructor(Adapter.class);
                animationAdapter = constructor.newInstance(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return animationAdapter;
    }
}
