package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.common.widgets.recycler.decoration.SpaceItemDecoration;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.recharge.RechargeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/8.
 * Descr:   代金券充值购买界面
 */

public class RechargeActivity extends ToolbarActivity {

    /**
     * UI
     */
    private NestedScrollView mNestedScrollView;
    private RecyclerView mRecyclerView;


    /**
     * DATA
     */
    private LoadManager mLoadManager;
    private RechargeAdapter mRechargeAdapter;


    /**
     * 显示购买充值代金券的界面
     *
     * @param context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, RechargeActivity.class));
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        mNestedScrollView = findViewById(R.id.nested_scroll_view);
        mRecyclerView = findViewById(R.id.recycler_view);

        mLoadManager = LoadFactory.getInstance().register(mNestedScrollView, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadManager.showSuccessView();
            }
        }, 1000);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(mContext, 10));
        mRechargeAdapter = new RechargeAdapter();
        mRecyclerView.setAdapter(mRechargeAdapter);

    }


    @Override
    protected void initDatas() {
        super.initDatas();

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 6; i++){
            list.add("a");
        }
        mRechargeAdapter.replace(list);
    }
}
