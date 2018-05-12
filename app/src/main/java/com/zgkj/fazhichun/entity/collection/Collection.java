package com.zgkj.fazhichun.entity.collection;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/9.
 * Descr: 收藏
 */
public class Collection {

    private String collection_id;//收藏记录id(删除收藏记录时会用)
    private String  shop_id;//店铺id
    private String  shop_name;//店铺名字
    private float shop_service_score;//店铺评分星级
    private String shop_image;//店铺形象图
    private String category_name;//类型名称
    private String hairdresser_id;//$type=hairdresser-商品id
    private String  pic_url;//$type=hairdresser-商品图片
    private String  hairdresser_name;//$type=hairdresser-
    private String  sale_price;//$type=hairdresser-市场价
    private String  favorable_Price;//$type=hairdresser-实际价格
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(String collection_id) {
        this.collection_id = collection_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public float getShop_service_score() {
        return shop_service_score;
    }

    public void setShop_service_score(float shop_service_score) {
        this.shop_service_score = shop_service_score;
    }

    public String getShop_image() {
        return shop_image;
    }

    public void setShop_image(String shop_image) {
        this.shop_image = shop_image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getHairdresser_id() {
        return hairdresser_id;
    }

    public void setHairdresser_id(String hairdresser_id) {
        this.hairdresser_id = hairdresser_id;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getHairdresser_name() {
        return hairdresser_name;
    }

    public void setHairdresser_name(String hairdresser_name) {
        this.hairdresser_name = hairdresser_name;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getFavorable_Price() {
        return favorable_Price;
    }

    public void setFavorable_Price(String favorable_Price) {
        this.favorable_Price = favorable_Price;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "collection_id='" + collection_id + '\'' +
                ", shop_id='" + shop_id + '\'' +
                ", shop_name='" + shop_name + '\'' +
                ", shop_service_score=" + shop_service_score +
                ", shop_image='" + shop_image + '\'' +
                ", category_name='" + category_name + '\'' +
                ", hairdresser_id='" + hairdresser_id + '\'' +
                ", pic_url='" + pic_url + '\'' +
                ", hairdresser_name='" + hairdresser_name + '\'' +
                ", sale_price='" + sale_price + '\'' +
                ", favorable_Price='" + favorable_Price + '\'' +
                '}';
    }
}
