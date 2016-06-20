package com.rsen.github;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.angcyo.rsen.R;

import java.util.ArrayList;

/**
 * Created by james on 14/12/15.
 *
 * https://github.com/xujinyang/GiftRainView
 */
public class GiftRainView extends View {

    private int duration;

    private int[] imgs;
    private int count;
    private int background;
    private int speed;
    private float maxSize;
    private float minSize;

    private ArrayList<Gift> giftList;
    private Matrix m = new Matrix();
    private ValueAnimator animator;
    private long startTime, prevTime;
    public static SparseArray<Bitmap> bitmapArray = new SparseArray<>();

    private boolean isDelyStop;

    public GiftRainView(Context context) {
        super(context, null);
        init();
    }

    public GiftRainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode())
            return;

        if (null == attrs) {
            throw new IllegalArgumentException("Attributes should be provided to this view,");
        }
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DropDownStyle);
        count = typedArray.getInt(R.styleable.DropDownStyle_gift_count, 20);
        speed = typedArray.getInt(R.styleable.DropDownStyle_gift_speed, 100);
        minSize = typedArray.getFloat(R.styleable.DropDownStyle_gift_min_size, 0.5f);
        maxSize = typedArray.getFloat(R.styleable.DropDownStyle_gift_max_size, 1.2f);
        background = typedArray.getInt(R.styleable.DropDownStyle_gift_background, android.R.color.white);
        typedArray.recycle();
        init();
    }


    private void setAnimator() {
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                long nowTime = System.currentTimeMillis();
                float secs = (float) (nowTime - prevTime) / 1000f;
                prevTime = nowTime;
                for (int i = 0; i < giftList.size(); ++i) {
                    Gift gift = giftList.get(i);

                    gift.y += (gift.speed * secs);

                    if (gift.y > getHeight()) {
                        if (isDelyStop) {
                            giftList.remove(i);
                        } else {
                            gift.y = 0 - gift.height;
                        }
                    }
                    gift.rotation = gift.rotation
                            + (gift.rotationSpeed * secs);
                }
                invalidate();
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(duration);
    }

    private void init() {
        giftList = new ArrayList<Gift>();
        animator = ValueAnimator.ofFloat(0, 1);
        setLayerType(View.LAYER_TYPE_NONE, null);
        setBackgroundColor(background);

        setAnimator();
    }

    public void setGiftCount(int quantity) {
        if (imgs == null || imgs.length == 0)
            return;
        int leftCount = Math.abs(quantity - giftList.size());
        for (int i = 0; i < leftCount; ++i) {
            Bitmap originalBitmap = BitmapFactory
                    .decodeResource(getResources(), imgs[i % imgs.length]);
            Gift gift = new Gift(getWidth(), originalBitmap, speed);
            gift.bitmap = bitmapArray.get(gift.width);
            if (gift.bitmap == null) {
                gift.bitmap = Bitmap.createScaledBitmap(originalBitmap,
                        (int) gift.width, (int) gift.height, true);
                bitmapArray.put(gift.width, gift.bitmap);
            }
            giftList.add(gift);
        }
    }


    /**
     * 将屏幕中的动画显示完成后，
     */
    public void stopRainDely() {
        this.isDelyStop = true;
    }

    /**
     * 立刻马上情况画布中所有的东西
     */
    public void stopRainNow() {
        giftList.clear();
        invalidate();
        animator.cancel();
    }


    /**
     * 重新开始显示动画
     */
    public void startRain() {
        this.isDelyStop = false;
        setGiftCount(count);
        animator.start();
    }

    /**
     * 设置要下落的物体
     *
     * @param images
     */
    public void setImages(int... images) {
        imgs = images;
        setGiftCount(count);
    }

    public void setSpeed(int speed) {
        for (Gift gift : giftList) {
            gift.setSpeed(speed);
        }
    }


    public void cutGiftCount(int quantity) {
        if (quantity > giftList.size())
            return;
        for (int i = 0; i < quantity; ++i) {
            giftList.remove(i);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        giftList.clear();
        setGiftCount(count);
        startTime = System.currentTimeMillis();
        prevTime = startTime;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < giftList.size(); ++i) {
            Gift gift = giftList.get(i);
            m.setTranslate(-gift.width / 2, -gift.height / 2);
            m.postRotate(gift.rotation);
            m.postTranslate(gift.width / 2 + gift.x, gift.height / 2
                    + gift.y);
            canvas.drawBitmap(gift.bitmap, m, null);
        }
    }

    public class Gift {
        private float x, y;
        private float rotation;
        private float speed;
        private float rotationSpeed;
        private int width, height;
        private Bitmap bitmap;

        public Gift(float xRange, Bitmap originalBitmap, int speed) {
            double widthRandom = Math.random();
            if (widthRandom < minSize || widthRandom > maxSize) {
                widthRandom = maxSize;
            }
            width = (int) (originalBitmap.getWidth() * widthRandom);
            float hwRatio = originalBitmap.getHeight() * 1.0f / originalBitmap.getWidth();
            height = (int) (width * hwRatio);
            x = (float) Math.random() * (xRange - width);
            y = 0 - (height + (float) Math.random() * height);
            this.speed = speed + (float) Math.random() * 550;
            rotation = (float) Math.random() * 180 - 90;
            rotationSpeed = (float) Math.random() * 90 - 45;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }
    }
}
