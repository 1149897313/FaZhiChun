package com.zgkj.fazhichun.activities.dialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zgkj.common.app.Activity;
import com.zgkj.common.widgets.PayPsdInputView;
import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/22.
 * Descr:
 */
public class PwdPayDialogActivity extends Activity {

    private TextView amount;
    private PayPsdInputView inputView;
    private float cashWithdrawalAmount;

    @Override
    protected boolean initArgs(Bundle bundle) {
        cashWithdrawalAmount = bundle.getFloat("AMOUNT");
        return cashWithdrawalAmount>0;
    }
    @Override
    protected int getLayoutSourceId() {
        return R.layout.dialog_pwd_pay;
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        amount = findViewById(R.id.amount);
        inputView = findViewById(R.id.pwd_input);
        amount.setText("￥" + cashWithdrawalAmount);
        inputView.getPasswordString();

        inputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                int length = text.toString().length();
                if (length == 6) {
                    Intent intent = new Intent();
                    intent.putExtra("PWD",text.toString());
                    setResult(1001, intent);
                    close();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @SuppressLint("NewApi")
    @Override
    public void onStart() {
        super.onStart();
        // 重新计算显示属性
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        // 设置底部弹出
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);

        View decorView = window.getDecorView();
        decorView.setPadding(0, 0, 0, 0);

        // 注册一个触摸的监听
        decorView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // 关闭dialog
                    close();
                }
                return true;
            }
        });

    }

    /**
     * 关闭当前dialog
     */
    private void close() {
        finish();
    }
}
