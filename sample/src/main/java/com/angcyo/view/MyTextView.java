package com.angcyo.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.angcyo.sample.R;

import java.util.Random;

/**
 * Created by angcyo on 16-01-10-010.
 */
public class MyTextView extends TextView {

    Random random;
    float mTranslate = 0f;
    private Matrix gradientMatrix;
    private Shader gradientShader;
    private Drawable drawable;

    public MyTextView(Context context) {
        super(context);
        e("1");
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        e("2");

        if (Build.VERSION.SDK_INT >= 21) {
            drawable = getResources().getDrawable(R.drawable.background_selector, getContext().getTheme());

        } else {
//            drawable = getResources().getDrawable(R.drawable.background_selector);
        }

        drawable.setCallback(this);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        e("3");
    }

    @Override
    public void invalidateDrawable(Drawable drawable) {
//        super.invalidateDrawable(drawable);
        e("invalidateDrawable");
        Rect rect = drawable.getBounds();
        invalidate(rect.left, rect.top, rect.right, rect.bottom);
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        super.scheduleDrawable(who, what, when);
        e("scheduleDrawable");

    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        super.unscheduleDrawable(who, what);        e("unscheduleDrawable");

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            drawable.setState(new int[]{android.R.attr.state_pressed});
        }

        if (action == MotionEvent.ACTION_UP) {
            drawable.setState(new int[]{});
        }

        return super.onTouchEvent(event);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        e("onDraw");
        drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        drawable.draw(canvas);

        super.onDraw(canvas);
        int viewWidth = getMeasuredWidth();
        mTranslate += viewWidth / 8;
        if (mTranslate > viewWidth / 2) {
            mTranslate = -viewWidth / 2;
        }
//        e(mTranslate + "");
        gradientMatrix.setTranslate(mTranslate, 0);
        gradientShader.setLocalMatrix(gradientMatrix);
//        postInvalidateDelayed(100);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        e("onSizeChanged");
        gradientShader = new LinearGradient(0, 0, getMeasuredWidth(), 0,
                new int[]{Color.BLUE, Color.WHITE, Color.GREEN}, null, Shader.TileMode.CLAMP);
        getPaint().setShader(gradientShader);
        gradientMatrix = new Matrix();
        random = new Random();
    }

    private void e(String msg) {
        Log.e("angcyo", msg + "");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDisplayHint(int hint) {
        super.onDisplayHint(hint);
    }

    @Override
    public boolean onPreDraw() {
        return super.onPreDraw();
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
    }

    @Override
    protected void onAnimationStart() {
        super.onAnimationStart();
    }

    @Override
    protected boolean onSetAlpha(int alpha) {
        return super.onSetAlpha(alpha);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        return super.onCreateDrawableState(extraSpace);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onScrollChanged(int horiz, int vert, int oldHoriz, int oldVert) {
        super.onScrollChanged(horiz, vert, oldHoriz, oldVert);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        super.onCreateContextMenu(menu);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        return super.onDragEvent(event);
    }

    @Override
    public boolean onHoverEvent(MotionEvent event) {
        return super.onHoverEvent(event);
    }

    @Override
    public boolean onFilterTouchEventForSecurity(MotionEvent event) {
        return super.onFilterTouchEventForSecurity(event);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
