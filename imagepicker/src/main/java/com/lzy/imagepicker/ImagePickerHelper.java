package com.lzy.imagepicker;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.ImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.util.ArrayList;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2016/11/24 12:23
 * 修改人员：Robi
 * 修改时间：2016/11/24 12:23
 * 修改备注：
 * Version: 1.0.0
 */
public class ImagePickerHelper {
    public static void startImagePicker(Activity activity, boolean crop, boolean multiMode, int selectLimit) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setCrop(crop);
        imagePicker.setMultiMode(multiMode);
        imagePicker.setSelectLimit(selectLimit);
        Intent intent = new Intent(activity, ImageGridActivity.class);
        activity.startActivityForResult(intent, 100);
    }

    public static ArrayList<String> getImages(Activity activity, int requestCode, int resultCode, Intent data) {
        ArrayList<String> list = new ArrayList<>();
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                for (ImageItem item : images) {
                    list.add(item.path);
                }
            } else {
                Toast.makeText(activity, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
        return list;
    }

    public static class GlideImageLoader implements ImageLoader {

        @Override
        public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
//            Glide.with(activity)                             //配置上下文
//                    .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
//                    .error(R.mipmap.default_image)           //设置错误图片
//                    .placeholder(R.mipmap.default_image)     //设置占位图片
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
//                    .into(imageView);
        }

        @Override
        public void clearMemoryCache() {
            //这里是清除缓存的方法,根据需要自己实现
        }
    }

    public static class PicassoImageLoader implements ImageLoader {

        @Override
        public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
//            Picasso.with(activity)//
//                    .load(new File(path))//
//                    .placeholder(R.mipmap.default_image)//
//                    .error(R.mipmap.default_image)//
//                    .resize(width, height)//
//                    .centerInside()//
//                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)//
//                    .into(imageView);
        }

        @Override
        public void clearMemoryCache() {
        }
    }
}
