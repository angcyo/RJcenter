package com.rsen.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 简单封装的RecyclerView
 * <p>
 * Created by angcyo on 16-03-01-001.
 */
public class RRecyclerView extends RecyclerView {
    private int orientation = HORIZONTAL;

    public RRecyclerView(Context context) {
        this(context, null);
    }

    public RRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, android.support.v7.recyclerview.R.styleable.RecyclerView);
        orientation = a.getInt(android.support.v7.recyclerview.R.styleable.RecyclerView_android_orientation, VERTICAL);
        a.recycle();
        initView(context);
    }

    private void initView(Context context) {
        this.setLayoutManager(new LinearLayoutManager(context, orientation == HORIZONTAL ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
        this.setItemAnimator(new DefaultItemAnimator());
    }
}
