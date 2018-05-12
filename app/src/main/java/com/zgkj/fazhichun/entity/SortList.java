package com.zgkj.fazhichun.entity;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/8.
 * Descr: 分类列表 -列表
 */
public class SortList {


    private int category_id;//分类id
    private String type_icon;//分类图标地址
    private String hairdresser_name;//分类名称
    private int hairdresser_sort;//分类排序

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getType_icon() {
        return type_icon;
    }

    public void setType_icon(String type_icon) {
        this.type_icon = type_icon;
    }

    public String getHairdresser_name() {
        return hairdresser_name;
    }

    public void setHairdresser_name(String hairdresser_name) {
        this.hairdresser_name = hairdresser_name;
    }

    public int getHairdresser_sort() {
        return hairdresser_sort;
    }

    public void setHairdresser_sort(int hairdresser_sort) {
        this.hairdresser_sort = hairdresser_sort;
    }

    @Override
    public String toString() {
        return "SortList{" +
                "category_id=" + category_id +
                ", type_icon='" + type_icon + '\'' +
                ", hairdresser_name='" + hairdresser_name + '\'' +
                ", hairdresser_sort=" + hairdresser_sort +
                '}';
    }
}
