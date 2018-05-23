package com.zgkj.fazhichun.entity.wallet;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/22.
 * Descr: 我的钱包 -佣金
 */
public class BrokerageTotal {

    private float user_money;//佣金余额
    private float total_user_money;//佣金总额

    public float getUser_money() {
        return user_money;
    }

    public void setUser_money(float user_money) {
        this.user_money = user_money;
    }

    public float getTotal_user_money() {
        return total_user_money;
    }

    public void setTotal_user_money(float total_user_money) {
        this.total_user_money = total_user_money;
    }

    @Override
    public String toString() {
        return "BrokerageTotal{" +
                "user_money='" + user_money + '\'' +
                ", total_user_money='" + total_user_money + '\'' +
                '}';
    }
}
