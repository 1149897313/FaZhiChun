

package com.zgkj.fazhichun.entity.shop;

import com.zgkj.fazhichun.entity.shop_service.ShopService;
import com.zgkj.fazhichun.entity.tag.Lable;

import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/8.
 * Descr: 店铺商品信息
 */
public class HairdresserInfo {

    private Hairdresser hairdresser;//商品详情
    private int is_love;//是否收藏(1是，0否)
    private List<ShopService> shop_service;//	array	商家服务
    private List<Lable> hairdresser_label;//	array	评价标签（干净卫生等）


    public Hairdresser getHairdresser() {
        return hairdresser;
    }

    public void setHairdresser(Hairdresser hairdresser) {
        this.hairdresser = hairdresser;
    }

    public int getIs_love() {
        return is_love;
    }

    public void setIs_love(int is_love) {
        this.is_love = is_love;
    }

    public List<ShopService> getShop_service() {
        return shop_service;
    }

    public void setShop_service(List<ShopService> shop_service) {
        this.shop_service = shop_service;
    }

    public List<Lable> getHairdresser_label() {
        return hairdresser_label;
    }

    public void setHairdresser_label(List<Lable> hairdresser_label) {
        this.hairdresser_label = hairdresser_label;
    }

    @Override
    public String toString() {
        return "HairdresserInfo{" +
                "hairdresser=" + hairdresser +
                ", is_love=" + is_love +
                ", shop_service=" + shop_service +
                ", hairdresser_label=" + hairdresser_label +
                '}';
    }

}
