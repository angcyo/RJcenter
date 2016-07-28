package com.rsen.viewgroup;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.rsen.animation.recyclerview.adapters.AnimationAdapter;
import com.rsen.animation.recyclerview.adapters.ScaleInAnimationAdapter;
import com.rsen.animation.recyclerview.animators.BaseItemAnimator;
import com.rsen.animation.recyclerview.animators.FadeInDownAnimator;
import com.rsen.base.RBaseAdapter;

import java.lang.reflect.Constructor;

/**
 * 简单封装的RecyclerView
 * <p>
 * 动画样式:https://github.com/wasabeef/recyclerview-animators
 * Created by angcyo on 16-03-01-001.
 */
public class RRecyclerView extends RecyclerView {
    private int orientation = LinearLayoutManager.VERTICAL;

    private Class<? extends AnimationAdapter> animatorAdapter;

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
        if (!TextUtils.isEmpty(tag) && "H".equalsIgnoreCase(tag)) {
            orientation = LinearLayoutManager.HORIZONTAL;
        }
        this.setLayoutManager(new LinearLayoutManager(context, orientation, false));
        //this.setItemAnimator(new DefaultItemAnimator());
        this.setItemAnimator(new FadeInDownAnimator());

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Adapter adapter = getAdapter();
                if (adapter != null && adapter instanceof RBaseAdapter) {
                    ((RBaseAdapter) adapter).onScrollStateChanged(recyclerView, newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Adapter adapter = getAdapter();
                if (adapter != null && adapter instanceof RBaseAdapter) {
                    ((RBaseAdapter) adapter).onScrolled(recyclerView, dx, dy);
                }
            }
        });
    }

    @Override
    public void setTag(Object tag) {
        super.setTag(tag);
        if (tag != null && "V".equalsIgnoreCase((String) tag)) {
            orientation = LinearLayoutManager.VERTICAL;
        }
        this.setLayoutManager(new LinearLayoutManager(getContext(), orientation, false));
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(getAnimationAdapter(adapter));
    }

    public RRecyclerView setItemAnimator(Class<? extends BaseItemAnimator> animator) {
        try {
            super.setItemAnimator(animator.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void setAnimatorAdapter(Class<? extends AnimationAdapter> animatorAdapter, Adapter adapter) {
        setAnimatorAdapter(animatorAdapter);
        setAdapter(adapter);
    }

    public RRecyclerView setAnimatorAdapter(Class<? extends AnimationAdapter> animatorAdapter) {
        this.animatorAdapter = animatorAdapter;
        return this;
    }

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
