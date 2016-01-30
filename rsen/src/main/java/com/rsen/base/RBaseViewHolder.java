package com.rsen.base;

/**
 * Created by angcyo on 2016-01-30.
 */

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通用ViewHolder
 */
public class RBaseViewHolder extends RecyclerView.ViewHolder {
    public RBaseViewHolder(View itemView) {
        super(itemView);
    }

    public View v(@IdRes int resId) {
        return itemView.findViewById(resId);
    }

    public View view(@IdRes int resId) {
        return v(resId);
    }

    /**
     * 返回 TextView
     */
    public TextView tV(@IdRes int resId) {
        return (TextView) v(resId);
    }

    public TextView textView(@IdRes int resId) {
        return tV(resId);
    }

    /**
     * 返回 ImageView
     */
    public ImageView imgV(@IdRes int resId) {
        return (ImageView) v(resId);
    }

    public ImageView imageView(@IdRes int resId) {
        return imgV(resId);
    }

    /**
     * 返回 ViewGroup
     */
    public ViewGroup groupV(@IdRes int resId) {
        return (ViewGroup) v(resId);
    }

    public ViewGroup viewGroup(@IdRes int resId) {
        return groupV(resId);
    }
}