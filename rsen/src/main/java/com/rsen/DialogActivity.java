package com.rsen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.angcyo.rsen.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rsen.base.RBaseViewHolder;
import com.rsen.facebook.DraweeViewUtil;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2016/11/03 17:04
 * 修改人员：Robi
 * 修改时间：2016/11/03 17:04
 * 修改备注：
 * Version: 1.0.0
 */

/*
* <style name="MyDialogStyle">
        <item name="android:windowBackground">@android:color/transparent</item>
        <item name="android:windowFrame">@null</item>
        <item name="android:windowNoTitle">true</item>
        <item name="android:windowIsFloating">true</item>
        <item name="android:windowIsTranslucent">true</item>
        <item name="android:windowContentOverlay">@null</item>
        <item name="android:windowAnimationStyle">@android:style/Animation.Dialog</item>
        <item name="android:backgroundDimEnabled">true</item>
    </style>
* */

    /*
    *
    *     <!--对话框样式的Activity-->
    <style name="DialogActivity" parent="AppTheme">
        <item name="android:windowFrame">@null</item>
        <item name="android:windowIsFloating">true</item>
        <item name="android:windowBackground">@android:color/transparent</item>
        <item name="android:windowIsTranslucent">true</item>
        <item name="android:windowContentOverlay">@null</item>
        <item name="android:windowAnimationStyle">@style/DialogWindowAnim</item>
        <item name="android:windowSoftInputMode">stateUnspecified|adjustPan</item>
        <item name="android:windowCloseOnTouchOutside">true</item>
        <item name="android:windowActionModeOverlay">true</item>
    </style>
    * */
public class DialogActivity extends AppCompatActivity {

    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String TOP_ICO = "topIco";
    public static final String BOTTOM_ICO = "bottomIco";
    public static final String OK_TEXT = "okText";
    public static final String CANCEL_TEXT = "cancelText";

    protected String title, content, topIco, bottomIco, okText, cancelText;

    protected TextView mTitle, mContent, mOkButton, mCancelButton;

    protected SimpleDraweeView mTopIco, mBottomIco;

    protected RBaseViewHolder mViewHolder;

    public static void launcher(Activity activity, String title, String content, Class<?> cls) {
        launcher(activity, title, content, "", "", "确定", "取消", cls);
    }

    public static void launcher(Activity activity, String title, String content, String topIco, Class<?> cls) {
        launcher(activity, title, content, topIco, "", "确定", "取消", cls);
    }

    public static void launcher(Activity activity, String title, String content,
                                String topIco, String bottomIco,
                                String okText, String cancelText, Class<?> cls) {
        activity.startActivity(createIntent(activity, title, content, topIco, bottomIco, okText, cancelText, cls));
    }

    public static Intent createIntent(Activity activity, String title, String content,
                                      String topIco, String bottomIco,
                                      String okText, String cancelText, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra(TITLE, title);
        intent.putExtra(CONTENT, content);
        intent.putExtra(TOP_ICO, topIco);
        intent.putExtra(BOTTOM_ICO, bottomIco);
        intent.putExtra(OK_TEXT, okText);
        intent.putExtra(CANCEL_TEXT, cancelText);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_base_common_layout);
        mViewHolder = new RBaseViewHolder(findViewById(R.id.root_layout));
        mContent = mViewHolder.tv(R.id.content_view);
        mTitle = mViewHolder.tv(R.id.title_view);
        mOkButton = mViewHolder.tv(R.id.ok_button);
        mCancelButton = mViewHolder.tv(R.id.cancel_button);

        mTopIco = (SimpleDraweeView) mViewHolder.v(R.id.ico_view);
        mBottomIco = (SimpleDraweeView) mViewHolder.v(R.id.ico_bottom_view);

        Intent intent = getIntent();
        title = intent.getStringExtra(TITLE);
        content = intent.getStringExtra(CONTENT);
        topIco = intent.getStringExtra(TOP_ICO);
        bottomIco = intent.getStringExtra(BOTTOM_ICO);
        okText = intent.getStringExtra(OK_TEXT);
        cancelText = intent.getStringExtra(CANCEL_TEXT);

        initTextView(mTitle, title);
        initTextView(mContent, content);
        initTextView(mOkButton, okText);
        initTextView(mCancelButton, cancelText);

        initImageView(mTopIco, topIco);
        initImageView(mBottomIco, bottomIco);

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkButtonClick(v);
                onBackPressed();
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelButtonClick(v);
                onBackPressed();
            }
        });

        initOtherView();
    }

    protected void initOtherView() {

    }

    protected void onOkButtonClick(View v) {

    }

    protected void onCancelButtonClick(View v) {

    }

    private void initTextView(TextView view, String text) {
        if (TextUtils.isEmpty(text)) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            view.setText(text);
        }
    }

    private void initImageView(SimpleDraweeView view, String image) {
        if (TextUtils.isEmpty(image)) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            DraweeViewUtil.setDraweeViewHttp2(view, image);
        }
    }
}
