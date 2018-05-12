package com.zgkj.common.http;

/**
 * Created by zw.bai on 2016/12/28.
 */
public class AsyncHttpResponse {

    private int code;
    private String headers;
    private String body;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "AsyncHttpResponse{" +
                "code=" + code +
                ", headers='" + headers + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
