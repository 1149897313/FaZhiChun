package com.zgkj.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.gyf.barlibrary.ImmersionBar;
import com.zgkj.common.R;
import com.zgkj.common.utils.AppUtil;

import java.util.List;

/**
 * Descr:   Activity活动的基类
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/1/19.
 */

public abstract class Activity extends AppCompatActivity {

    // 目标打印标识
    protected static String TAG;

    // 上下文对象
    protected Context mContext;


    /**
     * 启动Activity时执行的方法
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 如果能正常接收上一界面传递过来的参数则显示当前界面
        // 否则关闭当前界面的显示
        // 默认无参数传递的情况下是正常显示当前界面的
        if (initArgs(getIntent().getExtras())) {
            //添加所有页面
            AppUtil.add(this);
            // 初始化目标打印标识
            TAG = this.getClass().getSimpleName();

            // 初始化上下文对象
            mContext = this;

            // 初始化沉浸式状态栏框架
            ImmersionBar.with(this)
                    .statusBarColor(R.color.trans)
                    .statusBarDarkFont(true)
                    .init();

            // 加载布局界面
            setContentView(getLayoutSourceId());

            // 初始化控件之前调用
            init();

            // 初始化布局控件
            initWidgets();

            // 初始化数据
            initDatas();

        } else {
            finish();
        }
    }


    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        ImmersionBar.with(this).destroy();

        AppUtil.remove(this);
    }
    /**
     * 初始化接收上一界面传递过来的参数
     *
     * @param bundle 用于接收参数的Bundle对象
     * @return 返回true表示接收参数成功，返回false表示接收参数失败（默认返回true）
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }


    /**
     * 初始化获取界面布局资源ID（子类必须实现）
     *
     * @return 界面布局资源的ID
     */
    protected abstract int getLayoutSourceId();


    /**
     * 初始化控件之前调用
     */
    protected void init() {
    }


    /**
     * 初始化布局控件
     */
    protected void initWidgets() {
    }


    /**
     * 初始化数据
     */
    protected void initDatas() {
    }


    /**
     * Activity界面点击导航标题栏返回按钮触发的回调方法
     *
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        // 如果用户按下则关闭Activity显示界面
        finish();
        return super.onSupportNavigateUp();
    }


    /**
     * Activity界面返回按键触发的回调方法
     */
    @Override
    public void onBackPressed() {
        // 第一步，获取到Activity关联的所有Fragment碎片对象
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

        // 第二步，遍历所有Fragment碎片对象，并查看每个Fragment碎片对象的返回按键事件是否为自己处理
        for (Fragment fragment : fragmentList) {
            // 判断Fragment对象是否为我们自己定义的能够处理的Fragment碎片对象类型
            if (fragment instanceof com.zgkj.common.app.Fragment) {
                // 判断当前的Fragment碎片对象是否自己处理了返回按键事件
                // 如果自己处理了则直接返回，不需要Activity再进行处理
                if (((com.zgkj.common.app.Fragment) fragment).onBackPressed()) {
                    return;
                }
            }
        }
        super.onBackPressed();
        // 关闭Activity显示界面
        finish();
    }
}
