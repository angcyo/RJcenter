package com.angcyo.sample.FragmentDemo;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.angcyo.sample.R;
import com.rsen.angcyo.CommonDialogFragment;
import com.rsen.angcyo.LoadDialogFragment;
import com.rsen.base.RBaseActivity;
import com.rsen.util.L;

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
        mViewHolder.v(R.id.edit_text).setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                L.e("" + visibility);
            }
        });

        mViewHolder.v(R.id.edit_text).addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                L.e("");
            }
        });

        mViewHolder.viewGroup(R.id.root_layout).setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                L.e("");
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
                L.e("");
            }
        });

        mActivityLayout.setFitsSystemWindows(false);
    }

    @Override
    protected boolean enableStatusColor() {
        return true;
    }

    @Override
    protected void initAfter() {
    }

    public void button1(View view) {
        LoadDialogFragment.launch(getSupportFragmentManager(), "angcyo 很长很长很长很长很长很长的文字");
    }

    public void button2(View view) {
        //new TestFragment().show(getSupportFragmentManager(), "");
        CommonDialogFragment.launch(getSupportFragmentManager(), "", "").setClickListener(new CommonDialogFragment.SimpleButtonClickListener());
    }

    public void button3(View view) {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        RelativeLayout relativeLayout = new RelativeLayout(this);
//        relativeLayout.setBackgroundColor(Color.RED);
        TextView textView = new TextView(this);
        textView.setText("angcyo");
        textView.setBackgroundColor(Color.YELLOW);
//        relativeLayout.addView(textView);
        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
//        dialog.getWindow().setLayout(-1,500);

        attributes.width = -1;
        attributes.height = -2;
        dialog.getWindow().setAttributes(attributes);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(textView, new ViewGroup.LayoutParams(-2, -2));
        dialog.getWindow().setLayout(-1, -2);//setlayout 必须 在 setview 之后, 调用;并且 setBackgroundDrawable 必须设置

        dialog.show();
    }

    public void button4(View view) {
        LoadDialogFragment.launch(getSupportFragmentManager(), "");
    }


    public void button5(View view) {
        CommonDialogFragment.launch(getSupportFragmentManager(), "提示", "确定退出吗?").setClickListener(new CommonDialogFragment.SimpleButtonClickListener(){
            @Override
            public boolean onNegativeButtonClick(View view) {
                return false;
            }
        });
    }


    public void button6(View view) {
        //new TestFragment().show(getSupportFragmentManager(), "");
        CommonDialogFragment.launch(getSupportFragmentManager(), "提示", "").setClickListener(new CommonDialogFragment.SimpleButtonClickListener());
    }


    public void button7(View view) {
        //new TestFragment().show(getSupportFragmentManager(), "");
        CommonDialogFragment.launch(getSupportFragmentManager(), "", "无内容").setClickListener(new CommonDialogFragment.SimpleButtonClickListener());
    }


    public void button8(View view) {
        //new TestFragment().show(getSupportFragmentManager(), "");
        CommonDialogFragment.launch(getSupportFragmentManager(), "", "").setClickListener(new CommonDialogFragment.SimpleButtonClickListener());
    }


    public void button9(View view) {
        //new TestFragment().show(getSupportFragmentManager(), "");
        CommonDialogFragment.launch(getSupportFragmentManager(), "", "").setClickListener(new CommonDialogFragment.SimpleButtonClickListener());
    }


    public static class TestFragment extends DialogFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//            setStyle(STYLE_NO_FRAME, getTheme());
//            setStyle(STYLE_NO_TITLE, getTheme());
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final Window window = getDialog().getWindow();
//            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//            window.addFlags(Window.FEATURE_NO_TITLE);
            window.requestFeature(Window.FEATURE_NO_TITLE);
            View rootView = inflater.inflate(com.angcyo.rsen.R.layout.rsen_base_dialog_fragment_layout, ((ViewGroup) window.findViewById(android.R.id.content)));
            rootView.findViewById(R.id.fragment_layout).setBackgroundColor(Color.YELLOW);

//            window.addFlags(Window.FEATURE_NO_TITLE);

//            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

//            WindowManager.LayoutParams attributes = window.getAttributes();
//            attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
//            attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
//            attributes.x = 10;
//            attributes.y = 10;
//            attributes.gravity = Gravity.TOP;
//            window.setAttributes(attributes);
//            window.setBackgroundDrawable(new ColorDrawable(Color.BLUE));

//            window.setLayout(300, 400);

//            TextView textView = new TextView(getActivity());
//            textView.setText("angcyo");
//            textView.setLayoutParams(new FrameLayout.LayoutParams(2000,2000));
//            textView.setWidth(1180);
//            textView.setHeight(400);
//            textView.setBackgroundColor(Color.RED);
//            window.setLayout(-1,-1);
            window.setBackgroundDrawable(new ColorDrawable(Color.BLUE));

            WindowManager.LayoutParams attributes = window.getAttributes();


//            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

//           .addView(rootView);
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
//            attributes.x = 200;
//            attributes.y = 200;
            attributes.gravity = Gravity.CENTER;
            window.setAttributes(attributes);
//            window.setLayout(-1,-1);


//            textView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    ViewGroup viewById = (ViewGroup) window.findViewById(android.R.id.content);
//                    viewById.getMeasuredWidth();
//                    viewById.getHeight();
//                }
//            }, 1000);
//            try {
//                container.addView(textView);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            textView.setLayoutParams(new FrameLayout.LayoutParams(-1,-1));
            return null;
        }

    }
}
