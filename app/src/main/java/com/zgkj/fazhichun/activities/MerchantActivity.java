package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;

import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/2.
 * Descr:   商家列表
 */

public class MerchantActivity extends ToolbarActivity {


    /**
     * 显示商家列表界面
     *
     * @param context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, MerchantActivity.class));
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_merchant;
    }
}
