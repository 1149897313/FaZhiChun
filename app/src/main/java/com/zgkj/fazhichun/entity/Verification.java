package com.zgkj.fazhichun.entity;

import com.zgkj.fazhichun.entity.order.OrderVerification;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/21.
 * Descr: 消费二维码url验证接口
 */
public class Verification {

    private OrderVerification order_detail;
    private String url;//二维码路径

    public OrderVerification getOrder_detail() {
        return order_detail;
    }

    public void setOrder_detail(OrderVerification order_detail) {
        this.order_detail = order_detail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Verification{" +
                "order_detail=" + order_detail +
                ", url='" + url + '\'' +
                '}';
    }
}
