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
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.common.widgets.text.ClearTextIconEditText;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.activities.MainActivity;
import com.zgkj.fazhichun.entity.CodeStatus;
import com.zgkj.fazhichun.entity.user.Account;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.LoadingView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/8.
 * Descr:   实现登录功能的碎片
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    /**
     * UI
     */
    private ImageView mBackView;
    private TextView title,login_title;
    private TextView mRegisterView;
    private ClearTextIconEditText mPhoneView;
    private ClearTextIconEditText mCodeView;
    private TextView mGetCodeView;
    private TextView mLoginView;
    private TextView mWeixinLoginView;

    /**
     * DATA
     */
    // 定义一个接口对象
    private AccountTrigger mAccountTrigger;

    private boolean binding;

    /**
     * 显示登录界面的碎片
     *
     * @return
     */
    public static LoginFragment newInstance(AccountTrigger accountTrigger,boolean binding) {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setAccountTrigger(accountTrigger,binding);

        return loginFragment;
    }

    /**
     * 初始化接口对象
     *
     * @param accountTrigger
     */
    private void setAccountTrigger(AccountTrigger accountTrigger,boolean binding) {
        mAccountTrigger = accountTrigger;
        this.binding=binding;
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.fragment_login;
    }


    @Override
    protected void initWidgets(View rootView) {
        super.initWidgets(rootView);
        title=rootView.findViewById(R.id.title);
        login_title=rootView.findViewById(R.id.login_title);
        mBackView = rootView.findViewById(R.id.back);
        mRegisterView = rootView.findViewById(R.id.register);
        mPhoneView = rootView.findViewById(R.id.phone);
        mCodeView = rootView.findViewById(R.id.code);
        mGetCodeView = rootView.findViewById(R.id.get_code);
        mLoginView = rootView.findViewById(R.id.login);
        mWeixinLoginView = rootView.findViewById(R.id.weixin_login);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        if(binding){
            title.setText("绑定手机号");
            login_title.setText("为了您的账号安全，请绑定手机号");
        }
        // 为返回按钮注册监听事件
        mBackView.setOnClickListener(this);
        mRegisterView.setOnClickListener(this);
        mGetCodeView.setOnClickListener(this);
        mLoginView.setOnClickListener(this);
        mWeixinLoginView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String phone = mPhoneView.getText().toString();
        String code = mCodeView.getText().toString();
        switch (v.getId()) {
            case R.id.back:
                back();
                break;
            case R.id.register:
                register();
                break;
            case R.id.get_code:
                // 校验数据
                if (!MobileUtil.checkMobile(phone)) {
                    App.showMessage("请输入正确的手机号！");
                    return;
                } else {
                    if(binding){//绑定手机号
                        getCode(phone, "bind_mobile");
                    }else {
                        getCode(phone, "app_login");
                    }
                }
                break;
            case R.id.login:
                // 校验数据
                if (!MobileUtil.checkMobile(phone)) {
                    App.showMessage("请输入正确的手机号！");
                    return;
                } else {
                    if (!"".equals(code) && code.length() >= 4) {
                        if(binding){//绑定手机号
                            onBinding(phone, code);
                        }else {
                            onLogin(phone, code);
                        }
                    } else {
                        App.showMessage("请输入正确的验证码！");
                    }
                }
                break;
            case R.id.weixin_login:

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
     * 绑定手机号
     * @param mobile
     * @param code
     */
    private void onBinding(String mobile, String code) {

        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("mobile", mobile);
        AsyncHttpPostFormData.addFormData("code", code);
        okHttpClient.post("/v1/user/bind-mobile", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onLogin " + response.toString());
                Type type = new TypeToken<RspModel<Account>>() {
                }.getType();
                Account account = getAnalysis(response, type, "绑定手机号");
                if (account != null) {
                    if ("T".equals(account.getIs_success())) {
                        App.showMessage("绑定成功");
                        MainActivity.show(mContext);
                        back();
                    } else {
                        App.showMessage(account.getErr_msg());
                    }
                }
            }
        });
    }

    /**
     * 登录
     *
     * @param mobile
     * @param code
     */
    private void onLogin(String mobile, String code) {

        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("mobile", mobile);
        AsyncHttpPostFormData.addFormData("code", code);
        okHttpClient.post("/v1/login/mobile", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onLogin " + response.toString());
                Type type = new TypeToken<RspModel<Account>>() {
                }.getType();
                Account account = getAnalysis(response, type, "登录");
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

    private void back() {

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
     * 显示注册碎片
     */
    private void register() {
        // 释放倒计时对象
        if (mSmsCodeCountDownTimer != null) {
            mSmsCodeCountDownTimer.cancel();
            mSmsCodeCountDownTimer = null;
        }
        if (mAccountTrigger != null) {
            mAccountTrigger.onTriggerView();
        }
    }

    private void weixinLogin() {

    }

}
