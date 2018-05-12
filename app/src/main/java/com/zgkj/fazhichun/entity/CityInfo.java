package com.zgkj.fazhichun.entity;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/4.
 * Descr: 城市详情
 */
public class CityInfo {

    private String region_code;
    private String region_name;

    public String getRegion_code() {
        return region_code;
    }

    public void setRegion_code(String region_code) {
        this.region_code = region_code;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    @Override
    public String toString() {
        return "CityInfo{" +
                "region_code='" + region_code + '\'' +
                ", region_name='" + region_name + '\'' +
                '}';
    }
}
