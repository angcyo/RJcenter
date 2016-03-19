package com.rsen.angcyo;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;

import com.angcyo.rsen.R;
import com.rsen.base.RBaseDialogFragment;
import com.rsen.util.ResUtil;

/**
 * 标准的对话框
 * Created by angcyo on 16-01-31-031.
 */
public class CommonDialogFragment extends RBaseDialogFragment implements View.OnClickListener {
    public static final String KEY_LISTENER = "listener";
    private AppCompatTextView titleView, msgView;//标题,内容
    private AppCompatButton negativeButton;//消极的按钮
    private AppCompatButton neutralButton;//中立的按钮
    private AppCompatButton positiveButton;//积极的按钮
    private OnButtonClickListener clickListener;

    public static CommonDialogFragment launch(FragmentManager manager, String title, String msg) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_MSG, msg);
        CommonDialogFragment fragment = new CommonDialogFragment();
        fragment.setArguments(bundle);
        fragment.show(manager, CommonDialogFragment.class.getSimpleName());
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.rsen_common_dialog_fragment;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleView = (AppCompatTextView) mViewHolder.v(R.id.title);
        msgView = (AppCompatTextView) mViewHolder.v(R.id.msg);
        negativeButton = (AppCompatButton) mViewHolder.v(R.id.negative_button);
        neutralButton = (AppCompatButton) mViewHolder.v(R.id.neutral_button);
        positiveButton = (AppCompatButton) mViewHolder.v(R.id.positive_button);
        neutralButton.setVisibility(View.GONE);//默认不显示中立按钮

        initPositiveBg();
        initOtherBg();

        positiveButton.setOnClickListener(this);
        neutralButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);

        if (TextUtils.isEmpty(mDialogTitle)) {
            titleView.setVisibility(View.GONE);
        } else {
            titleView.setText(mDialogTitle);
        }
        if (TextUtils.isEmpty(mDialogMsg)) {
            msgView.setVisibility(View.GONE);
        } else {
            msgView.setText(mDialogMsg);
        }

        if (savedInstanceState != null) {
            clickListener = savedInstanceState.getParcelable(KEY_LISTENER);
        }
    }

    public CommonDialogFragment setClickListener(OnButtonClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
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

    /**
     * 生成一个具有透明度的颜色
     */
    private int generatePressColor(@ColorInt int defaultColor) {
        return Color.parseColor("#20000000");
//        return Color.argb(0xBB, Color.red(defaultColor), Color.green(defaultColor), Color.blue(defaultColor));
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

        RoundRectShape rectShape2 = new RoundRectShape(new float[]{round, round, round, round, round, round, round, round}, null, null);
        ShapeDrawable pressDrawable = new ShapeDrawable(rectShape2);
        pressDrawable.getPaint().setColor(pressColor);
        LayerDrawable pressLayer = new LayerDrawable(new Drawable[]{defaultDrawable, pressDrawable});

        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressLayer);
        stateListDrawable.addState(new int[]{}, defaultDrawable);
        return stateListDrawable;
    }

    @Override
    public void onClick(View v) {
        if (clickListener == null) {
            return;
        }
        int id = v.getId();
        boolean isDismiss = false;
        if (id == R.id.positive_button) {
            isDismiss = clickListener.onPositiveButtonClick(v);
        } else if (id == R.id.negative_button) {
            isDismiss = clickListener.onNegativeButtonClick(v);
        } else if (id == R.id.neutral_button) {//中立
            isDismiss = clickListener.onNeutralButtonClick(v);
        }

        if (isDismiss) {
            dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_LISTENER, clickListener);
        super.onSaveInstanceState(outState);
    }

    public interface OnButtonClickListener extends Parcelable {
        boolean onPositiveButtonClick(View view);//积极的按钮

        boolean onNegativeButtonClick(View view);//消极的按钮

        boolean onNeutralButtonClick(View view);//中立的按钮
    }

    public static class SimpleButtonClickListener implements OnButtonClickListener {

        public static final Creator<SimpleButtonClickListener> CREATOR = new Creator<SimpleButtonClickListener>() {
            public SimpleButtonClickListener createFromParcel(Parcel source) {
                return new SimpleButtonClickListener(source);
            }

            public SimpleButtonClickListener[] newArray(int size) {
                return new SimpleButtonClickListener[size];
            }
        };

        public SimpleButtonClickListener() {
        }

        protected SimpleButtonClickListener(Parcel in) {
        }

        @Override
        public boolean onPositiveButtonClick(View view) {
            return true;
        }

        @Override
        public boolean onNegativeButtonClick(View view) {
            return true;
        }

        @Override
        public boolean onNeutralButtonClick(View view) {
            return true;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }
    }
}
