package com.zgkj.common.widgets.load.view;

import android.content.Context;
import android.view.View;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/20.
 * Descr:   显示成功状态的视图
 */

public class SuccessView extends AbsView {


    public SuccessView(Context context, View view, OnReloadListener onReloadListener, OnViewChildClickListener onViewChildClickListener) {
        super(context, view, onReloadListener, onViewChildClickListener);
    }

    @Override
    protected int getLayoutSourceId() {
        return 0;
    }

    public void hide(){
        obtainRootView().setVisibility(View.GONE);
    }

    public void show(){
        obtainRootView().setVisibility(View.VISIBLE);
    }

    public void showWithView(boolean successViewVisiable){
        obtainRootView().setVisibility(successViewVisiable ? View.VISIBLE : View.GONE);
    }















}
