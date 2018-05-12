package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.history.HistoryAdapter;
import com.zgkj.fazhichun.fragments.dialog.voucher.VoucherHistoryDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/9.
 * Descr:   代金券历史显示界面
 */

public class HistoryActivity extends ToolbarActivity implements HistoryAdapter.OnSeeHistoryListener {


    /**
     * UI
     */
    private RecyclerView mRecyclerView;


    /**
     * DATA
     */
    private HistoryAdapter mAdapter;


    /**
     * 显示代金券历史界面
     *
     * @param context
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, HistoryActivity.class));
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_history;
    }


    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        mRecyclerView = findViewById(R.id.recycler_view);
    }


    @Override
    protected void initDatas() {
        super.initDatas();
        // init DATA
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        // 初始化适配器
        mAdapter = new HistoryAdapter(this);

        mRecyclerView.setAdapter(mAdapter);


        mAdapter.replace(getLoadData());


    }

    /**
     * 模拟数据请求
     *
     * @return
     */
    public List<String> getLoadData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("历史代金券");
        }

        return list;
    }


    /**
     * 点击查看详情的回调方法
     */
    @Override
    public void onSeeHistory() {
        // 显示一个查看详情的dialog
        VoucherHistoryDialogFragment voucherHistoryDialogFragment
                = VoucherHistoryDialogFragment.newInstance();

        if (!voucherHistoryDialogFragment.isAdded()) {
            voucherHistoryDialogFragment.show(getSupportFragmentManager(),
                    voucherHistoryDialogFragment.getClass().getName());
        }
    }
}
