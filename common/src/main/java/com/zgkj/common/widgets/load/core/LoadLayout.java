package com.zgkj.common.widgets.load.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

import com.zgkj.common.widgets.load.utils.LoadUtil;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.common.widgets.load.view.SuccessView;

import java.util.HashMap;
import java.util.Map;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/20.
 * Descr:   自定义视图显示控件
 */

public class LoadLayout extends FrameLayout {

    private Context mContext;
    private AbsView.OnReloadListener mOnReloadListener;
    private AbsView.OnViewChildClickListener mOnViewChildClickListener;
    private Map<Class<? extends AbsView>, AbsView> mViewMap = new HashMap<>();

    private Class<? extends AbsView> mCurrentViewClass;
    private Class<? extends AbsView> mPreViewClass;

    private static final int ABSVIEW_CUSTOM_INDEX = 1;


    public LoadLayout(@NonNull Context context) {
        super(context);
    }

    public LoadLayout(@NonNull Context context, AbsView.OnReloadListener onReloadListener, AbsView.OnViewChildClickListener onViewChildClickListener) {
        this(context);
        mContext = context;
        mOnReloadListener = onReloadListener;
        mOnViewChildClickListener = onViewChildClickListener;
    }

    public void setupSuccessView(AbsView absView) {
        addAbsView(absView);
        View successView = absView.getRootView();
        successView.setVisibility(View.GONE);
        // 添加到布局中
        addView(successView);
        // 标记状态
        mCurrentViewClass = SuccessView.class;
    }

    public void setupView(AbsView absView) {
        AbsView cloneAbsView = absView.copy();
        if (cloneAbsView != null) {
            cloneAbsView.setAbsView(mContext, null, mOnReloadListener, mOnViewChildClickListener);
            addAbsView(cloneAbsView);
        }
    }

    private void addAbsView(AbsView absView) {
        // 如果不存在集合中则进行添加
        if (!mViewMap.containsKey(absView.getClass())) {
            mViewMap.put(absView.getClass(), absView);
        }
    }

    public void showView(Class<? extends AbsView> absClass) {
        checkViewExist(absClass);
        // 否则显示对应的状态View
        if (LoadUtil.isMainThread()) {
            // 如果处于主线程中
            showStateView(absClass);
        } else {
            postToMainThread(absClass);
        }
    }

    /**
     * 判断显示的视图是否存在于集合中，如果不存在则抛出异常
     *
     * @param absClass
     * @return
     */
    private void checkViewExist(Class<? extends AbsView> absClass) {
        if (!mViewMap.containsKey(absClass)) {
            throw new IllegalArgumentException(String.format("The Callback (%s) is nonexistent.",
                    absClass.getClass().getSimpleName()));
        }

    }


    /**
     * 在主线程中执行
     *
     * @param absClass
     */
    private void postToMainThread(final Class<? extends AbsView> absClass) {
        post(new Runnable() {
            @Override
            public void run() {
                showStateView(absClass);
            }
        });
    }


    /**
     * 切换显示状态View
     *
     * @param absClass
     */
    private void showStateView(Class<? extends AbsView> absClass) {
        if (mPreViewClass != null) {
            if (absClass == mPreViewClass) {
                return;
            }
            mViewMap.get(mPreViewClass).onDetach();
        }
        // 移除
        if (getChildCount() > 1) {
            removeViewAt(ABSVIEW_CUSTOM_INDEX);
        }
        for (Class key : mViewMap.keySet()) {
            if (key == absClass) {
                SuccessView successView = (SuccessView) mViewMap.get(SuccessView.class);
                // 如果是显示成功页
                if (key == SuccessView.class) {
                    successView.show();
                } else {
                    successView.showWithView(mViewMap.get(key).getSuccessViewVisible());
                    View rootView = mViewMap.get(key).getRootView();
                    // 添加到布局中
                    addView(rootView);
                    mViewMap.get(key).onAttach(mContext, rootView);
                }

                mPreViewClass = absClass;
            }
        }
        mCurrentViewClass = absClass;
    }


    public Class<? extends AbsView> getCurrentViewClass() {

        return mCurrentViewClass;
    }


}
