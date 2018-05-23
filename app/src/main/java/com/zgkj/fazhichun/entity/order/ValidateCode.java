package com.zgkj.fazhichun.entity.order;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/15.
 * Descr: 订单详情中消费券
 */
public class ValidateCode {


    private String pay_serianlno;//消费卷编号
    private String order_status;//消费券状态(1.待付款,2.已付款,4,商家验证消费,6.已完成，8 已退款 )
    private String record_id;//消费卷id

    public String getPay_serianlno() {
        return pay_serianlno;
    }

    public void setPay_serianlno(String pay_serianlno) {
        this.pay_serianlno = pay_serianlno;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    @Override
    public String toString() {
        return "ValidateCode{" +
                "pay_serianlno='" + pay_serianlno + '\'' +
                ", order_status='" + order_status + '\'' +
                ", record_id='" + record_id + '\'' +
                '}';
    }
}
