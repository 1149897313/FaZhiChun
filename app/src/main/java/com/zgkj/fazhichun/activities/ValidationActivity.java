package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.common.http.AsyncHttpPostFormData;
import com.zgkj.common.http.AsyncHttpResponse;
import com.zgkj.common.http.AsyncOkHttpClient;
import com.zgkj.common.http.AsyncResponseHandler;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.Verification;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.LoadingView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/21.
 * Descr:   消费验证
 */
public class ValidationActivity extends ToolbarActivity {

    /**
     * UI
     */
    private LinearLayout content;
    private TextView orderId,time,validationCode;
    private ImageView qrCode;

    /**
     * DATA
     */
    // 创建界面加载状态的管理对象
    private LoadManager mLoadManager;
    private String recordId;

    /**
     * @param context
     */
    public static void show(Context context,String recordId) {
        // 跳转并传递值
        Intent intent = new Intent(context, ValidationActivity.class);
        intent.putExtra("ID",recordId);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        recordId = bundle.getString("ID");
        return !TextUtils.isEmpty(recordId);
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_validation;
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        content=findViewById(R.id.content);
        orderId=findViewById(R.id.order_id);
        time=findViewById(R.id.time);
        qrCode=findViewById(R.id.qr_code);
        validationCode=findViewById(R.id.verification_code);
    }

    private void showView(Verification verification){
        orderId.setText("订单号："+verification.getOrder_detail().getRecord_sn());
        time.setText(verification.getOrder_detail().getCreate_time());
        Bitmap mBitmap = CodeUtils.createImage(verification.getUrl(), 400, 400, null);
        qrCode.setImageBitmap(mBitmap);
        validationCode.setText("消费验证码："+verification.getOrder_detail().getPay_serianlno());
    }

    @Override
    protected void initDatas() {
        super.initDatas();

        mLoadManager = LoadFactory.getInstance().register(content, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                mLoadManager.showStateView(LoadingView.class);
            }
        });
        orderVerification(recordId);
    }

    /**
     * 消费二维码url验证接口
     * @param id
     */
    private void orderVerification(String id) {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("record_id", id);
        okHttpClient.post("/v1/order/order-verification", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
                mLoadManager.showStateView(NetErrorView.class);
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:消费二维码 " + response.toString());
                Type type = new TypeToken<RspModel<Verification>>() {
                }.getType();
                Verification verification = getAnalysis(response, type, "消费二维码");
                if (verification != null) {
                    showView(verification);
                }
            }
        });
    }

    private <T> T getAnalysis(AsyncHttpResponse response, Type type, String log) {
        switch (response.getCode()) {
            case 200:
                try {
                    Gson gson = new Gson();
                    RspModel<T> rspModel = gson.fromJson(response.getBody(), type);
                    Log.i(TAG, "onSuccess: " + log + rspModel.toString());
                    switch (rspModel.getCode()) {
                        case 1:
                            mLoadManager.showStateView(EmptyView.class);
                            break;
                        case 200:
                            mLoadManager.showSuccessView();
                            return rspModel.getData();
                        default:
                            App.showMessage("错误码：" + rspModel.getCode());
                            mLoadManager.showStateView(EmptyView.class);
                            break;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
        }
        mLoadManager.showStateView(EmptyView.class);
        return null;
    }

}
