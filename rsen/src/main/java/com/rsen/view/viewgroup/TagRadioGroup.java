package com.rsen.view.viewgroup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * 标签单选按钮组
 * Created by angcyo on 2016-01-06.
 */
public class TagRadioGroup extends RadioGroup {
    private int childWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int childHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

    public TagRadioGroup(Context context) {
        this(context, null);
    }

    public TagRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initChildButton();
    }

    private void initChildButton() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof RadioButton) {
                RadioButton childView = (RadioButton) view;
                childView.setButtonDrawable(new ColorDrawable());//去掉系统默认的圆圈

                if (i == 0) {
                    initFirstButton(childView);

                } else if (i == count - 1) {
                    initLastButton(childView);
                } else {
                    initMidButton(childView);
                }
            }
        }
    }

    private void initMidButton(RadioButton childView) {

    }

    private void initLastButton(RadioButton childView) {

    }

    private void initFirstButton(RadioButton childView) {

    }
}
