package com.zgkj.fazhichun.entity.order;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/14.
 * Descr:
 */
public class OrderInfo {

    private String order_id;//条件不为退款时返回
    private String  return_id;//退款订单时返回
    private String  shop_image;//店铺图像
    private String  shop_name;//店铺名称
    private String  hairdresser_name;//分类名称
    private String shop_service_score;//评分，condition=all时返回，null时为未评价状态
    private String goods_name;//商品名称
    private String num;//购买数量
    private String  favorable_Price;//价格
    private String  order_status;//订单状态

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getReturn_id() {
        return return_id;
    }

    public void setReturn_id(String return_id) {
        this.return_id = return_id;
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

    public String getShop_service_score() {
        return shop_service_score;
    }

    public void setShop_service_score(String shop_service_score) {
        this.shop_service_score = shop_service_score;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
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

    /**
     * 1. 待付款  2. 待使用 4.5.6.已完成 8.退款
     * @return
     */
    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "order_id='" + order_id + '\'' +
                ", return_id='" + return_id + '\'' +
                ", shop_image='" + shop_image + '\'' +
                ", shop_name='" + shop_name + '\'' +
                ", hairdresser_name='" + hairdresser_name + '\'' +
                ", shop_service_score='" + shop_service_score + '\'' +
                ", goods_name='" + goods_name + '\'' +
                ", num='" + num + '\'' +
                ", favorable_Price='" + favorable_Price + '\'' +
                ", order_status='" + order_status + '\'' +
                '}';
    }
}
