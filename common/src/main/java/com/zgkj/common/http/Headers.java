package com.zgkj.common.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zw.bai @ 2016/12/28.
 */
public final class Headers {
    private Map<String, String> mHeaders = new HashMap<>();

    public Headers addHost(String host) {
        mHeaders.put("Host", host);
        return this;
    }

    public Headers addReferer(String referer) {
        mHeaders.put("Referer", referer);
        return this;
    }

    public Headers addOrigin(String origin) {
        mHeaders.put("Origin", origin);
        return this;
    }

    public Headers addAccept(String accept) {
        mHeaders.put("Accept", accept);
        return this;
    }

    public Headers addUserAgent(String userAgent) {
        mHeaders.put("User-Agent", userAgent);
        return this;
    }

    public Headers addAcceptEncoding(String acceptEncoding) {
        mHeaders.put("Accept-Encoding", acceptEncoding);
        return this;
    }

    public Headers addAcceptLanguage(String acceptLanguage) {
        mHeaders.put("Accept-Language", acceptLanguage);
        return this;
    }

    public Headers addCookie(String cookie) {
        mHeaders.put("Cookie", cookie);
        return this;
    }

    public Headers addConnection(String connection) {
        mHeaders.put("Connection", connection);
        return this;
    }

    public Headers addContentType(String contentType) {
        mHeaders.put("Content-Type", contentType);
        return this;
    }

    public Headers add(String key, String value) {
        mHeaders.put(key, value);
        return this;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }
}
