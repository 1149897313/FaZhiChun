package com.zgkj.fazhichun.entity.massage;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/18.
 * Descr:  消息列表
 */
public class Massage {

    private String message_id;//数据id
    private String message_title;//名称
    private String message_content;//内容
    private String create_time;//时间（已处理）

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMessage_title() {
        return message_title;
    }

    public void setMessage_title(String message_title) {
        this.message_title = message_title;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "Massage{" +
                "message_id='" + message_id + '\'' +
                ", message_title='" + message_title + '\'' +
                ", message_content='" + message_content + '\'' +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}
