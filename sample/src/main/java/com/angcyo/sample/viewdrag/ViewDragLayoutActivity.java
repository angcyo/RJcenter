package com.angcyo.sample.viewdrag;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;

public class ViewDragLayoutActivity extends RBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_view_drag_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mViewHolder.v(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "button1", Snackbar.LENGTH_LONG).show();
            }
        });
        mViewHolder.v(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "button2", Snackbar.LENGTH_LONG).show();
            }
        });
        mViewHolder.v(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "button3", Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
