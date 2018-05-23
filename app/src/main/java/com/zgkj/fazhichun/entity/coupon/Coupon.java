package com.zgkj.fazhichun.entity.coupon;

import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/10.
 * Descr:  优惠券
 */
public class Coupon {

    private String coupon_id;//优惠券id
    private String is_used;//0:未使用，1：已使用
    private String  coupon_money;//优惠券面额
    private String end_time;//结束时间(时间戳)
    private String use_range;//json使用范围，hairdresser_name
    private boolean selected;//状态

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getIs_used() {
        return is_used;
    }

    public void setIs_used(String is_used) {
        this.is_used = is_used;
    }

    public String getCoupon_money() {
        return coupon_money;
    }

    public void setCoupon_money(String coupon_money) {
        this.coupon_money = coupon_money;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getUse_range() {
        return use_range;
    }

    public void setUse_range(String use_range) {
        this.use_range = use_range;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "coupon_id='" + coupon_id + '\'' +
                ", is_used='" + is_used + '\'' +
                ", coupon_money='" + coupon_money + '\'' +
                ", end_time='" + end_time + '\'' +
                ", use_range='" + use_range + '\'' +
                ", selected=" + selected +
                '}';
    }
}
