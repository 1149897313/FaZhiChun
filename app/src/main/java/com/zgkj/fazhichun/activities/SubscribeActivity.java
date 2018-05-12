package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;

import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.fazhichun.R;

/*
 * Author:  bozaixing.
 * Date:    2018-03-04.
 * Email:   654152983@qq.com.
 * Descr:   发型师预约显示界面
 */
public class SubscribeActivity extends ToolbarActivity {


    /**
     * 显示发型师预约界面
     * @param context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, SubscribeActivity.class));
    }



    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_subscribe;
    }
}
