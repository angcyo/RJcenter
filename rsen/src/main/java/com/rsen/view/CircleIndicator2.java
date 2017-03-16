package com.rsen.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.angcyo.rsen.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 标准的 跟着滑动 移动的圆圈指示器
 * Created by Kevin on 2016/4/14.
 */
public class CircleIndicator2 extends View {
    //default value
    private final int DEFAULT_INDICATOR_RADIUS = 10;
    private final int DEFAULT_INDICATOR_MARGIN = 40;
    private final int DEFAULT_INDICATOR_BACKGROUND = Color.BLUE;
    private final int DEFAULT_INDICATOR_SELECTED_BACKGROUND = Color.RED;
    private final int DEFAULT_INDICATOR_LAYOUT_GRAVITY = Gravity.CENTER.ordinal();
    private final int DEFAULT_INDICATOR_MODE = Mode.SOLO.ordinal();
    private ViewPager viewPager;
    private List<ShapeHolder> tabItems;
    private ShapeHolder movingItem;
    //config list
    private int mCurItemPosition;
    private float mCurItemPositionOffset;
    private float mIndicatorRadius;
    private float mIndicatorMargin;
    private int mIndicatorBackground;
    private int mIndicatorSelectedBackground;
    private Gravity mIndicatorLayoutGravity;
    private Mode mIndicatorMode;

    public CircleIndicator2(Context context) {
        super(context);
        init(context, null);
    }

    public CircleIndicator2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleIndicator2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        tabItems = new ArrayList<>();
        handleTypedArray(context, attrs);
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null)
            return;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleIndicator);
        mIndicatorRadius = typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_radius, DEFAULT_INDICATOR_RADIUS);
        mIndicatorMargin = typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_margin, DEFAULT_INDICATOR_MARGIN);
        mIndicatorBackground = typedArray.getColor(R.styleable.CircleIndicator_ci_background, DEFAULT_INDICATOR_BACKGROUND);
        mIndicatorSelectedBackground = typedArray.getColor(R.styleable.CircleIndicator_ci_selected_background, DEFAULT_INDICATOR_SELECTED_BACKGROUND);
        int gravity = typedArray.getInt(R.styleable.CircleIndicator_ci_gravity, DEFAULT_INDICATOR_LAYOUT_GRAVITY);
        mIndicatorLayoutGravity = Gravity.values()[gravity];
        int mode = typedArray.getInt(R.styleable.CircleIndicator_ci_mode, DEFAULT_INDICATOR_MODE);
        mIndicatorMode = Mode.values()[mode];
        typedArray.recycle();
    }

    public void setViewPager(final ViewPager viewPager) {
        this.viewPager = viewPager;
        createTabItems();
        createMovingItem();
        setUpListener();
    }

    private void setUpListener() {
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (mIndicatorMode != Mode.SOLO) {
                    trigger(position, positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (mIndicatorMode == Mode.SOLO) {
                    trigger(position, 0);
                }
            }
        });
    }

    /**
     * trigger to redraw the indicator when the ViewPager's selected item changed!
     *
     * @param position
     * @param positionOffset
     */
    private void trigger(int position, float positionOffset) {
        this.mCurItemPosition = position;
        this.mCurItemPositionOffset = positionOffset;
//        Log.e("CircleIndicator", "onPageScrolled()" + position + ":" + positionOffset);
        requestLayout();
        invalidate();
    }

    private void createTabItems() {
        tabItems.clear();//fix bug
        for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
            OvalShape circle = new OvalShape();
            ShapeDrawable drawable = new ShapeDrawable(circle);
            ShapeHolder shapeHolder = new ShapeHolder(drawable);
            Paint paint = drawable.getPaint();
            paint.setColor(mIndicatorBackground);
            paint.setAntiAlias(true);
            shapeHolder.setPaint(paint);
            tabItems.add(shapeHolder);
        }
    }

    private void createMovingItem() {
        OvalShape circle = new OvalShape();
        ShapeDrawable drawable = new ShapeDrawable(circle);
        movingItem = new ShapeHolder(drawable);
        Paint paint = drawable.getPaint();
        paint.setColor(mIndicatorSelectedBackground);
        paint.setAntiAlias(true);

        switch (mIndicatorMode) {
            case INSIDE:
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
                break;
            case OUTSIDE:
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                break;
            case SOLO:
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                break;
        }

        movingItem.setPaint(paint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        Log.e("CircleIndicator","onLayout()");
        super.onLayout(changed, left, top, right, bottom);
        final int width = getWidth();
        final int height = getHeight();
        layoutTabItems(width, height);
        layoutMovingItem(mCurItemPosition, mCurItemPositionOffset);
    }

    private void layoutTabItems(final int containerWidth, final int containerHeight) {
        if (tabItems == null) {
            throw new IllegalStateException("forget to create tabItems?");
        }
        final float yCoordinate = containerHeight * 0.5f;
        final float startPosition = startDrawPosition(containerWidth);
        for (int i = 0; i < tabItems.size(); i++) {
            ShapeHolder item = tabItems.get(i);
            item.resizeShape(2 * mIndicatorRadius, 2 * mIndicatorRadius);
            item.setY(yCoordinate - mIndicatorRadius);
            float x = startPosition + (mIndicatorMargin + mIndicatorRadius * 2) * i;
            item.setX(x);
        }

    }

    private float startDrawPosition(final int containerWidth) {
        if (mIndicatorLayoutGravity == Gravity.LEFT)
            return 0;
        float tabItemsLength = tabItems.size() * (2 * mIndicatorRadius + mIndicatorMargin) - mIndicatorMargin;
        if (containerWidth < tabItemsLength) {
            return 0;
        }
        if (mIndicatorLayoutGravity == Gravity.CENTER) {
            return (containerWidth - tabItemsLength) / 2;
        }
        return containerWidth - tabItemsLength;
    }

    private void layoutMovingItem(final int position, final float positionOffset) {
        if (movingItem == null) {
            throw new IllegalStateException("forget to create movingItem?");
        }
        ShapeHolder item = tabItems.get(position);
        movingItem.resizeShape(item.getWidth(), item.getHeight());
        float x = item.getX() + (mIndicatorMargin + mIndicatorRadius * 2) * positionOffset;
        movingItem.setX(x);
        movingItem.setY(item.getY());

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Log.e("CircleIndicator", "onDraw()");
        super.onDraw(canvas);
        int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null,
                Canvas.MATRIX_SAVE_FLAG |
                        Canvas.CLIP_SAVE_FLAG |
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                        Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                        Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        for (ShapeHolder item : tabItems) {
            canvas.save();
            canvas.translate(item.getX(), item.getY());
            item.getShape().draw(canvas);
            canvas.restore();
        }

        if (movingItem != null) {
            canvas.save();
            canvas.translate(movingItem.getX(), movingItem.getY());
            movingItem.getShape().draw(canvas);
            canvas.restore();
        }
        canvas.restoreToCount(sc);
    }

    /*****************************
     * 对外提供在代码中设置属性的方法
     **********************************/
    public void setIndicatorRadius(float mIndicatorRadius) {
        this.mIndicatorRadius = mIndicatorRadius;
    }

    public void setIndicatorMargin(float mIndicatorMargin) {
        this.mIndicatorMargin = mIndicatorMargin;
    }

    public void setIndicatorBackground(int mIndicatorBackground) {
        this.mIndicatorBackground = mIndicatorBackground;
    }

    public void setIndicatorSelectedBackground(int mIndicatorSelectedBackground) {
        this.mIndicatorSelectedBackground = mIndicatorSelectedBackground;
    }

    public void setIndicatorLayoutGravity(Gravity mIndicatorLayoutGravity) {
        this.mIndicatorLayoutGravity = mIndicatorLayoutGravity;
    }

    public void setIndicatorMode(Mode mIndicatorMode) {
        this.mIndicatorMode = mIndicatorMode;
    }

    public enum Gravity {
        LEFT,
        CENTER,
        RIGHT
    }

    public enum Mode {
        INSIDE,
        OUTSIDE,
        SOLO
    }

    /**
     * 圆点特性控制中心
     */
    class ShapeHolder {
        private float x = 0, y = 0;//圆的x、y坐标
        private ShapeDrawable shape;
        private int color;
        private float alpha = 1f;
        private Paint paint;

        public ShapeHolder(ShapeDrawable s) {
            shape = s;
        }

        public Paint getPaint() {
            return paint;
        }

        public void setPaint(Paint value) {
            paint = value;
        }

        public float getX() {
            return x;
        }

        public void setX(float value) {
            x = value;
        }

        public float getY() {
            return y;
        }

        public void setY(float value) {
            y = value;
        }

        public ShapeDrawable getShape() {
            return shape;
        }

        public void setShape(ShapeDrawable value) {
            shape = value;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int value) {
            shape.getPaint().setColor(value);
            color = value;

        }

        public void setAlpha(float alpha) {
            this.alpha = alpha;
            shape.setAlpha((int) ((alpha * 255f) + .5f));
        }

        public float getWidth() {
            return shape.getShape().getWidth();
        }

        public void setWidth(float width) {
            Shape s = shape.getShape();
            s.resize(width, s.getHeight());
        }

        public float getHeight() {
            return shape.getShape().getHeight();
        }

        public void setHeight(float height) {
            Shape s = shape.getShape();
            s.resize(s.getWidth(), height);
        }

        public void resizeShape(final float width, final float height) {
            shape.getShape().resize(width, height);
        }
    }
}
