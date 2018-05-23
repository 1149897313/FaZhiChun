package com.zgkj.fazhichun.activities.mywallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
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
import com.zgkj.common.widgets.EditInputMoneyFilter;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.common.widgets.text.ClearTextIconEditText;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.activities.dialog.PwdPayDialogActivity;
import com.zgkj.fazhichun.entity.StateEntity;
import com.zgkj.fazhichun.view.LoadingView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 提现
 */

public class WithdrawDepositActivity extends ToolbarActivity {

    /**
     * UI
     */
    private LinearLayout content;
    private ClearTextIconEditText account_number, name, tx_balance;
    private TextView balance;

    /**
     * DATA
     */
    private LoadManager mLoadManager;
    private int mTypeValue;
    private float totalUserMoney;

    public static void show(Context context, float totalUserMoney) {
        // 跳转并传递值
        Intent intent = new Intent(context, WithdrawDepositActivity.class);
        intent.putExtra("TOTAL", totalUserMoney);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        totalUserMoney = bundle.getFloat("TOTAL");
        return totalUserMoney > 0;
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_wihdraw_deposit;
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        content = findViewById(R.id.content);
        account_number = findViewById(R.id.account_number);
        name = findViewById(R.id.name);
        tx_balance = findViewById(R.id.tx_balance);
        balance = findViewById(R.id.balance);
        balance.setText("当前奖励余额￥" + totalUserMoney);

        InputFilter[] filters = {new EditInputMoneyFilter()};

        tx_balance.setFilters(filters);

        mLoadManager = LoadFactory.getInstance().register(content, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                mLoadManager.showStateView(LoadingView.class);
            }
        });
        mLoadManager.showSuccessView();
    }

    /**
     * 用户提现
     *amount两位小数	提现金额
     withdraw_no支付宝账号
     account_name提现账户名
     pay_pwd 6位	支付密码
     * @param pay_pwd
     */
    private void getWithdrawDeposit(String amount,String withdraw_no,String account_name,String pay_pwd) {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("amount", amount);
        AsyncHttpPostFormData.addFormData("withdraw_no", withdraw_no);
        AsyncHttpPostFormData.addFormData("account_name", account_name);
        AsyncHttpPostFormData.addFormData("pay_pwd", pay_pwd);
        okHttpClient.post("/v1/wallet/acc-withdraw", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showStateView(NetErrorView.class);
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:用户提现" + response.toString());
                Type type = new TypeToken<RspModel<StateEntity>>() {
                }.getType();
                StateEntity value = getAnalysis(response, type, "用户提现");
                mLoadManager.showSuccessView();
                if (value != null && !"[]".equals(value.toString())) {
                    if ("T".equals(value.getIs_success())) {
                        SuccessWithdrawDepositActivity.show(mContext);
                        finish();
                    } else {
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
                String payValue = account_number.getText().toString();
                String nameValue = name.getText().toString();
                if(!TextUtils.isEmpty(payValue) && !TextUtils.isEmpty(nameValue)){
                    String JO=tx_balance.getText().toString();
                    float balanceValue=Float.parseFloat(TextUtils.isEmpty(JO)?"0":JO);
                    if(balanceValue>0 && balanceValue<=totalUserMoney){
                        Intent intent = new Intent(mContext, PwdPayDialogActivity.class);
                        intent.putExtra("AMOUNT", balanceValue);
                        startActivityForResult(intent, 1000);
                    }else{
                        App.showMessage("提取金额错误！");
                    }
                }else{
                    App.showMessage("账户和用户名不能为空！");
                }
                break;
            case R.id.all_withdraw_deposit:
                tx_balance.setText(String.valueOf(totalUserMoney));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000 && resultCode == 1001 && data != null) {
            getWithdrawDeposit(account_number.getText().toString(),name.getText().toString(),tx_balance.getText().toString(),data.getStringExtra("PWD"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
