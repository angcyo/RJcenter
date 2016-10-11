package com.rsen.base;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rsen.util.ResUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2016/10/11 14:11
 * 修改人员：Robi
 * 修改时间：2016/10/11 14:11
 * 修改备注：
 * Version: 1.0.0
 */
public class RBaseItemSelectorDialog extends RBaseDialogFragment {

    protected List<ItemBean> mItemBeenList = new ArrayList<>();
    protected String mTitleText;
    protected String mCancelText = "取消";
    int itemHeight, radii;

    public static Drawable generateRoundDrawable(Resources res, float topRadii, float bottomRadii, int pressColor, int defaultColor) {
        topRadii = ResUtil.dpToPx(res, topRadii);
        bottomRadii = ResUtil.dpToPx(res, bottomRadii);
        //外环的圆角矩形
        float[] outRadii = new float[]{topRadii, topRadii, topRadii, topRadii,
                bottomRadii, bottomRadii, bottomRadii, bottomRadii};//四个角的 圆角幅度,8个可以设置的值,每个角都有2个边 2*4=8个

        //按下状态
        Shape roundRectShape = new RoundRectShape(outRadii, null, null);//圆角背景
        ShapeDrawable shopDrawablePress = new ShapeDrawable(roundRectShape);//圆角shape
        shopDrawablePress.getPaint().setColor(pressColor);//设置颜色

        //正常状态
        Shape roundRectShapeNormal = new RoundRectShape(outRadii, null, null);
        ShapeDrawable shopDrawableNormal = new ShapeDrawable(roundRectShapeNormal);
        shopDrawableNormal.getPaint().setColor(defaultColor);

        StateListDrawable bgStateDrawable = new StateListDrawable();//状态shape
        bgStateDrawable.addState(new int[]{android.R.attr.state_pressed}, shopDrawablePress);//按下状态
        bgStateDrawable.addState(new int[]{}, shopDrawableNormal);//其他状态

        return bgStateDrawable;
    }

    @Override
    protected int getContentView() {
        return 0;
    }

    @Override
    protected void initArguments(Bundle arguments) {
        super.initArguments(arguments);
        itemHeight = (int) ResUtil.dpToPx(getResources(), 50);
        radii = (int) ResUtil.dpToPx(getResources(), 3);
    }

    @Override
    protected int getWindowWidth() {
        return 300;
    }

    @Override
    protected int getWindowHeight() {
        return 300;
    }

    @Override
    protected int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected View createView() {
        LinearLayout layout = new LinearLayout(mBaseActivity);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundDrawable(generateRoundDrawable(getResources(), radii, radii, Color.WHITE, Color.WHITE));

        boolean noTitle = true;

        //标题
        if (!TextUtils.isEmpty(mTitleText)) {
            TextView titleView = createTextView(mTitleText);
            layout.addView(titleView, generateDefaultLayoutParams());
            layout.addView(createLineView());
            noTitle = false;
        }

        //Item
        for (int i = 0; i < mItemBeenList.size(); i++) {
            ItemBean itemBean = mItemBeenList.get(i);
            TextView item = createItemTextView(itemBean.mText);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemBean.mClickListener.onClick(v);
                    dismiss();
                }
            });

            if (noTitle && i == 0) {
                item.setBackgroundDrawable(generateRoundDrawable(getResources(), radii, 0, Color.parseColor("#30000000"), Color.TRANSPARENT));
            } else {
                item.setBackgroundDrawable(generateRoundDrawable(getResources(), 0, 0, Color.parseColor("#30000000"), Color.TRANSPARENT));
            }

            layout.addView(item, generateDefaultLayoutParams());
            layout.addView(createLineView());
        }

        //取消按钮
        if (!TextUtils.isEmpty(mCancelText)) {
            TextView cancelTextView = createCancelTextView(mCancelText);
            cancelTextView.setBackgroundDrawable(generateRoundDrawable(getResources(), 0, radii, Color.parseColor("#30000000"), Color.parseColor("#EEEEEE")));
            layout.addView(cancelTextView, generateDefaultLayoutParams());
        }
        return layout;
    }

    public RBaseItemSelectorDialog addItem(ItemBean bean) {
        mItemBeenList.add(bean);
        return this;
    }

    public RBaseItemSelectorDialog setTitleText(String titleText) {
        mTitleText = titleText;
        return this;
    }

    public RBaseItemSelectorDialog setCancelText(String cancelText) {
        mCancelText = cancelText;
        return this;
    }

    protected TextView createTextView(String titleText) {
        TextView textView = new TextView(mBaseActivity);
        textView.setText(titleText);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(16);
        textView.setTextColor(Color.parseColor("#333333"));
        return textView;
    }

    protected TextView createItemTextView(String itemText) {
        TextView textView = createTextView(itemText);
        textView.setClickable(true);
        return textView;
    }

    protected TextView createCancelTextView(String cancelText) {
        TextView textView = createTextView(cancelText);
        textView.setClickable(true);
        textView.setTextColor(getResources().getColor(ResUtil.getThemeColor(mBaseActivity, "colorPrimaryDark")));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return textView;
    }

    protected View createLineView() {
        View view = new View(mBaseActivity);
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, (int) ResUtil.dpToPx(getResources(), 1)));
        view.setBackgroundColor(Color.parseColor("#efeff0"));
        return view;
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.LayoutParams(-1, itemHeight);
    }

    /**
     * 每个Item的数据模型
     */
    public static class ItemBean {
        public String mText;
        public View.OnClickListener mClickListener;

        public ItemBean(String text, View.OnClickListener clickListener) {
            mText = text;
            mClickListener = clickListener;
        }
    }
}
