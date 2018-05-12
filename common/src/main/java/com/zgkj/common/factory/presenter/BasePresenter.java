package com.zgkj.common.factory.presenter;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/21.
 * Descr:   基本的Presenter基类的定义
 */

public abstract class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter {

    // 定义一个视图对象，用于实现View层与Presenter层的双向绑定
    private T mView;


    /**
     * 构造方法，初始化化数据
     *
     * @param view
     */
    public BasePresenter(T view) {
        setView(view);
    }


    /**
     * 设置一个View，子类可以复写
     *
     * @param view
     */
    protected void setView(T view) {
        mView = view;
        // 像View层传递一个Presenter调度对象
        mView.setPresenter(this);
    }


    /**
     * 返回View，子类只拥有使用的权限，没有复写的权限
     *
     * @return
     */
    protected final T getView() {

        return mView;
    }


    /**
     * 开始调度之前触发
     */
    @Override
    public void start() {
        // 开始的时候进行Loading的调度
        T view = this.mView;
        if (view != null) {
            view.showLoading();
        }

    }


    /**
     * 销毁之前触发
     */
    @Override
    public void destroy() {
        T view = this.mView;
        mView = null;
        if (view != null) {
            // 释放View层的Presenter对象资源
            view.setPresenter(null);
        }

    }

}
