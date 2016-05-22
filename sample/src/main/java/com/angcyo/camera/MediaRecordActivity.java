package com.angcyo.camera;

import android.graphics.SurfaceTexture;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.TextureView;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class MediaRecordActivity extends RBaseActivity {

    protected SurfaceTexture mSurfaceTexture;
    protected HandlerThread mHandlerThread;
    private RecordHandler mRecordHandler;

    @Override
    protected int getContentView() {
        return R.layout.activity_media_record;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mHandlerThread = new HandlerThread("mHandlerThread");
        mHandlerThread.start();
        mRecordHandler = new RecordHandler(mHandlerThread.getLooper());

        ((TextureView) mViewHolder.v("textureView")).setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mSurfaceTexture = surface;
//                mRecordHandler.sendEmptyMessage(RecordHandler.MSG_OPEN_CAMERA);
                mRecordHandler.sendEmptyMessage(RecordHandler.MSG_OPEN_RECORD);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                mRecordHandler.sendEmptyMessage(RecordHandler.MSG_STOP_CAMERA);
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }

    class RecordHandler extends Handler implements MediaRecorder.OnInfoListener, MediaRecorder.OnErrorListener {
        public static final int MSG_OPEN_CAMERA = 1;
        public static final int MSG_OPEN_RECORD = 2;
        public static final int MSG_STOP_RECORD = 3;
        public static final int MSG_STOP_CAMERA = 4;
        private static final int MAX_DURATION = 10 * 1000;
        android.hardware.Camera mCamera;
        MediaRecorder mMediaRecorder;
        boolean isRecorderStart = false;

        public RecordHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_OPEN_CAMERA:
                    openCamera();
                    break;
                case MSG_OPEN_RECORD:
                    startRecorder();
                    break;
                case MSG_STOP_RECORD:
                    stopRecorder();
                    break;
                case MSG_STOP_CAMERA:
                    releaseRecorder();
                    releaseCamera();
                    break;
            }
        }

        private void openCamera() {
            mCamera = android.hardware.Camera.open();
            try {
                mCamera.setPreviewTexture(mSurfaceTexture);//注意处
            } catch (IOException e) {
                e.printStackTrace();
            }
            initCamera(mCamera);
        }

        private void releaseCamera() {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.release();
                mCamera = null;
            }
        }

        private void initCamera(android.hardware.Camera camera) {
            if (camera == null) {
                return;
            }
            android.hardware.Camera.Parameters parameters = camera.getParameters();
            parameters.getSupportedPreviewSizes();
            parameters.getSupportedPictureSizes();
            parameters.setPreviewSize(1920, 1080);
            parameters.setPictureSize(1920, 1080);
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            camera.setParameters(parameters);
        }

        private void createMediaRecorder() {
            if (mMediaRecorder == null) {
                mMediaRecorder = new MediaRecorder();
                isRecorderStart = false;
            }
        }

        private void stopRecorder() {
            if (mMediaRecorder != null) {
                mMediaRecorder.reset();
                isRecorderStart = false;
            }
        }

        private void releaseRecorder() {
            if (mMediaRecorder != null) {
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mMediaRecorder = null;
                isRecorderStart = false;
            }
        }

        private void startRecorder() {
            Log.e("angcyo", "startRecorder...");

            if (isRecorderStart) {
                return;
            }

//            if (mCamera == null) {
//                return;
//            }

//            try {
//                mCamera.unlock();//注意处
//            } catch (Exception e) {
//                Log.e("angcyo", e.getMessage());
//                return;
//            }

            if (mMediaRecorder == null) {
                createMediaRecorder();
            }

            mMediaRecorder.reset();
//            mMediaRecorder.setCamera(mCamera);//注意顺序

            CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
//            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);

//            mMediaRecorder.setProfile(camcorderProfile);
            boolean isMultiMic = true;
            if (isMultiMic) {
//                camcorderProfile.audioSampleRate = 16000;//16K
//                camcorderProfile.audioBitRate = 256000;
//                camcorderProfile.audioChannels = 1;
//                camcorderProfile.audioCodec = MediaRecorder.AudioEncoder.AMR_WB;
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            }

            mMediaRecorder.setOutputFormat(camcorderProfile.fileFormat);
            if (isMultiMic) {
                /*一个都不能少,成群出现*/
                mMediaRecorder.setAudioChannels(camcorderProfile.audioChannels);
                mMediaRecorder.setAudioSamplingRate(camcorderProfile.audioSampleRate);
                mMediaRecorder.setAudioEncodingBitRate(camcorderProfile.audioBitRate);
                mMediaRecorder.setAudioEncoder(camcorderProfile.audioCodec);
            }

//            /*一个都不能少,成群出现*/
//            mMediaRecorder.setVideoEncodingBitRate(camcorderProfile.videoBitRate);
//            mMediaRecorder.setVideoFrameRate(camcorderProfile.videoFrameRate);
//            mMediaRecorder.setVideoSize(1920, 1080);
//            mMediaRecorder.setVideoEncoder(camcorderProfile.videoCodec);


            String filePath = getFilePath();
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.setMaxDuration(MAX_DURATION);
            mMediaRecorder.setOnInfoListener(this);
            mMediaRecorder.setOnErrorListener(this);


            Log.e("angcyo", "准备开始录制...");
            try {
                mMediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaRecorder.start();

            isRecorderStart = true;
            Log.e("angcyo", "录制开始." + filePath);
        }

        private String getFilePath() {
            String format = "yyyy-MM-dd_HH-mm-ss-SSS";
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            String name = sdf.format(new Date());

            String folder = "/sdcard/test_r/";
            File file = new File(folder);
            if (!file.exists()) {
                file.mkdirs();
            }
            return folder + name + ".mp4";
        }

        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            Log.e("angcyo", "onInfo " + what);
            if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                stopRecorder();
            }
        }

        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            Log.e("angcyo", "onError " + what);
        }
    }
}
