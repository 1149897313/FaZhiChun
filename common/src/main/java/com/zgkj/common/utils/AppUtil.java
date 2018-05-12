package com.zgkj.common.utils;

import com.zgkj.common.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/2/9.
 * Descr:   App辅助工具类
 */

public class AppUtil {

    private static List<Activity> mActivityList = new ArrayList<>();

    /**
     * 添加一个activity对象到集合中
     *
     * @param activity
     */
    public static void add(Activity activity){
        if (activity != null && !mActivityList.contains(activity)){
            mActivityList.add(activity);
        }
    }

    /**
     * 从集合中移除一个activity对象
     * @param activity
     */
    public static void remove(Activity activity){
        if (activity != null && mActivityList.contains(activity)){
            mActivityList.remove(activity);
        }
    }

    /**
     * 关闭所有actiivity对象
     */
    public static void finishAll(){
        for (Activity activity : mActivityList){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
        // 通知回收
        System.gc();
    }











}
