package com.zgkj.fazhichun.entity.order;

import java.math.BigDecimal;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/15.
 * Descr: 订单详情
 */
public class OrderDetail {

    private String hairdresser_id;//美发套餐ID
    private String shop_image;//店铺头像
    private String shop_name;//店铺名称
    private String hairdresser_name;//服务名称
    private String  num;//购买数量
    private String  favorable_Price;//购买优惠单价
    private String  order_sn;//订单号
    private BigDecimal order_amount;//订单总价
    private String address;//商家详细地址
    private String shop_lng;//店铺位置精度
    private String shop_telphone;//店铺电话
    private String pay_time;//付款时间，只有已付款装存在
    private String  buy_know;//购买需知
    private List<ValidateCode> validate_code;//（待付款状态为空）验证码信息，pay_serianlno：验证码，order_status:状态
    private int order_status;//订单状态
    private String shop_service_score;//评分

    public String getHairdresser_id() {
        return hairdresser_id;
    }

    public void setHairdresser_id(String hairdresser_id) {
        this.hairdresser_id = hairdresser_id;
    }

    public String getShop_image() {
        return shop_image;
    }

    public void setShop_image(String shop_image) {
        this.shop_image = shop_image;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getHairdresser_name() {
        return hairdresser_name;
    }

    public void setHairdresser_name(String hairdresser_name) {
        this.hairdresser_name = hairdresser_name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getFavorable_Price() {
        return favorable_Price;
    }

    public void setFavorable_Price(String favorable_Price) {
        this.favorable_Price = favorable_Price;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public BigDecimal getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(BigDecimal order_amount) {
        this.order_amount = order_amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShop_lng() {
        return shop_lng;
    }

    public void setShop_lng(String shop_lng) {
        this.shop_lng = shop_lng;
    }

    public String getShop_telphone() {
        return shop_telphone;
    }

    public void setShop_telphone(String shop_telphone) {
        this.shop_telphone = shop_telphone;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getBuy_know() {
        return buy_know;
    }

    public void setBuy_know(String buy_know) {
        this.buy_know = buy_know;
    }

    public List<ValidateCode> getValidate_code() {
        return validate_code;
    }

    public void setValidate_code(List<ValidateCode> validate_code) {
        this.validate_code = validate_code;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public String getShop_service_score() {
        return shop_service_score;
    }

    public void setShop_service_score(String shop_service_score) {
        this.shop_service_score = shop_service_score;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "hairdresser_id='" + hairdresser_id + '\'' +
                ", shop_image='" + shop_image + '\'' +
                ", shop_name='" + shop_name + '\'' +
                ", hairdresser_name='" + hairdresser_name + '\'' +
                ", num='" + num + '\'' +
                ", favorable_Price='" + favorable_Price + '\'' +
                ", order_sn='" + order_sn + '\'' +
                ", order_amount=" + order_amount +
                ", address='" + address + '\'' +
                ", shop_lng='" + shop_lng + '\'' +
                ", shop_telphone='" + shop_telphone + '\'' +
                ", pay_time='" + pay_time + '\'' +
                ", buy_know='" + buy_know + '\'' +
                ", validate_code=" + validate_code +
                ", order_status=" + order_status +
                ", shop_service_score='" + shop_service_score + '\'' +
                '}';
    }
}
