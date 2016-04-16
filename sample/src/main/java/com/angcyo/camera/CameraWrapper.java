package com.angcyo.camera;

import android.annotation.SuppressLint;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.*;
import android.util.Log;
import android.view.SurfaceHolder;

import com.angcyo.camera.rencoder.FileUtils;
import com.angcyo.camera.rencoder.MediaMuxerRunnable;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class CameraWrapper {
    public static final int IMAGE_HEIGHT = 1080;
    public static final int IMAGE_WIDTH = 1920;
    private static final String TAG = "CameraWrapper";
    private static final boolean DEBUG = true;    // TODO set false on release
    private static CameraWrapper mCameraWrapper;

    Camera.PreviewCallback previewCallback;
    private Camera mCamera;
    private Camera.Parameters mCameraParamters;
    private boolean mIsPreviewing = false;
    private float mPreviewRate = -1.0f;
    private CameraPreviewCallback mCameraPreviewCallback;
    private byte[] mImageCallbackBuffer = new byte[CameraWrapper.IMAGE_WIDTH
            * CameraWrapper.IMAGE_HEIGHT * 3 / 2];
    private boolean isBlur = false;
    private int openCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
//    private int openCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;

    private CameraWrapper() {
    }

    public static CameraWrapper getInstance() {
        if (mCameraWrapper == null) {
            synchronized (CameraWrapper.class) {
                if (mCameraWrapper == null) {
                    mCameraWrapper = new CameraWrapper();
                }
            }
        }
        return mCameraWrapper;
    }

    private static String getSaveFilePath(String fileName) {
        StringBuilder fullPath = new StringBuilder();
        fullPath.append(FileUtils.getExternalStorageDirectory());
        fullPath.append(FileUtils.getMainDirName());
        fullPath.append("/video2/");
        fullPath.append(fileName);
        fullPath.append(".mp4");

        String string = fullPath.toString();
        File file = new File(string);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        return string;
    }

    /**
     * 停止录制,预览继续
     */
    public static void stopMediaMuxer() {
        MediaMuxerRunnable.stopMuxer();
    }

    /**
     * 预览未停止的情况下,继续录制
     */
    public static void startMediaMuxer() {
        MediaMuxerRunnable.startMuxer();
    }

    public void switchCameraId() {
        if (openCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            openCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            openCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
    }

    public void doOpenCamera(CamOpenOverCallback callback) {
        Log.i(TAG, "Camera open....");
        int numCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < numCameras; i++) {
            Camera.getCameraInfo(i, info);
            if (info.facing == openCameraId) {//0
                mCamera = Camera.open(i);
                break;
            }
        }
        if (mCamera == null) {
            Log.d(TAG, "No front-facing camera found; opening default");
            mCamera = Camera.open();    // opens first back-facing camera
        }
        if (mCamera == null) {
            throw new RuntimeException("Unable to open camera");
        }
        Log.i(TAG, "Camera open over....");
        callback.cameraHasOpened();
    }

    public void doStartPreview(SurfaceHolder holder, float previewRate) {
        Log.i(TAG, "doStartPreview...");
        if (mIsPreviewing) {
            this.mCamera.stopPreview();
            return;
        }

        try {
            this.mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initCamera();
    }

    public void stopPreview() {
        if (!mIsPreviewing || mCamera == null) {
            return;
        }

        mCamera.stopPreview();
        mIsPreviewing = false;
    }

    public void startPreview(SurfaceTexture surface) {
        if (mIsPreviewing || mCamera == null) {
            return;
        }

        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
//            mCamera.addCallbackBuffer(mImageCallbackBuffer);
//            mCamera.setPreviewCallbackWithBuffer(mCameraPreviewCallback);
            mCamera.setPreviewCallback(mCameraPreviewCallback);
            mIsPreviewing = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doStartPreview(SurfaceTexture surface) {
        Log.i(TAG, "doStartPreview()");
        if (mCamera == null) {
            return;
        }
        if (mIsPreviewing) {
            this.mCamera.stopPreview();
            return;
        }

        try {
            this.mCamera.setPreviewTexture(surface);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initCamera();
    }

    public void doStopCamera() {
        Log.i(TAG, "doStopCamera");
        if (this.mCamera != null) {
            if (mCameraPreviewCallback != null) {
                mCameraPreviewCallback.close();
            }
            this.mCamera.setPreviewCallback(null);
            this.mCamera.stopPreview();
            this.mIsPreviewing = false;
            this.mPreviewRate = -1f;
            this.mCamera.release();
            this.mCamera = null;
        }
    }

    private void initCamera() {
        if (this.mCamera != null) {
            this.mCameraParamters = this.mCamera.getParameters();
            this.mCameraParamters.setPreviewFormat(ImageFormat.NV21);
            this.mCameraParamters.setFlashMode("off");
            this.mCameraParamters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
            this.mCameraParamters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
            this.mCameraParamters.setPreviewSize(IMAGE_WIDTH, IMAGE_HEIGHT);
//            this.mCamera.setDisplayOrientation(90);
            mCameraPreviewCallback = new CameraPreviewCallback();
            mCamera.addCallbackBuffer(mImageCallbackBuffer);
            mCamera.setPreviewCallbackWithBuffer(mCameraPreviewCallback);
//            mCamera.setPreviewCallback(mCameraPreviewCallback);
            List<String> focusModes = this.mCameraParamters.getSupportedFocusModes();
            if (focusModes.contains("continuous-video")) {
                this.mCameraParamters
                        .setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            this.mCamera.setParameters(this.mCameraParamters);
            this.mCamera.startPreview();

            this.mIsPreviewing = true;
        }
    }

    public void setBlur(boolean blur) {
        isBlur = blur;
    }

    public void setPreviewCallback(Camera.PreviewCallback callback) {
        previewCallback = callback;
    }

    public interface CamOpenOverCallback {
        public void cameraHasOpened();
    }

    class CameraPreviewCallback implements Camera.PreviewCallback {

        private CameraPreviewCallback() {
            startRecording();
        }

        public void close() {
            stopRecording();
        }

        private void startRecording() {
            MediaMuxerRunnable.startMuxer();
        }

        private void stopRecording() {
            MediaMuxerRunnable.stopMuxer();
        }

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (DEBUG) {
                Log.e("CameraWrapper", "onPreviewFrame " + data.length);
            }
            MediaMuxerRunnable.addVideoFrameData(data);
            camera.addCallbackBuffer(data);
        }
    }
}
