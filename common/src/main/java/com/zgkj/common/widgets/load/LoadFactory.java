package com.zgkj.common.widgets.load;

import android.support.annotation.NonNull;

import com.zgkj.common.widgets.load.core.Converter;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.core.TargetContext;
import com.zgkj.common.widgets.load.utils.LoadUtil;
import com.zgkj.common.widgets.load.view.AbsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/20.
 * Descr:   加载状态的工厂类（单例模式构建）
 */

public class LoadFactory {

    // 单例对象
    private static volatile LoadFactory sInstance;
    private Builder mBuilder;

    /**
     * 私有化的构造方法，初始化对象
     */
    private LoadFactory() {
        mBuilder = new Builder();
    }

    // 返回单例对象
    public static LoadFactory getInstance() {
        if (sInstance == null) {
            synchronized (LoadFactory.class) {
                if (sInstance == null) {
                    sInstance = new LoadFactory();
                }
            }
        }
        return sInstance;
    }

    public LoadFactory addView(@NonNull AbsView absView) {
        mBuilder.addView(absView);
        return this;
    }

    public LoadFactory setDefaultViewClass(@NonNull Class<? extends AbsView> absClass) {
        mBuilder.setDefaultViewClass(absClass);
        return this;
    }

    public LoadManager register(Object target) {
        return register(target, null, null);
    }

    public LoadManager register(Object target, AbsView.OnReloadListener onReloadListener) {
        return register(target, onReloadListener, null, null);
    }

    public LoadManager register(Object target, AbsView.OnViewChildClickListener onViewChildClickListener) {
        return register(target, null, onViewChildClickListener, null);
    }

    public LoadManager register(Object target, AbsView.OnReloadListener onReloadListener,
                                AbsView.OnViewChildClickListener onViewChildClickListener) {

        return register(target, onReloadListener, onViewChildClickListener, null);
    }

    /**
     * 实现绑定目标对象注册点击事件的方法
     *
     * @param target
     * @param onReloadListener
     * @param converter
     * @param <T>
     * @return
     */
    public <T> LoadManager register(Object target,
                                    AbsView.OnReloadListener onReloadListener,
                                    AbsView.OnViewChildClickListener onViewChildClickListener,
                                    Converter<T> converter) {

        TargetContext targetContext = LoadUtil.getTargetContext(target);
        return new LoadManager(converter, targetContext, onReloadListener, onViewChildClickListener, mBuilder);
    }

    public static class Builder {

        private List<AbsView> mViewList;
        private Class<? extends AbsView> mDefaultViewClass;

        /**
         * 构造方法，初始化数据
         */
        public Builder() {
            mViewList = new ArrayList<>();
        }

        public void addView(@NonNull AbsView absView) {
            if (mViewList != null && !mViewList.contains(absView)) {
                mViewList.add(absView);
            }
        }

        public List<AbsView> getViewList() {
            return mViewList;
        }

        public void setDefaultViewClass(@NonNull Class<? extends AbsView> absClass) {
            mDefaultViewClass = absClass;
        }

        public Class<? extends AbsView> getDefaultViewClass() {
            return mDefaultViewClass;
        }
    }


}
