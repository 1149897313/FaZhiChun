package com.zgkj.common.utils;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Descr:   账户信息相关的管理类
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2017/12/12.
 */

public class AccountManagers {


    // 请求时间

    // 商户编码
    public static final String MERNO = "Cqzgkj0001";
    // Key
    public static final String KEY = "lsx";

    // 请求时间
    public static String requestTime;


    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_ACCOUNT = "KEY_ACCOUNT";

    // 登录状态的Token，用来接口请求
    private static String token;
    // 登录的账户
    private static String account;

    /**
     * 存储数据到XML文件，持久化
     */
    private static void save() {
        // 存储数据
        SPUtil.put(KEY_TOKEN, token);
        SPUtil.put(KEY_ACCOUNT, account);
    }

    /**
     * 获取token的值
     *
     * @return
     */
    public static String getToken(){
        token = (String) SPUtil.get(KEY_TOKEN, "");
        return token;
    }

    /**
     * 获取账号
     *
     * @return
     */
    public static String getAccount(){
        account = (String) SPUtil.get(KEY_ACCOUNT, "");
        return account;
    }

    /**
     * 保存用户的登录信息到持久化XML中
     *
     */
    public static void login(String token, String account){
        AccountManagers.token = token;
        AccountManagers.account = account;
        // 保存到本地
        save();
    }

    /**
     * 返回当前账户是否登录
     *
     * @return True已登录
     */
    public static boolean isLogin() {
        // 用户账号和Token不能为空
        return !TextUtils.isEmpty(getToken())
                && !TextUtils.isEmpty(getAccount());
    }

    /**
     * 实现退出登录的方法
     */
    public static void logOut(){
        if (isLogin()){
            SPUtil.remove(KEY_TOKEN);
            SPUtil.remove(KEY_ACCOUNT);
        }
    }
}
