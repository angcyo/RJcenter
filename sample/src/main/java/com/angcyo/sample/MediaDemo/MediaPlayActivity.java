package com.angcyo.sample.MediaDemo;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.angcyo.sample.R;

public class MediaPlayActivity extends Activity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        e("onCreate");
        setContentView(R.layout.activity_media_play);

        videoView = (VideoView) findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);


        videoView.setVideoPath("/sdcard/test_r/2016-05-22_14-12-54-647.mp4");
//        videoView.setVideoPath("/sdcard/2016_04_13_20_53.mp4");

    }

    public void pause(View view) {
        videoView.pause();
    }

    public void play(View view) {
        videoView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        e("onResume");

        videoView.requestFocus();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        e("onConfigurationChanged");

//        setContentView(R.layout.activity_media_play);

        printConfig(newConfig);

        initConfig(newConfig);
    }

    private void initConfig(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
            //全屏...
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            findViewById(R.id.controlLayout).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tip)).setText("横屏中...");
            ((TextView) findViewById(R.id.tip)).setTextColor(Color.GREEN);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            //不全屏
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            findViewById(R.id.controlLayout).setVisibility(View.VISIBLE);

            ((TextView) findViewById(R.id.tip)).setText("竖屏中...");
            ((TextView) findViewById(R.id.tip)).setTextColor(Color.BLUE);
        }
    }

    private void printConfig(Configuration configuration) {
        e(configuration.orientation + " " +
                configuration.keyboardHidden + " " +
                configuration.keyboard + " " +
                configuration.uiMode + " " +
                configuration.touchscreen + " " +
                configuration.densityDpi + " " +
                configuration.screenLayout);

    }

    @Override
    protected void onPause() {
        super.onPause();
        e("onPause");

        videoView.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        e("onRestart");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        e("onPostResume");
        printConfig(getResources().getConfiguration());
        initConfig(getResources().getConfiguration());
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        e("onContentChanged");

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        e("onPostCreate");

    }

    @Override
    protected void onStart() {
        super.onStart();
        e("onStart");

    }

    @Override
    protected void onStop() {
        super.onStop();
        e("onStop");

    }

    @Override
    public void onStateNotSaved() {
        super.onStateNotSaved();
        e("onStateNotSaved");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        e("onSaveInstanceState");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        e("onDestroy");

        videoView.stopPlayback();
    }

    public static void e(String log) {
        Log.e("angcyo " + Thread.currentThread().getName() + ":" + Thread.currentThread().getId(), log);
    }
}
