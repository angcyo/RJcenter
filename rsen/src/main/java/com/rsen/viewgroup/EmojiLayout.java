package com.rsen.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.angcyo.rsen.R;


/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：只是在EmojiView上面加了一个悬浮的删除按钮
 * 创建人员：Robi
 * 创建时间：2016/10/23 17:48
 * 修改人员：Robi
 * 修改时间：2016/10/23 17:48
 * 修改备注：
 * Version: 1.0.0
 */
public class EmojiLayout extends FrameLayout {

    EmojiView mEmojiView;
    ImageView mImageView;
    int mTop = 4;

    public EmojiLayout(Context context) {
        super(context);
        initView();
    }

    public EmojiLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        Context context = getContext();
        mEmojiView = new EmojiView(context);

        mImageView = new ImageView(context);
        mImageView.setScaleType(ImageView.ScaleType.CENTER);
        mImageView.setImageResource(R.drawable.emoji_delete_selector);

        addView(mEmojiView, new ViewGroup.LayoutParams(-1, -1));
        addView(mImageView, new ViewGroup.LayoutParams(-2, -2));

        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmojiView.onDelete();
            }
        });

        mTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTop, getResources().getDisplayMetrics());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int height = mImageView.getMeasuredHeight();
        int width = mImageView.getMeasuredWidth();

        mImageView.layout(right - width, mTop, right, mTop + height);
    }

    /**
     * 自动关联EditText
     */
    public void setupWithEditText(EditText editText) {
        mEmojiView.setupWithEditText(editText);
    }
}
