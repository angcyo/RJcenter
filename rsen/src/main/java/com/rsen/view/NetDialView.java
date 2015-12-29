package com.rsen.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;

import java.util.Map;

/**
 * 大转盘
 * Created by angcyo on 15-12-18 018 15:09 下午.
 */
public class NetDialView extends DialView {

    private String[] icoUrl;
    private Map<String, Integer> awardMap;

    public NetDialView(Context context) {
        this(context, null);
    }

    public NetDialView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetDialView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTexts(null);
        setRatios(null);
        setIcons(null);
    }

    public void setIcoUrl(String[] icoUrl) {
        this.icoUrl = icoUrl;
        this.setIcons(new Bitmap[icoUrl.length]);
        loadImage();
    }

    private void loadImage() {
        if (icoUrl != null) {
            for (int i = 0; i < icoUrl.length; i++) {
                String url = icoUrl[i];
                if (TextUtils.isEmpty(url)) {
                    continue;
                }

                final int finalI = i;
//                ImageLoader.getInstance().loadImage(url, new SimpleImageLoadingListener() {
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                        super.onLoadingComplete(imageUri, view, loadedImage);
//                        setIconAt(finalI, loadedImage);
//                    }
//                });
            }
        }
    }

    public void setAwardMap(Map<String, Integer> awardMap) {
        this.awardMap = awardMap;
    }

    public void startWidthAward(String awarded, Runnable endAciton) {
        if (awardMap != null) {
            int index = awardMap.get(awarded);
            start(index, endAciton);
        }
    }
}
