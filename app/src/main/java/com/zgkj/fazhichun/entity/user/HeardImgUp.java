package com.zgkj.fazhichun.entity.user;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/15.
 * Descr:
 */
public class HeardImgUp {

    private String msg;//提示信息
    private String sta;//int	1：成功---0：失败

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSta() {
        return sta;
    }

    public void setSta(String sta) {
        this.sta = sta;
    }

    @Override
    public String toString() {
        return "HeardImgUp{" +
                "msg='" + msg + '\'' +
                ", sta='" + sta + '\'' +
                '}';
    }
}
