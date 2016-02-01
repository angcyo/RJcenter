package com.rsen.angcyo;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.angcyo.rsen.R;
import com.rsen.base.RBaseDialogFragment;
import com.rsen.util.ResUtil;

/**
 * 标准的对话框
 * Created by angcyo on 16-01-31-031.
 */
public class CommonDialogFragment extends RBaseDialogFragment {
    private AppCompatTextView title, context;//标题,内容
    private AppCompatButton negativeButton;//消极的按钮
    private AppCompatButton neutralButton;//中立的按钮
    private AppCompatButton positiveButton;//积极的按钮

    public static void launch(FragmentManager manager) {
        Bundle bundle = new Bundle();
        CommonDialogFragment fragment = new CommonDialogFragment();
        fragment.setArguments(bundle);
        fragment.show(manager, CommonDialogFragment.class.getSimpleName());
    }

    @Override
    protected int getContentView() {
        return R.layout.rsen_common_dialog_fragment;
    }

    @Override
    protected void initView() {
        super.initView();
        title = (AppCompatTextView) mViewHolder.v(R.id.title);
        context = (AppCompatTextView) mViewHolder.v(R.id.content);
        negativeButton = (AppCompatButton) mViewHolder.v(R.id.negative_button);
        neutralButton = (AppCompatButton) mViewHolder.v(R.id.neutral_button);
        positiveButton = (AppCompatButton) mViewHolder.v(R.id.positive_button);
        neutralButton.setVisibility(View.GONE);

        initPositiveBg();
        initOtherBg();

//        title.setText("");
        context.setText("");
    }

    /**
     * 设置积极按钮的背景
     */
    private void initPositiveBg() {
        int colorAccent = ResUtil.getThemeColorAccent(mBaseActivity);
        int color = getResources().getColor(colorAccent);
        ResUtil.setBgDrawable(positiveButton, generateDrawable(color, generatePressColor(color)));
    }

    /**
     * 设置其他按钮的背景
     */
    private void initOtherBg() {
        int color = Color.parseColor("#D6D7D7");
        ResUtil.setBgDrawable(negativeButton, generateDrawable(color, generatePressColor(color)));
        ResUtil.setBgDrawable(neutralButton, generateDrawable(color, generatePressColor(color)));
    }

    /**生成一个具有透明度的颜色*/
    private int generatePressColor(@ColorInt int defaultColor) {
        return Color.argb(0xBB, Color.red(defaultColor), Color.green(defaultColor), Color.blue(defaultColor));
//        StringBuilder colorBuild = new StringBuilder("#40");
//        colorBuild.append(Integer.toHexString(Color.red(defaultColor)));
//        colorBuild.append(Integer.toHexString(Color.green(defaultColor)));
//        colorBuild.append(Integer.toHexString(Color.blue(defaultColor)));
//        return Color.parseColor(colorBuild.toString());
    }

    private Drawable generateDrawable(@ColorInt int defaultColor, @ColorInt int pressColor) {
        float round = ResUtil.dpToPx(getResources(), 5f);
        StateListDrawable stateListDrawable = new StateListDrawable();
        RoundRectShape rectShape = new RoundRectShape(new float[]{round, round, round, round, round, round, round, round}, null, null);
        ShapeDrawable defaultDrawable = new ShapeDrawable(rectShape);
        defaultDrawable.getPaint().setColor(defaultColor);
//        defaultDrawable.getPaint().setAlpha(20);

        RoundRectShape rectShape2 = new RoundRectShape(new float[]{round, round, round, round, round, round, round, round}, null, null);
        ShapeDrawable pressDrawable = new ShapeDrawable(rectShape2);
        pressDrawable.getPaint().setColor(pressColor);

        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressDrawable);
        stateListDrawable.addState(new int[]{}, defaultDrawable);
        return stateListDrawable;
    }
}
