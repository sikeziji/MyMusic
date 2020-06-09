package com.wnagj.lib_audio.mediaplayer.core;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * 带状态的播放器
 */
public class CustomMediaPlayer extends MediaPlayer implements MediaPlayer.OnCompletionListener {


    /**
     * 播放状态
     */
    public enum Status {


        IDLE,//
        INITIALIZED,//初始化
        STARTED, //开始
        PAUSED, // 暂停
        STOPPED, //停止
        COMPLETED//完成
    }

    /**
     * 状态变量
     */
    private Status mState;
    /**
     * 设置完成是事件
     */
    private OnCompletionListener mOnCompletionListener;


    public CustomMediaPlayer() {
        super();
        this.mState =Status.IDLE;
        super.setOnCompletionListener(this);
    }

    @Override
    public void reset() {
        super.reset();
        mState = Status.IDLE;
    }


    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, IllegalStateException, SecurityException {
        super.setDataSource(path);
        mState = Status.INITIALIZED;
    }


    @Override
    public void start() throws IllegalStateException {
        super.start();
        mState = Status.STARTED;
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {
        this.mOnCompletionListener = listener;
    }



    /**
     * 播放完成事件
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        mState = Status.COMPLETED;
        if (mOnCompletionListener != null) {
            mOnCompletionListener.onCompletion(mp);
        }
    }

    @Override public void stop() throws IllegalStateException {
        super.stop();
        mState = Status.STOPPED;
    }

    @Override public void pause() throws IllegalStateException {
        super.pause();
        mState = Status.PAUSED;
    }

    public void setState(Status mState) {
        this.mState = mState;
    }

    public Status getState() {
        return mState;
    }

    public boolean isComplete() {
        return mState == Status.COMPLETED;
    }
}
