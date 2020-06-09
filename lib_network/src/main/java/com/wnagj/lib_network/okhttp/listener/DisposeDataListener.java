package com.wnagj.lib_network.okhttp.listener;

public interface DisposeDataListener {


    /**
     * 请求成功回调事件处理
     * @param resposeObj
     */
    void onSuccess(Object resposeObj);


    /**
     * 请求失败回调事件处理
     * @param reasonObj
     */
    void onFailure(Object reasonObj);
}
