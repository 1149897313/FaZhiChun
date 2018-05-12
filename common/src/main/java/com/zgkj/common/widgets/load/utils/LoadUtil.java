package com.zgkj.common.widgets.load.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;

import com.zgkj.common.widgets.load.core.TargetContext;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/20.
 * Descr:   加载相关的工具类
 */

public class LoadUtil {

    /**
     * 判断是否处于主线程中
     *
     * @return
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }


    /**
     * 返回一个TargetContext对象
     *
     * @param target
     * @return
     */
    public static TargetContext getTargetContext(Object target) {
        Context context;
        ViewGroup contentParent;
        if (target instanceof Activity) {
            Activity activity = (Activity) target;
            context = activity;
            contentParent = activity.findViewById(android.R.id.content);
        } else if (target instanceof View) {
            View view = (View) target;
            contentParent = (ViewGroup) (view.getParent());
            context = view.getContext();
        } else {
            throw new IllegalArgumentException("The target must be within Activity, Fragment, View.");
        }
        int childIndex = 0;
        int childCount = contentParent == null ? 0 : contentParent.getChildCount();
        View contentView;
        if (target instanceof View) {
            contentView = (View) target;
            for (int i = 0; i < childCount; i++) {
                if (contentParent.getChildAt(i) == contentView) {
                    childIndex = i;
                    break;
                }
            }
        } else {
            contentView = contentParent != null ? contentParent.getChildAt(0) : null;
        }
        if (contentView == null) {
            throw new IllegalArgumentException(String.format("enexpected error when register LoadSir in %s",
                    target.getClass().getSimpleName()));
        }
        // 将内容视图先移除
        if (contentParent != null) {
            contentParent.removeView(contentView);
        }
        return new TargetContext(context, contentParent, contentView, childIndex);

    }


}
