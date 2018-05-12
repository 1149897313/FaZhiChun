package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;

import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/25.
 * Descr:   用户消息显示界面
 */
public class MessageActivity extends ToolbarActivity {


    /**
     * 显示用户消息界面
     *
     * @param context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, MessageActivity.class));
    }



    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_message;
    }
}
