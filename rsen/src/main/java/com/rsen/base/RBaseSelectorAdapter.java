package com.rsen.base;

import android.content.Context;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2016/10/17 17:07
 * 修改人员：Robi
 * 修改时间：2016/10/17 17:07
 * 修改备注：
 * Version: 1.0.0
 */
public abstract class RBaseSelectorAdapter<T> extends RBaseAdapter<T> {

    private boolean isSelectorMode = false;
    private Set<Integer> mSelectorIntegerSet = new HashSet<>();
    private OnSelectorModeChangeListener mModeChangeListener;

    public RBaseSelectorAdapter(Context context) {
        super(context);
    }

    public RBaseSelectorAdapter(Context context, List<T> datas) {
        super(context, datas);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return getItemLayoutIdMode(isSelectorMode, viewType);
    }

    protected abstract int getItemLayoutIdMode(boolean isSelectorMode, int viewType);

    @Override
    public int getItemCount() {
        return getItemCountMode(isSelectorMode);
    }

    protected int getItemCountMode(boolean isSelectorMode) {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewTypeMode(isSelectorMode, position);
    }

    protected int getItemViewTypeMode(boolean isSelectorMode, int position) {
        return -1;
    }

    @Override
    protected void onBindView(RBaseViewHolder holder, int position, T bean) {
        onBindViewMode(holder, position, bean, isSelectorMode);
    }

    protected abstract void onBindViewMode(RBaseViewHolder holder, int position, T bean, boolean isSelectorMode);

    /**
     * 设置选择模式
     */
    public void setSelectorMode(boolean selectorMode) {
        if (isSelectorMode == selectorMode) {
            return;
        }

        if (mModeChangeListener != null) {
            mModeChangeListener.onSelectorModeChanged(selectorMode);
        }

        if (!isSelectorMode) {
            mSelectorIntegerSet.clear();
        }
        isSelectorMode = selectorMode;
        notifyDataSetChanged();
    }

    public void selectorItem(boolean selector, int position) {
        if (selector) {
            mSelectorIntegerSet.add(position);
        } else {
            mSelectorIntegerSet.remove(position);
        }

        if (mModeChangeListener != null) {
            mModeChangeListener.onSelectorItem(mSelectorIntegerSet, selector, position);
        }
    }

    /**
     * 判断Item是否处于选择状态
     */
    public boolean isItemSelector(int position) {
        return mSelectorIntegerSet.contains(position);
    }

    /**
     * 切换选择模式
     */
    public void setSelectorMode() {
        setSelectorMode(!isSelectorMode);
    }

    public Set<Integer> getSelectorIntegerSet() {
        return mSelectorIntegerSet;
    }

    public void setModeChangeListener(OnSelectorModeChangeListener modeChangeListener) {
        mModeChangeListener = modeChangeListener;
    }

    public interface OnSelectorModeChangeListener {
        void onSelectorModeChanged(boolean isSelector);

        void onSelectorItem(Set<Integer> integers, boolean selector, int position);
    }
}