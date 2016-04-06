package com.rsen.media;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.SurfaceTexture;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.TextureView;

import com.angcyo.rsen.R;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by robi on 2016-04-06 14:54.
 * 用于播放mp4格式的视频,最低支持API16 Android4.1
 * 使用前请注意: 1:不支持播放视频中的音频,2:不支持暂停播放,3:支持快速播放,4:支持播放结束回调
 */
public class VideoTextureView extends TextureView implements TextureView.SurfaceTextureListener, MoviePlayer.PlayerFeedback {

    boolean isReadyPlay = false;
    MoviePlayer.PlayTask mPlayTask;
    boolean mShowStopLabel = false;
    private String filePath;//mp4文件的绝对路径
    private boolean isAutoStart = true;//是否自动开始播放
    private boolean isLoopMode = true;//是否循环播放
    private PlayListener playListener;
    private int fps = -1;//播放的帧率,-1为默认值


    public VideoTextureView(Context context) {
        this(context, null);
    }

    public VideoTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VideoTextureView);
        filePath = array.getString(R.styleable.VideoTextureView_file_path);
        isAutoStart = array.getBoolean(R.styleable.VideoTextureView_auto_start, isAutoStart);
        isLoopMode = array.getBoolean(R.styleable.VideoTextureView_loop_mode, isLoopMode);
        fps = array.getInteger(R.styleable.VideoTextureView_fps, -1);
        array.recycle();
        init();
    }

    private void init() {
        setSurfaceTextureListener(this);
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setPlayListener(PlayListener playListener) {
        this.playListener = playListener;
    }

    public void setAutoStart(boolean autoStart) {
        isAutoStart = autoStart;
    }

    public void setLoopMode(boolean loopMode) {
        isLoopMode = loopMode;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void startPlay() {
        if (isReadyPlay && !TextUtils.isEmpty(filePath)) {
            if (mShowStopLabel) {
                stopPlay();
            }

            MoviePlayer moviePlayer;
            MoviePlayer.FrameCallback frameCallback = null;
            if (fps != -1) {
                frameCallback = new SpeedControlCallback(fps);
            }

            try {
                moviePlayer = new MoviePlayer(new File(filePath), new WeakReference<VideoTextureView>(this), frameCallback);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            mPlayTask = new MoviePlayer.PlayTask(moviePlayer, this);
            mPlayTask.setLoopMode(isLoopMode);
            mPlayTask.execute();
            mShowStopLabel = true;
        }
    }


    public void startPlay(PlayListener playListener) {
        setPlayListener(playListener);
        startPlay();
    }

    public void stopPlay() {
        if (mPlayTask != null) {
            mPlayTask.requestStop();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setKeepScreenOn(true);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        isReadyPlay = true;
        if (isAutoStart) {
            startPlay();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        isReadyPlay = false;
        stopPlay();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void playbackStopped() {
        if (playListener != null) {
            playListener.onPlayEnd();
        }
        mShowStopLabel = false;
    }

    public interface PlayListener {
        void onPlayEnd();
    }
}
