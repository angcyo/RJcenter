package com.rsen.facebook;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.rsen.util.FastBlurUtil;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2016/11/10 14:50
 * 修改人员：Robi
 * 修改时间：2016/11/10 14:50
 * 修改备注：
 * Version: 1.0.0
 */
public class BlurPostprocessor extends BasePostprocessor {

    private ImageView mImageView;
    private SimpleDraweeView mSimpleDraweeView;

    public BlurPostprocessor(ImageView imageView) {
        mImageView = imageView;
    }

    public BlurPostprocessor(ImageView imageView, SimpleDraweeView simpleDraweeView) {
        mImageView = imageView;
        mSimpleDraweeView = simpleDraweeView;
    }

    public static void blur(SimpleDraweeView simpleDraweeView, ImageView blurImageView, String imgUrl) {

        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(imgUrl))
                .setPostprocessor(new BlurPostprocessor(blurImageView, simpleDraweeView))
                .setProgressiveRenderingEnabled(false)
                .build();

        AbstractDraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(simpleDraweeView.getController())
                .build();

        simpleDraweeView.setController(draweeController);
    }

    @Override
    public void process(Bitmap bitmap) {
        Observable.just(bitmap)
                .map(new Func1<Bitmap, Bitmap>() {
                    @Override
                    public Bitmap call(Bitmap bitmap) {
                        return FastBlurUtil.doBlur(bitmap, 20, false);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        mImageView.setImageBitmap(bitmap);

                        if (mSimpleDraweeView != null) {
                            GenericDraweeHierarchy hierarchy = mSimpleDraweeView.getHierarchy();
                            hierarchy.setPlaceholderImage(new BitmapDrawable(bitmap));
                            hierarchy.setFadeDuration(0);
                        }
                    }
                });
    }
}
