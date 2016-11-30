package com.rsen.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.angcyo.rsen.R;


/**
 * Created by angcyo on 2016-11-20.
 */

public class ExEditText extends AppCompatEditText {

    Rect clearRect = new Rect();//删除按钮区域
    boolean isDownIn = false;//是否在按钮区域按下
    Drawable clearDrawable;

    public ExEditText(Context context) {
        super(context);
    }

    public ExEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if ("emoji".equalsIgnoreCase(String.valueOf(getTag()))) {
            final InputFilter[] filters = getFilters();
            final InputFilter[] newFilters = new InputFilter[filters.length + 1];
            System.arraycopy(filters, 0, newFilters, 0, filters.length);
            newFilters[filters.length] = new EmojiFilter();
            setFilters(newFilters);
        }
        clearDrawable = ResourcesCompat.getDrawable(
                getResources(),
                R.drawable.base_edit_delete_selector,
                getContext().getTheme());
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        checkEdit(focused);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (isFocused()) {
            checkEdit(hasWindowFocus);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        clearRect.set(w - getPaddingRight() - clearDrawable.getIntrinsicWidth(), getPaddingTop(), w - getPaddingRight(), Math.min(w, h) - getPaddingBottom());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isFocused()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                isDownIn = checkClear(event.getX(), event.getY());
                updateState(isDownIn);
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                updateState(checkClear(event.getX(), event.getY()));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                updateState(false);
                if (isDownIn && checkClear(event.getX(), event.getY())) {
                    if (!TextUtils.isEmpty(getText())) {
                        setText("");
                        return true;
                    }
                }
                isDownIn = false;
            } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                updateState(false);
                isDownIn = false;
            }
        }
        return super.onTouchEvent(event);
    }

    private void updateState(boolean isDownIn) {
        final Drawable clearDrawable = getCompoundDrawables()[2];
        if (clearDrawable == null) {
            return;
        }
        if (isDownIn) {
            clearDrawable.setState(new int[]{android.R.attr.state_checked});
        } else {
            clearDrawable.setState(new int[]{});
        }
    }

    private void checkEdit(boolean focused) {
        if (TextUtils.isEmpty(getText()) || !focused) {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, clearDrawable, null);
        }
    }

    private boolean checkClear(float x, float y) {
        return clearRect.contains(((int) x), (int) y);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        checkEdit(true);
    }
}
