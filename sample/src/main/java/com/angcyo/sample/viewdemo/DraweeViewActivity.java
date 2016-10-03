package com.angcyo.sample.viewdemo;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.angcyo.sample.R;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rsen.base.RBaseActivity;
import com.rsen.util.AnimUtil;
import com.rsen.util.T;

public class DraweeViewActivity extends RBaseActivity {

    static String url = "http://mvimg1.meitudata.com/559e50975a5039607.jpg";
//    final String url = "res://" + getPackageName() + "/" + R.mipmap.ic_launcher;

    @Override
    protected int getContentView() {
        return R.layout.content_drawee_view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) mViewHolder.v(R.id.drawee_view);
        url = "res://" + getPackageName() + "/" + R.mipmap.ic_launcher;
        simpleDraweeView.setImageURI(Uri.parse(url));

        final GenericDraweeHierarchy hierarchy = simpleDraweeView.getHierarchy();
        hierarchy.setProgressBarImage(R.mipmap.progress_bar);
        hierarchy.setFailureImage(R.mipmap.fail_image);
        hierarchy.setPlaceholderImage(R.mipmap.placeholder_image);
        hierarchy.setRetryImage(R.mipmap.retry_image);

        mFab.setVisibility(View.VISIBLE);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.show(DraweeViewActivity.this, "测试. .. .");
                simpleDraweeView.setImageURI(Uri.parse(url + "123"));
            }
        });

        AnimUtil.startArgb(mAppbarLayout, Color.WHITE, getResources().getColor(R.color.colorAccent));
    }
}
