package com.wnagj.lib_audio.mediaplayer.view;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wnagj.lib_audio.R;
import com.wnagj.lib_audio.mediaplayer.core.AudioController;
import com.wnagj.lib_audio.mediaplayer.events.AudioLoadEvent;
import com.wnagj.lib_audio.mediaplayer.events.AudioPauseEvent;
import com.wnagj.lib_audio.mediaplayer.events.AudioProgressEvent;
import com.wnagj.lib_audio.mediaplayer.events.AudioStartEvent;
import com.wnagj.lib_audio.mediaplayer.model.AudioBean;
import com.wnagj.lib_image_loader.app.ImageLoaderManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 播放器底部View
 */
public class BottomMusicView extends RelativeLayout {

  private Context mContext;

  /*
   * View
   */
  private ImageView mLeftView;
  private TextView mTitleView;
  private TextView mAlbumView;
  private ImageView mPlayView;
  private ImageView mRightView;
  /*
   * data
   */
  private AudioBean mAudioBean;
  private ObjectAnimator mAnimator;

  /**
   * 构造方法
   * @param context
   */
  public BottomMusicView(Context context) {
    this(context, null);
  }

  public BottomMusicView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BottomMusicView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mContext = context;
    //注册EventBus
    EventBus.getDefault().register(this);
    initView();
  }


  /**
   * 初始化视图
   */
  private void initView() {
    //初始化根视图
    View rootView = LayoutInflater.from(mContext).inflate(R.layout.bottom_view, this);
    //设置点击事件
    rootView.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        System.out.println("跳转页面的点击事件");
        //跳到音乐播放Activitity
        MusicPlayerActivity.start((Activity) mContext);
      }
    });
    mLeftView = rootView.findViewById(R.id.album_view);
    //设置旋转动画
    mAnimator = ObjectAnimator.ofFloat(mLeftView, View.ROTATION.getName(), 0f, 360);
    //设置周期时长
    mAnimator.setDuration(10000);
    //设置内插器
    mAnimator.setInterpolator(new LinearInterpolator());
    //设置重复次数 无限循环
    mAnimator.setRepeatCount(-1);
    //开启动画
    mAnimator.start();

    mTitleView = rootView.findViewById(R.id.audio_name_view);
    mAlbumView = rootView.findViewById(R.id.audio_album_view);
    mPlayView = rootView.findViewById(R.id.play_view);
    //播放的点击事件
    mPlayView.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        System.out.println("进入播放的点击事件");
        //处理播放暂停事件
        AudioController.getInstance().playOrPause();
      }
    });
    mRightView = rootView.findViewById(R.id.show_list_view);
    //列表框的点击事件
    mRightView.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        System.out.println("进入列表框的点击事件");
        //显示音乐列表对话框
        MusicListDialog dialog = new MusicListDialog(mContext);
        dialog.show();
      }
    });
  }

  /**
   * 窗口销毁时调用 ， 解除EventBus的注册
   */
  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    EventBus.getDefault().unregister(this);
  }


  /**
   * eventBus 设置问在主线程中
   *
   * 加载事件
   *
   * @param event
   */
  @Subscribe(threadMode = ThreadMode.MAIN) public void onAudioLoadEvent(AudioLoadEvent event) {
    //更新当前view为load状态
    mAudioBean = event.mAudioBean;
    showLoadView();
  }
  /**
   * eventBus 设置问在主线程中
   *
   * 暂停事件
   *
   * @param event
   */
  @Subscribe(threadMode = ThreadMode.MAIN) public void onAudioPauseEvent(AudioPauseEvent event) {
    //更新当前view为暂停状态
    showPauseView();
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void onAudioStartEvent(AudioStartEvent event) {
    //更新当前view为播放状态
    showPlayView();
  }
  /**
   * eventBus 设置问在主线程中
   *
   * 更新播放进度事件
   *
   * @param event
   */
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onAudioProgrssEvent(AudioProgressEvent event) {
    //更新当前view的播放进度
  }

  /**
   * 显示加载视图
   */
  private void showLoadView() {
    //目前loading状态的UI处理与pause逻辑一样，分开为了以后好扩展
    if (mAudioBean != null) {
      ImageLoaderManager.getInstance().displayImageForCircle(mLeftView, mAudioBean.albumPic);
      mTitleView.setText(mAudioBean.name);
      mAlbumView.setText(mAudioBean.album);
      mPlayView.setImageResource(R.mipmap.note_btn_pause_white);
    }
  }

  /**
   * 显示暂停动画
   */
  private void showPauseView() {
    if (mAudioBean != null) {
      mPlayView.setImageResource(R.mipmap.note_btn_play_white);
    }
  }

  /**
   * 显示播放动画
   */
  private void showPlayView() {
    if (mAudioBean != null) {
      mPlayView.setImageResource(R.mipmap.note_btn_pause_white);
    }
  }
}
