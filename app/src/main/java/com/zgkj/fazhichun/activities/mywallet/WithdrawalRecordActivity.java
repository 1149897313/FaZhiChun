package com.zgkj.fazhichun.activities.mywallet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.common.http.AsyncHttpPostFormData;
import com.zgkj.common.http.AsyncHttpResponse;
import com.zgkj.common.http.AsyncOkHttpClient;
import com.zgkj.common.http.AsyncResponseHandler;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.common.widgets.recycler.decoration.DividerItemDecoration;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.wallet.WithdrawalRecordAdapter;
import com.zgkj.fazhichun.entity.wallet.Withdraw;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.LoadingView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/23.
 * Descr:   提现记录
 */
public class WithdrawalRecordActivity extends ToolbarActivity {


    private SmartRefreshLayout refresh_layout;
    private RecyclerView mRecyclerView;
    /**
     * DATA
     */
    private LoadManager mLoadManager;
    private WithdrawalRecordAdapter withdrawalRecordAdapter;

    /**
     * 显示用户消息界面
     *
     * @param context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, WithdrawalRecordActivity.class));
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_list;
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        refresh_layout = findViewById(R.id.refresh_layout);
        mRecyclerView=findViewById(R.id.recycler_view);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        mLoadManager = LoadFactory.getInstance().register(refresh_layout, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                mLoadManager.showStateView(LoadingView.class);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL,
                R.drawable.shape_divider_line,
                16, false));

        withdrawalRecordAdapter = new WithdrawalRecordAdapter(new RecyclerViewAdapter.AdapterListenerImpl<Withdraw>() {
            @Override
            public void onItemClick(RecyclerViewAdapter.ViewHolder<Withdraw> holder, Withdraw data) {
                super.onItemClick(holder, data);
            }
        });
        mRecyclerView.setAdapter(withdrawalRecordAdapter);

        setRefresh();
    }


    private int page = 1;

    /**
     * 刷新
     */
    private void setRefresh() {

        getWithdrawList(1);
        // 下拉刷新控件
        refresh_layout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                //全局设置主题颜色
                layout.setPrimaryColorsId(R.color.colorAccent, android.R.color.white);
                //指定为经典Header，默认是 贝塞尔雷达Header
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });

        // 下拉刷新
        refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getWithdrawList(page);
                refreshlayout.finishRefresh();
            }
        });

        // 滑到底部加载更多数据的监听接口
        refresh_layout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refresh_layout.finishLoadmore();
                page++;
                getWithdrawList(page);
            }
        });
    }


    /**
     * 提现记录
     * @param page
     */
    private void getWithdrawList(final int page) {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("page", page);
        okHttpClient.post("/v1/wallet/withdraw-list", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showStateView(NetErrorView.class);
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:提现记录" + response.toString());
                Type type = new TypeToken<RspModel<List<Withdraw>>>() {
                }.getType();
                List<Withdraw> list = getAnalysis(response, type, "提现记录");
                if (list != null && !"[]".equals(list.toString())) {
                    if(page==1){
                        withdrawalRecordAdapter.replace(list);
                    }else{
                        withdrawalRecordAdapter.add(list);
                    }
                } else {
                    mLoadManager.showStateView(EmptyView.class);
                }
            }
        });
    }

    /**
     * 数据解析
     *
     * @param response
     * @param type
     * @param log
     * @param <T>
     * @return
     */
    private <T> T getAnalysis(AsyncHttpResponse response, Type type, String log) {
        switch (response.getCode()) {
            case 200:
                try {
                    Gson gson = new Gson();
                    RspModel<T> rspModel = gson.fromJson(response.getBody(), type);
                    Log.i(TAG, "onSuccess: " + log + rspModel.toString());
                    switch (rspModel.getCode()) {
                        case 1:
                            mLoadManager.showStateView(EmptyView.class);
                            break;
                        case 200:
                            mLoadManager.showSuccessView();
                            return rspModel.getData();
                        default:
                            App.showMessage("错误码：" + rspModel.getCode());
                            mLoadManager.showStateView(EmptyView.class);
                            break;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }
}
