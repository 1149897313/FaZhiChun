package com.zgkj.common.widgets.load.view;

import android.content.Context;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/20.
 * Descr:   定义状态视图的基础抽象类
 */

public abstract class AbsView implements Serializable {

    private Context mContext;
    private View mRootView;
    private OnReloadListener mOnReloadListener;
    private OnViewChildClickListener mOnViewChildClickListener;
    private boolean mSuccessViewVisible;


    /**
     * 默认无参的构造方法
     */
    public AbsView() {
    }

    public AbsView(Context context, View view, OnReloadListener onReloadListener, OnViewChildClickListener onViewChildClickListener) {
        mContext = context;
        mRootView = view;
        mOnReloadListener = onReloadListener;
        mOnViewChildClickListener = onViewChildClickListener;
    }

    public AbsView setAbsView(Context context, View view, OnReloadListener onReloadListener, OnViewChildClickListener onViewChildClickListener) {
        mContext = context;
        mRootView = view;
        mOnReloadListener = onReloadListener;
        mOnViewChildClickListener = onViewChildClickListener;
        return this;
    }

    public View getRootView() {
        int layoutResId = getLayoutSourceId();
        // 此段代码用于成功View的初始化
        if (layoutResId == 0 && mRootView != null) {
            return mRootView;
        }
        // 其他视图初始化View的方式
        if (mRootView == null) {
            mRootView = View.inflate(mContext, layoutResId, null);
        }
        // 为View注册点事件
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnReloadListener != null){
                    mOnReloadListener.onReload(view);
                }

            }
        });
        // 为View的子视图注册点击事件
        if (mOnViewChildClickListener != null) {
            onViewChildClick(mRootView, mOnViewChildClickListener);
        }

        onViewCreated(mContext, mRootView);

        return mRootView;
    }


    public View obtainRootView() {
        if (mRootView == null) {
            mRootView = View.inflate(mContext, getLayoutSourceId(), null);
        }
        return mRootView;
    }

    public boolean getSuccessViewVisible() {
        return mSuccessViewVisible;
    }

    public void setSuccessViewVisible(boolean successViewVisible) {
        mSuccessViewVisible = successViewVisible;
    }


    /**
     * 实现复制克隆对象的方法（深拷贝）
     *
     * @return
     */
    public AbsView copy() {
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        Object object = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            object = objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return (AbsView) object;
    }


    public void onAttach(Context context, View view) {
    }

    public void onDetach() {
    }


    protected abstract int getLayoutSourceId();


    protected void onViewCreated(Context context, View view) {
    }

    protected void onViewChildClick(View view, OnViewChildClickListener onViewChildClickListener) {

    }


    public interface OnReloadListener extends Serializable {

        void onReload(View view);
    }


    public interface OnViewChildClickListener {

        void onChildClick(View view);
    }


}
