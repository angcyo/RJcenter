package com.rsen.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.angcyo.rsen.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rsen.facebook.DraweeViewUtil;
import com.rsen.util.ResUtil;

import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：九宫格添加图片的适配器
 * 创建人员：Robi
 * 创建时间：2016/10/18 10:51
 * 修改人员：Robi
 * 修改时间：2016/10/18 10:51
 * 修改备注：
 * Version: 1.0.0
 */
public abstract class RBaseAddPhotoAdapter<T> extends RBaseAdapter<T> {

    public static final int TYPE_ADD = 2;
    public static final int TYPE_NORMAL = 1;

    /**
     * 允许添加的图片最大数量
     */
    private int mMaxPhotoCount = 9;

    /**
     * 每一行中, Item的数量, 用来计算item的宽高
     */
    private int mItemCountLine = 4;

    public RBaseAddPhotoAdapter(Context context, List<T> datas) {
        super(context, datas);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return 0;
    }

    @Override
    protected void onBindView(RBaseViewHolder holder, int position, T bean) {

    }

    @Override
    public int getItemViewType(int position) {
        if (mAllDatas == null || mAllDatas.size() == 0) {
            return TYPE_ADD;
        }
        if (position == mAllDatas.size() && mAllDatas.size() < mMaxPhotoCount) {
            return TYPE_ADD;//最后一个添加item
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        if (mAllDatas == null || mAllDatas.isEmpty()) {
            return 1;
        }
        if (mAllDatas.size() < mMaxPhotoCount) {
            return mAllDatas.size() + 1;
        }
        return mAllDatas.size();
    }

    @Override
    protected View createContentView(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ADD) {
            return createAddView();
        } else {
            return createImageView();
        }
    }

    @Override
    public void onBindViewHolder(RBaseViewHolder holder, int position) {
        if (holder.getViewType() == TYPE_ADD) {
            holder.itemView.findViewWithTag("add_view").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAddViewClick(v);
                }
            });
        } else {
            SimpleDraweeView imageView = (SimpleDraweeView) holder.itemView.findViewWithTag("image_view");
            DraweeViewUtil.setDraweeViewFile(imageView, getImageFilePath(position));
            holder.itemView.findViewWithTag("click_view").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemViewClick(v, position, mAllDatas.get(position));
                }
            });
        }
    }


    protected abstract String getImageFilePath(int position);

    protected abstract void onAddViewClick(View view);

    protected abstract void onItemViewClick(View view, int position, T bean);

    public RBaseAddPhotoAdapter setItemCountLine(int itemCountLine) {
        mItemCountLine = itemCountLine;
        return this;
    }

    public RBaseAddPhotoAdapter setMaxPhotoCount(int maxPhotoCount) {
        mMaxPhotoCount = maxPhotoCount;
        return this;
    }

    private View createImageView() {
        int screenWidth = ResUtil.getScreenWidth(mContext);

        int itemSpace = (int) ResUtil.dpToPx(mContext.getResources(), 6);
        int itemSize = screenWidth / mItemCountLine;

        RelativeLayout layout = new RelativeLayout(mContext);
        layout.setLayoutParams(new ViewGroup.LayoutParams(itemSize, itemSize));
        layout.setPadding(itemSpace, itemSpace / 2, itemSpace, itemSpace / 2);
//        layout.setBackgroundColor(Color.RED);

        SimpleDraweeView imageView = new SimpleDraweeView(mContext);
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));

        View clickView = new View(mContext);
        clickView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
//        clickView.setBackgroundResource(R.drawable.default_bg_selector);

        Drawable drawable = ResUtil.generateRoundBorderDrawable(0, Color.parseColor("#80000000"), Color.TRANSPARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            clickView.setBackground(drawable);
        } else {
            clickView.setBackgroundDrawable(drawable);
        }

        imageView.setTag("image_view");
        clickView.setTag("click_view");
        layout.addView(imageView);
        layout.addView(clickView);
        return layout;
    }

    private View createAddView() {
        int screenWidth = ResUtil.getScreenWidth(mContext);

        int itemSpace = (int) ResUtil.dpToPx(mContext.getResources(), 6);
        int itemSize = screenWidth / mItemCountLine;

        RelativeLayout layout = new RelativeLayout(mContext);
        layout.setLayoutParams(new ViewGroup.LayoutParams(itemSize, itemSize));
        layout.setPadding(itemSpace, itemSpace / 2, itemSpace, itemSpace / 2);
//        layout.setBackgroundColor(Color.RED);

        ImageButton clickView = new ImageButton(mContext);
        clickView.setImageResource(R.drawable.add);
        clickView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        clickView.setBackgroundResource(R.drawable.default_add_button_selector);

        clickView.setTag("add_view");
        layout.addView(clickView);
        return layout;
    }
}
