package com.angcyo.sample.viewdemo;

import android.os.Bundle;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;

public class DraweeViewActivity extends RBaseActivity {

    static final String url = "http://mvimg1.meitudata.com/559e50975a5039607.jpg";

    @Override
    protected int getContentView() {
        return R.layout.content_drawee_view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
//        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) mViewHolder.v(R.id.drawee_view);
//        simpleDraweeView.setImageURI(Uri.parse(url));
//
//        simpleDraweeView.getHierarchy().setProgressBarImage(R.mipmap.progress_bar);
    }
}
