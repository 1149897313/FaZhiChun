package com.zgkj.fazhichun.entity.tag;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/8.
 * Descr: 评价标签
 */
public class Lable {
    private String label_name;//评价标签-名字
    private String label_count;//评价标签-数量

    public String getLabel_name() {
        return label_name;
    }

    public void setLabel_name(String label_name) {
        this.label_name = label_name;
    }

    public String getLabel_count() {
        return label_count;
    }

    public void setLabel_count(String label_count) {
        this.label_count = label_count;
    }

    @Override
    public String toString() {
        return "Lable{" +
                "label_name='" + label_name + '\'' +
                ", label_count='" + label_count + '\'' +
                '}';
    }
}
