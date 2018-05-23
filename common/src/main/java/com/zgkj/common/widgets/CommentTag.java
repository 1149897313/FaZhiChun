package com.zgkj.common.widgets;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/16.
 * Descr:  评论标签
 */
public class CommentTag {

    private String evaluate_id;//1-5评价等级
    private String  label_id;//评价标签id
    private String  label_detail;//评价标签名称

    public String getEvaluate_id() {
        return evaluate_id;
    }

    public void setEvaluate_id(String evaluate_id) {
        this.evaluate_id = evaluate_id;
    }

    public String getLabel_id() {
        return label_id;
    }

    public void setLabel_id(String label_id) {
        this.label_id = label_id;
    }

    public String getLabel_detail() {
        return label_detail;
    }

    public void setLabel_detail(String label_detail) {
        this.label_detail = label_detail;
    }

    @Override
    public String toString() {
        return "CommentTag{" +
                "evaluate_id='" + evaluate_id + '\'' +
                ", label_id='" + label_id + '\'' +
                ", label_detail='" + label_detail + '\'' +
                '}';
    }
}
