package com.angcyo.sample.FragmentDemo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;
import com.rsen.base.RBaseDialogFragment;

public class FragmentDemoActivity extends RBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_fragment_demo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
//        mAppbarLayout.setVisibility(View.GONE);
//        mActivityLayout.removeView(mAppbarLayout);
//
//        setSupportActionBar((Toolbar) mViewHolder.v(R.id.toolbar));
//        mAppbarLayout.setFitsSystemWindows(true);
    }

    @Override
    protected void initAfter() {

    }

    public void button1(View view) {
        new RBaseDialogFragment().show(getSupportFragmentManager(), "");
    }

    public void button2(View view) {
        new TestFragment().show(getSupportFragmentManager(), "");
    }

    public void button3(View view) {

    }

    public void button4(View view) {

    }

    public static class TestFragment extends DialogFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(STYLE_NO_FRAME, getTheme());
            setStyle(STYLE_NO_TITLE, getTheme());
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


//            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            final Window window = getDialog().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
            attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
//            attributes.x = 10;
//            attributes.y = 10;
//            attributes.gravity = Gravity.TOP;
            window.setAttributes(attributes);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//            window.setLayout(300, 400);

            TextView textView = new TextView(getActivity());
            textView.setText("angcyo");
//            textView.setLayoutParams(new FrameLayout.LayoutParams(2000,2000));
            textView.setWidth(1180);
            textView.setHeight(400);
            textView.setBackgroundColor(Color.RED);

            textView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ViewGroup viewById = (ViewGroup) window.findViewById(android.R.id.content);
                    viewById.getMeasuredWidth();
                    viewById.getHeight();
                }
            }, 1000);
            try {
                container.addView(textView);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            textView.setLayoutParams(new FrameLayout.LayoutParams(-1,-1));
            return textView;
        }

    }
}
