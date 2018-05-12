package com.zgkj.factory.net;

import com.zgkj.factory.model.api.RspModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/14.
 * Descr:   异步网络请求的回调处理类
 */

public abstract class AsyncResponseHandler<T extends RspModel> implements Callback<T> {


    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.raw().code() == 200){
            if (response.body().success()){
                onSuccess(response);
            }else {
                onError(response.body().getCode(), response.body().getMessage());
            }
        }else {
            onFailure(call, new RuntimeException("服务器响应请求失败！"));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFailure(t.getMessage());
    }

    public abstract void onSuccess(Response<T> response);

    public abstract void onError(int code, String errorMsg);

    public abstract void onFailure(String failureMsg);
}

