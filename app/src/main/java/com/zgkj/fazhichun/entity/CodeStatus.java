package com.zgkj.fazhichun.entity;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/4.
 * Descr:
 */
public class CodeStatus {


    private String is_success;//T:成功，F:失败
    private String  tips;//提示消息

    public String getIs_success() {
        return is_success;
    }

    public void setIs_success(String is_success) {
        this.is_success = is_success;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    @Override
    public String toString() {
        return "CodeStatus{" +
                "is_success='" + is_success + '\'' +
                ", tips='" + tips + '\'' +
                '}';
    }
}
