package com.zgkj.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.gyf.barlibrary.ImmersionBar;
import com.zgkj.common.http.AsyncHttpResponse;

import java.lang.reflect.Type;

/**
 * Descr:   Fragment碎片的基类
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/1/19.
 */

public abstract class Fragment extends android.support.v4.app.Fragment {

    // 目标打印标识
    protected static String TAG;

    // 上下文对象
    protected Context mContext;

    // 根视图对象
    private View mRootView;

    // 创建沉浸式状态栏的对象
    protected ImmersionBar mImmersionBar;


    /**
     * Fragment碎片与Activity活动建立关联时执行的方法
     *
     * @param context 与Fragment碎片建议关联的Activity活动的上下文对象
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // 初始化打印标识
        TAG = this.getClass().getSimpleName();

        // 初始化上下文对象
        mContext = context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化接收参数
        initArgs(getArguments());
    }

    /**
     * 创建加载Fragment碎片布局视图时执行的方法
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 如果根视图对象为空则加载布局
        // 否则移除根视图对象
        if (mRootView == null) {

            mRootView = inflater.inflate(getLayoutSourceId(), container, false);
            // 初始化布局控件
            initWidgets(mRootView);

        } else {
            // 将根视图对象从他的父类中移除
            if (mRootView.getParent() != null) {
                ((ViewGroup) mRootView.getParent()).removeView(mRootView);
            }
        }

        return mRootView;
    }


    /**
     * 在Fragment碎片执行onCreateView()方法后触发执行的方法（视图创建完成时）
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        // 初始化沉浸式状态栏
//        ImmersionBar.with(this)
//                .statusBarColor(R.color.trans)
//                .statusBarDarkFont(true).init();

        // 初始化数据
        initDatas();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        // 销毁沉浸式状态栏
//        ImmersionBar.with(this).destroy();
    }

    /**
     * 初始化接收上一界面传递过来的参数
     *
     * @param bundle 用于接收参数的Bundle对象
     */
    protected void initArgs(Bundle bundle) {

    }


    /**
     * 初始化获取界面布局资源ID（子类必须实现）
     *
     * @return 界面布局资源的ID
     */
    protected abstract int getLayoutSourceId();


    /**
     * 初始化布局控件
     *
     * @param rootView 根视图对象
     */
    protected void initWidgets(View rootView) {
    }


    /**
     * 初始化数据
     */
    protected void initDatas() {
    }


    /**
     * 返回按键触发事件时执行
     *
     * @return true表示当前的按键触发事件在本Fragment碎片界面已经处理，Activity活动不需要去处理
     * false表示在本界面没有处理，需要返回给Activity活动帮助处理
     * 默认情况下交给Activity活动来进行处理返回事件
     */
    public boolean onBackPressed() {

        return false;
    }

    private <T> T getAnalysis(AsyncHttpResponse response, Type type, String log) {
        return null;
    }
}
