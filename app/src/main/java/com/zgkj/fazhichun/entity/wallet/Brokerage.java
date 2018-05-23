package com.zgkj.fazhichun.entity.wallet;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/23.
 * Descr: 佣金记录明细
 */
public class Brokerage {

    private String order_sn;//订单号
    private String total_Commission;//佣金金额
    private String create_time;//创建时间
    private String nickname;//创建人

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getTotal_Commission() {
        return total_Commission;
    }

    public void setTotal_Commission(String total_Commission) {
        this.total_Commission = total_Commission;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "Brokerage{" +
                "order_sn='" + order_sn + '\'' +
                ", total_Commission='" + total_Commission + '\'' +
                ", create_time='" + create_time + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
