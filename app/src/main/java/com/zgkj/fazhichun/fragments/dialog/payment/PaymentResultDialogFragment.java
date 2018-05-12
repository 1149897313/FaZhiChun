package com.zgkj.fazhichun.fragments.dialog.payment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/24.
 * Descr:   支付结果的显示碎片
 */
public class PaymentResultDialogFragment extends DialogFragment implements View.OnClickListener {


    /**
     * 创建一个支付结果的监听接口对象
     */
    private OnPaymentResultListener mOnPaymentResultListener;

    // 创建沉浸式状态对象
    private ImmersionBar mImmersionBar;


    /**
     * 显示支付结果的dialog
     *
     * @return
     */
    public static PaymentResultDialogFragment newInstance(OnPaymentResultListener onPaymentResultListener) {
        PaymentResultDialogFragment fragment = new PaymentResultDialogFragment();
        fragment.setOnPaymentResultListener(onPaymentResultListener);

        return fragment;

    }


    /**
     * 初始化监听接口对象
     *
     * @param onPaymentResultListener
     */
    private void setOnPaymentResultListener(OnPaymentResultListener onPaymentResultListener) {
        mOnPaymentResultListener = onPaymentResultListener;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 设置无标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 加载界面布局
        View view = inflater.inflate(R.layout.dialog_payment_result, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 初始化沉浸式状态栏管理对象
        mImmersionBar = ImmersionBar.with(this, getDialog())
                .statusBarColor(R.color.colorPrimary)
                .statusBarDarkFont(true, 1.0f);

        mImmersionBar.init();


        TextView mSeeOrderView = view.findViewById(R.id.see_order);
        TextView mBackHomeView = view.findViewById(R.id.back_home);

        // 注册点击响应事件
        mSeeOrderView.setOnClickListener(this);
        mBackHomeView.setOnClickListener(this);

    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        window.setWindowAnimations(R.style.CityPickerDialogFragmentAnimStyle);

        View decorView = window.getDecorView();
        // 设置无边距
        decorView.setPadding(0, 0, 0, 0);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.see_order:
                seeOrder();
                break;
            case R.id.back_home:
                backHome();
                break;
            default:
                break;
        }
    }



    /**
     * 查看订单信息
     */
    private void seeOrder(){
        if (mOnPaymentResultListener != null){
            mOnPaymentResultListener.onSeeOrderInfo();
            close();
        }
    }

    private void backHome(){
        if (mOnPaymentResultListener != null){
            mOnPaymentResultListener.onBackHome();
            close();
        }
    }


    /**
     * 关闭当前dialog
     */
    private void close(){

        if (getDialog() != null && getDialog().isShowing()){
            dismiss();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 销毁沉浸式状态栏管理对象
        if (mImmersionBar != null){
            mImmersionBar.destroy();
        }
    }

    public interface OnPaymentResultListener {

        /**
         * 返回首页
         */
        void onBackHome();

        /**
         * 查看订单详情
         */
        void onSeeOrderInfo();


    }





}
