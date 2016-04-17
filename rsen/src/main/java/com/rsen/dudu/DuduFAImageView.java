package com.rsen.dudu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angcyo on 2016-04-17 08:47.
 * 帧动画
 */
public class DuduFAImageView extends ImageView {
    private List<Drawable> drawableList;
    private Runnable anim;
    private long DELAY_TIME = 300l;
    private long index = 0;

    public DuduFAImageView(Context context) {
        super(context);
        init();
    }

    public DuduFAImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        addToList(getDrawable());
        anim = new AnimRunnable();
    }

    public void addFrame(@DrawableRes int drawable) {
        addToList(getResources().getDrawable(drawable));
    }

    public void addFrame(Drawable drawable) {
        addToList(drawable);
    }

    public void addFrame(List<Drawable> drawable) {
        drawableList = drawable;
        start();
    }

    private void addToList(Drawable drawable) {
        if (drawableList == null) {
            drawableList = new ArrayList<>();
        }
        if (drawable != null) {
            drawableList.add(drawable);
            start();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    public void start() {
        removeCallbacks(anim);
        postDelayed(anim, DELAY_TIME);
    }

    private List<Drawable> getDrawableList() {
        return drawableList;
    }

    public void stop() {
        removeCallbacks(anim);
    }

    class AnimRunnable implements Runnable {
        @Override
        public void run() {
            List<Drawable> drawableList = getDrawableList();
            if (drawableList != null && drawableList.size() > 1) {
                setImageDrawable(drawableList.get((int) (index++ % drawableList.size())));
                postDelayed(anim, DELAY_TIME);
                if (index > Integer.MAX_VALUE >> 2) {
                    index = 0;
                }
            }
        }
    }

}
