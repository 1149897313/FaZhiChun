package com.zgkj.common.utils;

import android.content.Context;

/**
 * Descr:   屏幕显示相关的工具类
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/1/24.
 */

public class DisplayUtil {


    /**
     * 私有化的构造方法，禁止通过new的方式创建对象
     */
    private DisplayUtil(){

        // 抛出不支持的操作异常
        throw new UnsupportedOperationException("cannot be instantiated!");
    }


    /**
     * 将px值转换为dp值，保证尺寸大小不变
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        // 得到设备的密度
        float density = context.getResources().getDisplayMetrics().density;

        return (int) (pxValue / density + 0.5f);
    }


    /**
     * 将dp值转换为px值，保证尺寸大小不变
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }


    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        // 得到屏幕的缩放密度
        float scaleDensity = context.getResources().getDisplayMetrics().scaledDensity;

        return (int) (pxValue / scaleDensity + 0.5f);
    }


    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        float scaleDensity = context.getResources().getDisplayMetrics().scaledDensity;

        return (int) (spValue * scaleDensity + 0.5f);
    }







}
