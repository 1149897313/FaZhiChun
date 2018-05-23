package com.zgkj.fazhichun.entity.comment;

import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/17.
 * Descr: 评价信息
 */
public class Comment {

    private String evaluate_id;
    private String evaluate_detail;
    private String image_url;
    private float service_score;
    private String create_time;
    private String shop_reply;
    private String nickname;
    private String image_path;

    public String getEvaluate_id() {
        return evaluate_id;
    }

    public void setEvaluate_id(String evaluate_id) {
        this.evaluate_id = evaluate_id;
    }

    public String getEvaluate_detail() {
        return evaluate_detail;
    }

    public void setEvaluate_detail(String evaluate_detail) {
        this.evaluate_detail = evaluate_detail;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public float getService_score() {
        return service_score;
    }

    public void setService_score(float service_score) {
        this.service_score = service_score;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getShop_reply() {
        return shop_reply;
    }

    public void setShop_reply(String shop_reply) {
        this.shop_reply = shop_reply;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "evaluate_id='" + evaluate_id + '\'' +
                ", evaluate_detail='" + evaluate_detail + '\'' +
                ", image_url='" + image_url + '\'' +
                ", service_score=" + service_score +
                ", create_time='" + create_time + '\'' +
                ", shop_reply='" + shop_reply + '\'' +
                ", nickname='" + nickname + '\'' +
                ", image_path='" + image_path + '\'' +
                '}';
    }
}
