package com.rsen.media;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.annotation.RawRes;

/**
 * Created by robi on 2016-04-29 14:42.
 * 暂且只支持播放拍照音效
 */
public class SoundPlayManager {
    static SoundPlayManager sSoundPlayManager;
    private final SoundPool mSoundPool;
    Context mContext;
    private int mPlayId;


    private SoundPlayManager(Context context, @RawRes int id) {
        mContext = context;
        mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        mPlayId = mSoundPool.load(context, id, 1);
    }

    public static void init(Context context, @RawRes int id) {
        if (sSoundPlayManager == null) {
            sSoundPlayManager = new SoundPlayManager(context, id);
        }
    }

    public static void play() {
        if (sSoundPlayManager != null) {
            sSoundPlayManager.playMusic();
        }
    }

    private void playMusic() {
        //实例化AudioManager对象，控制声音
        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        float audioMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //当前音量
        float audioCurrentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volumeRatio = audioCurrentVolume / audioMaxVolume;

        mSoundPool.play(mPlayId, volumeRatio, volumeRatio, 1, 0, 1);
    }
}
