package com.zgkj.fazhichun.entity.shop;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/9.
 * Descr:  理发师服务
 */
public class BarberService {
    private String  favorable_Price;//理发师服务详情列表-服务价格
    private String barber_goods_id;//理发师服务详情列表-服务id
    private String hairdresser_name;//理发师服务详情列表-服务名字
    private String pic_url;//理发师服务详情列表-服务形象图

    public String getFavorable_Price() {
        return favorable_Price;
    }

    public void setFavorable_Price(String favorable_Price) {
        this.favorable_Price = favorable_Price;
    }

    public String getBarber_goods_id() {
        return barber_goods_id;
    }

    public void setBarber_goods_id(String barber_goods_id) {
        this.barber_goods_id = barber_goods_id;
    }

    public String getHairdresser_name() {
        return hairdresser_name;
    }

    public void setHairdresser_name(String hairdresser_name) {
        this.hairdresser_name = hairdresser_name;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    @Override
    public String toString() {
        return "BarberService{" +
                "favorable_Pric='" + favorable_Price + '\'' +
                ", barber_goods_id='" + barber_goods_id + '\'' +
                ", hairdresser_name='" + hairdresser_name + '\'' +
                ", pic_url='" + pic_url + '\'' +
                '}';
    }
}
