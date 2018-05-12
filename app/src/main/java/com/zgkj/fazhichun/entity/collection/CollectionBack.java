package com.zgkj.fazhichun.entity.collection;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/10.
 * Descr:
 */
public class CollectionBack {
    private String status; //true或false	成功或失败
    private String  is_walk;//成功时返回-1添加收藏-取消收藏
    private String msg;//返回提示信息

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_walk() {
        return is_walk;
    }

    public void setIs_walk(String is_walk) {
        this.is_walk = is_walk;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "CollectionBack{" +
                "status='" + status + '\'' +
                ", is_walk='" + is_walk + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
