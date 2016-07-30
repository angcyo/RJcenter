package com.angcyo.sample.viewdrag;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by robi on 2016-07-30 16:32.
 */
public class ViewDragLayout extends RelativeLayout {

    public static Logger log = LoggerFactory.getLogger(ViewDragLayout.class);

    public ViewDragLayout(Context context) {
        super(context);
    }

    public ViewDragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
