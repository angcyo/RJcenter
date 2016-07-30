package com.rsen.view;

import android.content.Context;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by robi on 2016-07-30 12:33.
 */
public class MyTextClock extends TextView {

    private CharSequence mFormat = "yyyy/MM/dd HH:mm:ss ";

    private Calendar mTime;
    private final Runnable mTicker = new Runnable() {
        public void run() {
            onTimeChanged();

            long now = SystemClock.uptimeMillis();
            long next = now + (1000 - now % 1000);

            getHandler().postAtTime(mTicker, next);
        }
    };
    private boolean mAttached;

    public MyTextClock(Context context) {
        super(context);
        init();
    }

    public MyTextClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        createTime("GMT+08:00");
    }

    private void createTime(String timeZone) {
        if (timeZone != null) {
            mTime = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        } else {
            mTime = Calendar.getInstance();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mAttached) {
            mAttached = true;
            mTicker.run();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mAttached) {
            getHandler().removeCallbacks(mTicker);
            mAttached = false;
        }
    }

    private void onTimeChanged() {
        mTime.setTimeInMillis(System.currentTimeMillis());
        setText(new StringBuilder(DateFormat.format(mFormat, mTime)).append(getWeakString()).toString());
    }

    private String getWeakString() {
        String result = "星期";
        switch (mTime.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                result = "星期日";
                break;
            case 2:
                result = "星期一";
                break;
            case 3:
                result = "星期二";
                break;
            case 4:
                result = "星期三";
                break;
            case 5:
                result = "星期四";
                break;
            case 6:
                result = "星期五";
                break;
            case 7:
                result = "星期六";
                break;

        }
        return result;
    }
}
