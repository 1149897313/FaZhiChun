package com.zgkj.fazhichun.entity.order;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/21.
 * Descr:
 */
public class OrderVerification {

    private String record_id;//子订单id
    private String order_id;//主订单id
    private String  record_sn;//子订单号
    private String  pay_serianlno;//消费验证码
    private String  order_status;//订单状态
    private String  create_time;//时间

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getRecord_sn() {
        return record_sn;
    }

    public void setRecord_sn(String record_sn) {
        this.record_sn = record_sn;
    }

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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "OrderVerification{" +
                "record_id='" + record_id + '\'' +
                ", order_id='" + order_id + '\'' +
                ", record_sn='" + record_sn + '\'' +
                ", pay_serianlno='" + pay_serianlno + '\'' +
                ", order_status='" + order_status + '\'' +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}
