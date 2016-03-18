package com.angcyo.sample.SurfaceViewMathDemo;

/**
 * Created by robi on 2016-03-16 11:35.
 */

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import java.util.List;


/**
 * 预览摄像头
 */
@SuppressWarnings("deprecation")
public class ShowCamera extends SurfaceView {

    public static final String TAG = "ShowCamera";
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
                    try {
                        // parameters.setFlashMode("off"); // 无闪光灯
                        parameters.setPictureFormat(PixelFormat.JPEG); //Sets the image format for picture 设定相片格式为JPEG，默认为NV21
                        parameters.setPreviewFormat(PixelFormat.YCbCr_420_SP); //Sets the image format for preview picture，默认为NV21
                        /*【ImageFormat】JPEG/NV16(YCrCb format，used for Video)/NV21(YCrCb format，used for Image)/RGB_565/YUY2/YU12*/

                        // 【调试】获取caera支持的PictrueSize，看看能否设置？？
                        List<Camera.Size> pictureSizes = mCamera.getParameters().getSupportedPictureSizes();
                        List<Camera.Size> previewSizes = mCamera.getParameters().getSupportedPreviewSizes();
                        List<Integer> previewFormats = mCamera.getParameters().getSupportedPreviewFormats();
                        List<Integer> previewFrameRates = mCamera.getParameters().getSupportedPreviewFrameRates();
                        Log.i(TAG + "initCamera", "cyy support parameters is ");
                        Camera.Size psize = null;
                        for (int i = 0; i < pictureSizes.size(); i++) {
                            psize = pictureSizes.get(i);
                            Log.i(TAG + "initCamera", "PictrueSize,width: " + psize.width + " height" + psize.height);
                        }
                        for (int i = 0; i < previewSizes.size(); i++) {
                            psize = previewSizes.get(i);
                            Log.i(TAG + "initCamera", "PreviewSize,width: " + psize.width + " height" + psize.height);
                        }
                        Integer pf = null;
                        for (int i = 0; i < previewFormats.size(); i++) {
                            pf = previewFormats.get(i);
                            Log.i(TAG + "initCamera", "previewformates:" + pf);
                        }

                        // 设置拍照和预览图片大小
                        int width = 3840;
                        int height = 2160;
                        parameters.setPictureSize(1920, 1080); //指定拍照图片的大小
                        parameters.setPreviewSize(width, height); // 指定preview的大小
                        //这两个属性 如果这两个属性设置的和真实手机的不一样时，就会报错

                        // 横竖屏镜头自动调整
                        if (getContext().getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                            parameters.set("orientation", "portrait"); //
                            parameters.set("rotation", 90); // 镜头角度转90度（默认摄像头是横拍）
                            mCamera.setDisplayOrientation(90); // 在2.2以上可以使用
                        } else// 如果是横屏
                        {
                            parameters.set("orientation", "landscape"); //
                            mCamera.setDisplayOrientation(0); // 在2.2以上可以使用
                        }

                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

                           /* 视频流编码处理 */
                        //添加对视频流处理函数

                    } catch (Exception e) {
                    }
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
        setAlpha(0.8f);
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
