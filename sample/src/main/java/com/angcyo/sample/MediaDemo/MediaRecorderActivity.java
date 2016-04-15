package com.angcyo.sample.MediaDemo;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.angcyo.sample.R;

import java.io.IOException;
import java.util.List;

public class MediaRecorderActivity extends Activity implements SurfaceHolder.Callback, MediaRecorder.OnInfoListener {

    SurfaceView surfaceView;
    MediaRecorder mediaRecorder;
    long DELAY_TIME = 60 * 1000;
    Runnable swapFile = new SwapFileRunnable();

    public static String getFileName() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/sdcard/");
        stringBuilder.append(FileSwapHelper.getTempFileName());
        stringBuilder.append(".mp4");
        return stringBuilder.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_recorder);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);
//        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private synchronized Camera initCamera(int cameraId) throws Exception {
        Camera camera = Camera.open(cameraId);
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewFormat(ImageFormat.NV21);
        parameters.setFlashMode("off");
        parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
        parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
        parameters.setPreviewSize(1920, 1080);
//            this.mCamera.setDisplayOrientation(90);
//        mCameraPreviewCallback = new CameraPreviewCallback();
//        mCamera.addCallbackBuffer(mImageCallbackBuffer);
//        mCamera.setPreviewCallbackWithBuffer(mCameraPreviewCallback);
//            mCamera.setPreviewCallback(mCameraPreviewCallback);
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains("continuous-video")) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        camera.setParameters(parameters);
        return camera;
    }

    private MediaRecorder initMediaRecorder(Camera camera, Surface surface, String filePath) throws IOException {
        //注意方法调用顺序
        MediaRecorder mediaRecorder = new MediaRecorder();
        camera.unlock();
        mediaRecorder.setCamera(camera);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        mediaRecorder.setVideoEncodingBitRate(12 * 1024 * 1024);

        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);

        mediaRecorder.setMaxDuration(60 * 1000);
        mediaRecorder.setOnInfoListener(this);

        int video_width = 1920;
        int video_height = 1080;
        mediaRecorder.setVideoSize(video_width, video_height);
        mediaRecorder.setVideoFrameRate(30);

        mediaRecorder.setPreviewDisplay(surface);

        mediaRecorder.setOutputFile(filePath);
        mediaRecorder.prepare();

        return mediaRecorder;
    }

    private void closeMediaRecorder(MediaRecorder mediaRecorder) {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceView.setKeepScreenOn(true);
//        surfaceView.postDelayed(swapFile, DELAY_TIME);
        try {
            mediaRecorder = initMediaRecorder(initCamera(Camera.CameraInfo.CAMERA_FACING_BACK), holder.getSurface(), getFileName());
            mediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
            e("初始化失败:" + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        surfaceView.setKeepScreenOn(false);
        closeMediaRecorder(mediaRecorder);
    }

    private void resetMediaFileName() {
        if (mediaRecorder != null) {
            String fileName = getFileName();
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.setOutputFile(fileName);
            e("重置文件名:" + fileName);
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
                e("mediaRecorder 重置失败:" + e.getMessage());
            }
        }
    }

    private void e(String msg) {
        Log.e("angcyo-->", msg);
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        e("angcyo");
    }

    class SwapFileRunnable implements Runnable {
        @Override
        public void run() {
            resetMediaFileName();
            if (surfaceView != null) {
                surfaceView.postDelayed(swapFile, DELAY_TIME);
            }
        }
    }
}
