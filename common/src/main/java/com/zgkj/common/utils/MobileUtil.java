package com.zgkj.common.utils;

import android.text.TextUtils;

import com.zgkj.common.Common;

import java.util.regex.Pattern;

/**
 * Descr:   手机相关的工具辅助类
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/1/2.
 */

public class MobileUtil {


    /**
     * 检查手机号是否合法
     *
     * @param phone 手机号码
     * @return 合法为True
     */
    public static boolean checkMobile(String phone) {
        // 手机号不为空，并且满足格式
        return !TextUtils.isEmpty(phone)
                && Pattern.matches(Common.Constant.REGEX_MOBILE, phone);
    }


}
