package com.zgkj.fazhichun.entity;

import com.zgkj.fazhichun.entity.shop.StoreListAndProductList;

import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/8.
 * Descr:
 */
public class ShopList {

    private List<StoreListAndProductList> shop_list;

    public List<StoreListAndProductList> getShop_list() {
        return shop_list;
    }

    public void setShop_list(List<StoreListAndProductList> shop_list) {
        this.shop_list = shop_list;
    }

    @Override
    public String toString() {
        return "ShopList{" +
                "shop_list=" + shop_list +
                '}';
    }
}
