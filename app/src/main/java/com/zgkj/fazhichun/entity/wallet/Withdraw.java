package com.zgkj.fazhichun.entity.wallet;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/23.
 * Descr: 提现记录
 */
public class Withdraw {

    private String create_time;//提交时间
    private String amount;//金额
    private int withdraw_status;//提现状态1:审核中2：成功3：失败

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getWithdraw_status() {
        return withdraw_status;
    }

    public void setWithdraw_status(int withdraw_status) {
        this.withdraw_status = withdraw_status;
    }

    @Override
    public String toString() {
        return "Withdraw{" +
                "create_time='" + create_time + '\'' +
                ", amount='" + amount + '\'' +
                ", withdraw_status=" + withdraw_status +
                '}';
    }
}
