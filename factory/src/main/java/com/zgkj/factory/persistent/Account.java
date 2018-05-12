package com.zgkj.factory.persistent;

import com.zgkj.common.utils.SPUtil;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/8.
 * Descr:   用于用户登录信息的持久化
 */
public class Account {


    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_ACCOUNT = "key_account";


    // 用户token
    private static String token;

    // 登录的账户
    private static String account;


    /**
     * 储存数据到XML文件中,持久化
     */
    public static void save(){
        SPUtil.put(KEY_TOKEN, token);
        SPUtil.put(KEY_ACCOUNT, account);
    }



    public static void load(){
        token = (String) SPUtil.get(KEY_TOKEN, "");
        account = (String) SPUtil.get(KEY_ACCOUNT, "");
    }




    public static String getToken(){

        return token;
    }


























}
