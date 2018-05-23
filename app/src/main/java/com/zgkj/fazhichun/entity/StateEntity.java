package com.zgkj.fazhichun.entity;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/19.
 * Descr: 返回状态
 */
public class StateEntity {

    private String is_success;//T:成功F:失败
    private String err_msg;//失败时返回的错误信息

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
        return "StateEntity{" +
                "is_success='" + is_success + '\'' +
                ", err_msg='" + err_msg + '\'' +
                '}';
    }
}
