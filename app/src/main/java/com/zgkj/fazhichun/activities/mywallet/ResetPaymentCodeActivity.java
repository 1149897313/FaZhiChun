package com.zgkj.fazhichun.activities.mywallet;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.zgkj.fazhichun.activities.AccountActivity;
import com.zgkj.fazhichun.entity.CodeStatus;
import com.zgkj.fazhichun.entity.StateEntity;
import com.zgkj.fazhichun.entity.user.BindMobile;
import com.zgkj.fazhichun.view.LoadingView;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 忘记支付密码
 */

public class ResetPaymentCodeActivity extends ToolbarActivity {

    /**
     * UI
     */
    private LinearLayout content;
    private ClearTextIconEditText payment_code, payment_code_ok, mCodeView;
    private TextView before_payment_code, mGetCodeView;

    /**
     * DATA
     */
    private LoadManager mLoadManager;
    private int mTypeValue;

    public static void show(Context context) {
        // 跳转并传递值
        Intent intent = new Intent(context, ResetPaymentCodeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_reset_payment;
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        content = findViewById(R.id.content);
        before_payment_code = findViewById(R.id.before_payment_code);
        mCodeView = findViewById(R.id.code);
        mGetCodeView = findViewById(R.id.get_code);
        payment_code = findViewById(R.id.payment_code);
        payment_code_ok = findViewById(R.id.payment_code_ok);

        mLoadManager = LoadFactory.getInstance().register(content, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                mLoadManager.showStateView(LoadingView.class);
            }
        });
        mLoadManager.showSuccessView();
        getBindMobile();
    }


    /**
     * 绑定手机号查询
     */
    private void getBindMobile() {
        mLoadManager.showStateView(LoadingView.class);
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        okHttpClient.post("/v1/user/is-bind-mobile", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showSuccessView();
                App.showMessage("修改失败");
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:绑定手机号查询" + response.toString());
                Type type = new TypeToken<RspModel<BindMobile>>() {
                }.getType();
                BindMobile value = getAnalysis(response, type, "绑定手机号查询");
                mLoadManager.showSuccessView();
                if (value != null && !"[]".equals(value.toString())) {
                    if (value.getIs_bind()==1) {
                        before_payment_code.setText(value.getMobile());
                    } else {
                        AccountActivity.show(mContext,true);
                    }
                } else {
                    App.showMessage();
                }
            }
        });
    }

    /**
     * 修改支付密码
     *
     * @param old_pwd
     * @param new_pwd
     */
    private void setPayPwd(String old_pwd, String new_pwd) {
        mLoadManager.showStateView(LoadingView.class);
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("old_pwd", old_pwd);//old_pwd	string	是		6位长度	原密码
        AsyncHttpPostFormData.addFormData("new_pwd", new_pwd);//new_pwd	string	是		6位长度	新密码
        okHttpClient.post("/v1/wallet/pwd-edit", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showSuccessView();
                App.showMessage("修改失败");
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
                    if ("T".equals(value.getIs_success())) {
                        App.showMessage("修改支付密码成功！");
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


    /**
     * 发送验证码
     *
     * @param mobile
     * @param type
     */
    private void getCode(String mobile, String type) {


        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData asyncHttpPostFormData = new AsyncHttpPostFormData();
        asyncHttpPostFormData.addFormData("mobile", mobile);
        asyncHttpPostFormData.addFormData("type", type);
        okHttpClient.post("/v1/smscode/get-sms-code", asyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess: " + response.toString());
                if (response.getCode() == 200) {
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<RspModel<CodeStatus>>() {
                        }.getType();
                        RspModel<CodeStatus> rspModel = gson.fromJson(response.getBody(), type);
                        //发送成功
                        if ("T".equals(rspModel.getData().getIs_success())) {
                            // 初始化短信倒计时计算器（60秒）
                            mSmsCodeCountDownTimer = new SmsCodeCountDownTimer(60000, 1000);
                            // 启动短信计时器
                            mSmsCodeCountDownTimer.start();
                        }
                        Toast.makeText(mContext, rspModel.getData().getTips(), Toast.LENGTH_SHORT).show();

                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // 创建短信倒计时计数器的对象
    private SmsCodeCountDownTimer mSmsCodeCountDownTimer;


    /**
     * 实现短信验证码倒计时功能
     */
    public class SmsCodeCountDownTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public SmsCodeCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mGetCodeView.setEnabled(false);
            // 短信验证码倒计时
            mGetCodeView.setText("重新发送（" + millisUntilFinished / 1000L + "s)");
            mGetCodeView.setTextColor(mContext.getResources().getColor(R.color.textColorAccent));
        }

        @Override
        public void onFinish() {
            mGetCodeView.setEnabled(true);
            mGetCodeView.setText("获取验证码");
            mGetCodeView.setTextColor(getResources().getColor(R.color.textColorFirst));
        }
    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.get_code:
                getCode("13896654867", "update_paypwd");
                break;
            case R.id.sumbit_button:
                String oldPwd = before_payment_code.getText().toString();
                String newPwd = payment_code.getText().toString();
                String payValueOk = payment_code_ok.getText().toString();
                if (!"".equals(oldPwd) && oldPwd.length() == 6 && !"".equals(newPwd) && newPwd.length() == 6) {
                    if (newPwd.equals(payValueOk)) {
                        setPayPwd(oldPwd, newPwd);
                    } else {
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

    @Override
    protected void onDestroy() {
        // 释放倒计时对象
        if (mSmsCodeCountDownTimer != null) {
            mSmsCodeCountDownTimer.cancel();
            mSmsCodeCountDownTimer = null;
        }
        super.onDestroy();
    }
}
