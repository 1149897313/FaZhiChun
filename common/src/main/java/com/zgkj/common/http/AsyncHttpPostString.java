package com.zgkj.common.http;

/**
 * Created by zw.bai on 2016/12/28.
 */
public class AsyncHttpPostString {
    private String contentType;
    private String content;

    public AsyncHttpPostString(String contentType, String content) {
        this.contentType = contentType;
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
