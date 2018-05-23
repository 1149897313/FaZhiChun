package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
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
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.LoadingView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/19
 * Descr:  退款
 */
public class OrderRefundActivity extends ToolbarActivity implements View.OnClickListener {
    /**
     * DATA
     */
    private LoadManager mLoadManager;
    private String id;

    /**
     * UI
     */
    private NestedScrollView mNestedScrollView;
    private RadioGroup radio_group;
    private EditText text_content;
    private TextView refund_order;

    public static void show(Context context, String id) {
        Intent intent = new Intent(context, OrderRefundActivity.class);
        intent.putExtra("ID", id);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        id = bundle.getString("ID");
        return !TextUtils.isEmpty(id);
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        mNestedScrollView = findViewById(R.id.nested_scroll_view);
        radio_group = findViewById(R.id.radio_group);
        text_content = findViewById(R.id.text_content);
        refund_order = findViewById(R.id.refund_order);


        refund_order.setOnClickListener(this);
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                selectText = radioButton.getText().toString();
                App.showMessage(selectText);
            }
        });
    }

    private String selectText;//退款原因

    @Override
    protected void initDatas() {
        super.initDatas();
        mLoadManager = LoadFactory.getInstance().register(mNestedScrollView, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                mLoadManager.showStateView(LoadingView.class);
            }
        });
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.order_refund;
    }


    /**
     * 提交退款
     *
     * @param record_id
     * @param refund_reason
     * @param other_reason
     */
    private void onRefundSubmit(String record_id, String refund_reason, String other_reason) {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("record_id", record_id);//订单id
        AsyncHttpPostFormData.addFormData("refund_reason", refund_reason);//退款原因
        AsyncHttpPostFormData.addFormData("other_reason", other_reason);//其他原因
        okHttpClient.post("/v1/order/refund", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showStateView(NetErrorView.class);
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:提交退款" + response.toString());
                Type type = new TypeToken<RspModel<RspModel>>() {
                }.getType();
                RspModel value = getAnalysis(response, type, "提交退款");
                if (value != null && !"[]".equals(value.toString())) {
                    if (value.getCode() == 1) {

                    }
                    App.showMessage(value.getData().toString());
                } else {
                    mLoadManager.showStateView(EmptyView.class);
                }
            }
        });
    }

    /**
     * 数据解析
     *
     * @param response
     * @param type
     * @param log
     * @param <T>
     * @return
     */
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
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refund_order://提交退款
                onRefundSubmit(id,selectText,text_content.getText().toString());
                break;
            default:
                break;
        }
    }
}
