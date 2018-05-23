package com.zgkj.fazhichun.entity.shop;

import com.zgkj.fazhichun.entity.shop_service.ShopService;

import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/9.
 * Descr:
 */
public class ShopB {

    private Shop shop;
    private float persin_pay_money;//人均消费
    private int talk_num;//评价数量
    private List<Talk> talk_list;//	array评价列表(2条)
    private int is_love;//是否收藏(1是，0否)
    private List<ShopService> shop_service;//商家服务

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public float getPersin_pay_money() {
        return persin_pay_money;
    }

    public void setPersin_pay_money(float persin_pay_money) {
        this.persin_pay_money = persin_pay_money;
    }

    public int getTalk_num() {
        return talk_num;
    }

    public void setTalk_num(int talk_num) {
        this.talk_num = talk_num;
    }

    public List<Talk> getTalk_list() {
        return talk_list;
    }

    public void setTalk_list(List<Talk> talk_list) {
        this.talk_list = talk_list;
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

    @Override
    public String toString() {
        return "ShopB{" +
                "shop=" + shop +
                ", persin_pay_money=" + persin_pay_money +
                ", talk_num=" + talk_num +
                ", talk_list=" + talk_list +
                ", is_love=" + is_love +
                ", shop_service=" + shop_service +
                '}';
    }
}
