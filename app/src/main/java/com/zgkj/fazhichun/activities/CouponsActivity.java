package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
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
import com.zgkj.common.widgets.recycler.decoration.SpaceItemDecoration;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.type.CouponsAdapter;
import com.zgkj.fazhichun.entity.ShopList;
import com.zgkj.fazhichun.entity.coupon.Coupon;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.LoadingView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的优惠券
 */
public class CouponsActivity extends ToolbarActivity{

    /**
     * UI
     */
    private TextView mTitleView;
    private NestedScrollView mNestedScrollView;
    private RecyclerView mRecyclerView;

    /**
     * DATA
     */
    private LoadManager mLoadManager;

    private CouponsAdapter couponsAdapter;


    /**
     * 显示具体的理发类型项目的详细信息界面
     *
     * @param context
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, CouponsActivity.class));
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_coupons;
    }


    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        mTitleView = findViewById(R.id.title);
        mNestedScrollView = findViewById(R.id.nested_scroll_view);
        mRecyclerView = findViewById(R.id.recycler_view);

    }

    @Override
    protected void initDatas() {
        super.initDatas();
        mLoadManager = LoadFactory.getInstance().register(mNestedScrollView, new AbsView.OnReloadListener() {
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

        couponsAdapter = new CouponsAdapter(new RecyclerViewAdapter.AdapterListenerImpl<Coupon>() {
            @Override
            public void onItemClick(RecyclerViewAdapter.ViewHolder<Coupon> holder, Coupon data) {
                super.onItemClick(holder, data);
                // 跳转到服务详情页面
                CommodityActivity.show(mContext,null);
            }
        });
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(mContext,15));
        mRecyclerView.setAdapter(couponsAdapter);
        onGetCouponList();
    }

    /**
     * 获取我的优惠券
     */
    private void onGetCouponList() {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        okHttpClient.post("/v1/voucher/list", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showStateView(NetErrorView.class);
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onGetCouponList " + response.toString());

                Type type = new TypeToken<RspModel<List<Coupon>>>() {
                }.getType();
                List<Coupon> list=getAnalysis(response,type,"onGetCouponList");
                couponsAdapter.replace(list);
            }
        });
    }

    private <T> T getAnalysis(AsyncHttpResponse response,Type type,String log) {
        switch (response.getCode()) {
            case 200:
                try {
                    Gson gson = new Gson();
                    RspModel<T> rspModel = gson.fromJson(response.getBody(), type);
                    Log.i(TAG, "onSuccess: "+log + rspModel.toString());
                    switch (rspModel.getCode()) {
                        case 1:
                            mLoadManager.showStateView(EmptyView.class);
                            break;
                        case 200:
                            mLoadManager.showSuccessView();
                            return rspModel.getData();
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }
}
