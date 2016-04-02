package com.angcyo.demo.draglayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rsen.util.ResUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angcyo on 2016-03-31 23:07.
 */
public class RDragLayout extends RelativeLayout {

    public static final boolean DEBUG = true;
    public static final String TAG = "RDragLayout";
    private ImageView dragImageView;//当前拖拽的View
    List<Rect> gridList;//所有格子
    View[] gridArrayView;//格子内对应的View
    Paint paint;//格子画笔
    private int gridNum = 4;//横向格子数量
    DragViewClickListener dragViewClickListener;

    public RDragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        initLayout();
    }

    private void initLayout() {
        setWillNotDraw(false);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        dragViewClickListener = new DragViewClickListener();
    }

    private ImageView getImageView(Bitmap bitmap) {
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setImageBitmap(bitmap);
        imageView.setOnClickListener(dragViewClickListener);
        return imageView;
    }

    /**
     * 设置一个View,用于开始拖拽
     */
    public void setDragView(View view) {
        Bitmap dragViewBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dragViewBitmap);
        view.draw(canvas);

        dragImageView = getImageView(dragViewBitmap);

        float scale = 1.2f;
        dragImageView.setScaleX(scale);
        dragImageView.setScaleY(scale);

        addView(dragImageView);
        dragView(view.getLeft(), view.getTop());
    }

    private void removeView(int index) {
        View view = gridArrayView[index];
        if (view != null && index >= 0 && index < gridArrayView.length) {
            removeViewAnimation(view);
        }
    }

    /**
     * 停止拖拽,找到适合放下的Rect,并add View,否则remove view
     */
    private void stopDragView(float x, float y) {
        Rect rect = new Rect();
        int index = findRect(x, y, rect);
        if (index < 0) {
            removeViewAnimation(dragImageView);
        } else {
            View view = gridArrayView[index];
            if (view != null) {
                removeViewAnimation(view);
            }
            dragImageView.setScaleY(1f);
            dragImageView.setScaleX(1f);
            dragImageView.setTag(String.valueOf(index));
            dragView(rect.centerX(), rect.centerY());
            gridArrayView[index] = dragImageView;
            dragImageView = null;
        }
    }

    private void removeViewAnimation(View view) {
        getRemoveAnimation(view);
    }

    /**
     * 查找当前触摸点对应的Rect,通过rect参数返回,返回值表示格子的索引,用于判断当前格子,是否已经添加了view
     */
    private int findRect(float x, float y, Rect rect) {
        for (int i = 0; i < gridList.size(); i++) {
            Rect rt = gridList.get(i);
            if (rt.contains((int) x, (int) y)) {
                rect.set(rt);
                return i;
            }
        }

        return -1;
    }

    /**
     * 将View的中心位置,应触摸点的位置
     */
    private void dragView(float x, float y) {
        int width = dragImageView.getWidth();
        int height = dragImageView.getHeight();

        dragImageView.setX(x - width / 2);
        dragImageView.setY(y - height / 2);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        e("onInterceptTouchEvent " + ev.getAction());
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int paddingLeft = (int) ResUtil.dpToPx(getResources(), 100);

        int measuredWidth = getMeasuredWidth() - paddingLeft;
        int gridWidth = (int) Math.ceil(measuredWidth / (float) gridNum);//每个格子的宽度

        int measuredHeight = getMeasuredHeight();
        int gridHeightNum = (int) Math.ceil(measuredHeight / (float) gridWidth);//高度的个数


        Rect rect;
        gridList = new ArrayList<>();
        for (int j = 0; j < gridHeightNum; j++) {
            //纵向第几个
            for (int i = 0; i < gridNum; i++) {
                //横向第几个
                rect = new Rect(0, 0, gridWidth, gridWidth);
                rect.offsetTo(i * gridWidth + paddingLeft, j * gridWidth);
                gridList.add(rect);

            }
        }
        if (gridArrayView == null || gridArrayView.length != gridList.size()) {
            gridArrayView = new View[gridList.size()];
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < gridList.size(); i++) {
            int line = i / gridNum;//第几行
            int col = i % gridNum;//第几列

            if (line % 2 == 0 && col % 2 == 0 || line % 2 != 0 && col % 2 != 0) {
                paint.setColor(Color.GRAY);
            } else {
                paint.setColor(Color.YELLOW);
            }
            canvas.drawRect(gridList.get(i), paint);
        }

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        e("onTouchEvent " + event.getAction());
        if (dragImageView != null && dragImageView.getParent() != null) {
            //解决无法像正常情况一下拖放ImageView的BUG
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        e("dispatchTouchEvent " + ev.getAction());

        if (dragImageView != null && dragImageView.getParent() != null) {
            if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                dragView(ev.getX(), ev.getY());
            } else if (ev.getAction() == MotionEvent.ACTION_UP) {
                stopDragView(ev.getX(), ev.getY());
                //判断是否可以放下
                //TODO: 16-03-31-031
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    private Animation getRemoveAnimation(View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.2f, 1f, 0.2f,
                Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
        scaleAnimation.setDuration(300);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                removeView(view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(scaleAnimation);
        return scaleAnimation;
    }

    public void e(String msg) {
        if (DEBUG) {
            Log.e(TAG, msg);
        }
    }

    class DragViewClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            if (tag != null && tag instanceof String) {
                try {
                    int index = Integer.valueOf((String) tag);
                    e("onClick " + index);
                    removeView(index);
                } catch (Exception e) {
                }
            }

        }
    }

}
