package com.zgkj.fazhichun.entity.order;

import java.math.BigDecimal;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/10.
 * Descr:  订单准备
 */
public class ReadyOrder {
    private String hairdresser_name;//商品名称
    private BigDecimal favorable_Price;//商品价格
    private String member_level;//用户会员等级-1普通、2vip
    private BigDecimal user_commission;//会员折扣比例
    private String user_coupon_count;//用户可用代金券数量

    public String getHairdresser_name() {
        return hairdresser_name;
    }

    public void setHairdresser_name(String hairdresser_name) {
        this.hairdresser_name = hairdresser_name;
    }

    public BigDecimal getFavorable_Price() {
        return favorable_Price;
    }

    public void setFavorable_Price(BigDecimal favorable_Price) {
        this.favorable_Price = favorable_Price;
    }

    public String getMember_level() {
        return member_level;
    }

    public void setMember_level(String member_level) {
        this.member_level = member_level;
    }

    public BigDecimal getUser_commission() {
        return user_commission;
    }

    public void setUser_commission(BigDecimal user_commission) {
        this.user_commission = user_commission;
    }

    public String getUser_coupon_count() {
        return user_coupon_count;
    }

    public void setUser_coupon_count(String user_coupon_count) {
        this.user_coupon_count = user_coupon_count;
    }

    @Override
    public String toString() {
        return "ReadyOrder{" +
                "hairdresser_name='" + hairdresser_name + '\'' +
                ", favorable_Price='" + favorable_Price + '\'' +
                ", member_level='" + member_level + '\'' +
                ", user_commission='" + user_commission + '\'' +
                ", user_coupon_count='" + user_coupon_count + '\'' +
                '}';
    }
}
