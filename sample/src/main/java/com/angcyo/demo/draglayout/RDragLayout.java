package com.angcyo.demo.draglayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by angcyo on 2016-03-31 23:07.
 */
public class RDragLayout extends RelativeLayout {

    public static final boolean DEBUG = true;
    public static final String TAG = "RDragLayout";
    private Bitmap dragViewBitmap;//当前拖动View的Bitmap
    private ImageView dragImageView;

    public RDragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        initLayout();
    }

    private void initLayout() {
        dragImageView = new ImageView(getContext());
        dragImageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setDragView(View view) {
        if (dragViewBitmap != null) {
            dragViewBitmap.recycle();
        }
        dragViewBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dragViewBitmap);
        view.draw(canvas);
        dragImageView.setImageBitmap(dragViewBitmap);

        addView(dragImageView);
        dragView(view.getLeft() + 40, view.getTop() + 40);
    }

    private void stopDragView(float x, float y) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(dragViewBitmap);
        imageView.setX(x);
        imageView.setY(y);
        addView(imageView);

//        if (dragViewBitmap != null) {
//            dragViewBitmap.recycle();
//            dragViewBitmap = null;
//        }

        dragImageView.setImageBitmap(null);
        removeView(dragImageView);
    }

    private void startDragView(float x, float y) {

    }

    private void dragView(float x, float y) {
        dragImageView.setVisibility(VISIBLE);
        dragImageView.setX(x);
        dragImageView.setY(y);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        e("onInterceptTouchEvent");
        boolean intercept = false;
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (dragImageView.getParent() != null) {
                dragView(ev.getX(), ev.getY());
                intercept = true;
            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (dragImageView.getParent() != null) {
                stopDragView(ev.getX(), ev.getY());
                //判断是否可以放下
                //TODO: 16-03-31-031

                intercept = true;
            }
        } else if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            if (dragImageView.getVisibility() == View.INVISIBLE) {
//                startDragView(ev.getX(), ev.getY());
//                intercept = true;
//            }
        }

        return !intercept && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        e("onTouchEvent");
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        e("dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    public void e(String msg) {
        if (DEBUG) {
            Log.e(TAG, msg);
        }
    }
}
