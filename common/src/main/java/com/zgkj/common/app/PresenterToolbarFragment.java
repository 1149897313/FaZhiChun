package com.zgkj.common.app;

import android.content.Context;

import com.zgkj.common.factory.presenter.BaseContract;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/21.
 * Descr:   持有MVP模式的ToolbarFragment碎片的基类的定义
 */

public abstract class PresenterToolbarFragment<T extends BaseContract.Presenter>
        extends ToolbarFragment implements BaseContract.View<T> {

    protected T mPresenter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 初始化Presenter对象
        initPresenter();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 释放持有的资源
        if (mPresenter != null){
            mPresenter.destroy();
        }
    }

    /**
     * 初始化Presenter调度者对象的抽象方法，由子类去实现
     *
     * @return
     */
    protected abstract T initPresenter();
}
