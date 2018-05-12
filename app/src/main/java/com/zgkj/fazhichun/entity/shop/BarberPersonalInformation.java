package com.zgkj.fazhichun.entity.shop;

import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/9.
 * Descr:  理发师个人详细信息
 */
public class BarberPersonalInformation extends BarberInfo{

    private String work_years;//理发师及店铺信息-理发师工作年限
    private String keywords;//理发师及店铺信息-理发师擅长
    private String barber_detail;//理发师及店铺信息-理发师说明
    private float service_score;//理发师及店铺信息-理发师星级评分
    private String shop_name;//理发师及店铺信息-店铺名字
    private String address;//理发师及店铺信息-店铺详细地址
    private double shop_lng;//理发师及店铺信息-店铺经度
    private double shop_lat;//理发师及店铺信息-店铺纬度
    private String shop_id;//理发师及店铺信息-店铺id
    private String shop_telphone;//	理发师及店铺信息-店铺电话

    public String getWork_years() {
        return work_years;
    }

    public void setWork_years(String work_years) {
        this.work_years = work_years;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getBarber_detail() {
        return barber_detail;
    }

    public void setBarber_detail(String barber_detail) {
        this.barber_detail = barber_detail;
    }

    public float getService_score() {
        return service_score;
    }

    public void setService_score(float service_score) {
        this.service_score = service_score;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_telphone() {
        return shop_telphone;
    }

    public void setShop_telphone(String shop_telphone) {
        this.shop_telphone = shop_telphone;
    }

    @Override
    public String toString() {
        return "BarberPersonalInformation{" +
                "work_years='" + work_years + '\'' +
                ", keywords='" + keywords + '\'' +
                ", barber_detail='" + barber_detail + '\'' +
                ", service_score=" + service_score +
                ", shop_name='" + shop_name + '\'' +
                ", address='" + address + '\'' +
                ", shop_lng=" + shop_lng +
                ", shop_lat=" + shop_lat +
                ", shop_id='" + shop_id + '\'' +
                ", shop_telphone='" + shop_telphone + '\'' +
                '}';
    }
}
