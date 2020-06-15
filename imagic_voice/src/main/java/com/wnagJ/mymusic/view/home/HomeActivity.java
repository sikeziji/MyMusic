package com.wnagJ.mymusic.view.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.wnagJ.mymusic.R;
import com.wnagJ.mymusic.model.CHANNEL;
import com.wnagJ.mymusic.model.login.LoginEvent;
import com.wnagJ.mymusic.utils.UserManager;
import com.wnagJ.mymusic.view.home.adapter.HomePagerAdapter;
import com.wnagJ.mymusic.view.login.LoginActivity;
import com.wnagj.lib_audio.app.AudioHelper;
import com.wnagj.lib_audio.mediaplayer.model.AudioBean;
import com.wnagj.lib_common_ui.base.BaseActivity;
import com.wnagj.lib_common_ui.pager_indeictor.ScaleTransitionPagerTitleView;
import com.wnagj.lib_image_loader.app.ImageLoaderManager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private static final CHANNEL[] CHANNELS =
            new CHANNEL[]{CHANNEL.MY, CHANNEL.DISCORY, CHANNEL.FRIEND};


    private DrawerLayout mDrawerLayout;
    private View mToggleView;
    private View mSearchView;
    private ViewPager mViewPager;
    private HomePagerAdapter mAdapter;


    private View unLogginLayout;
    private ImageView mPhotoView;
    private ArrayList<AudioBean> mLists = new ArrayList<>();


    /*
     * data
     */
//    private ArrayList<AudioBean> mLists = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        AudioBean audioBean0 = new AudioBean("100001", "http://sp-sycdn.kuwo.cn/resource/n2/85/58/433900159.mp3",
                "以你的名字喊我", "周杰伦", "七里香", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698076304&di=e6e99aa943b72ef57b97f0be3e0d2446&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201401%2F04%2F20140104170315_XdG38.jpeg",
                "4:30");
        AudioBean audioBean1 = new AudioBean("100002", "http://sq-sycdn.kuwo.cn/resource/n1/98/51/3777061809.mp3", "勇气",
                "梁静茹", "勇气", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698193627&di=711751f16fefddbf4cbf71da7d8e6d66&imgtype=jpg&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D213168965%2C1040740194%26fm%3D214%26gp%3D0.jpg",
                "4:40");
        AudioBean audioBean2 = new AudioBean("100003", "http://sp-sycdn.kuwo.cn/resource/n2/52/80/2933081485.mp3", "灿烂如你",
                "汪峰", "春天里", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698239736&di=3433a1d95c589e31a36dd7b4c176d13a&imgtype=0&src=http%3A%2F%2Fpic.zdface.com%2Fupload%2F201051814737725.jpg",
                "3:20");
        AudioBean audioBean3 = new AudioBean("100004", "http://sr-sycdn.kuwo.cn/resource/n2/33/25/2629654819.mp3", "小情歌",
                "五月天", "小幸运", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698289780&di=5146d48002250bf38acfb4c9b4bb6e4e&imgtype=0&src=http%3A%2F%2Fpic.baike.soso.com%2Fp%2F20131220%2Fbki-20131220170401-1254350944.jpg",
                "2:45");

        if (!mLists.contains(audioBean0)) {
            mLists.add(audioBean0);
        }
        if (!mLists.contains(audioBean1)) {
            mLists.add(audioBean1);
        }
        if (!mLists.contains(audioBean2)) {
            mLists.add(audioBean2);
        }
        if (!mLists.contains(audioBean3)) {
            mLists.add(audioBean3);
        }

        AudioHelper.startMusicService(mLists);
    }

    private void initView() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggleView = findViewById(R.id.toggle_view);
        mToggleView.setOnClickListener(this);
        mSearchView = findViewById(R.id.search_view);
        mSearchView.setOnClickListener(this);


        //初始化adpater
        mAdapter = new HomePagerAdapter(getSupportFragmentManager(), CHANNELS);
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(mAdapter);
        initMagicIndicator();


        unLogginLayout = findViewById(R.id.unloggin_layout);
        unLogginLayout.setOnClickListener(this);
        mPhotoView = findViewById(R.id.avatr_view);
        findViewById(R.id.exit_layout).setOnClickListener(this);

    }

    private void initMagicIndicator() {
        //初始化MagicIndicator
        MagicIndicator magicIndicator = findViewById(R.id.magic_indicator);
        //设置MagicIndicator的背景颜色
        magicIndicator.setBackgroundColor(Color.WHITE);
        //设置Viewpager指示器
        CommonNavigator commonNavigator = new CommonNavigator(this);
        //设置自适应title
        commonNavigator.setAdjustMode(true);
        //设置adapter
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            /**
             * 获取数量
             * @return
             */
            @Override
            public int getCount() {
                return CHANNELS == null ? 0 : CHANNELS.length;
            }


            /**
             * 后去标题信息
             * @param context
             * @param index
             * @return
             */
            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                //初始化带文本的指示器 ，设置为自定义指示器
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                //设置文字
                simplePagerTitleView.setText(CHANNELS[index].getKey());
                //设置字体大小
                simplePagerTitleView.setTextSize(19);
                //设置
                simplePagerTitleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                //设置默认颜色
                simplePagerTitleView.setNormalColor(Color.parseColor("#999999"));
                //设置选中颜色
                simplePagerTitleView.setSelectedColor(Color.parseColor("#333333"));
                //设置点击事件
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            /**
             * 设置知悉其
             * @param context
             * @return
             */
            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }

            /**
             * 设置title占比
             * @param context
             * @param index
             * @return
             */
            @Override
            public float getTitleWeight(Context context, int index) {
                return 1.0f;
            }
        });
        //设置setNavigator
        magicIndicator.setNavigator(commonNavigator);
        //绑定适配器
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit_layout:
                finish();
                System.exit(0);
                break;
            case R.id.unloggin_layout:
                if (!UserManager.getInstance().hasLogined()) {
                    LoginActivity.start(this);
                } else {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
                break;
            case R.id.toggle_view:
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
        }
    }

    /**
     * 处理登陆事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {
        unLogginLayout.setVisibility(View.GONE);
        mPhotoView.setVisibility(View.VISIBLE);
        ImageLoaderManager.getInstance()
                .displayImageForCircle(mPhotoView, UserManager.getInstance().getUser().data.photoUrl);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
