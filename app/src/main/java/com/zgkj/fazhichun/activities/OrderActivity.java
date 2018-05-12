package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;

import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/13.
 * Descr:   支付订单显示界面
 */

public class OrderActivity extends ToolbarActivity {

    /**
     * 显示支付订单界面
     *
     * @param context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, OrderActivity.class));
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_order;
    }
}
