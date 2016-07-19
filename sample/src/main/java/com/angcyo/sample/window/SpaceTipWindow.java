package com.angcyo.sample.window;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.angcyo.sample.R;

/**
 * Created by robi on 2016-07-19 13:12.
 */
public class SpaceTipWindow extends RecordFailedWindow {

    public static SpaceTipWindow sSpaceTipWindow;

    public SpaceTipWindow(Context context) {
        super(context);
    }

    public static SpaceTipWindow showTip(Context context, String content) {
        if (sSpaceTipWindow == null) {
            sSpaceTipWindow = new SpaceTipWindow(context);
            sSpaceTipWindow.setContent(content);
            sSpaceTipWindow.show();
        } else if (sSpaceTipWindow.isShow()) {
            sSpaceTipWindow.setContent(content);
            sSpaceTipWindow.updateLayout();
        }

        return sSpaceTipWindow;
    }

    @Override
    protected void initView() {
        mBaseViewHolder.v(R.id.okButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
//                setContent("onClick");
//                updateLayout();
            }
        });

        updateLayout();

//        mBaseViewHolder.tV(R.id.title).setTextColor(Color.WHITE);
//        mBaseViewHolder.tV(R.id.content).setTextColor(Color.WHITE);
//        mBaseViewHolder.tV(R.id.okButton).setTextColor(Color.WHITE);
    }

    @Override
    protected int getWindowWidth() {
        return -2;
    }

    @Override
    protected int getGravity() {
        return Gravity.CENTER;
    }
}
