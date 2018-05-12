package com.zgkj.fazhichun.activities;


import android.content.Context;
import android.content.Intent;

import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.fazhichun.R;

/*
 * Author:  bozaixing.
 * Date:    2018-03-04.
 * Email:   654152983@qq.com.
 * Descr:   我的团购显示界面
 */
public class PurchaseActivity extends ToolbarActivity {


    /**
     * 显示我的团购界面
     *
     * @param context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, PurchaseActivity.class));
    }





    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_purchase;
    }








}
