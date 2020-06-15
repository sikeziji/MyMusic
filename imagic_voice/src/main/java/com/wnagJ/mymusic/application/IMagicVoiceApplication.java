package com.wnagJ.mymusic.application;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.wnagj.lib_audio.app.AudioHelper;
import com.wnagj.lib_share.ShareManager;

public class IMagicVoiceApplication extends Application {
    private static IMagicVoiceApplication mApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        //视频SDK初始化
//        VideoHelper.init(this);
//        //音频SDK初始化
        AudioHelper.init(this);
//        //分享SDK初始化
        ShareManager.initSDK(this);
//        //更新组件下载
//        UpdateHelper.init(this);
        //ARouter初始化
        ARouter.init(this);
    }

    public static IMagicVoiceApplication getInstance() {
        return mApplication;
    }
}
