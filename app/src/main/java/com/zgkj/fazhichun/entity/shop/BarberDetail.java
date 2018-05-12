package com.zgkj.fazhichun.entity.shop;

import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/9.
 * Descr:  理发师详细信息
 */
public class BarberDetail{

    private BarberPersonalInformation barber_msg;//理发师及店铺信息
    private String distance;//理发师及店铺信息-店铺距离
    private List<BarberService> barber_msg_node;//理发师服务详情列表

    public BarberPersonalInformation getBarber_msg() {
        return barber_msg;
    }

    public void setBarber_msg(BarberPersonalInformation barber_msg) {
        this.barber_msg = barber_msg;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<BarberService> getBarber_msg_node() {
        return barber_msg_node;
    }

    public void setBarber_msg_node(List<BarberService> barber_msg_node) {
        this.barber_msg_node = barber_msg_node;
    }

    @Override
    public String toString() {
        return "BarberDetail{" +
                "barber_msg=" + barber_msg +
                ", distance='" + distance + '\'' +
                ", barber_msg_node=" + barber_msg_node +
                '}';
    }
}
