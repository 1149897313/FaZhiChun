package com.zgkj.common.factory.presenter;

import android.support.annotation.StringRes;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/21.
 * Descr:   MVP模式中基本契约接口的定义
 */

public interface BaseContract {

    /**
     * 基本的View视图界面功能接口的定义
     *
     * @param <T>
     */
    interface View<T extends Presenter> {

        /**
         * 显示一般的错误
         *
         * @param strResId
         */
        void showError(@StringRes int strResId);


        /**
         * 显示一条网络错误
         *
         * @param strResId
         */
        void showNetError(@StringRes int strResId);


        /**
         * 显示加载进度条
         */
        void showLoading();


        /**
         * 初始化一个Presenter对象
         *
         * @param t
         */
        void setPresenter(T t);

    }


    /**
     * 基本的调度者功能接口的定义
     */
    interface Presenter {

        /**
         * 共用的开始触发
         */
        void start();


        /**
         * 共用的销毁触发
         */
        void destroy();


    }


}
