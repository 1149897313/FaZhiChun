package com.zgkj.factory.net;

import android.text.TextUtils;
import android.widget.TextView;

import com.zgkj.common.Common;
import com.zgkj.factory.Factory;
import com.zgkj.factory.persistent.Account;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/14.
 * Descr:   App程序网络请求的调度类（单例模式实现）
 */

public class Network {

    // 创建单例对象
    private static Network sInstance;

    // 定义实现网络请求的对象
    private Retrofit mRetrofit;

    static {
        sInstance = new Network();
    }

    /**
     * 私有化的构造方法
     */
    private Network() {
    }

    /**
     * 构建一个Retrofit网络请求对象
     *
     * @return
     */
    private static Retrofit getRetrofit() {
        if (sInstance.mRetrofit != null) {
            return sInstance.mRetrofit;
        }

        // 创建一个Okhttp对象
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                // 添加一个拦截器
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        // 拿到初始的请求
                        Request original = chain.request();

                        // 重新构建一个新的请求
                        Request.Builder builder = original.newBuilder();
                        // 添加头文件
                        builder.addHeader("Content-Type", "application/json");

                        // 添加token
                        if (!TextUtils.isEmpty(Account.getToken())){
                            // 如果token值不为空则将token添加进头文件中
                            builder.addHeader("token", Account.getToken());
                        }
                        // 构建
                        Request newRequest = builder.build();

                        return chain.proceed(newRequest);
                    }
                })
                .build();

        Retrofit.Builder builder = new Retrofit.Builder();

        sInstance.mRetrofit = builder.baseUrl(Common.Constant.API_URL)
                // 设置OkHttpClient
                .client(okHttpClient)
                // 设置gson解析器
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                // 构建一个Retrofit对象
                .build();

        return sInstance.mRetrofit;
    }

    /**
     * 返回一个请求代理
     *
     * @return
     */
    public static RemoteService remote() {

        return Network.getRetrofit().create(RemoteService.class);
    }


}
