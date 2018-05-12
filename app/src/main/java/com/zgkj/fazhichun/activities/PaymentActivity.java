package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.fragments.dialog.payment.PaymentResultDialogFragment;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/24.
 * Descr:   支付订单显示界面
 */
public class PaymentActivity extends ToolbarActivity implements PaymentResultDialogFragment.OnPaymentResultListener, View.OnClickListener {

    /**
     * UI
     */
    private LinearLayout mContentLayout;
    private TextView mPriceView;
    private TextView mTimeView;
    private RelativeLayout mWechatLayout;
    private ImageView mWechatView;
    private RelativeLayout mAlipayLayout;
    private ImageView mAlipayView;
    private TextView mPaymentView;


    /**
     * DATA
     */
    private LoadManager mLoadManager;
    private boolean mIsWechatPay;
    private boolean mIsAlipayPay;


    /**
     * 支付订单显示界面
     *
     * @param context
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, PaymentActivity.class));
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_payment;
    }


    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        mContentLayout = findViewById(R.id.content_layout);
        mPriceView = findViewById(R.id.price);
        mTimeView = findViewById(R.id.time);
        mWechatLayout = findViewById(R.id.wechat_layout);
        mWechatView = findViewById(R.id.wechat);
        mAlipayLayout = findViewById(R.id.alipay_layout);
        mAlipayView = findViewById(R.id.alipay);
        mPaymentView = findViewById(R.id.payment);

        mWechatLayout.setOnClickListener(this);
        mAlipayLayout.setOnClickListener(this);
        mPaymentView.setOnClickListener(this);

        mLoadManager = LoadFactory.getInstance().register(mContentLayout, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadManager.showSuccessView();
            }
        }, 1000);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wechat_layout:       // 微信支付
                changeWechatShowState();
                break;
            case R.id.alipay_layout:
                changeAlipayShowState();        // 支付宝支付
                break;
            case R.id.payment:      // 支付
                choicePayWay();
                break;
            default:
                break;
        }

    }



    /**
     * 改变微信支付显示的状态
     */
    private void changeWechatShowState(){
        if (!mIsWechatPay){
            mWechatView.setImageDrawable(getResources().getDrawable(R.drawable.ic_payment_checked));
            mAlipayView.setImageDrawable(getResources().getDrawable(R.drawable.ic_payment_unchecked));
            mIsWechatPay = true;
            mIsAlipayPay = false;
        }else {
            mWechatView.setImageDrawable(getResources().getDrawable(R.drawable.ic_payment_unchecked));
            mIsWechatPay = false;
        }
    }

    /**
     * 改变支付宝支付显示的状态
     */
    private void changeAlipayShowState(){
        if (!mIsAlipayPay){
            mAlipayView.setImageDrawable(getResources().getDrawable(R.drawable.ic_payment_checked));
            mWechatView.setImageDrawable(getResources().getDrawable(R.drawable.ic_payment_unchecked));
            mIsAlipayPay = true;
            mIsWechatPay = false;
        }else {
            mAlipayView.setImageDrawable(getResources().getDrawable(R.drawable.ic_payment_unchecked));
            mIsAlipayPay = false;
        }
    }


    /**
     * 根据状态值判定用户选择的支付方式
     */
    private void choicePayWay(){
        if (mIsWechatPay){
            wechatPay();
        }else if (mIsAlipayPay){
            alipayPay();
        }else {
            // 提示用户选择支付方式
            Toast.makeText(mContext, "请选择你的支付方式！", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 微信支付
     */
    private void wechatPay(){

    }


    /**
     * 支付宝支付
     */
    private void alipayPay(){


    }








    /**
     * 显示支付结果的dialog
     */
    private void showPaymentResultDialogFragment() {

        PaymentResultDialogFragment fragment = PaymentResultDialogFragment.newInstance(this);
        if (!fragment.isAdded()) {
            fragment.show(getSupportFragmentManager(), fragment.getClass().getName());
        }

    }


    /**
     * 返回首页
     */
    @Override
    public void onBackHome() {

    }


    /**
     * 查看订单信息
     */
    @Override
    public void onSeeOrderInfo() {

    }


}
