package com.rsen.angcyo;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.angcyo.rsen.R;
import com.rsen.base.RBaseDialogFragment;
import com.rsen.util.ResUtil;

/**
 * Created by angcyo on 16-01-31-031.
 */
public class LoadFragment extends RBaseDialogFragment {
    public static final String KEY_TIP = "tip";
    private String tip;

    public static void launch(@NonNull FragmentManager fragmentManager, String tip) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TIP, tip);
        LoadFragment fragment = new LoadFragment();
        fragment.setArguments(bundle);
        fragment.show(fragmentManager, LoadFragment.class.getSimpleName());
    }

    @Override
    protected void initView(Bundle arguments) {
        if (arguments != null) {
            tip = arguments.getString(KEY_TIP);
        }
        if (TextUtils.isEmpty(tip)) {
            mViewHolder.v(R.id.load_tip).setVisibility(View.GONE);
        } else {
            mViewHolder.tV(R.id.load_tip).setText(tip);
        }

        initBgDrawable();//设置圆角背景
    }

    @Override
    protected int getContentView() {
        return R.layout.rsen_load_fragment_layout;
    }

    @Override
    protected int getWindowWidth() {
        return -2;
    }

    @Override
    protected int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected boolean isDimEnabled() {
        return false;
    }

    private void initBgDrawable() {
        float round = ResUtil.dpToPx(getResources(), 5);
        RoundRectShape rectShape = new RoundRectShape(new float[]{round, round, round, round, round, round, round, round}, null, null);
        ShapeDrawable bgDrawable = new ShapeDrawable(rectShape);
        bgDrawable.getPaint().setColor(Color.parseColor("#80000000"));
        ResUtil.setBgDrawable(mViewHolder.v(R.id.load_layout), bgDrawable);
    }
}
