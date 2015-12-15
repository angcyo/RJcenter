package com.rsen.view.viewgroup;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angcyo on 15-10-22-022.
 */
public class FlowRadioGroup extends RadioGroup {
    /**
     * The M all views.
     */
    List<List<View>> mAllViews;//保存所有行的所有View
    /**
     * The M line height.
     */
    List<Integer> mLineHeight;//保存每一行的行高

    /**
     * Instantiates a new Flow radio group.
     *
     * @param context the context
     */
    public FlowRadioGroup(Context context) {
        this(context, null);
    }

    /**
     * Instantiates a new Flow radio group.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public FlowRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
    }

    /**
     * 获取选中按钮的索引,从开始, 未选中返回 -1
     *
     * @return the checked radio button index
     */
    public int getCheckedRadioButtonIndex() {
        return indexOfChild(findViewById(getCheckedRadioButtonId()));
    }

    /**
     * 获取选中按钮的文本,未选中 返回 空字符串
     *
     * @return the checked radio button text
     */
    public String getCheckedRadioButtonText() {
        if (getCheckedRadioButtonId() == -1) {
            return "";
        }
        return ((RadioButton) findViewById(getCheckedRadioButtonId())).getText().toString();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0, height = 0;
        int lineWidth = 0, lineHeight = 0;
        int childWidth = 0, childHeight = 0;

        mAllViews = new ArrayList<>();
        mLineHeight = new ArrayList<>();

        List<View> lineViews = new ArrayList<>();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();

            childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
                mLineHeight.add(lineHeight);
                mAllViews.add(lineViews);

                lineWidth = childWidth;
                lineHeight = childHeight;
                lineViews = new ArrayList<>();
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(childHeight, lineHeight);
            }
            lineViews.add(child);

            if (i == (count - 1)) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);
        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(modeWidth == MeasureSpec.AT_MOST ? width : sizeWidth, modeHeight == MeasureSpec.AT_MOST ? height : sizeHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = getPaddingTop();//开始布局子view的 top距离
        int left = getPaddingLeft();//开始布局子view的 left距离

        int lineNum = mAllViews.size();//行数
        List<View> lineView;
        int lineHeight;
        for (int i = 0; i < lineNum; i++) {
            lineView = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);

            for (int j = 0; j < lineView.size(); j++) {
                View child = lineView.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();
                int ld = left + params.leftMargin;
                int td = top + params.topMargin;
                int rd = ld + child.getMeasuredWidth();//不需要加上 params.rightMargin,
                int bd = td + child.getMeasuredHeight();//不需要加上 params.bottomMargin, 因为在 onMeasure , 中已经加在了 lineHeight 中
                child.layout(ld, td, rd, bd);

                left += child.getMeasuredWidth() + params.leftMargin + params.rightMargin;//因为在 这里添加了;
            }

            left = getPaddingLeft();
            top += lineHeight;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        check(getChildAt(0).getId());//默认按钮
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, getCheckedRadioButtonIndex());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setCheckedStateForView(ss.checkIndex, true);
    }

    private void setCheckedStateForView(int checkIndex, boolean checked) {
        View checkedView = getChildAt(checkIndex);
        if (checkedView != null && checkedView instanceof RadioButton) {
            ((RadioButton) checkedView).setChecked(checked);
        }
    }

    /**
     * The type Saved state.
     */
    public static class SavedState extends BaseSavedState {

        /**
         * The constant CREATOR.
         */
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        private int checkIndex;//选中按钮的索引

        /**
         * Instantiates a new Saved state.
         *
         * @param parcel     the parcel
         * @param checkIndex the check index
         */
        public SavedState(Parcelable parcel, int checkIndex) {
            super(parcel);
            this.checkIndex = checkIndex;
        }

        private SavedState(Parcel in) {
            super(in);
            checkIndex = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(checkIndex);
        }
    }
}
