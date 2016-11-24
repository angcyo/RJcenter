package com.rsen.facebook;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;

import com.angcyo.rsen.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.rsen.util.ResUtil;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2016/09/30 10:00
 * 修改人员：Robi
 * 修改时间：2016/09/30 10:00
 * 修改备注：
 * Version: 1.0.0
 */
public class DraweeViewUtil {
    public static void setDraweeViewRes(SimpleDraweeView view, @DrawableRes int res) {
        view.setBackgroundColor(Color.TRANSPARENT);
        String url = "res://" + view.getContext().getPackageName() + "/" + res;
        view.setImageURI(Uri.parse(url));
    }

    public static void setDraweeViewFile(SimpleDraweeView view, String filePath) {
        view.setBackgroundColor(Color.TRANSPARENT);
        String url = "file://" + filePath;
        view.setImageURI(Uri.parse(url));
    }

    public static void setDraweeViewHttp(SimpleDraweeView view, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (url.startsWith("http")) {
            view.setBackgroundColor(Color.TRANSPARENT);
            view.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
            view.setImageURI(Uri.parse(url));
        } else {
            setDraweeViewHttp2(view, url);
        }
    }

    public static void setDraweeViewHttp2(SimpleDraweeView view, String url) {
        Uri uri = Uri.parse(/*Http.BASE_IMAGE_URL +*/ url);
        view.setBackgroundColor(Color.TRANSPARENT);
        view.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
        view.setImageURI(uri);
    }

    public static void setDraweeViewHttp2(SimpleDraweeView view, String url, boolean progressive) {
        Uri uri = Uri.parse(/*Http.BASE_IMAGE_URL +*/ url);
        view.setBackgroundColor(Color.TRANSPARENT);
        //view.setImageURI(uri);

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(progressive)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(view.getController())
                .build();
        view.setController(controller);
    }

    /**
     * 重置加载的图片大小,不修改原图, 效果很好
     */
    public static void resize(SimpleDraweeView view, Uri uri, int width, int height) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(view.getController())
                .setImageRequest(request)
                .build();
        view.setController(controller);
    }

    public static void resize(SimpleDraweeView view, String url, int width, int height) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (url.startsWith("http")) {
            view.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
            view.setBackgroundColor(Color.TRANSPARENT);
            resize(view, Uri.parse(url), width, height);
        } else {
            resize(view, Uri.parse("" + url), width, height);
        }
    }

    public static int getItemSize(Context context, int itemCount) {
        int screenWidth = ResUtil.getScreenWidth(context);
        int itemSize = screenWidth / itemCount;

        return itemSize;
    }

    /**
     * 设置占位图以及缩放类型
     */
    public static void setPlaceholderImage(SimpleDraweeView view) {
        view.getHierarchy().setPlaceholderImage(R.drawable.abc_ic_clear_material, ScalingUtils.ScaleType.CENTER_INSIDE);
    }

    /**
     * 设置圆形
     */
    public static void setDraweeViewRound(SimpleDraweeView view) {
        RoundingParams roundingParams = RoundingParams.asCircle();
        view.getHierarchy().setRoundingParams(roundingParams);
    }

    /**
     * 设置圆角
     */
    public static void setDraweeViewRadius(SimpleDraweeView view, float radius) {
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(radius);//圆角图片
        view.getHierarchy().setRoundingParams(roundingParams);
    }
}

