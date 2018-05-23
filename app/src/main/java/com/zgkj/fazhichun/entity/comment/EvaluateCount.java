package com.zgkj.fazhichun.entity.comment;

import com.google.gson.annotations.SerializedName;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/17.
 * Descr: 订单评论分类数量
 */
public class EvaluateCount {

    private String all;//全部
    private String is_img;//有图	int	有图
    private String is_low;//低分	int	低分
    @SerializedName("new")
    private String newest;//最新	int	最新

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public String getIs_img() {
        return is_img;
    }

    public void setIs_img(String is_img) {
        this.is_img = is_img;
    }

    public String getIs_low() {
        return is_low;
    }

    public void setIs_low(String is_low) {
        this.is_low = is_low;
    }

    public String getNewest() {
        return newest;
    }

    public void setNewest(String newest) {
        this.newest = newest;
    }

    @Override
    public String toString() {
        return "EvaluateCount{" +
                "all='" + all + '\'' +
                ", is_img='" + is_img + '\'' +
                ", is_low='" + is_low + '\'' +
                ", newest='" + newest + '\'' +
                '}';
    }
}
