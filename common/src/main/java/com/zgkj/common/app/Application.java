package com.zgkj.common.app;

import android.os.SystemClock;


import java.io.File;

/**
 * Descr:   Application基类的定义
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/1/19.
 */

public class Application extends android.app.Application {

    // 单例对象
    private static Application sInstance;


    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化单例对象
        sInstance = this;

    }


    /**
     * 外部获取单例
     *
     * @return      Application
     */
    public static Application getInstance(){

        return sInstance;
    }

    /**
     * 获取缓存文件夹地址
     *
     * @return      当前APP的缓存文件夹地址
     */
    public static File getCacheDirFile() {

        return sInstance.getCacheDir();
    }



    /**
     * 获取头像文件的缓存地址
     *
     * @return
     */
    public static File getPortraitTmpFile(){
        // 得到头像目录的缓存地址
        File dir = new File(getCacheDirFile(), "portrait");
        // 强制创建
        dir.mkdirs();

        // 删除旧的文件头像文件
        File[] files = dir.listFiles();
        if (files != null && files.length > 0){
            for (File file : files){
                // 删除
                file.delete();
            }
        }
        // 创建新的头像文件
        File path = new File(dir, SystemClock.uptimeMillis() + ".png");

        return path.getAbsoluteFile();
    }






}
