package com.zgkj.fazhichun.activities.mywallet;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.fazhichun.R;

/**
 * 密码管理
 */

public class PwdSettingActivity extends ToolbarActivity {

    public static void show(Context context) {
        // 跳转并传递值
        Intent intent = new Intent(context, PwdSettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_pwd_setting;
    }


    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.update_layout://修支付密码
                UpdatePaymentCodeActivity.show(mContext);
                break;
            case R.id.reset_layout://忘记支付密码
                ResetPaymentCodeActivity.show(mContext);
                break;
            default:
                break;
        }
    }
}
