package com.wnagj.lib_network.okhttp.request;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestParams {

    public ConcurrentHashMap<String ,String> urlParams = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String ,Object> fileParams = new ConcurrentHashMap<>();

    /**
     * Constructs a new empty {@code RequestParams} instance.
     */
    public RequestParams() {
        this((Map<String, String>) null);
    }

    /**
     * 实现Request参数的方法
     *
     * @param source 传入map
     *
     */
    public RequestParams(Map<String, String> source) {
        if (source != null) {
            for (Map.Entry<String, String> entry : source.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }
    /**
     * 实现Request参数的方法
     *
     * @param key 传入map的Key
     * @param value 传入map的value
     */
    public RequestParams(final String key, final String value) {
        this(new HashMap<String, String>() {
            {
                put(key, value);
            }
        });
    }

    /**
     * 将key和value放入到请求中
     *
     * @param key   the key name for the new param.
     * @param value the value string for the new param.
     */
    public void put(String key, String value) {
        if (key != null && value != null) {
            urlParams.put(key, value);
        }
    }

    /**
     * 将key和value放入文件下载或上传的请求中
     * @param key
     * @param object
     * @throws FileNotFoundException
     */
    public void put(String key, Object object) throws FileNotFoundException {

        if (key != null) {
            fileParams.put(key, object);
        }
    }

    /**
     * 判断是否有参数
     * @return
     */
    public boolean hasParams() {
        if(urlParams.size() > 0 || fileParams.size() > 0){

            return true;
        }
        return false;
    }

}
