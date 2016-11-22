package com.rsen.github.pinchimageviewpager;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.rsen.util.AnimUtil;
import com.rsen.util.ResUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：图片查看器
 * 创建人员：Robi
 * 创建时间：2016/10/10 13:52
 * 修改人员：Robi
 * 修改时间：2016/10/10 13:52
 * 修改备注：
 * Version: 1.0.0
 */
public class ImagePagerActivity extends AppCompatActivity {
    public static final String KEY_START_POS = "start_pos";
    public static final String KEY_START_X = "start_x";
    public static final String KEY_START_Y = "start_y";
    public static final String KEY_START_W = "start_w";
    public static final String KEY_START_H = "start_h";
    public static final String KEY_PHOTO_LIST = "photo_list";

    protected ArrayList<String> mPhotoList;
    protected PinchImageViewPager mViewPager;
    protected PinchCircleIndicator mCircleIndicator;

    protected long ANIM_TIME = 300;
    protected ValueAnimator mValueAnimator;

    //开始时的位置(ViewPager的位置)
    protected int mStartPosition;
    //动画开始 坐标x,y 和宽高
    protected int mStartX, mStartY, mStartW, mStartH;
    private boolean isToFinish = false;
    private RelativeLayout mRootLayout;

    public static void launcher(Activity activity, int position, ArrayList<String> values) {
        Intent intent = new Intent(activity, ImagePagerActivity.class);
        Bundle args = new Bundle();
        args.putInt(KEY_START_POS, position);
        args.putStringArrayList(KEY_PHOTO_LIST, values);
        intent.putExtras(args);
        activity.startActivity(intent);
    }

    public static void launcher(Activity activity, View view, int position, String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }

        ArrayList<String> values = new ArrayList<>();
        if (value.startsWith("http")) {
            values.add(value);
        } else {
            values.add("http://" + value);
        }

        launcher(activity, view, position, values);
    }

    /**
     * 图片地址 需要是全路径
     */
    public static void launcher(Activity activity, View view, int position, ArrayList<String> values) {
        Intent intent = new Intent(activity, ImagePagerActivity.class);
        Bundle args = new Bundle();
        args.putInt(KEY_START_POS, Math.min(values.size(), Math.max(0, position)));

        int[] rt = new int[2];
        view.getLocationOnScreen(rt);

        float w = view.getMeasuredWidth();
        float h = view.getMeasuredHeight();

        float x = rt[0] + w / 2;
        float y = rt[1] + h / 2;

        args.putInt(KEY_START_X, rt[0]);
        args.putInt(KEY_START_Y, rt[1]);
        args.putInt(KEY_START_W, (int) w);
        args.putInt(KEY_START_H, (int) h);
        args.putStringArrayList(KEY_PHOTO_LIST, values);

        intent.putExtras(args);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootLayout = new RelativeLayout(this);
        mViewPager = new PinchImageViewPager(this);
        mCircleIndicator = new PinchCircleIndicator(this);

        RelativeLayout.LayoutParams indicatorParams = new RelativeLayout.LayoutParams(-2, -2);
        indicatorParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        indicatorParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        indicatorParams.setMargins(0, 0, 0, (int) ResUtil.dpToPx(getResources(), 50));

        mRootLayout.addView(mViewPager, new ViewGroup.LayoutParams(-1, -1));
        mRootLayout.addView(mCircleIndicator, indicatorParams);

        setContentView(mRootLayout, new ViewGroup.LayoutParams(-1, -1));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        final Bundle extras = getIntent().getExtras();
        mStartPosition = extras.getInt(KEY_START_POS);
        mPhotoList = extras.getStringArrayList(KEY_PHOTO_LIST);
        mStartX = extras.getInt(KEY_START_X);
        mStartY = extras.getInt(KEY_START_Y);
        mStartW = extras.getInt(KEY_START_W);
        mStartH = extras.getInt(KEY_START_H);

        initWindow();
        initView();
        initViewEvent();
    }


    protected void initView() {

        ImageAdapter imageAdapter = new ImageAdapter();
        mViewPager.setAdapter(imageAdapter);
        mCircleIndicator.setViewPager(mViewPager);

        mViewPager.setCurrentItem(mStartPosition);

        mViewPager.addOnPageChangeListener(new PinchImageViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                PinchImageViewPager.ItemInfo curItemInfo = mViewPager.getCurItemInfo();
                if (curItemInfo != null) {
                    ViewGroup viewGroup = (ViewGroup) curItemInfo.object;
                    mViewPager.setMainPinchImageView((PinchImageView) viewGroup.getChildAt(0));
                }
            }
        });
    }

    private void initWindow() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    protected void initViewEvent() {
        startAnimation();
    }

    private void startAnimation() {
        mCircleIndicator.setVisibility(View.GONE);
        mValueAnimator = AnimUtil.startArgb(mRootLayout, Color.TRANSPARENT, Color.BLACK, ANIM_TIME);
//        final ViewGroup.LayoutParams layoutParams = mViewPager.getLayoutParams();
//        layoutParams.width = mStartW;
//        layoutParams.height = mStartH;
//        mViewPager.setLayoutParams(layoutParams);
        final int screenWidth = ResUtil.getScreenWidth(this);
        final int screenHeight = ResUtil.getScreenHeight(this);
        mViewPager.setX(mStartX + mStartW / 2 - screenWidth / 2);
        mViewPager.setY(mStartY + mStartH / 2 - screenHeight / 2);
        mViewPager.setScaleX((mStartW + 0f) / screenWidth);
        mViewPager.setScaleY((mStartH + 0f) / screenHeight);
        mViewPager.animate().x(0).y(0).scaleX(1).scaleY(1).setInterpolator(new DecelerateInterpolator()).setDuration(ANIM_TIME).start();
        mViewPager.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mPhotoList.size() > 1) {
                    mCircleIndicator.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void animToFinish() {
        if (isToFinish) {
            return;
        }
        isToFinish = true;
        mCircleIndicator.setVisibility(View.GONE);
        AnimUtil.startArgb(mRootLayout, Color.BLACK, Color.TRANSPARENT, ANIM_TIME);
        ViewCompat.animate(mViewPager).alpha(0).scaleX(0.2f).scaleY(0.2f)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(ANIM_TIME)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        isToFinish = false;
                        finish();
                    }
                }).start();
    }

    @Override
    public void onBackPressed() {
        animToFinish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mValueAnimator.cancel();
        mViewPager.animate().cancel();
    }

    class ImageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPhotoList == null ? 0 : mPhotoList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            RelativeLayout layout = new RelativeLayout(ImagePagerActivity.this);
            PinchImageView imageView = new PinchImageView(ImagePagerActivity.this);
            Picasso.with(ImagePagerActivity.this).load(mPhotoList.get(position)).into(imageView);
            layout.addView(imageView, new RelativeLayout.LayoutParams(-1, -1));
            container.addView(layout, new ViewGroup.LayoutParams(-1, -1));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    animToFinish();
                }
            });
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
