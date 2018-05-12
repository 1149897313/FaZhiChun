package com.zgkj.common.factory.data;

import android.support.annotation.StyleRes;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/21.
 * Descr:   定义一个数据源的回调接口
 */

public interface DataSource {

    /**
     * 同时包括了成功和失败的接口
     *
     * @param <T> 服务器返回的泛型数据
     */
    interface Callback<T> extends SucceedCallback<T>, FailedCallback {

    }


    /**
     * 网络请求成功回调接口的定义
     *
     * @param <T> 服务器返回的泛型数据
     */
    interface SucceedCallback<T> {

        /**
         * 服务器返回数据成功
         *
         * @param t
         */
        void onDataLoadSucceed(T t);

    }

    /**
     * 网络请求失败回调接口的定义
     */
    interface FailedCallback {


        /**
         * 服务器返回数据错误
         *
         * @param strResId
         */
        void onError(@StyleRes int strResId);


        /**
         * 网络连接失败
         *
         * @param strResId
         */
        void onFailed(@StyleRes int strResId);

    }


}
