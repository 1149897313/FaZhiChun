package com.zgkj.fazhichun.fragments.dialog.phone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/12.
 * Descr:   点击商家电话按钮弹出碎片
 */

public class CallPhoneDialogFragment extends DialogFragment implements View.OnClickListener {


    /**
     * 商家的电话号码
     */
    private String mPhone;


    public static CallPhoneDialogFragment newInstance(String phone){
        CallPhoneDialogFragment phoneDialogFragment = new CallPhoneDialogFragment();
        phoneDialogFragment.setPhone(phone);

        return phoneDialogFragment;
    }


    /**
     * 设置需要显示的电话号码
     *
     * @param phone
     */
    private void setPhone(String phone){
        mPhone = phone;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 设置无标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 加载布局View
        View view = inflater.inflate(R.layout.dialog_call_phone, container, false);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 电话号码
        TextView phoneView = view.findViewById(R.id.phone);
        // 取消
        TextView cancelView = view.findViewById(R.id.cancel);

        // 注册监听事件
        phoneView.setOnClickListener(this);
        cancelView.setOnClickListener(this);

    }


    @SuppressLint("NewApi")
    @Override
    public void onStart() {
        super.onStart();
        // 重新计算显示属性
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        // 设置底部弹出
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);

        View decorView = window.getDecorView();
//        decorView.setBackground(getContext().getResources().getColor(R.color.windowsBackground));
        decorView.setPadding(0, 0, 0, 0);

        // 注册一个触摸的监听
        decorView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    // 关闭dialog
                    close();
                }
                return true;
            }
        });

    }

    /**
     * 点击事件的回调方法
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone:
                // 跳转到拨号界面
                callPhone();
                break;
            case R.id.cancel:
                // 关闭dialog
                close();
                break;
            default:
                break;
        }
    }


    /**
     * 拨打电话的操作，并关闭当前dialog
     */
    private void callPhone() {
        // 跳转到拨号界面，同时传递电话号码
        if (!TextUtils.isEmpty(mPhone)){
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPhone));
            startActivity(intent);
            // 关闭当前dialog
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


}
