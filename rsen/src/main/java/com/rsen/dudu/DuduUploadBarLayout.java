package com.rsen.dudu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.angcyo.rsen.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by angcyo on 2016-04-17 09:01.
 */
public class DuduUploadBarLayout extends RelativeLayout {

    public static final int STATE_NORMAL = 0;//正常状态
    public static final int STATE_UPING = 1;//上传中
    public static final int STATE_FINISH = 2;//上传完成

    private int upState = 0;

    private List<ViewGroup> childList;//保存3个状态的ViewGroup
    private Animation exitAnim, enterAnim;//切换View时,退出和进入的动画

    private Drawable normalDrawable;
    private Drawable cancelDrawable;
    private Drawable finishDrawable;
    private List<Drawable> drawableList;
    private String upText;
    private String finishText;
    private DuduFAImageView faImageView;
    private long ANIM_TIME = 300;

    public DuduUploadBarLayout(Context context) {
        this(context, null);
    }

    public DuduUploadBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DuduUploadBarLayout);
        normalDrawable = typedArray.getDrawable(R.styleable.DuduUploadBarLayout_normalDrawable);
        cancelDrawable = typedArray.getDrawable(R.styleable.DuduUploadBarLayout_cancelDrawable);
        finishDrawable = typedArray.getDrawable(R.styleable.DuduUploadBarLayout_finishDrawable);
        upText = typedArray.getString(R.styleable.DuduUploadBarLayout_upText);
        finishText = typedArray.getString(R.styleable.DuduUploadBarLayout_finishText);
        typedArray.recycle();
        init();
    }

    private void init() {
        if (TextUtils.isEmpty(upText)) {
            upText = "正在上传至APP";
        }
        if (TextUtils.isEmpty(finishText)) {
            finishText = "上传完成";
        }

        initAnim();

        childList = new ArrayList<>();
        ViewGroup normal = makeNormalLayout();
        ViewGroup upload = makeUpingLayout();
        ViewGroup finish = makeFinishLayout();

        childList.add(normal);
        childList.add(upload);
        childList.add(finish);

        addView(normal);
        addView(upload);
        addView(finish);

        updateState(STATE_NORMAL);
    }

    private void initAnim() {
        AnimationSet exitSet = new AnimationSet(true);
        exitSet.setDuration(ANIM_TIME);
        exitSet.setInterpolator(new AccelerateInterpolator());
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, -1f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0.2f);
        exitSet.addAnimation(translateAnimation);
        exitSet.addAnimation(alphaAnimation);
        exitSet.setFillAfter(true);

        AnimationSet enterSet = new AnimationSet(true);
        enterSet.setDuration(ANIM_TIME);
        enterSet.setInterpolator(new AccelerateInterpolator());
        TranslateAnimation translateAnimation2 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
        AlphaAnimation alphaAnimation2 = new AlphaAnimation(0.2f, 1f);
        enterSet.addAnimation(translateAnimation2);
        enterSet.addAnimation(alphaAnimation2);
        enterSet.setFillAfter(true);


        exitAnim = exitSet;
        enterAnim = enterSet;
    }

    public void updateState(int newState) {
        if (newState == upState) {
            setUpState(newState);
        } else {
            if (onUploadChangeListener != null) {
                onUploadChangeListener.onStateChange(this, upState, newState);
            }
            int old = upState;
            upState = newState;
            showChild(upState);
            animToView(old, newState);
        }
    }

    private void showChild(int index) {
        for (int i = 0; i < childList.size(); i++) {
            childList.get(i).setVisibility(i == index ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void onUpClick(View view) {
        if (onUploadChangeListener != null) {
            onUploadChangeListener.onUploadClick(this, view);
        }
        updateState(STATE_UPING);
    }

    private void animToView(int from, int to) {
        if (from < 0 || to < 0 || from == to) {
            return;
        }
        startAnim(childList.get(from), childList.get(to));
    }

    private void onCancelClick(View view) {
        if (onUploadChangeListener != null) {
            onUploadChangeListener.onCancelClick(this, view);
        }
        updateState(STATE_NORMAL);
    }

    public void setUpState(@State int state) {
        showChild(state);
    }

    private void startAnim(final View exitView, View enterView) {
        exitView.startAnimation(exitAnim);
        enterView.startAnimation(enterAnim);
    }

    public void addFrame(List<Drawable> drawable) {
        drawableList = drawable;
        if (faImageView != null) {
            faImageView.addFrame(drawableList);
        }
    }

    private ViewGroup makeNormalLayout() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(makeLayoutParams());

        final View view = new View(getContext());
        view.setLayoutParams(new LinearLayout.LayoutParams(0, 1, 1));

        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(normalDrawable);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(-2, -1));
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpClick(v);
            }
        });

        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(view);
        linearLayout.addView(imageView);

        return linearLayout;
    }

    private ViewGroup makeUpingLayout() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setLayoutParams(makeLayoutParams());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        faImageView = new DuduFAImageView(getContext());
        faImageView.addFrame(drawableList);
        faImageView.setLayoutParams(new LinearLayout.LayoutParams(-2, -1));

        TextView textView = new TextView(getContext());
        textView.setText(upText);
        textView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        textView.setTextColor(Color.WHITE);
        textView.setPadding(10, 0, 10, 0);

        ImageView cancelImageView = new ImageView(getContext());
        cancelImageView.setImageDrawable(cancelDrawable);
        cancelImageView.setLayoutParams(new LinearLayout.LayoutParams(-2, -1));
        cancelImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClick(v);
            }
        });

        linearLayout.addView(faImageView);
        linearLayout.addView(textView);
        linearLayout.addView(cancelImageView);

        return linearLayout;
    }

    private ViewGroup makeFinishLayout() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setLayoutParams(makeLayoutParams());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView = new TextView(getContext());
        textView.setText(finishText);
        textView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        textView.setTextColor(Color.WHITE);
        textView.setPadding(10, 0, 10, 0);

        ImageView cancelImageView = new ImageView(getContext());
        cancelImageView.setImageDrawable(finishDrawable);
        cancelImageView.setLayoutParams(new LinearLayout.LayoutParams(-2, -1));

        linearLayout.addView(textView);
        linearLayout.addView(cancelImageView);
        return linearLayout;
    }

    private LayoutParams makeLayoutParams() {
        return new LayoutParams(-1, -1);
    }


    @IntDef({STATE_NORMAL, STATE_UPING, STATE_FINISH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    OnUploadChangeListener onUploadChangeListener;

    public void setOnUploadChangeListener(OnUploadChangeListener onUploadChangeListener) {
        this.onUploadChangeListener = onUploadChangeListener;
    }

    public interface OnUploadChangeListener {
        void onStateChange(View view, int oldState, int newState);

        void onUploadClick(View view, View uploadView);

        void onCancelClick(View view, View cancelView);
    }
}
