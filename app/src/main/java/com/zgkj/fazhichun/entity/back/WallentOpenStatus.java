package com.zgkj.fazhichun.entity.back;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/22.
 * Descr:   钱包开通的状态
 */
public class WallentOpenStatus {

    private int open_status;//	0:未开通（未设置支付密码），1:已开通（已设置支付密码）


    public int getOpen_status() {
        return open_status;
    }

    public void setOpen_status(int open_status) {
        this.open_status = open_status;
    }

    @Override
    public String toString() {
        return "WallentOpenStatus{" +
                "open_status=" + open_status +
                '}';
    }
}
