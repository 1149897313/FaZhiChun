package com.zgkj.factory.net;

import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zgkj.common.Common;
import com.zgkj.common.http.AsyncHttpPostFormData;
import com.zgkj.common.http.AsyncHttpResponse;
import com.zgkj.common.http.AsyncOkHttpClient;
import com.zgkj.common.http.AsyncResponseHandler;
import com.zgkj.factory.Factory;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.factory.model.api.home.Banner;
import com.zgkj.factory.persistent.Account;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/3.
 * Descr:
 */
public class NewOkHttpClient {


    // 创建单例对象
    private static NewOkHttpClient sInstance;

    // 定义实现网络请求的对象
    private Retrofit mRetrofit;

//    FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
//        new NewOkHttpClient().init(formBody,"/v1/banner/show").enqueue(new Callback() {
//        @Override
//        public void onFailure(okhttp3.Call call, IOException e) {
//            e.printStackTrace();
//        }
//
//        @Override
//        public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
//            if(response.code()==200){
//                try {
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<RspModel<List<Banner>>>() {
//                    }.getType();
//                    RspModel<List<Banner>> rspModel=gson.fromJson(response.body().string(),type);
//                    System.out.println(rspModel.toString());
//
//                    AsyncOkHttpClient okHttpClient=new AsyncOkHttpClient();
//                    AsyncHttpPostFormData AsyncHttpPostFormData=new AsyncHttpPostFormData();
//                    okHttpClient.post("/v1/banner/show", AsyncHttpPostFormData, new AsyncResponseHandler() {
//                        @Override
//                        public void onFailure(IOException e) {
//
//                        }
//
//                        @Override
//                        public void onSuccess(AsyncHttpResponse response) {
//                            System.out.println("-----:"+response.getBody().toString());
//                            Toast.makeText(mContext, "测试", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } catch (JsonSyntaxException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    });

    static {
        sInstance = new NewOkHttpClient();
    }

    /**
     *
     * @param formBody 参数
     * @param methods 如：/v1/banner/show
     * @return
     */
    public static Call init(FormBody.Builder formBody,String methods)  {

        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        formBody.add("is_from","android");
        formBody.add("request_time",String.valueOf(System.currentTimeMillis()/1000));
        formBody.add("sign","123");//传递键值对参数

        Request request = new Request.Builder()//创建Request 对象。
                .url(Common.Constant.API_URL+methods)
                .post(formBody.build())//传递请求体
                .build();
        return client.newCall(request);
    }
}
