package com.rsen.view.viewgroup;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.angcyo.rsen.R;
import com.rsen.util.ResUtil;


/**
 * 标签单选按钮组
 * Created by angcyo on 2016-01-06.
 */
public class TagRadioGroup extends RadioGroup implements RadioGroup.OnCheckedChangeListener {
    public static final String KEY_CHECK = "key_check";
    private int childWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int childHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int childTextColor = Color.parseColor("#969696");
    private int childPressTextColor = Color.WHITE;
    private int padding = -1;//dp
    private int paddingLeft = 15;//dp
    private int paddingTop = 10;
    private int paddingRight = 15;
    private int paddingBottom = 10;
    private float borderWidth = 2f;//线框的宽度
    private float round = 8f;//dp 单位
    private int childCount = -1;//自动填充子view的数量
    private String[] childTexts;//填充子view的文本
    private float childTextSize = -1;//文本大小
    private int checkButtonIndex = 0;//默认选中按钮
    private int curCheckButtonIndex = -1;
    private String curCheckButtonText;
    private OnCheckListener listener;

    public TagRadioGroup(Context context) {
        this(context, null);
    }

    public TagRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagRadioGroup);
        childWidth = (int) typedArray.getDimension(R.styleable.TagRadioGroup_childWidth, childWidth);
        childHeight = (int) typedArray.getDimension(R.styleable.TagRadioGroup_childHeight, childHeight);
        padding = typedArray.getInteger(R.styleable.TagRadioGroup_childPadding, padding);
        paddingLeft = typedArray.getInteger(R.styleable.TagRadioGroup_childPaddingLeft, paddingLeft);
        paddingTop = typedArray.getInteger(R.styleable.TagRadioGroup_childPaddingTop, paddingTop);
        paddingRight = typedArray.getInteger(R.styleable.TagRadioGroup_childPaddingRight, paddingRight);
        paddingBottom = typedArray.getInteger(R.styleable.TagRadioGroup_childPaddingBottom, paddingBottom);

        childTextColor = typedArray.getColor(R.styleable.TagRadioGroup_childTextColor, childTextColor);
        childPressTextColor = typedArray.getColor(R.styleable.TagRadioGroup_childTextColorPress, childPressTextColor);

        borderWidth = typedArray.getDimension(R.styleable.TagRadioGroup_childBorderWidth, borderWidth);
        round = typedArray.getDimension(R.styleable.TagRadioGroup_childRound, round);
        childTextSize = typedArray.getDimension(R.styleable.TagRadioGroup_childTextSize, childTextSize);

        childCount = typedArray.getInteger(R.styleable.TagRadioGroup_childCount, childCount);
        checkButtonIndex = typedArray.getInteger(R.styleable.TagRadioGroup_checkButtonIndex, checkButtonIndex);
        init();
    }

    public static ColorStateList generateTextColor(int pressColor, int defaultColor) {
        ColorStateList stateList = new ColorStateList(new int[][]{{android.R.attr.state_pressed}, {android.R.attr.state_checked}, {}},
                new int[]{pressColor, pressColor, defaultColor});
        return stateList;
    }

    public static Drawable generateBgDrawable(float radiiL, float radiiR, RectF insetOffset, int pressColor) {
        //外环的圆角矩形
        float[] outRadii = new float[]{radiiL, radiiL, radiiR, radiiR, radiiR, radiiR, radiiL, radiiL};//四个角的 圆角幅度,8个可以设置的值,每个角都有2个边 2*4=8个

        //与内环的距离
        RectF inset = new RectF(0, 0, 0, 0);

        //按下状态
        Shape roundRectShape = new RoundRectShape(outRadii, inset, outRadii);//圆角背景
        ShapeDrawable shopDrawablePress = new ShapeDrawable(roundRectShape);//圆角shape
        shopDrawablePress.getPaint().setColor(pressColor);//设置颜色

        //正常状态
        Shape roundRectShapeNormal = new RoundRectShape(outRadii, insetOffset, outRadii);
        ShapeDrawable shopDrawableNormal = new ShapeDrawable(roundRectShapeNormal);
        shopDrawableNormal.getPaint().setColor(pressColor);
        shopDrawableNormal.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);

        StateListDrawable bgStateDrawable = new StateListDrawable();//状态shape
        bgStateDrawable.addState(new int[]{android.R.attr.state_pressed}, shopDrawablePress);//按下状态
        bgStateDrawable.addState(new int[]{android.R.attr.state_checked}, shopDrawablePress);//按下状态
        bgStateDrawable.addState(new int[]{}, shopDrawableNormal);//其他状态

        return bgStateDrawable;
    }

    private void init() {
        fillChildButton();
        setOnCheckedChangeListener(this);
    }

    @Override
    public void addView(View child) {
        if (childCount <= 0) {
            super.addView(child);
        }
    }

    @Override
    public void addView(View child, int index) {
        if (childCount <= 0) {
            super.addView(child, index);
        }
    }

    @Override
    public void addView(View child, int width, int height) {
        if (childCount <= 0) {
            super.addView(child, width, height);
        }
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (childCount <= 0) {
            super.addView(child, params);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
    }

    private RadioButton generateChildButton(int index) {
        RadioButton button = new RadioButton(getContext(), null, android.R.attr.radioButtonStyle);
        String text = "";
        if (childTexts != null && childTexts.length > index) {
            text = childTexts[index];
        }
        button.setText(text);
        return button;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (childCount <= 0) {
            initChildButton();
        }
    }

    private void initChildButton() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof RadioButton) {
                RadioButton childView = (RadioButton) view;
                childView.setButtonDrawable(new ColorDrawable());//去掉系统默认的圆圈
                childView.setTextColor(textColor());

                if (childTextSize > 0) {
                    childView.setTextSize(childTextSize);
                }

                if (padding <= 0) {
                    childView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
                } else {
                    childView.setPadding(padding, padding, padding, padding);
                }

                if (i == 0) {
                    initFirstButton(childView);
                } else if (i == count - 1) {
                    initLastButton(childView);
                } else {
                    initMidButton(childView);
                }

                if (checkButtonIndex >= 0 && checkButtonIndex == i) {
                    childView.setChecked(true);
                    if (listener != null) {
                        curCheckButtonIndex = i;
                        curCheckButtonText = childView.getText().toString();
                        listener.onCheck(this, childView, curCheckButtonIndex, curCheckButtonText);
                    }
                }
            }
        }
    }

    private Drawable bgDrawable(float roundL, float roundR, RectF insetOffset) {
        return generateBgDrawable(roundL, roundR, insetOffset, childTextColor);
    }

    private ColorStateList textColor() {
        return generateTextColor(childPressTextColor, childTextColor);
    }

    private void initMidButton(RadioButton childView) {
        ResUtil.setBgDrawable(childView, bgDrawable(0, 0, new RectF(borderWidth / 2f, borderWidth, borderWidth / 2, borderWidth)));
    }

    private void initLastButton(RadioButton childView) {
        ResUtil.setBgDrawable(childView, bgDrawable(0, round, new RectF(borderWidth / 2f, borderWidth, borderWidth, borderWidth)));
    }

    private void initFirstButton(RadioButton childView) {
        ResUtil.setBgDrawable(childView, bgDrawable(round, 0, new RectF(borderWidth, borderWidth, borderWidth / 2, borderWidth)));
    }

    private void fillChildButton() {
        removeAllViews();
        for (int i = 0; i < childCount; i++) {
            addView(generateChildButton(i), i, new ViewGroup.LayoutParams(childWidth, childHeight));
        }
        initChildButton();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_CHECK, getCheckIndex());
        bundle.putParcelable("state", super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        int index = bundle.getInt(KEY_CHECK);
        RadioButton childAt = (RadioButton) getChildAt(index);
        if (childAt != null) {
            childAt.setChecked(true);
        }
        super.onRestoreInstanceState(bundle.getParcelable("state"));
    }

    private void checkView() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            RadioButton button = (RadioButton) getChildAt(i);
            if (button.isChecked()) {
                curCheckButtonIndex = i;
                curCheckButtonText = button.getText().toString();
            }
        }
    }

    /**
     * 获取选中按钮的索引
     */
    public int getCheckIndex() {
        checkView();
        return curCheckButtonIndex;
    }

    /**
     * 获取选中按钮的文本
     */
    public String getCheckText() {
        checkView();
        return curCheckButtonText;
    }

    public int getChildWidth() {
        return childWidth;
    }

    public void setChildWidth(int childWidth) {
        this.childWidth = childWidth;
    }

    public int getChildHeight() {
        return childHeight;
    }

    public void setChildHeight(int childHeight) {
        this.childHeight = childHeight;
    }

    public int getChildTextColor() {
        return childTextColor;
    }

    public void setChildTextColor(int childTextColor) {
        this.childTextColor = childTextColor;
    }

    public int getChildPressTextColor() {
        return childPressTextColor;
    }

    public void setChildPressTextColor(int childPressTextColor) {
        this.childPressTextColor = childPressTextColor;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    @Override
    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    @Override
    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    @Override
    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    @Override
    public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }

    public float getRound() {
        return round;
    }

    public void setRound(float round) {
        this.round = round;
    }

    public int getChildSetCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
        fillChildButton();
    }

    public String[] getChildTexts() {
        return childTexts;
    }

    public void setChildTexts(String[] childTexts) {
        this.childTexts = childTexts;
        fillChildButton();
    }

    public void setListener(OnCheckListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        checkView();
        if (listener != null) {
            listener.onCheck(this, (RadioButton) getChildAt(curCheckButtonIndex), curCheckButtonIndex, curCheckButtonText);
        }
    }

    public interface OnCheckListener {
        void onCheck(TagRadioGroup radioGroup, RadioButton button, int index, String text);
    }
}