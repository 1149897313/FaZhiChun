package com.zgkj.fazhichun.entity.user;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/9.
 * Descr:
 */
public class Account {

    private String token;//app端的token
    private String is_success;//T:成功，F:失败
    private String err_msg;//失败时的错误信息

    public String getToken() {
        return token;
    }

    public void setAccessToken(String access_token) {
        this.token = token;
    }

    public String getIs_success() {
        return is_success;
    }

    public void setIs_success(String is_success) {
        this.is_success = is_success;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    @Override
    public String toString() {
        return "Account{" +
                "token='" + token + '\'' +
                ", is_success='" + is_success + '\'' +
                ", err_msg='" + err_msg + '\'' +
                '}';
    }
}


