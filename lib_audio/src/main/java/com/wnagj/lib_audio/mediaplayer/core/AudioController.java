package com.wnagj.lib_audio.mediaplayer.core;

import com.wnagj.lib_audio.mediaplayer.db.GreenDaoHelper;
import com.wnagj.lib_audio.mediaplayer.events.AudioCompleteEvent;
import com.wnagj.lib_audio.mediaplayer.events.AudioErrorEvent;
import com.wnagj.lib_audio.mediaplayer.events.AudioFavouriteEvent;
import com.wnagj.lib_audio.mediaplayer.events.AudioPlayModeEvent;
import com.wnagj.lib_audio.mediaplayer.exception.AudioQueueEmptyException;
import com.wnagj.lib_audio.mediaplayer.model.AudioBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Random;

public class AudioController {

    /**
     * 播放类型枚举
     */
    public enum PlayMode {
        /**
         * 列表循环
         */
        LOOP,
        /**
         * 随机
         */
        RANDOM,
        /**
         * 单曲循环
         */
        REPEAT

        /**
         * 继续补充状态。。。。
         */
    }

    private final AudioPlayer mAudioPlayer;


    //播放队列,不能为空,不设置主动抛错
    private ArrayList<AudioBean> mQueue = new ArrayList<>();
    //当前队列下标
    private int mQueueIndex = 0;
    //默认播放模式
    private PlayMode mPlayMode = PlayMode.LOOP;


    public static AudioController getInstance() {
        return AudioController.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static AudioController instance = new AudioController();
    }

    private AudioController() {
        EventBus.getDefault().register(this);
        mAudioPlayer = new AudioPlayer();
    }

    /**
     * 添加到指定位置的音乐
     *
     * @param index
     * @param bean
     */
    private void addCustomAudio(int index, AudioBean bean) {
        if (mQueue == null) {
            throw new AudioQueueEmptyException("当前播放队列为空,请先设置播放队列.");
        }
        mQueue.add(index, bean);
    }

    /**
     * 查询音乐
     *
     * @param bean
     * @return
     */
    private int queryAudio(AudioBean bean) {
        return mQueue.indexOf(bean);
    }

    /**
     * 加载音乐
     *
     * @param bean
     */
    public void load(AudioBean bean) {
        mAudioPlayer.load(bean);
    }


    /**
     * 获取播放器当前状态
     */
    private CustomMediaPlayer.Status getStatus() {
        return mAudioPlayer.getStatus();
    }

    /**
     * 获取下一个播放
     *
     * @return
     */
    public void getFirstLoading() {
        if (mQueue.size() != 0) {
            load(mQueue.get(0));
        }
    }


    /**
     * 获取下一个播放
     *
     * @return
     */
    public AudioBean getNextPlaying() {
        switch (mPlayMode) {
            case LOOP://列表循环
                mQueueIndex = (mQueueIndex + 1) % mQueue.size();
                return getPlaying(mQueueIndex);
            case RANDOM://随机
                mQueueIndex = new Random().nextInt(mQueue.size()) % mQueue.size();
                return getPlaying(mQueueIndex);
            case REPEAT://单曲循环
                return getPlaying(mQueueIndex);
        }
        return null;
    }

    /**
     * 获取上一个播放
     *
     * @return
     */
    private AudioBean getPreviousPlaying() {
        switch (mPlayMode) {
            case LOOP:
                mQueueIndex = (mQueueIndex + mQueue.size() - 1) % mQueue.size();
                return getPlaying(mQueueIndex);
            case RANDOM:
                mQueueIndex = new Random().nextInt(mQueue.size()) % mQueue.size();
                return getPlaying(mQueueIndex);
            case REPEAT:
                return getPlaying(mQueueIndex);
        }
        return null;
    }

    /**
     * 获取正在播放
     *
     * @param index
     * @return
     */
    private AudioBean getPlaying(int index) {
        //判断和下标越界判断
        if (mQueue != null && !mQueue.isEmpty() && index >= 0 && index < mQueue.size()) {
            return mQueue.get(index);
        } else {
            //TODO 可以做一个默认界面不进行音频播放
            throw new AudioQueueEmptyException("当前播放队列为空,请先设置播放队列.");
        }
    }

    /**
     * 对外提供是否播放中状态
     */
    public boolean isStartState() {
        return CustomMediaPlayer.Status.STARTED == getStatus();
    }

    /**
     * 对外提提供是否暂停状态
     */
    public boolean isPauseState() {
        return CustomMediaPlayer.Status.PAUSED == getStatus();
    }


    public boolean isQueueNull() {
        return mQueue.size() == 0;
    }

    /**
     * 获取当前列表
     *
     * @return
     */
    public ArrayList<AudioBean> getQueue() {
        return mQueue == null ? new ArrayList<AudioBean>() : mQueue;
    }

    /**
     * 设置播放队列
     *
     * @param queue 队列
     */
    public void setQueue(ArrayList<AudioBean> queue) {
        if (queue == null || queue.size() == 0) {
            return;
        }
        if (mQueue.size() != 0) {
            addQueue(queue, 0);
        }
        setQueue(queue, 0);
    }

    /**
     * 设置播放队列并修改下标
     *
     * @param queue
     * @param queueIndex
     */
    public void setQueue(ArrayList<AudioBean> queue, int queueIndex) {
        mQueue.clear();
        mQueue.addAll(queue);
        mQueueIndex = queueIndex;
    }


    /**
     * 添加播放队列到底部，并修改下标
     *
     * @param queue
     * @param queueIndex
     */
    public void addQueue(ArrayList<AudioBean> queue, int queueIndex) {
        for (AudioBean mBean : queue) {
            if (mQueue.contains(mBean)) {
                mQueue.add(mBean);
            }
        }
        mQueueIndex = queueIndex;
    }


    /**
     *  ======= 添加播放 和设置播放位置  start=======
     */

    /**
     * 队列头添加播放哥曲
     */
    public void addAudio(AudioBean bean) {
        this.addAudio(0, bean);
    }

    public void addAudio(int index, AudioBean bean) {
        if (mQueue == null) {
            throw new AudioQueueEmptyException("当前播放队列为空,请先设置播放队列.");
        }
        int query = queryAudio(bean);
        if (query <= -1) {
            //没添加过此id的歌曲，添加且直播番放
            addCustomAudio(index, bean);
            setPlayIndex(index);
        } else {
            AudioBean currentBean = getNowPlaying();
            if (!currentBean.id.equals(bean.id)) {
                //添加过且不是当前播放，播，否则什么也不干
                setPlayIndex(query);
            }
        }
    }


    public void setPlayIndex(int index) {
        if (mQueue == null) {
            throw new AudioQueueEmptyException("当前播放队列为空,请先设置播放队列.");
        }
        mQueueIndex = index;
        play();
    }

    public PlayMode getPlayMode() {
        return mPlayMode;
    }


    public void setPlayMode(PlayMode playMode) {
        mPlayMode = playMode;
        //还要对外发送切换事件，更新UI
        EventBus.getDefault().post(new AudioPlayModeEvent(mPlayMode));
    }

    public int getQueueIndex() {
        return mQueueIndex;
    }


    /**
     *  ======= 添加播放 和设置播放位置 end=======
     */


    /***
     *  ============ 播放器控制和收藏  start=============
     */


    /**
     * 添加/移除到收藏
     */
    public void changeFavourite() {
        if (null != GreenDaoHelper.selectFavourite(getNowPlaying())) {
            //已收藏，移除
            GreenDaoHelper.removeFavourite(getNowPlaying());
            EventBus.getDefault().post(new AudioFavouriteEvent(false));
        } else {
            //未收藏，添加收藏
            GreenDaoHelper.addFavourite(getNowPlaying());
            EventBus.getDefault().post(new AudioFavouriteEvent(true));
        }
    }

    /**
     * 播放/暂停切换
     */
    public void playOrPause() {
        if (isStartState()) {
            pause();
        } else if (isPauseState()) {
            resume();
        }
    }

    /**
     * 加载当前index歌曲
     */
    public void play() {
        AudioBean bean = getPlaying(mQueueIndex);
        load(bean);
    }

    /**
     * 加载next index歌曲
     */
    public void next() {
        AudioBean bean = getNextPlaying();
        load(bean);
    }

    public void resume() {
        mAudioPlayer.resume();
    }

    public void pause() {
        mAudioPlayer.pause();
    }


    /**
     * 加载previous index歌曲
     */
    public void previous() {
        AudioBean bean = getPreviousPlaying();
        load(bean);
    }

    /***
     *  ============ 播放器控制和收藏  end =============
     */


    /**
     * ======== 对外获取播放信息 start=======
     */

    /**
     * 对外提供获取当前播放时间
     */
    public int getNowPlayTime() {
        return mAudioPlayer.getCurrentPosition();
    }

    /**
     * 对外提供获取总播放时间
     */
    public int getTotalPlayTime() {
        return mAudioPlayer.getCurrentPosition();
    }

    /**
     * 对外提供的获取当前歌曲信息
     */
    public AudioBean getNowPlaying() {
        return getPlaying(mQueueIndex);
    }


    /**
     * ======== 对外获取播放信息 end=======
     */

    /**
     *  =============== 事件处理 和资源释放  start==========
     */


    /**
     * 释放相关
     */
    public void release() {
        mAudioPlayer.release();
        EventBus.getDefault().unregister(this);
    }


    //插放完毕事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioCompleteEvent(
            AudioCompleteEvent event) {
        next();
    }

    //播放出错事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioErrorEvent(AudioErrorEvent event) {
        next();
    }
    /**
     *  =============== 事件处理 和资源释放  end==========
     */

}
