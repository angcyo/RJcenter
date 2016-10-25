package com.rsen.util;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.angcyo.rsen.R;
import com.rsen.github.swiperefresh.HnSwipeRefreshLayout;
import com.rsen.github.swiperefresh.SwipeRefreshLayoutDirection;


/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2016/10/25 9:46
 * 修改人员：Robi
 * 修改时间：2016/10/25 9:46
 * 修改备注：
 * Version: 1.0.0
 */
public class Refresh {

    public static HnSwipeRefreshLayout apply(final View target) {
        ViewParent parent = target.getParent();

        if (TextUtils.equals(HnSwipeRefreshLayout.class.getName(), parent.getClass().getName())) {
            return (HnSwipeRefreshLayout) parent;
        }

        ViewGroup viewGroup = (ViewGroup) parent;
        viewGroup.removeView(target);

        HnSwipeRefreshLayout refreshLayout = new HnSwipeRefreshLayout(target.getContext());
        refreshLayout.setDirection(SwipeRefreshLayoutDirection.BOTH);
        refreshLayout.addView(target, -1, -1);

        refreshLayout.setColorSchemeResources(R.color.main_color, R.color.main_color, R.color.main_color);

        viewGroup.addView(refreshLayout, target.getLayoutParams());

        return refreshLayout;
    }
}
