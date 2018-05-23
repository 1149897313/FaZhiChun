package com.zgkj.fazhichun.fragments.account;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zgkj.common.app.Fragment;
import com.zgkj.common.http.AsyncHttpPostFormData;
import com.zgkj.common.http.AsyncHttpResponse;
import com.zgkj.common.http.AsyncOkHttpClient;
import com.zgkj.common.http.AsyncResponseHandler;
import com.zgkj.common.utils.AccountManagers;
import com.zgkj.common.utils.MobileUtil;
import com.zgkj.common.widgets.text.ClearTextIconEditText;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.activities.MainActivity;
import com.zgkj.fazhichun.entity.CodeStatus;
import com.zgkj.fazhichun.entity.user.Account;
import com.zgkj.fazhichun.view.EmptyView;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/8.
 * Descr:   实现
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {


    /**
     * UI
     */
    private ImageView mBackView;
    private ClearTextIconEditText mPhoneView;
    private ClearTextIconEditText mCodeView, referrer_phone;
    private TextView mGetCodeView;
    private TextView mRegisterView;


    /**
     * DATA
     */
    // 定义一个切换碎片的接口对象
    private AccountTrigger mAccountTrigger;


    /**
     * 显示注册界面
     *
     * @return
     */
    public static RegisterFragment newInstance(AccountTrigger accountTrigger) {
        RegisterFragment registerFragment = new RegisterFragment();
        registerFragment.setAccountTrigger(accountTrigger);

        return registerFragment;
    }

    /**
     * 初始化接口对象
     *
     * @param accountTrigger
     */
    private void setAccountTrigger(AccountTrigger accountTrigger) {
        mAccountTrigger = accountTrigger;
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initWidgets(View rootView) {
        super.initWidgets(rootView);
        // init UI
        mBackView = rootView.findViewById(R.id.back);
        mPhoneView = rootView.findViewById(R.id.phone);
        mCodeView = rootView.findViewById(R.id.code);
        mGetCodeView = rootView.findViewById(R.id.get_code);
        mRegisterView = rootView.findViewById(R.id.register);
        referrer_phone = rootView.findViewById(R.id.referrer_phone);
    }


    @Override
    protected void initDatas() {
        super.initDatas();

        mBackView.setOnClickListener(this);
        mGetCodeView.setOnClickListener(this);
        mRegisterView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                back();
                break;
            case R.id.get_code:
                String phone = mPhoneView.getText().toString();
                // 校验数据
                if (!MobileUtil.checkMobile(phone)) {
                    App.showMessage("请输入正确的手机号！");
                    return;
                } else {
                    getCode(phone, "register");
                }
                break;
            case R.id.register:
                String code = mCodeView.getText().toString();
                String mobile = mPhoneView.getText().toString();
                // 校验数据
                if (!MobileUtil.checkMobile(mobile)) {
                    App.showMessage("请输入正确的手机号！");
                    return;
                } else {
                    if (!"".equals(code) && code.length() >= 4) {
                        onRegister(mobile, code);
                    } else {
                        App.showMessage("请输入正确的验证码！");
                    }
                }
                break;
            default:
                break;

        }
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
                        //发送成功启动定时器
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
     * 注册
     *
     * @param mobile
     * @param code
     */
    private void onRegister(String mobile, String code) {

        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData asyncHttpPostFormData = new AsyncHttpPostFormData();
        asyncHttpPostFormData.addFormData("mobile", mobile);
        asyncHttpPostFormData.addFormData("code", code);
        okHttpClient.post("/v1/register/register", asyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onRegister " + response.toString());
                Type type = new TypeToken<RspModel<Account>>() {
                }.getType();
                Account account = getAnalysis(response, type, "onRegister");
                if (account != null) {
                    if ("T".equals(account.getIs_success())) {
                        AccountManagers.login(account.getToken(), "");
                        App.showMessage("登录成功");
                        MainActivity.show(mContext);
                       back();
                    } else {
                        App.showMessage(account.getErr_msg());
                    }
                }
            }
        });
    }

    protected <T> T getAnalysis(AsyncHttpResponse response, Type type, String log) {
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
                            App.showMessage(rspModel.getMessage());
                            break;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
            default:
                App.showMessage("错误码："+response.getCode());
                break;
        }
        return null;
    }



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
            mGetCodeView.setText("重新发送（"+millisUntilFinished / 1000L+"s)");
            mGetCodeView.setTextColor(getResources().getColor(R.color.textColorAccent));
        }

        @Override
        public void onFinish() {
            mGetCodeView.setEnabled(true);
            mGetCodeView.setText("获取验证码");
            mGetCodeView.setTextColor(getResources().getColor(R.color.textColorFirst));
        }
    }

    private void back() {

        // 切换碎片，即显示登录碎片
        if (mAccountTrigger != null) {
            mAccountTrigger.onTriggerView();
        }
        // 释放倒计时对象
        if (mSmsCodeCountDownTimer != null) {
            mSmsCodeCountDownTimer.cancel();
            mSmsCodeCountDownTimer = null;
        }

        if (getActivity() != null) {
            getActivity().finish();
        }
    }


    /**
     * 重写返回按键的监听方法
     *
     * @return
     */
    @Override
    public boolean onBackPressed() {
        back();
        return true;
    }
}
