package com.zgkj.common.http;


import android.util.Log;

import com.zgkj.common.Common;
import com.zgkj.common.utils.AccountManagers;
import com.zgkj.common.utils.MD5Util;
import com.zgkj.common.utils.SignUtil;

import java.io.File;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

import static okhttp3.internal.Util.delimiterOffset;
import static okhttp3.internal.Util.trimSubstring;
import static okhttp3.internal.platform.Platform.WARN;

/**
 * Created by zw.bai on 2016/12/28.
 */
public class AsyncOkHttpClient {
    public static final String TAG = "AsyncOkHttpClient";

    /**
     * 网络超时常量的定义
     */
    private static final int TIME_OUT = 10;


    // OkHttpClient连接对象
    private OkHttpClient mOkHttpClient;

    public AsyncOkHttpClient() {

        CookieManager cookieManager = new CookieManager();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 连接超时
        builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        // 设置重定向,其实默认也是true
        builder.followRedirects(true);
        builder.cookieJar(new JavaNetCookieJar(cookieManager));
        mOkHttpClient = builder.build();

    }


    public void postFile(String method, AsyncHttpPostFormData formData, final AsyncResponseHandler responseHandler) {
        String url=Common.Constant.API_URL+method;
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        multipartBody.setType(MultipartBody.FORM);
        if (formData != null) {
            formData.addFormData("is_from","android");
            formData.addFormData("request_time",System.currentTimeMillis()/1000);
            String paramString = url;
            if (url.indexOf("?") > 0) {
                paramString += "&";
            } else {
                paramString += "?";
            }
            Map<String, Object> formDataMap = formData.getFormData();
            String sign= SignUtil.createSign(formDataMap,"fazhichun");

            formData.addFormData("sign",sign);

            Set<String> postParamKeys = formDataMap.keySet();
            for (String key : postParamKeys) {
                multipartBody.addFormDataPart(key, formDataMap.get(key).toString());
            }
        }
        Map<String, Object> paramsMap = formData.getFormData();
        for (String key : paramsMap.keySet()) {
            Object object = paramsMap.get(key);
            if (key.equals("file")) {
                File file = new File(object.toString());
                multipartBody.addFormDataPart(key, file.getName(), RequestBody.create(null, file));
            } else if (key.equals("image")) {
                File file = new File(object.toString());
                multipartBody.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
            } else {
                multipartBody.addFormDataPart(key, object.toString());
            }
        }
        requestBuilder.post(multipartBody.build());
        requestBuilder.addHeader("access-token", AccountManagers.getToken());
        Request request = requestBuilder.build();
        executeRequest(mOkHttpClient, request, responseHandler);

    }

    public void post(String method, AsyncHttpPostFormData formData, final AsyncResponseHandler responseHandler) {
        String url=Common.Constant.API_URL+method;
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (formData != null) {
            formData.addFormData("is_from","android");
            formData.addFormData("request_time",System.currentTimeMillis()/1000);
            String paramString = url;
            if (url.indexOf("?") > 0) {
                paramString += "&";
            } else {
                paramString += "?";
            }
            Map<String, Object> formDataMap = formData.getFormData();
            String sign= SignUtil.createSign(formDataMap,"fazhichun");
            formData.addFormData("sign",sign);

            Set<String> postParamKeys = formDataMap.keySet();
            for (String key : postParamKeys) {
                formBodyBuilder.add(key, formDataMap.get(key).toString());
            }
        }
        requestBuilder.post(formBodyBuilder.build());
        requestBuilder.addHeader("Connection", "close");
        requestBuilder.addHeader("access-token", AccountManagers.getToken());
        Log.i(TAG, "token: "+AccountManagers.getToken());
        Request request = requestBuilder.build();
        executeRequest(mOkHttpClient, request, responseHandler);
    }

    public void post(String url, Headers headers, AsyncHttpPostFormData formData, final AsyncResponseHandler responseHandler) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        // 设置 headers
        okhttp3.Headers.Builder headersBuilder = new okhttp3.Headers.Builder();
        if (headers != null) {
            Map<String, String> headersMap = headers.getHeaders();
            Set<String> headerKeys = headersMap.keySet();
            for (String key : headerKeys) {
                headersBuilder.add(key, headersMap.get(key));
            }
            requestBuilder.headers(headersBuilder.build());
        }
        // 设置RequestBody
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (formData != null) {
            Map<String, Object> formDataMap = formData.getFormData();
            Set<String> postParamKeys = formDataMap.keySet();
            for (String key : postParamKeys) {
                formBodyBuilder.add(key, formDataMap.get(key).toString());
            }
        }
        requestBuilder.post(formBodyBuilder.build());
        Request request = requestBuilder.build();
        executeRequest(mOkHttpClient, request, responseHandler);
    }


    public void post(String url, AsyncHttpPostFile postBody, final AsyncResponseHandler responseHandler) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(RequestBody.create(MediaType.parse(postBody.getContentType()), postBody.getContent()));
        Request request = requestBuilder.build();
        executeRequest(mOkHttpClient, request, responseHandler);
    }

    public void post(String url, Headers headers, AsyncHttpPostFile postBody, final AsyncResponseHandler responseHandler) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(RequestBody.create(MediaType.parse(postBody.getContentType()), postBody.getContent()));
        // 设置 headers
        okhttp3.Headers.Builder headersBuilder = new okhttp3.Headers.Builder();
        if (headers != null) {
            Map<String, String> headersMap = headers.getHeaders();
            Set<String> headerKeys = headersMap.keySet();
            for (String key : headerKeys) {
                headersBuilder.add(key, headersMap.get(key));
            }
            requestBuilder.headers(headersBuilder.build());
        }

        Request request = requestBuilder.build();
        executeRequest(mOkHttpClient, request, responseHandler);
    }

    public void get(String url, final AsyncResponseHandler responseHandler) {
        get(url, null, null, responseHandler);
    }

    public void get(String url, Headers headers, AsyncHttpPostFormData formData, final AsyncResponseHandler responseHandler) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        okhttp3.Headers.Builder headersBuilder = new okhttp3.Headers.Builder();

        if (headers != null) {
            Map<String, String> headersMap = headers.getHeaders();
            Set<String> headerKeys = headersMap.keySet();
            for (String key : headerKeys) {
                headersBuilder.add(key, headersMap.get(key));
            }
            requestBuilder.headers(headersBuilder.build());
        }

        if (formData != null) {
            String paramString = url;
            if (url.indexOf("?") > 0) {
                paramString += "&";

            } else {
                paramString += "?";
            }
            Map<String, Object> formDataMap = formData.getFormData();
            Set<String> postParamKeys = formDataMap.keySet();
            for (String key : postParamKeys) {
                paramString += key + "=" + formDataMap.get(key) + "&";
            }
            requestBuilder.url(paramString);


        }

        Request request = requestBuilder.build();
        executeRequest(mOkHttpClient, request, responseHandler);
    }

    public OkHttpClient buildClient(OkHttpClient httpClient, AsyncResponseHandler responseProgressHandler) {

        if (!responseProgressHandler.isProgressListenerEmpty()) {
            final AsyncHttpProgressListener listener = new AsyncHttpProgressListener(responseProgressHandler);
            OkHttpClient progressHttpClient = httpClient.newBuilder().addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());

                    return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), listener)).build();
                }
            }).build();
            return progressHttpClient;
        } else {
            return httpClient;
        }
    }

    private void executeRequest(OkHttpClient httpClient, Request request, final AsyncResponseHandler responseProgressHandler) {


        OkHttpClient client = buildClient(httpClient, responseProgressHandler);


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (responseProgressHandler != null) {
                    responseProgressHandler.sendFailureMessage(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (responseProgressHandler != null) {
                    AsyncHttpResponse asyncHttpResponse = new AsyncHttpResponse();
                    String result = response.body().string();

                    asyncHttpResponse.setBody(result);
                    asyncHttpResponse.setCode(response.code());
                    asyncHttpResponse.setHeaders(response.headers().toString());
                    responseProgressHandler.sendSuccessMessage(asyncHttpResponse);
                }
            }
        });
    }


    private static class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final AsyncHttpProgressListener progressListener;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody, AsyncHttpProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }

    private class AsyncHttpProgressListener {
        private AsyncResponseHandler responseProgressHandler;

        public AsyncHttpProgressListener(AsyncResponseHandler httpResponseProgressHandler) {
            this.responseProgressHandler = httpResponseProgressHandler;
        }

        public void update(long bytesRead, long contentLength, boolean done) {
            if (responseProgressHandler != null) {
                responseProgressHandler.sendUpdateMessage(bytesRead, contentLength);
            }
        }
    }

    /**
     * OrderList cookie jar that delegates to a {@link CookieHandler}.
     */
    public final class JavaNetCookieJar implements CookieJar {
        private final CookieHandler cookieHandler;

        public JavaNetCookieJar(CookieHandler cookieHandler) {
            this.cookieHandler = cookieHandler;
        }

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (cookieHandler != null) {
                List<String> cookieStrings = new ArrayList<>();
                for (Cookie cookie : cookies) {
                    cookieStrings.add(cookie.toString());
                }
                Map<String, List<String>> multimap = Collections.singletonMap("Set-Cookie", cookieStrings);
                try {
                    cookieHandler.put(url.uri(), multimap);
                } catch (IOException e) {
                    Platform.get().log(WARN, "Saving cookies failed for " + url.resolve("/..."), e);
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            // The RI passes all headers. We don't have 'em, so we don't pass 'em!
            Map<String, List<String>> headers = Collections.emptyMap();
            Map<String, List<String>> cookieHeaders;
            try {
                cookieHeaders = cookieHandler.get(url.uri(), headers);
            } catch (IOException e) {
                Platform.get().log(WARN, "Loading cookies failed for " + url.resolve("/..."), e);
                return Collections.emptyList();
            }

            List<Cookie> cookies = null;
            for (Map.Entry<String, List<String>> entry : cookieHeaders.entrySet()) {
                String key = entry.getKey();
                if (("Cookie".equalsIgnoreCase(key) || "Cookie2".equalsIgnoreCase(key))
                        && !entry.getValue().isEmpty()) {
                    for (String header : entry.getValue()) {
                        if (cookies == null) {
                            cookies = new ArrayList<>();
                        }
                        cookies.addAll(decodeHeaderAsJavaNetCookies(url, header));
                    }
                }
            }

            return cookies != null
                    ? Collections.unmodifiableList(cookies)
                    : Collections.<Cookie>emptyList();
        }

        /**
         * Convert a request header to OkHttp's cookies via {@link HttpCookie}. That extra step handles
         * multiple cookies in a single request header, which {@link Cookie#parse} doesn't support.
         */
        private List<Cookie> decodeHeaderAsJavaNetCookies(HttpUrl url, String header) {
            List<Cookie> result = new ArrayList<>();
            for (int pos = 0, limit = header.length(), pairEnd; pos < limit; pos = pairEnd + 1) {
                pairEnd = delimiterOffset(header, pos, limit, ";,");
                int equalsSign = delimiterOffset(header, pos, pairEnd, '=');
                String name = trimSubstring(header, pos, equalsSign);
                if (name.startsWith("$")) {
                    continue;
                }

                // We have either name=value or just a name.
                String value = equalsSign < pairEnd
                        ? trimSubstring(header, equalsSign + 1, pairEnd)
                        : "";

                // If the value is "quoted", drop the quotes.
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }

                result.add(new Cookie.Builder()
                        .name(name)
                        .value(value)
                        .domain(url.host())
                        .build());
            }
            return result;
        }
    }
}
