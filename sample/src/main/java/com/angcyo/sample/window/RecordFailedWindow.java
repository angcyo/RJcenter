package com.angcyo.sample.window;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angcyo.sample.R;
import com.rsen.base.RBaseTipWindow;

/**
 * Created by robi on 2016-07-18 22:16.
 */
public class RecordFailedWindow extends RBaseTipWindow {
    InfoBean mInfoBean = new InfoBean();

    public RecordFailedWindow(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        mBaseViewHolder.v(R.id.okButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

        updateLayout();
    }

    protected void updateLayout() {
        if (TextUtils.isEmpty(mInfoBean.title)) {
            mBaseViewHolder.tV(R.id.title).setVisibility(View.GONE);
        } else {
            mBaseViewHolder.tV(R.id.title).setText(mInfoBean.title);
        }

        if (TextUtils.isEmpty(mInfoBean.content)) {
            mBaseViewHolder.tV(R.id.content).setVisibility(View.GONE);
        } else {
            mBaseViewHolder.tV(R.id.content).setText(mInfoBean.content);
        }
    }

    @Override
    protected View getView(ViewGroup viewGroup) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.window_record_failed, viewGroup);
        return view;
    }

    public RecordFailedWindow setContent(String content) {
        mInfoBean.content = content;
        return this;
    }

    public RecordFailedWindow setTitle(String title) {
        mInfoBean.title = title;
        return this;
    }

    @Override
    protected int getGravity() {
        return Gravity.RIGHT | Gravity.TOP;
    }

    @Override
    protected int getWindowWidth() {
        return 240;
    }

    protected class InfoBean {
        public String title;
        public String content;
    }
}
