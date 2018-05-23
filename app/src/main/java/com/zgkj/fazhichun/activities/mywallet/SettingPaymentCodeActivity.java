package com.zgkj.fazhichun.activities.mywallet;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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
import com.zgkj.common.widgets.text.ClearTextIconEditText;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.StateEntity;
import com.zgkj.fazhichun.view.LoadingView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 设置支付密码
 */

public class SettingPaymentCodeActivity extends ToolbarActivity {

    /**
     * UI
     */
    private LinearLayout content;
    private ClearTextIconEditText payment_code, payment_code_ok;

    /**
     * DATA
     */
    private LoadManager mLoadManager;
    private int mTypeValue;

    public static void show(Context context) {
        // 跳转并传递值
        Intent intent = new Intent(context, SettingPaymentCodeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_setting_pay;
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        content=findViewById(R.id.content);
        payment_code = findViewById(R.id.payment_code);
        payment_code_ok = findViewById(R.id.payment_code_ok);

        mLoadManager = LoadFactory.getInstance().register(content, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                mLoadManager.showStateView(LoadingView.class);
            }
        });
        mLoadManager.showSuccessView();
    }

    /**
     * 设置支付密码
     * @param pay_pwd
     */
    private void setPayPwd(String pay_pwd) {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("pay_pwd", pay_pwd);
        okHttpClient.post("/v1/wallet/open-step_4", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showStateView(NetErrorView.class);
                e.printStackTrace();
            }

        @Override
        public void onSuccess(AsyncHttpResponse response) {
            Log.i(TAG, "onSuccess:设置支付密码" + response.toString());
            Type type = new TypeToken<RspModel<StateEntity>>() {
            }.getType();
            StateEntity value = getAnalysis(response, type, "设置支付密码");
            mLoadManager.showSuccessView();
            if (value != null && !"[]".equals(value.toString())) {
                if("T".equals(value.getIs_success())){
                    App.showMessage("支付密码设置成功！");

                }else {
                    App.showMessage(value.getErr_msg());
                }
            } else {
                App.showMessage();
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
                            break;
                        case 200:
                            return rspModel.getData();
                        default:
                            App.showMessage("错误码：" + rspModel.getCode());
                            break;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.sumbit_button:
                String payValue=payment_code.getText().toString();
                String payValueOk=payment_code_ok.getText().toString();
                if (!"".equals(payValue) && payValue.length() == 6) {
                    if(payValue.equals(payValueOk)){
                        setPayPwd(payValue);
                    }else {
                        App.showMessage("确认密码错误！");
                    }
                } else {
                    App.showMessage("请输入六位支付密码！");
                }
                break;
            default:
                break;
        }
    }
}
