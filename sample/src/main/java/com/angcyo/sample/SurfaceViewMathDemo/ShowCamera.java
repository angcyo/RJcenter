package com.angcyo.sample.SurfaceViewMathDemo;

/**
 * Created by robi on 2016-03-16 11:35.
 */

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;


/**
 * 预览摄像头
 */
public class ShowCamera extends SurfaceView {

    public SurfaceHolder surfaceHolder;
    int rotate;
    private Camera mCamera;
    Callback surfaceHolderCallback = new Callback() {

        public void surfaceDestroyed(SurfaceHolder arg0) {
            if (mCamera != null) {
                try {
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                } catch (Exception e) {
                }
            }
        }

        public void surfaceCreated(SurfaceHolder arg0) {
            surfaceHolder = arg0;
            try {
                mCamera = Camera.open();
                mCamera.setPreviewDisplay(surfaceHolder);
            } catch (Exception exception) {
                try {
                    mCamera.release();
                    mCamera = null;
                } catch (Exception e) {
                }

            }
        }

        public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
                                   int arg3) {
            surfaceHolder = arg0;
            if (mCamera != null) {
                try {
                    /**从新开启预览*/
                    Camera.Parameters parameters = mCamera.getParameters();
                    mCamera.setParameters(parameters);
                    mCamera.stopPreview();
                    mCamera.startPreview();
                } catch (Exception e) {
                }

            }
        }
    };

    public ShowCamera(Context context) {
        super(context);
        init();
    }

    public ShowCamera(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShowCamera(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(surfaceHolderCallback);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void stop() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void rotate() {
        if (mCamera != null) {
            rotate += 90;
            mCamera.setDisplayOrientation(rotate % 360);

        }
    }
}
