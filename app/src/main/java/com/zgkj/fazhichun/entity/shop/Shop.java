package com.zgkj.fazhichun.entity.shop;

import com.zgkj.fazhichun.entity.shop_service.ShopService;

import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/9.
 * Descr:  商铺信息
 */
public class Shop {

    private String shop_name;//店铺名称
    private int shop_type;//店铺类型（1个人，2个体，3企业）
    private String merchant_name;//店主姓名
    private String  merchant_telphone;//店主手机号
    private int score_quantity;//评分人数
    private float shop_service_score;//商家星级评分(共5星 5.00)
    private String shop_telphone;//店铺电话
    private int shop_status;//商家状态(0未启用，1启用，2停用)
    private String aproval_memo;//审核状况
    private String opening_hour;//营业时间
    private float shop_lng;//经度
    private float shop_lat;//纬度
    private String address;//店铺详细地址
    private String shop_image;//商家形象图
    private String shop_photo;//店铺头像
    private String shop_detail;//店铺介绍
    private List<String> shop_banner;//店铺banner图

    public List<String> getShop_banner() {
        return shop_banner;
    }

    public void setShop_banner(List<String> shop_banner) {
        this.shop_banner = shop_banner;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public int getShop_type() {
        return shop_type;
    }

    public void setShop_type(int shop_type) {
        this.shop_type = shop_type;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getMerchant_telphone() {
        return merchant_telphone;
    }

    public void setMerchant_telphone(String merchant_telphone) {
        this.merchant_telphone = merchant_telphone;
    }

    public int getScore_quantity() {
        return score_quantity;
    }

    public void setScore_quantity(int score_quantity) {
        this.score_quantity = score_quantity;
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

    public int getShop_status() {
        return shop_status;
    }

    public void setShop_status(int shop_status) {
        this.shop_status = shop_status;
    }

    public String getAproval_memo() {
        return aproval_memo;
    }

    public void setAproval_memo(String aproval_memo) {
        this.aproval_memo = aproval_memo;
    }

    public String getOpening_hour() {
        return opening_hour;
    }

    public void setOpening_hour(String opening_hour) {
        this.opening_hour = opening_hour;
    }

    public float getShop_lng() {
        return shop_lng;
    }

    public void setShop_lng(float shop_lng) {
        this.shop_lng = shop_lng;
    }

    public float getShop_lat() {
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

    public String getShop_image() {
        return shop_image;
    }

    public void setShop_image(String shop_image) {
        this.shop_image = shop_image;
    }

    public String getShop_photo() {
        return shop_photo;
    }

    public void setShop_photo(String shop_photo) {
        this.shop_photo = shop_photo;
    }

    public String getShop_detail() {
        return shop_detail;
    }

    public void setShop_detail(String shop_detail) {
        this.shop_detail = shop_detail;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "shop_name='" + shop_name + '\'' +
                ", shop_type=" + shop_type +
                ", merchant_name='" + merchant_name + '\'' +
                ", merchant_telphone='" + merchant_telphone + '\'' +
                ", score_quantity=" + score_quantity +
                ", shop_service_score=" + shop_service_score +
                ", shop_telphone='" + shop_telphone + '\'' +
                ", shop_status=" + shop_status +
                ", aproval_memo='" + aproval_memo + '\'' +
                ", opening_hour='" + opening_hour + '\'' +
                ", shop_lng=" + shop_lng +
                ", shop_lat=" + shop_lat +
                ", address='" + address + '\'' +
                ", shop_image='" + shop_image + '\'' +
                ", shop_photo='" + shop_photo + '\'' +
                ", shop_detail='" + shop_detail + '\'' +
                ", shop_banner=" + shop_banner +
                '}';
    }
}
