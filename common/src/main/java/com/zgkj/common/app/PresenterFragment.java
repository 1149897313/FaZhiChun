package com.zgkj.common.app;

import android.content.Context;

import com.zgkj.common.factory.presenter.BaseContract;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/21.
 * Descr:   持有MVP模式的Fragment碎片的基类的定义
 */

public abstract class PresenterFragment<T extends BaseContract.Presenter>
        extends Fragment implements BaseContract.View<T> {

    protected T mPresenter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 初始化Presenter
        initPresenter();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }


    @Override
    public void setPresenter(T presenter) {
        mPresenter = presenter;
    }

    /**
     * 实现初始化Presenter调度者对象的抽象方法，由子类去实现
     *
     * @return
     */
    protected abstract T initPresenter();
}
