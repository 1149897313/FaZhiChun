package com.zgkj.fazhichun.entity.shop_service;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/8.
 * Descr:
 */
public class ShopService {

    private String service_id;//商家服务-服务id
    private String service_name;//商家服务-服务说明
    private String service_url;//商家服务-服务图片

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getService_url() {
        return service_url;
    }

    public void setService_url(String service_url) {
        this.service_url = service_url;
    }

    @Override
    public String toString() {
        return "ShopService{" +
                "service_id=" + service_id +
                ", service_name='" + service_name + '\'' +
                ", service_url='" + service_url + '\'' +
                '}';
    }
}
