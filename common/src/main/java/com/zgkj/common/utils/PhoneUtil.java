package com.zgkj.common.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Descr:   手机相关的辅助工具类
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/1/22.
 */

public class PhoneUtil {

    /**
     * 私有化的构造方法，禁止通过new的方式创建对象
     */
    private PhoneUtil() {

        // 抛出不支持的操作异常
        throw new UnsupportedOperationException("cannot be instantiated!");
    }



    /**
     * 校验手机号码是否合法的方法
     *
     * @param phoneNumber       需要校验的手机号码
     * @return                  返回true表示合法，返回false表示不合法
     */
    public static boolean checkPhoneNumber(String phoneNumber){
        String regex = "[1][3,4,5,7,8][0-9]{9}$";

        return !TextUtils.isEmpty(phoneNumber)
                && Pattern.matches(regex, phoneNumber);
    }



}
