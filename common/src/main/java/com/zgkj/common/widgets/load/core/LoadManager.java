package com.zgkj.common.widgets.load.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.common.widgets.load.view.SuccessView;

import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/20.
 * Descr:   视图加载管理类
 */

public class LoadManager<T> {

    private LoadLayout mLoadLayout;
    private Converter<T> mConverter;

    public LoadManager(Converter<T> converter, TargetContext targetContext,
                       AbsView.OnReloadListener onReloadListener,
                       AbsView.OnViewChildClickListener onViewChildClickListener,
                       LoadFactory.Builder builder) {
        mConverter = converter;
        Context context = targetContext.getContext();
        View contentView = targetContext.getContentView();
        ViewGroup.LayoutParams contentLayoutParams = contentView.getLayoutParams();
        // 初始化LoadLayout
        mLoadLayout = new LoadLayout(context, onReloadListener, onViewChildClickListener);
        // 先将加载成功的显示视图添加到布局中
        mLoadLayout.setupSuccessView(new SuccessView(context, contentView, onReloadListener, onViewChildClickListener));
        if (targetContext.getParentView() != null) {
            targetContext.getParentView().addView(mLoadLayout, targetContext.getChildIndex(), contentLayoutParams);
        }
        // 添加其他的状态视图
        initView(builder);
    }

    private void initView(LoadFactory.Builder builder) {
        List<AbsView> viewList = builder.getViewList();
        Class<? extends AbsView> defaultViewClass = builder.getDefaultViewClass();
        // 判断并将其他状态的碎片储存到集合中备用
        if (viewList != null && viewList.size() > 0) {
            for (AbsView absView : viewList) {
                mLoadLayout.setupView(absView);
            }
        }
        // 如果默认视图不为空则显示默认的View
        if (defaultViewClass != null) {
            mLoadLayout.showView(defaultViewClass);
        }
    }


    /**
     * 显示成功的视图
     */
    public void showSuccessView() {
        mLoadLayout.showView(SuccessView.class);
    }


    public void showStateView(Class<? extends AbsView> absClass) {
        mLoadLayout.showView(absClass);
    }


    public Class<? extends AbsView> getCurrentViewClass() {

        return mLoadLayout.getCurrentViewClass();
    }


    public LoadLayout getLoadLayout() {

        return mLoadLayout;
    }


}
