package com.angcyo.sample.animation;

import android.app.WallpaperManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angcyo.sample.R;
import com.rsen.base.RBaseDialogFragment;
import com.rsen.util.Rx;
import com.rsen.viewgroup.RectTransitionLayout;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ColorAnimationActivity extends AppCompatActivity {

    static String TAG = "angcyo";

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_animation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findViewById(R.id.content_color_animation).setBackground(WallpaperManager.getInstance(this).getDrawable());

        Rx.base(new Func1<String, Object>() {
            @Override
            public Object call(String s) {
                Log.i(TAG, "call 1: " + Thread.currentThread().getName() + " " + Thread.currentThread().getId());
                return null;
            }
        }, new Action1<Object>() {
            @Override
            public void call(Object o) {
                Log.i(TAG, "call 2: " + Thread.currentThread().getName() + " " + Thread.currentThread().getId());
            }
        });

        Observable.just(1).observeOn(Schedulers.computation())/*.subscribeOn(Schedulers.newThread())*/.map(new Func1<Integer, Object>() {
            @Override
            public Object call(Integer integer) {
                Log.i(TAG, "call 3: " + Thread.currentThread().getName() + " " + Thread.currentThread().getId());
                return null;
            }
        }).subscribeOn(Schedulers.newThread()).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                Log.i(TAG, "call 4: " + Thread.currentThread().getName() + " " + Thread.currentThread().getId());
            }
        });

        mTextView = new TextView(this);
        mTextView.setText("Test ... ");
        mTextView.setTextColor(Color.WHITE);
        mTextView.setTextSize(30);

        ((ViewGroup) findViewById(R.id.top_layout)).addView(mTextView);
    }

    public void startAnimation(View view) {
//        View animationView = findViewById(R.id.view);
//        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.RED, Color.BLUE);
//        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int color = (int) animation.getAnimatedValue();
//                animationView.setBackgroundColor(color);
//            }
//        });
//        colorAnimator.setDuration(700);
//        colorAnimator.start();
        ViewGroup topLayout = ((ViewGroup) findViewById(R.id.top_layout));
        ViewGroup bottomLayout = ((ViewGroup) findViewById(R.id.bottom_layout));
        mTextView.setText("------>new text");


        topLayout.removeView(mTextView);
        bottomLayout.addView(mTextView);

        View testView = findViewById(R.id.testView);
        topLayout.removeView(testView);
        bottomLayout.addView(testView);
    }


    public void bottomDialog(View view) {
        BottomDialog.show(getSupportFragmentManager());
    }

    public void topDialog(View view) {
        TopDialog.show(getSupportFragmentManager());
    }

    public static class TopDialog extends RBaseDialogFragment {

        public static void show(FragmentManager fragmentManager) {
            DialogFragment dialogFragment = new TopDialog();
            dialogFragment.show(fragmentManager, dialogFragment.getClass().getSimpleName());
        }

        @Override
        protected int getContentView() {
            return R.layout.content_color_dialog_fragment;
        }

        @Override
        protected void initViewEvent() {
            RectTransitionLayout transitionLayout = (RectTransitionLayout) mViewHolder.v(R.id.transitionLayout);
            transitionLayout.startClip();
        }

        @Override
        protected int getGravity() {
            return Gravity.TOP;
        }
    }

    public static class BottomDialog extends RBaseDialogFragment {

        public static void show(FragmentManager fragmentManager) {
            DialogFragment dialogFragment = new BottomDialog();
            dialogFragment.show(fragmentManager, dialogFragment.getClass().getSimpleName());
        }

        @Override
        protected int getContentView() {
            return R.layout.content_color_dialog_fragment;
        }

        @Override
        protected void initViewEvent() {
            RectTransitionLayout transitionLayout = (RectTransitionLayout) mViewHolder.v(R.id.transitionLayout);
            transitionLayout.startClip(100);
        }

        @Override
        protected int getGravity() {
            return Gravity.BOTTOM;
        }
    }

}
