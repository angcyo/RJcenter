package com.angcyo.sample.SurfaceViewMathDemo;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.angcyo.sample.R;

import java.io.IOException;

@SuppressWarnings("deprecation")
public class TextureViewActivity extends Activity implements TextureView.SurfaceTextureListener {
    private Camera mCamera;
    private TextureView mTextureView;
    private Button mRotate;
    private float rotate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture_view);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//        mTextureView = new TextureView(this);
        mTextureView = (TextureView) findViewById(R.id.textureView);
        mRotate = (Button) findViewById(R.id.rotate);

        mTextureView.setSurfaceTextureListener(this);

        mTextureView.setAlpha(0.5f);

        mRotate.setOnClickListener(v -> {
            rotate += 90;
            mTextureView.setRotation(rotate % 360);
        });
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);

        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(1920, 1080);
        parameters.setPictureSize(1920, 1080);
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//        parameters.setRotation(90);
//        parameters.setPreviewFormat();
        mCamera.setParameters(parameters);
        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
            mCamera.autoFocus((success, camera) -> {

            });
            mCamera.cancelAutoFocus();
//            mCamera.setDisplayOrientation(Surface.ROTATION_90);
        } catch (IOException ioe) {
            // Something bad happened
        }
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Ignored, Camera does all the work for us
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Invoked every time there's a new Camera preview frame
    }

    public void getBitmap(View view) {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(mTextureView.getBitmap(imageView.getWidth(), imageView.getHeight()));
    }

}
