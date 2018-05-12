package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.common.widgets.QuantityView;
import com.zgkj.fazhichun.R;

import java.security.PublicKey;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/13.
 * Descr:   使用抵用券的显示界面
 */

public class ConsumptionActivity extends ToolbarActivity {


    /**
     * 显示使用抵用券界面
     *
     * @param context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, ConsumptionActivity.class));
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_consumption;
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();

    }
}
