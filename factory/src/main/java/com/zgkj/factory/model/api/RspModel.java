package com.zgkj.factory.model.api;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/15.
 * Descr:   定义json数据的公共的解析类
 */

public class RspModel<T> {


    // 请求成功的返回
    public static final int SUCCEED = 200;

    private int code;
    private String message;
    private T data;

    /**
     * 判断是否返回成功的方法
     *
     * @return 返回true表示成功， 返回false表示失败
     */
    public boolean success() {
        return code == SUCCEED;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RspModel{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
