package com.zgkj.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zgkj.factory.persistent.Account;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/14.
 * Descr:   公共的工厂管理类
 */

public class Factory {

    // 单例对象
    private static final Factory sInstance;

    // 定义一个Gson对象
    private final Gson mGson;

    static {
        sInstance = new Factory();
    }

    /**
     * 私有化的构造方法，初始化数据
     */
    private Factory(){
        // 初始化Gson对象
        mGson = new GsonBuilder()
                // 智能解析为NULL的数据
                .serializeNulls()
                // 设置时间格式
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                // 构建对象
                .create();
    }





    /**
     * 返回一个全局的Gson对象
     *
     * @return
     */
    public static Gson getGson(){

        return sInstance.mGson;
    }


    /**
     * 初始化加载程序的相关数据
     */
    public static void setup(){
        // 加载账户相关的信息
        Account.load();






    }













}
