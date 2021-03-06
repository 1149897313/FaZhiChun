package com.zgkj.common.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zw.bai @ 2016/12/28.
 */
public final class AsyncHttpPostFormData {
    //map key.equals("file")
    private Map<String, Object> mFormData = new HashMap<>();

    public AsyncHttpPostFormData addFormData(String headerName, Object headerValue) {
        if (headerValue == null) {
            headerValue = "";
        }
        String value="";
        try {
            value=URLDecoder.decode(String.valueOf(headerValue), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mFormData.put(headerName, value);
        return this;
    }

    public Map<String, Object> getFormData() {
        return mFormData;
    }

    public void post(String url, final AsyncResponseHandler responseHandler) {
        AsyncOkHttpClient request = new AsyncOkHttpClient();
        request.post(url, this, responseHandler);
    }

    public void postFile(String url, final AsyncResponseHandler responseHandler) {
        AsyncOkHttpClient request = new AsyncOkHttpClient();
        request.postFile(url, this, responseHandler);
    }

    public void get(String url, final AsyncResponseHandler responseHandler) {
        AsyncOkHttpClient request = new AsyncOkHttpClient();
        request.get(url, null, this, responseHandler);
    }

}
