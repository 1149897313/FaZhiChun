package com.zgkj.fazhichun.helper;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zgkj.common.app.Fragment;

import java.util.List;

/**
 * Descr:   用于碎片切换的辅助工具类（单例模式构建）
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2017/12/26.
 */

public class FragmentController {

    // 创建单例对象
    private static volatile FragmentController sInstance;

    // 创建碎片的管理对象
    private FragmentManager mFragmentManager;

    // 创建容器的Id对象
    private @IdRes int mContainerId;

    // 创建存放碎片的集合对象
    private List<Fragment> mFragmentList;

     /**
     * 私有化的构造方法
     */
    private FragmentController(){

    }

    /**
     * 初始化单例对象
     *
     * @return
     */
    public static FragmentController getInstance(){
        if (sInstance == null){
            synchronized (FragmentController.class){
                if (sInstance == null){
                    sInstance = new FragmentController();
                }
            }
        }

        return sInstance;
    }

    /**
     * 初始化参数
     * @param fragmentManager
     * @param containerId
     */
    public FragmentController init(FragmentManager fragmentManager, @IdRes int containerId){

        this.mFragmentManager = fragmentManager;
        this.mContainerId = containerId;

        return sInstance;
    }

    /**
     * 初始化碎片对象
     */
    public void setFragments(List<Fragment> fragments){

        mFragmentList = fragments;

        // 开启一个事务
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        // 循环遍历将Fragment碎片对象添加到事务中
        for (Fragment fragment : mFragmentList){
            if (fragment != null){
                fragmentTransaction.add(mContainerId, fragment);
            }
        }

        // 提交事务
        fragmentTransaction.commit();
    }



    /**
     * 创建显示fragment碎片对象的方法
     */
    public void showFragment(int position) {
        // 调用隐藏方法
        hideFragment();

        Fragment fragment = mFragmentList.get(position);
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (fragment != null) {
            ft.show(fragment);
        }
        ft.commitAllowingStateLoss();
    }

    /**
     * 创建隐藏fragment碎片的方法
     */
    private void hideFragment() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        for (Fragment fragment : mFragmentList) {
            if (fragment != null) {
                ft.hide(fragment);
            }
        }
        ft.commitAllowingStateLoss();
    }

    /**
     * 创建返回fragment碎片对象的方法
     */
    public Fragment getFragment(int position) {

        return mFragmentList.get(position);
    }


}
