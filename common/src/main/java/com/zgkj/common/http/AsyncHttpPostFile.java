package com.zgkj.common.http;

import java.io.File;

/**
 * Created by zw.bai on 2016/12/28.
 */
public class AsyncHttpPostFile {
    private String contentType="application/octet-stream";
    private File content;

    public AsyncHttpPostFile( File content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public File getContent() {
        return content;
    }

    public void setContent(File content) {
        this.content = content;
    }
}
