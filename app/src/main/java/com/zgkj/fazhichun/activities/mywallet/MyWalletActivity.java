package com.zgkj.fazhichun.activities.mywallet;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.zgkj.fazhichun.entity.wallet.BrokerageTotal;
import com.zgkj.fazhichun.view.LoadingView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 我的钱包
 */

public class MyWalletActivity extends ToolbarActivity {

    /**
     * UI
     */
    private LinearLayout content;
    private TextView balance_value,add_up;

    /**
     * DATA
     */
    private LoadManager mLoadManager;
    private int mTypeValue;

    public static void show(Context context) {
        // 跳转并传递值
        Intent intent = new Intent(context, MyWalletActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_my_wallet;
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        content = findViewById(R.id.content);
        balance_value=findViewById(R.id.balance_value);
        add_up=findViewById(R.id.add_up);

        mLoadManager = LoadFactory.getInstance().register(content, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                mLoadManager.showStateView(LoadingView.class);
            }
        });
        mLoadManager.showSuccessView();
        getWallet();
    }

    private float userMoney;

    private void showView(BrokerageTotal data){
        balance_value.setText("￥"+data.getUser_money());
        userMoney=data.getUser_money();
        add_up.setText("您已累计获得奖励￥"+data.getTotal_user_money());
    }

    /**
     * 获取佣金总额和佣金余额
     */
    private void getWallet() {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        okHttpClient.post("/v1/extension/extension-total-money", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showStateView(NetErrorView.class);
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:佣金总额" + response.toString());
                Type type = new TypeToken<RspModel<BrokerageTotal>>() {
                }.getType();
                BrokerageTotal value = getAnalysis(response, type, "佣金总额");
                mLoadManager.showSuccessView();
                if (value != null && !"[]".equals(value.toString())) {
                    showView(value);
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
            case R.id.wallet_rule://推广规则
                GeneralizeActivity.show(mContext);
                break;
            case R.id.record_text://提现记录
                WithdrawalRecordActivity.show(mContext);
                break;
            case R.id.withdraw_deposit_button://提现
                WithdrawDepositActivity.show(mContext,userMoney);
                break;
            case R.id.yjmx_layout://佣金明细
                BrokerageRecordActivity.show(mContext);
                break;
            case R.id.tgtj_layout://推广统计
                GeneralizeRecordActivity.show(mContext);
                break;
            case R.id.tgmp_layout://推广名片
                CallingCardActivity.show(mContext);
                break;
            case R.id.zfgl_layout://支付管理
                PwdSettingActivity.show(mContext);
                break;
            default:
                break;
        }
    }
}
