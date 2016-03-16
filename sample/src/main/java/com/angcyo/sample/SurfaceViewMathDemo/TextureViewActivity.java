package com.angcyo.sample.SurfaceViewMathDemo;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import com.angcyo.sample.R;

import java.io.IOException;

@SuppressWarnings("deprecation")
public class TextureViewActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {
    private Camera mCamera;
    private TextureView mTextureView;
    private Button mRotate;
    private float rotate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture_view);

//        mTextureView = new TextureView(this);
        mTextureView = (TextureView) findViewById(R.id.textureView);
        mRotate = (Button) findViewById(R.id.rotate);

        mTextureView.setSurfaceTextureListener(this);


        mRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotate += 90;
                mTextureView.setRotation(rotate % 360);
            }
        });
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mCamera = Camera.open();
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(1920, 1080);
        parameters.setPictureSize(1920, 1080);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//        parameters.setPreviewFormat();
        mCamera.setParameters(parameters);
        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();

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

}
