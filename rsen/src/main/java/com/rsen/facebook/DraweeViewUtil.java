package com.rsen.facebook;

import android.net.Uri;
import android.support.annotation.DrawableRes;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

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
        String url = "res://" + view.getContext().getPackageName() + "/" + res;
        view.setImageURI(Uri.parse(url));
    }

    public static void setDraweeViewFile(SimpleDraweeView view, String filePath) {
        String url = "file://" + filePath;
        view.setImageURI(Uri.parse(url));
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
