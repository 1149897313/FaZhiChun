package com.zgkj.fazhichun.entity.shop;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/15.
 * Descr:  分类商品列表详情
 */
public class Goods {

    private  String hairdresser_id;//商品id
    private  String hairdresser_name;//商品名字
    private  String shop_name;//店铺名称
    private  String sale_price;//商品vip价格
    private  String  favorable_Price;//商品实际价格
    private  String pic_url;//商品形象图
    private  String hairdresser_category_name;//商品分类名称
    private  double shop_lng;//店铺经度
    private  double  shop_lat;//店铺纬度
    private int distance;//距离

    public String getHairdresser_id() {
        return hairdresser_id;
    }

    public void setHairdresser_id(String hairdresser_id) {
        this.hairdresser_id = hairdresser_id;
    }

    public String getHairdresser_name() {
        return hairdresser_name;
    }

    public void setHairdresser_name(String hairdresser_name) {
        this.hairdresser_name = hairdresser_name;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getFavorable_Price() {
        return favorable_Price;
    }

    public void setFavorable_Price(String favorable_Price) {
        this.favorable_Price = favorable_Price;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getHairdresser_category_name() {
        return hairdresser_category_name;
    }

    public void setHairdresser_category_name(String hairdresser_category_name) {
        this.hairdresser_category_name = hairdresser_category_name;
    }

    public double getShop_lng() {
        return shop_lng;
    }

    public void setShop_lng(double shop_lng) {
        this.shop_lng = shop_lng;
    }

    public double getShop_lat() {
        return shop_lat;
    }

    public void setShop_lat(double shop_lat) {
        this.shop_lat = shop_lat;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "hairdresser_id='" + hairdresser_id + '\'' +
                ", hairdresser_name='" + hairdresser_name + '\'' +
                ", shop_name='" + shop_name + '\'' +
                ", sale_price='" + sale_price + '\'' +
                ", favorable_Price='" + favorable_Price + '\'' +
                ", pic_url='" + pic_url + '\'' +
                ", hairdresser_category_name='" + hairdresser_category_name + '\'' +
                ", shop_lng=" + shop_lng +
                ", shop_lat=" + shop_lat +
                ", distance=" + distance +
                '}';
    }
}
