package com.angcyo.sample.MediaDemo;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;

public class RecorderThreadActivity extends RBaseActivity implements SurfaceHolder.Callback, TextureView.SurfaceTextureListener {

    SurfaceView surfaceView;
    TextureView textureView;

    @Override
    protected int getContentView() {
        return R.layout.activity_recorder_thread;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("RecorderThread");
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);
//        textureView = (TextureView) mViewHolder.v("textureView");
//        textureView.setSurfaceTextureListener(this);

//        Matrix matrix = new Matrix();
//        matrix.postScale(2.f, 2.f);
//        textureView.setTransform(matrix);


//        Paint paint = new Paint();
//        paint.setMaskFilter(new BlurMaskFilter(25, BlurMaskFilter.Blur.NORMAL));
//        textureView.setLayerPaint(paint);
//        textureView.setLayerType(View.LAYER_TYPE_SOFTWARE, paint);

        mViewHolder.v("button").setOnClickListener(v -> TestActivity.show(RecorderThreadActivity.this));
        mViewHolder.v("takePicture").setOnClickListener(v -> RecorderThread.takePhoto());
        mViewHolder.v("exit").setOnClickListener(v -> {
            RecorderThread.exitThread();
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        RecorderThread.e("onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        RecorderThread.e("onPause");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        RecorderThread.e("surfaceCreated");
        holder.setFixedSize(1920, 1080);
        RecorderThread.startThread(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        RecorderThread.e("surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        RecorderThread.e("surfaceDestroyed");
//        RecorderThread.exitThread();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        RecorderThread.e("onSurfaceTextureAvailable");
        RecorderThread.startThread(surface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        RecorderThread.e("onSurfaceTextureSizeChanged");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        RecorderThread.e("onSurfaceTextureDestroyed");
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//        RecorderThread.e("onSurfaceTextureUpdated");
    }
}
