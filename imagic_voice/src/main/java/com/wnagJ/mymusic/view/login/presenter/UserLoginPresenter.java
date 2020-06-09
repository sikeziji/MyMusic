package com.wnagJ.mymusic.view.login.presenter;

import com.google.gson.Gson;
import com.wnagJ.mymusic.api.MockData;
import com.wnagJ.mymusic.api.RequestCenter;
import com.wnagJ.mymusic.model.login.LoginEvent;
import com.wnagJ.mymusic.model.user.User;
import com.wnagJ.mymusic.utils.UserManager;
import com.wnagJ.mymusic.view.login.inter.IUserLoginPresenter;
import com.wnagJ.mymusic.view.login.inter.IUserLoginView;
import com.wnagj.lib_network.okhttp.listener.DisposeDataListener;

import org.greenrobot.eventbus.EventBus;

public class UserLoginPresenter implements IUserLoginPresenter, DisposeDataListener {

    private IUserLoginView mIView;

    public UserLoginPresenter(IUserLoginView iView) {
        mIView = iView;
    }

    @Override
    public void login(String username, String password) {
        mIView.showLoadingView();
        RequestCenter.login( this);
    }

    @Override
    public void onSuccess(Object responseObj) {
        mIView.hideLoadingView();
        User user = (User) responseObj;
        UserManager.getInstance().setUser(user);
        //发送登陆Event
        EventBus.getDefault().post(new LoginEvent());
        mIView.finishActivity();
    }

    @Override
    public void onFailure(Object reasonObj) {
        mIView.hideLoadingView();
        onSuccess(new Gson().fromJson(MockData.LOGIN_DATA, User.class));
        mIView.showLoginFailedView();
    }
}
