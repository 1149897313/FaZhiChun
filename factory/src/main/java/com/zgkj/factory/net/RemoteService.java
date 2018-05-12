package com.zgkj.factory.net;

import com.zgkj.factory.model.api.RspModel;
import com.zgkj.factory.model.api.TestRspModel;
import com.zgkj.factory.model.api.home.HomeRspModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/14.
 * Descr:   网络请求Api接口的定义
 */

public interface RemoteService {

    @GET("{page}/json")
    Call<RspModel<HomeRspModel>> getData(@Path("page") int page);

    @POST("/v1/banner/show")
    Call<RspModel<HomeRspModel>> setData(@Query("request_time") String request_time, @Query("is_from") String is_from, @Query("sign") String sign);

    @GET("test")
    Call<RspModel<TestRspModel>> test();


    @POST("{}")
    Call<RspModel<HomeRspModel>> setData(@Path("page") int page);
}
