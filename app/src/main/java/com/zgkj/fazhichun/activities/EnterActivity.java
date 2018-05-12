package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;

import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/8.
 * Descr:   商家入驻的显示界面
 */

public class EnterActivity extends ToolbarActivity {


    /**
     * 显示商家入驻界面
     *
     * @param context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, EnterActivity.class));
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_enter;
    }
}
