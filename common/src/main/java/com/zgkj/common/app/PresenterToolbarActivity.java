package com.zgkj.common.app;

import com.zgkj.common.factory.presenter.BaseContract;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/21.
 * Descr:   持有MVP模式的Activity基类的定义
 */

public abstract class PresenterToolbarActivity<T extends BaseContract.Presenter> extends ToolbarActivity
        implements BaseContract.View<T> {


    // 定义一个泛型类型的Presenter对象
    protected T mPresenter;


    @Override
    protected void init() {
        super.init();
        // 初始化Presenter对象
        initPresenter();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 界面关闭时进行释放Presenter持有资源的操作
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }

    /**
     * View层中赋值Presenter
     *
     * @param presenter
     */
    @Override
    public void setPresenter(T presenter) {
        mPresenter = presenter;
    }

    /**
     * 初始化并返回一个具体的Presenter调度对象，由子类去实现
     *
     * @return
     */
    protected abstract T initPresenter();
}
