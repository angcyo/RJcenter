package com.rsen.viewgroup;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.angcyo.rsen.R;
import com.rsen.base.RBaseAdapter;
import com.rsen.base.RBaseViewHolder;
import com.rsen.util.EmojiUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：表情布局
 * 创建人员：Robi
 * 创建时间：2016/10/23 15:54
 * 修改人员：Robi
 * 修改时间：2016/10/23 15:54
 * 修改备注：
 * Version: 1.0.0
 */
public class EmojiView extends RRecyclerView {

    int padding = 6;

    EditText mEditText;
    int mDeletePosition = 6;

    public EmojiView(Context context) {
        super(context);
        initView();
    }

    public EmojiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, padding, getResources().getDisplayMetrics());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setTag("GV" + (mDeletePosition + 1));
        setAdapter(new EmojiAdapter(getContext(), initEmoji()));
    }

    private List<EmojiBean> initEmoji() {
        List<EmojiBean> list = new ArrayList<>();
        for (int i = 0; i < EmojiUtil.EmojiResArray.length; i++) {
            EmojiBean bean = new EmojiBean();
            bean.emojiRes = EmojiUtil.EmojiResArray[i];
            list.add(bean);
        }
        return list;
    }

    private void insertEmoji(int position) {
        if (mEditText != null) {
            int selectionStart = mEditText.getSelectionStart();
            int selectionEnd = mEditText.getSelectionEnd();
            String text = EmojiUtil.EmojiTextArray[position];
            mEditText.getText().replace(selectionStart, selectionEnd, text);
            try {
                EmojiUtil.handlerEmojiText(mEditText);
                mEditText.setSelection(selectionStart + text.length());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送删除按钮事件
     */
    public void onDelete() {
        if (mEditText != null) {
            KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
            mEditText.dispatchKeyEvent(event);
        }
    }

    /**
     * 删除按键所在的位置, 腾出位置, 用来放置悬浮的删除按钮
     */
    public void setDeletePosition(int position) {
        mDeletePosition = position;
    }

    /**
     * 自动关联EditText
     */
    public void setupWithEditText(EditText editText) {
        mEditText = editText;
    }

    public static class EmojiBean {
        public int emojiRes;
    }

    /**
     * 表情适配器
     */
    class EmojiAdapter extends RBaseAdapter<EmojiBean> {

        public EmojiAdapter(Context context, List<EmojiBean> datas) {
            super(context, datas);
        }

        @Override
        public int getItemCount() {
            return super.getItemCount() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == mDeletePosition) {
                return 100;
            }
            return 0;
        }

        @Override
        protected int getItemLayoutId(int viewType) {
            return 0;
        }

        @Override
        protected void onBindView(RBaseViewHolder holder, int position, EmojiBean bean) {
            if (position == mDeletePosition) {
                holder.itemView.setClickable(false);
//                ((ViewGroup) holder.itemView).getChildAt(0).setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onDelete();
//                    }
//                });
            } else {
                holder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position > mDeletePosition) {
                            insertEmoji(position - 1);
                        } else {
                            insertEmoji(position);
                        }
                    }
                });
                if (position > mDeletePosition) {
                    bean = mAllDatas.get(position - 1);
                } else {
                    bean = mAllDatas.get(position);
                }
                ((ImageView) ((ViewGroup) (holder.itemView)).getChildAt(0)).setImageResource(bean.emojiRes);
            }
        }

        @Override
        protected View createContentView(ViewGroup parent, int viewType) {

            RelativeLayout relativeLayout = new RelativeLayout(mContext);
            relativeLayout.setBackgroundResource(R.drawable.default_bg_selector);
            relativeLayout.setPadding(0, padding, 0, padding);

            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
//            if (viewType == 100) {
//                imageView.setImageResource(R.drawable.emoji_delete_selector);
//            }
            relativeLayout.addView(imageView, new ViewGroup.LayoutParams(-1, -1));
            return relativeLayout;
        }
    }
}
