package com.zgkj.fazhichun.fragments.exist;

import android.content.Context;
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
import com.zgkj.common.app.Fragment;
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
import com.zgkj.fazhichun.activities.OrderDetailActivity;
import com.zgkj.fazhichun.adapter.order.OrderAdapter;
import com.zgkj.fazhichun.entity.order.OrderInfo;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.ErrorView;
import com.zgkj.fazhichun.view.LoadingView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/12.
 * Descr:   待使用订单的显示碎片
 */
public class UsedOrderFragment extends Fragment {

    /**
     * UI
     */
    private SmartRefreshLayout refresh_layout;
    private RecyclerView mRecyclerView;


    /**
     * DATA
     */
    private OrderAdapter mOrderAdapter;

    private LoadManager mLoadManager;

    /**
     * 显示待使用订单的碎片
     *
     * @return
     */
    public static UsedOrderFragment newInstance(){
        UsedOrderFragment fragment = new UsedOrderFragment();

        return fragment;
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.fragment_order_list;
    }

    @Override
    protected void initWidgets(View rootView) {
        super.initWidgets(rootView);
        // init UI
        refresh_layout = rootView.findViewById(R.id.refresh_layout);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mLoadManager = LoadFactory.getInstance().register(refresh_layout, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                mLoadManager.showStateView(LoadingView.class);
            }
        });
        setRefresh();
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL,
                R.drawable.shape_divider_line,
                16,
                false));

        mOrderAdapter = new OrderAdapter(new RecyclerViewAdapter.AdapterListenerImpl<OrderInfo>() {
            @Override
            public void onItemClick(RecyclerViewAdapter.ViewHolder<OrderInfo> holder, OrderInfo data) {
                super.onItemClick(holder, data);
                OrderDetailActivity.show(mContext,data.getOrder_id());//订单详情
            }
        });

        mRecyclerView.setAdapter(mOrderAdapter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            page=1;
            geOrderList(page);
        }
    }

    private int page = 1;


    /**
     * 刷新
     */
    private void setRefresh() {

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
                geOrderList(page);
                refreshlayout.finishRefresh();
            }
        });

        // 滑到底部加载更多数据的监听接口
        refresh_layout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refresh_layout.finishLoadmore();
                page++;
                geOrderList(page);
            }
        });
    }

    /**
     *
     * @param page int	否	1	无	页数
     * string	否	all	all:全部;wait_pay:待付款;wait_use:待使用;wait_eva:待评价；refund:退款	筛选条件
     */
    private void geOrderList(final int page) {

        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("page", String.valueOf(page));
        AsyncHttpPostFormData.addFormData("condition", "wait_use");
        okHttpClient.post("/v1/order/list", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showStateView(NetErrorView.class);
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:geOrderList" + response.toString());
                Type type = new TypeToken<RspModel<List<OrderInfo>>>() {
                }.getType();
                List<OrderInfo> order = getAnalysis(response, type, "geOrderList");
                if (order != null && !"[]".equals(order.toString())) {
                    if(page==1) {
                        mOrderAdapter.replace(order);
                    }else {
                        mOrderAdapter.add(order);
                    }
                } else {
                    if(page==1){
                        mLoadManager.showStateView(EmptyView.class);
                    }
                }
            }
        });
    }

    /**
     * 数据解析
     *
     * @param response
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
                            Log.i(TAG, log + ": " + rspModel.getData());
                            return rspModel.getData();
                        default:
                            App.showMessage("data:code" + rspModel.getCode());
                            mLoadManager.showStateView(EmptyView.class);
                            break;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    mLoadManager.showStateView(ErrorView.class);
                }
                break;
            default:
                mLoadManager.showStateView(EmptyView.class);
                break;
        }
        return null;
    }

}
