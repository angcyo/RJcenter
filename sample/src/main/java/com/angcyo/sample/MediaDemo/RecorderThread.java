package com.angcyo.sample.MediaDemo;

import android.graphics.SurfaceTexture;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by robi on 2016-04-24 11:00.
 */
@SuppressWarnings("deprecation")
public class RecorderThread extends HandlerThread implements MediaRecorder.OnInfoListener, MediaRecorder.OnErrorListener {

    public static final int MAX_DURATION = 10 * 1000;//最常录制时间
    public static final int MSG_START = 0x01;
    public static final int MSG_ERROR = MSG_START << 1;
    public static final int MSG_CAMERA_ERROR = MSG_ERROR << 1;
    public static final int MSG_NO_PREVIEW = MSG_CAMERA_ERROR << 1;
    static RecorderThread mThread;
    Object lock = new Object();
    android.hardware.Camera mCamera;
    MediaRecorder mMediaRecorder;
    SurfaceHolder mSurfaceHolder;
    SurfaceTexture mSurfaceTexture;
    private Handler mHandler;

    public RecorderThread(String name, SurfaceHolder surface) {
        super(name);
        this.mSurfaceHolder = surface;
        mSurfaceTexture = null;
    }

    public RecorderThread(String name, SurfaceTexture surface) {
        super(name);
        this.mSurfaceTexture = surface;
        mSurfaceHolder = null;
    }

    public static void startThread(SurfaceHolder surface) {
        if (mThread == null) {
            synchronized (RecorderThread.class) {
                if (mThread == null) {
                    mThread = new RecorderThread("RecorderThread", surface);
                    mThread.start();
                }
            }
        }
    }

    public static void startThread(SurfaceTexture surface) {
        if (mThread == null) {
            synchronized (RecorderThread.class) {
                if (mThread == null) {
                    mThread = new RecorderThread("RecorderThread", surface);
                    mThread.start();
                }
            }
        } else {
            mThread.setSurfaceTexture(surface);
        }
    }

    public static void destroyThread() {
        if (mThread != null) {
            mThread.setSurfaceTexture(null);
        }
    }

    public static void exitThread() {
        synchronized (RecorderThread.class) {
            if (mThread != null) {
                mThread.exit();
            }
        }
    }

    public static void e(String msg) {
        Log.e("angcyo-->" + Thread.currentThread().getId(), msg);
    }

    public static String getFileName() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/sdcard/angcyo/");
        stringBuilder.append(getTempFileName());
        stringBuilder.append(".mp4");
        String filePath = stringBuilder.toString();
        File parentFile = new File(filePath).getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        return filePath;
    }

    public static String getTempFileName() {
        return new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(System.currentTimeMillis());
    }

    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        mSurfaceTexture = surfaceTexture;
        if (mCamera != null) {
            try {
//                mCamera.setPreviewTexture(mSurfaceTexture);
                mMediaRecorder.setPreviewDisplay(new Surface(mSurfaceTexture));
            } catch (Exception e) {
                e.printStackTrace();
                e("setSurfaceTexture " + e.getMessage());
            }
        }
    }

    private void exit() {
        releaseMediaRecorder();
        releaseCamera();
        quit();
    }

    @Override
    public void run() {
        super.run();
        e("线程退出");
    }

    @Override
    protected void onLooperPrepared() {
        initHandler();
        restartRecorder(getFileName());
    }

    private void createMediaRecorder() {
        releaseMediaRecorder();

        mMediaRecorder = new MediaRecorder();
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            // release the camera for other applications
            mCamera.release();
            mCamera = null;
        }
    }

    private void initHandler() {
        mHandler = new Handler(getLooper(), msg -> {
            e("handleMessage " + msg.what);
            switch (msg.what) {
                case MSG_START:
                    break;
                case MSG_ERROR:
                    break;
                default:
                    break;
            }
//            if (msg.what == 1001) {
//                e("收到消息:" + 1001);
//                mMediaRecorder.reset();
//            }
            return true;
        });
    }

    private void restartRecorder(String filePath) {
        if (mMediaRecorder == null) {
            createMediaRecorder();
        }
        mMediaRecorder.reset();

        if (mCamera == null) {
            try {
                openCamera(android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK);
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(MSG_CAMERA_ERROR);
                return;
            }
        }

        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        CamcorderProfile mProfile = CamcorderProfile.get(android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK, CamcorderProfile.QUALITY_HIGH);

//        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
//        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//
        mMediaRecorder.setProfile(mProfile);
//
        mMediaRecorder.setOutputFile(filePath);
        mMediaRecorder.setMaxDuration(MAX_DURATION);
        mMediaRecorder.setOnInfoListener(this);
        mMediaRecorder.setOnErrorListener(this);
//
//        mMediaRecorder.setVideoSize(mProfile.videoFrameWidth, mProfile.videoFrameHeight);
////        mMediaRecorder.setVideoFrameRate(mProfile.videoFrameRate);
////        mMediaRecorder.setVideoEncodingBitRate(mProfile.videoBitRate);
////        mMediaRecorder.setAudioEncodingBitRate(mProfile.audioBitRate);
////        mMediaRecorder.setAudioChannels(mProfile.audioChannels);
////        mMediaRecorder.setAudioSamplingRate(mProfile.audioSampleRate);
//
////        mMediaRecorder.setProfile(mProfile);
//
////        mMediaRecorder.setPreviewDisplay(mSurface);
//
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            e("重新 开始录制:" + filePath);
        } catch (IOException e) {
            e("重新 录制失败:" + e.getMessage());
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_ERROR);
        }
    }

    private void initMediaRecorder() {
        if (mMediaRecorder != null) {
            return;
        }
        //注意方法调用顺序
        MediaRecorder mediaRecorder = new MediaRecorder();
        mediaRecorder.reset();
//        mCamera.unlock();
//        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
//        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
//        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

        String filePath = getFileName();
        mediaRecorder.setOutputFile(filePath);

        int video_width = 1920;
        int video_height = 1080;
//        mediaRecorder.setVideoSize(video_width, video_height);
//        mediaRecorder.setVideoFrameRate(30);
//        mediaRecorder.setVideoEncodingBitRate(12 * 1024 * 1024);

        mediaRecorder.setMaxDuration(60 * 1000);
        mediaRecorder.setOnInfoListener(this);
        mediaRecorder.setOnErrorListener(this);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            e("开始录制:" + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaRecorder = mediaRecorder;
    }

    private void openCamera(int cameraId) throws Exception {
        if (mCamera != null) {
            return;
        }
        android.hardware.Camera camera = android.hardware.Camera.open(cameraId);
        android.hardware.Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode("off");
        parameters.setWhiteBalance(android.hardware.Camera.Parameters.WHITE_BALANCE_AUTO);
        parameters.setSceneMode(android.hardware.Camera.Parameters.SCENE_MODE_AUTO);
        int width, height;
        width = 1920;
        height = 1080;
        parameters.setPreviewSize(width, height);
        parameters.setPictureSize(width, height);
//            this.mCamera.setDisplayOrientation(90);
//        mCameraPreviewCallback = new CameraPreviewCallback();
//        mCamera.addCallbackBuffer(mImageCallbackBuffer);
//        mCamera.setPreviewCallbackWithBuffer(mCameraPreviewCallback);
//            mCamera.setPreviewCallback(mCameraPreviewCallback);
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains("continuous-video")) {
            parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        camera.setParameters(parameters);
        try {
            if (mSurfaceTexture != null) {
                camera.setPreviewTexture(mSurfaceTexture);
            } else if (mSurfaceHolder != null) {
                camera.setPreviewDisplay(mSurfaceHolder);
            } else {
                mHandler.sendEmptyMessage(MSG_NO_PREVIEW);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_ERROR);
            return;
        }

        mCamera = camera;
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        e("onInfo " + what + " " + extra);
        if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
            e("MEDIA_RECORDER_INFO_MAX_DURATION_REACHED");
            restartRecorder(getFileName());
//            mHandler.sendMessage(mHandler.obtainMessage(1001));
        }
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        e("onError " + what + " " + extra);
    }
}
