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
    //    private Bitmap dragViewBitmap;//当前拖动View的Bitmap
    private ImageView dragImageView;
    List<Rect> gridList;
    View[] gridArrayView;
    Paint paint;
    private int gridNum = 4;        //横向格子数量


    public RDragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        initLayout();
    }

    private void initLayout() {
        setWillNotDraw(false);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private ImageView getImageView(Bitmap bitmap) {
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setImageBitmap(bitmap);
        imageView.setClickable(false);
        imageView.setFocusable(false);
        return imageView;
    }

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

    private void stopDragView(float x, float y) {
//        ImageView imageView = new ImageView(getContext());
//        imageView.setImageBitmap(dragViewBitmap);
//        imageView.setX(x);
//        imageView.setY(y);
//        addView(imageView);
//
////        if (dragViewBitmap != null) {
////            dragViewBitmap.recycle();
////            dragViewBitmap = null;
////        }
//
//        dragImageView.setImageBitmap(null);
//        removeView(dragImageView);
        Rect rect = new Rect();
        int index = findRect(x, y, rect);
        if (index < 0) {
            removeView(dragImageView);
        } else {
            View view = gridArrayView[index];
            if (view != null) {
                removeView(view);
            }
            dragImageView.setScaleY(1f);
            dragImageView.setScaleX(1f);
            dragView(rect.centerX(), rect.centerY());
            gridArrayView[index] = dragImageView;
        }
    }

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

    private void dragView(float x, float y) {
        int width = dragImageView.getWidth();
        int height = dragImageView.getHeight();

        dragImageView.setX(x - width / 2);
        dragImageView.setY(y - height / 2);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        e("onInterceptTouchEvent");
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (dragImageView.getParent() != null) {
                dragView(ev.getX(), ev.getY());
            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (dragImageView.getParent() != null) {
                stopDragView(ev.getX(), ev.getY());
                //判断是否可以放下
                //TODO: 16-03-31-031
            }
        } else if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            if (dragImageView.getVisibility() == View.INVISIBLE) {
//                startDragView(ev.getX(), ev.getY());
//                intercept = true;
//            }
        }

//        if (intercept) {
//            return true;
//        }
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
//        e("onTouchEvent");
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        e("dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    public void e(String msg) {
        if (DEBUG) {
            Log.e(TAG, msg);
        }
    }
}
