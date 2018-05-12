package com.zgkj.common.widgets.load.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/20.
 * Descr:   目标对象类的定义
 */

public class TargetContext {

    private Context mContext;
    private ViewGroup mParentView;
    private View mContentView;
    private int mChildIndex;

    public TargetContext(Context context, ViewGroup parentView, View contentView, int childIndex) {
        mContext = context;
        mParentView = parentView;
        mContentView = contentView;
        mChildIndex = childIndex;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public ViewGroup getParentView() {
        return mParentView;
    }

    public void setParentView(ViewGroup parentView) {
        mParentView = parentView;
    }

    public View getContentView() {
        return mContentView;
    }

    public void setContentView(View contentView) {
        this.mContentView = contentView;
    }

    public int getChildIndex() {
        return mChildIndex;
    }

    public void setChildIndex(int childIndex) {
        mChildIndex = childIndex;
    }
}
