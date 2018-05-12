package com.zgkj.factory.model.api.home;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/3.
 * Descr: 主页banner实体
 */
public class Banner {

    private String Carousel_path;//banner图片完整地址
    private String Carousel_urll;//点击banner图所跳转地址

    public String getCarousel_path() {
        return Carousel_path;
    }

    public void setCarousel_path(String carousel_path) {
        Carousel_path = carousel_path;
    }

    public String getCarousel_urll() {
        return Carousel_urll;
    }

    public void setCarousel_urll(String carousel_urll) {
        Carousel_urll = carousel_urll;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "Carousel_path='" + Carousel_path + '\'' +
                ", Carousel_urll='" + Carousel_urll + '\'' +
                '}';
    }
}
