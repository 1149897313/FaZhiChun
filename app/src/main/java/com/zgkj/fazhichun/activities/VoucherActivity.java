package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/5.
 * Descr:   我的代金券显示界面
 */

public class VoucherActivity extends ToolbarActivity implements View.OnClickListener {

    /**
     * UI
     */
    private TextView mVoucherView;  // 代金券金额
    private TextView mBuyView;      // 购买代金券
    private TextView mHistoryView;  // 查看历史代金券


    /**
     * 显示我的代金券界面
     *
     * @param context
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, VoucherActivity.class));
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_voucher;
    }


    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        mVoucherView = findViewById(R.id.voucher);
        mBuyView = findViewById(R.id.buy);
        mHistoryView = findViewById(R.id.history);
    }


    @Override
    protected void initDatas() {
        super.initDatas();
        // 为控件注册点击响应事件
        mBuyView.setOnClickListener(this);
        mHistoryView.setOnClickListener(this);
    }


    /**
     * 显示控件点击事件的回调方法
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy:      // 购买代金券
                // 跳转到购买代金券的界面
                RechargeActivity.show(mContext);
                break;
            case R.id.history:  // 查看代金券历史
                // 跳转到代金券历史界面
                HistoryActivity.show(mContext);
                break;
            default:
                break;
        }
    }
}
