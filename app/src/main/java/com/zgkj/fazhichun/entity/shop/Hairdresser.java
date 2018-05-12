package com.zgkj.fazhichun.entity.shop;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/7.
 * Descr: 商品详情
 */
public class Hairdresser {

    private String hairdresser_id;//	int	商品数组-商品id
    private String hairdresser_name;//	商品数组-商品名字
    private int quantity;//商品数组-商品已售数量
    private String sale_price;//商品数组-商品市场价格
    private String favorable_Price;//商品数组-商品实际价格
    private int hairdresser_num;//商品数量

    private String buy_know;//购买须知
    private int talk_num;//商品评价数量
    private String pic_url;//商品形象图
    private String hairdresser_category_name;//商品分类名称
    private int shop_id;//店铺id
    private float shop_service_score;//店铺评分星级
    private String shop_telphone	;//店铺电话
    private double shop_lng;//店铺经度
    private double shop_lat;//店铺纬度
    private String address;//店铺详细地址


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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public int getHairdresser_num() {
        return hairdresser_num;
    }

    public void setHairdresser_num(int hairdresser_num) {
        this.hairdresser_num = hairdresser_num;
    }

    public String getBuy_know() {
        return buy_know;
    }

    public void setBuy_know(String buy_know) {
        this.buy_know = buy_know;
    }

    public int getTalk_num() {
        return talk_num;
    }

    public void setTalk_num(int talk_num) {
        this.talk_num = talk_num;
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

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public float getShop_service_score() {
        return shop_service_score;
    }

    public void setShop_service_score(float shop_service_score) {
        this.shop_service_score = shop_service_score;
    }

    public String getShop_telphone() {
        return shop_telphone;
    }

    public void setShop_telphone(String shop_telphone) {
        this.shop_telphone = shop_telphone;
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

    public void setShop_lat(float shop_lat) {
        this.shop_lat = shop_lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Hairdresser{" +
                "hairdresser_id=" + hairdresser_id +
                ", hairdresser_name='" + hairdresser_name + '\'' +
                ", quantity=" + quantity +
                ", sale_price=" + sale_price +
                ", favorable_Price=" + favorable_Price +
                ", hairdresser_num=" + hairdresser_num +
                ", buy_know='" + buy_know + '\'' +
                ", talk_num=" + talk_num +
                ", pic_url='" + pic_url + '\'' +
                ", hairdresser_category_name='" + hairdresser_category_name + '\'' +
                ", shop_id=" + shop_id +
                ", shop_service_score=" + shop_service_score +
                ", shop_telphone=" + shop_telphone +
                ", shop_lng=" + shop_lng +
                ", shop_lat=" + shop_lat +
                ", address='" + address + '\'' +
                '}';
    }
}
