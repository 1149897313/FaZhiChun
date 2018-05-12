package com.zgkj.fazhichun.entity.order;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/11.
 * Descr:  订单信息
 */
public class Oder {

    private String order_id;//订单id

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    @Override
    public String toString() {
        return "Oder{" +
                "order_id='" + order_id + '\'' +
                '}';
    }
}
