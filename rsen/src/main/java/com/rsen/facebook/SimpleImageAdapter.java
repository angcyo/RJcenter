package com.rsen.facebook;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.angcyo.rsen.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rsen.base.RBaseAdapter;
import com.rsen.base.RBaseViewHolder;
import com.rsen.util.ResUtil;
import com.rsen.util.T;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2016/09/30 10:57
 * 修改人员：Robi
 * 修改时间：2016/09/30 10:57
 * 修改备注：
 * Version: 1.0.0
 */
public class SimpleImageAdapter extends RBaseAdapter<SimpleImageAdapter.Image> {

    public static final int DEFAULT_SIZE = -100;

    protected Image mImage;
    /**
     * Item的大小
     */
    int mItemSize = DEFAULT_SIZE;
    /**
     * Item的空隙
     */
    int mItemSpaceSize = 6;

    /**
     * Item 的数量(横向排列的数量)
     */
    int mItemCount = 3;

    boolean mBorder = false;

    OnItemClickListener mItemClickListener;

    public SimpleImageAdapter(Context context) {
        super(context);
        mImage = new Image();
        mAllDatas.add(mImage);
    }

    public SimpleImageAdapter setItemSize(int itemSize) {
        mItemSize = itemSize;
        return this;
    }

    public SimpleImageAdapter setItemSpaceSize(int itemSpaceSize) {
        mItemSpaceSize = itemSpaceSize;
        return this;
    }

    public SimpleImageAdapter setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
        return this;
    }

    public SimpleImageAdapter setBorder(boolean border) {
        mBorder = border;
        return this;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return 0;
    }

    @Override
    public int getItemCount() {
        if (mAllDatas == null || mAllDatas.isEmpty()) {
            return 0;
        }
        if (getImage().mListImageRes.isEmpty()) {
            return getImage().mListImageString.size();
        } else {
            return getImage().mListImageRes.size();
        }
    }

    public SimpleImageAdapter setItemCount(int itemCount) {
        mItemCount = itemCount;
        return this;
    }

    @Override
    protected View createContentView(ViewGroup parent, int viewType) {
        int screenWidth = ResUtil.getScreenWidth(mContext);
        float paddLeft = ResUtil.dpToPx(mContext.getResources(), 10);
        float paddRight = paddLeft;

        int itemSpace = (int) ResUtil.dpToPx(mContext.getResources(), mItemSpaceSize);
//        int itemSize = (int) (screenWidth - paddLeft - paddRight - 3 * itemSpace) / 3;//计算Item的宽高
        int itemSize = screenWidth / mItemCount;

        if (mItemSize == DEFAULT_SIZE) {
            mItemSize = itemSize;
        }

        RelativeLayout layout = new RelativeLayout(mContext);
        layout.setLayoutParams(new ViewGroup.LayoutParams(mItemSize, mItemSize));
        layout.setPadding(itemSpace, itemSpace / 2, itemSpace, itemSpace / 2);
        if (mBorder) {
            layout.setBackgroundDrawable(ResUtil.generateRoundBorderDrawable(0,
                    ResUtil.dpToPx(mContext.getResources(), 0.5f), Color.parseColor("#efeff0")));
        }
//        layout.setBackgroundColor(Color.RED);

        SimpleDraweeView imageView = new SimpleDraweeView(mContext);
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
//        imageView.setPadding(itemSpace, itemSpace / 2, itemSpace, itemSpace / 2);

        if (mBorder) {
            int px = (int) ResUtil.dpToPx(mContext.getResources(), 2);
            imageView.setPadding(px, px, px, px);
        }

        View clickView = new View(mContext);
        clickView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
//        clickView.setBackgroundResource(R.drawable.default_bg_selector);

        Drawable drawable = ResUtil.generateRoundBorderDrawable(0, Color.parseColor("#60000000"), Color.TRANSPARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            clickView.setBackground(drawable);
        } else {
            clickView.setBackgroundDrawable(drawable);
        }

        layout.addView(imageView);

        if (mBorder) {
            layout.setClickable(true);
            layout.setBackgroundDrawable(drawable);
        }

        layout.addView(clickView);
        return layout;
    }

    @Override
    public void onBindViewHolder(RBaseViewHolder holder, int position) {
        onBindView(holder, position, getImage());
    }

    @Override
    protected void onBindView(RBaseViewHolder holder, int position, Image bean) {
        SimpleDraweeView draweeView = (SimpleDraweeView) ((ViewGroup) holder.itemView).getChildAt(0);
        View clickView = ((ViewGroup) holder.itemView).getChildAt(1);
        if (isResData()) {
            DraweeViewUtil.setDraweeViewRes(draweeView, R.drawable.face800);
        } else {
            DraweeViewUtil.setDraweeViewRes(draweeView, R.drawable.face800);
        }
        if (mBorder) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    T.show(mContext, position + " ");
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(v, position, bean);
                    }
                }
            });
        } else {
            clickView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    T.show(mContext, position + " ");
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(v, position, bean);
                    }
                }
            });
        }
    }

    protected Image getImage() {
        return mAllDatas.get(0);
    }

    public void resetImagesString(List<String> list) {
        cleanImage();
        mImage.mListImageString.addAll(list);
        notifyDataSetChanged();
    }

    public void resetImagesRes(List<Integer> list) {
        cleanImage();
        mImage.mListImageRes.addAll(list);
        notifyDataSetChanged();
    }

    protected void cleanImage() {
        mImage.mListImageRes.clear();
        mImage.mListImageString.clear();
    }

    protected boolean isResData() {
        return !mImage.mListImageRes.isEmpty();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, Image bean);
    }

    public static class Image {
        public ArrayList<String> mListImageString = new ArrayList<>();
        public ArrayList<Integer> mListImageRes = new ArrayList<>();
    }
}
