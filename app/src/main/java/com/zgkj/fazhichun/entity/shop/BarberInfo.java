package com.zgkj.fazhichun.entity.shop;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/9.
 * Descr:  发型师
 */
public class BarberInfo {

    private String barber_id;//理发师列表-理发师id
    private String barber_name;//理发师列表-理发师名字
    private String position;//理发师列表-理发师职位
    private String pic_url;//理发师列表-理发师头像

    public String getBarber_id() {
        return barber_id;
    }

    public void setBarber_id(String barber_id) {
        this.barber_id = barber_id;
    }

    public String getBarber_name() {
        return barber_name;
    }

    public void setBarber_name(String barber_name) {
        this.barber_name = barber_name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    @Override
    public String toString() {
        return "BarberInfo{" +
                "barber_id=" + barber_id +
                ", barber_name='" + barber_name + '\'' +
                ", position='" + position + '\'' +
                ", pic_url='" + pic_url + '\'' +
                '}';
    }
}
