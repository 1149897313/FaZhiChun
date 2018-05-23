package com.zgkj.fazhichun.activities.mywallet;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/22.
 * Descr:
 */
public class SuccessWithdrawDepositActivity extends ToolbarActivity {


    public static void show(Context context) {
        // 跳转并传递值
        Intent intent = new Intent(context, SuccessWithdrawDepositActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.succees_withdraw_deposit;
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
    }

    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.back_wallet_home:
                finish();
                break;
            default:
                break;
        }
    }
}
