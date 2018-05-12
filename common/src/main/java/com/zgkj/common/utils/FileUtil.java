package com.zgkj.common.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/15.
 * Descr:   文件操作的辅助工具类
 */

public class FileUtil {

    // 定义App文件夹的名字
    private static final String APP_FOLDERS_NAME = "FaZhiChun";


    /**
     * 私有化的构造方法，禁止通过new的方式创建对象
     */
    private FileUtil(){
        // 抛出不支持的操作异常
        throw new UnsupportedOperationException("cannot be instantiated!");
    }

    /**
     * 判断是否挂载了SD卡
     *
     * @return      返回true表示挂载了SD卡，反之亦然
     */
    private static boolean isMountedSDCard(){
        // 如果挂载了SD卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
    }


    /**
     * 获取SD卡的根路径
     *
     * @return
     */
    private static String getSDCardRootPath(){
        String sdCardRootPath = null;
        if (isMountedSDCard()){
            sdCardRootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        }
        return sdCardRootPath;
    }


    /**
     * 创建一个App文件的根目录，并返回根目录地址
     *
     * @return
     */
    public static String createAppFoldersRootPath(){

        String sdCardRootPath = getSDCardRootPath();

        // 如果根目录地址不为空
        if (!TextUtils.isEmpty(sdCardRootPath)){

            String appFoldersRootPath = sdCardRootPath + APP_FOLDERS_NAME;

            File file = new File(appFoldersRootPath);
            // 如果文件不存在则创建
            if (!file.exists()){
                // 创建文件
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        return null;
    }






































}
