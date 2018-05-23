package com.zgkj.fazhichun.entity.city;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/4.
 * Descr: 城市详情
 */
public class CityInfo {

    private String region_code;
    private String region_name;
    private String region_pinyin;

    public CityInfo(String name, String region_pinyin) {
        this.region_name = name;
        this.region_pinyin = region_pinyin;
    }

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

    public String getRegion_pinyin() {
        return region_pinyin;
    }

    public void setRegion_pinyin(String region_pinyin) {
        this.region_pinyin = region_pinyin;
    }

    @Override
    public String toString() {
        return "CityInfo{" +
                "region_code='" + region_code + '\'' +
                ", region_name='" + region_name + '\'' +
                ", region_pinyin='" + region_pinyin + '\'' +
                '}';
    }
}
