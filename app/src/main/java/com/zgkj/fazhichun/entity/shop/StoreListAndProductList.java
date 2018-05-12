package com.zgkj.fazhichun.entity.shop;

import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/7.
 * Descr: 商家列表和商品列表
 */
public class StoreListAndProductList {

    private String shop_id;//	string	店铺id/虚拟店铺id
    private String virtual_shop_id;
    private String shop_name;//店铺名称
    private String  shop_image;//店铺头像
    private String distance;//店铺距离
    private float  shop_service_score;//		商家星级评分(共5星 5.00)
    private int  score_quantity;//	int	商家评分人数
    private String  address;//	商家详细地址
    private String  shopcategory;//店铺类型
    private List<Hairdresser> hairdresser;

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getVirtual_shop_id() {
        return virtual_shop_id;
    }

    public void setVirtual_shop_id(String virtual_shop_id) {
        this.virtual_shop_id = virtual_shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_image() {
        return shop_image;
    }

    public void setShop_image(String shop_image) {
        this.shop_image = shop_image;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public float getShop_service_score() {
        return shop_service_score;
    }

    public void setShop_service_score(float shop_service_score) {
        this.shop_service_score = shop_service_score;
    }

    public int getScore_quantity() {
        return score_quantity;
    }

    public void setScore_quantity(int score_quantity) {
        this.score_quantity = score_quantity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShopcategory() {
        return shopcategory;
    }

    public void setShopcategory(String shopcategory) {
        this.shopcategory = shopcategory;
    }

    public List<Hairdresser> getHairdresser() {
        return hairdresser;
    }

    public void setHairdresser(List<Hairdresser> hairdresser) {
        this.hairdresser = hairdresser;
    }

    @Override
    public String toString() {
        return "List{" +
                "shop_id='" + shop_id + '\'' +
                ", virtual_shop_id='" + virtual_shop_id + '\'' +
                ", shop_name='" + shop_name + '\'' +
                ", shop_image='" + shop_image + '\'' +
                ", distance=" + distance +
                ", shop_service_score=" + shop_service_score +
                ", score_quantity=" + score_quantity +
                ", address='" + address + '\'' +
                ", shopcategory='" + shopcategory + '\'' +
                ", hairdresser=" + hairdresser +
                '}';
    }
}
