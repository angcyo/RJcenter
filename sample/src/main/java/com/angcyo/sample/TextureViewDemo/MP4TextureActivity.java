package com.angcyo.sample.TextureViewDemo;

import android.app.Activity;
import android.os.Bundle;

import com.angcyo.sample.R;
import com.rsen.media.VideoTextureView;

public class MP4TextureActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp4_texture);

        VideoTextureView carView = (VideoTextureView) findViewById(R.id.carView);
        VideoTextureView carView2 = (VideoTextureView) findViewById(R.id.carView2);
        carView2.setAlpha(0.6f);
        carView.setAlpha(0.5f);
        carView.setPlayListener(() -> {
            carView.setFilePath("/sdcard/robi/cycle.mp4");
            carView.setLoopMode(true);
            carView.startPlay();
        });
    }
}
