package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.zgkj.common.Common;
import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.common.http.AsyncHttpPostFormData;
import com.zgkj.common.http.AsyncHttpResponse;
import com.zgkj.common.http.AsyncOkHttpClient;
import com.zgkj.common.http.AsyncResponseHandler;
import com.zgkj.common.utils.SPUtil;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.common.widgets.recycler.decoration.DividerItemDecoration;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.factory.model.api.home.Banner;
import com.zgkj.factory.net.NewOkHttpClient;
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.type.TypeAdapter;
import com.zgkj.fazhichun.entity.shop.Goods;
import com.zgkj.fazhichun.entity.shop.StoreListAndProductList;
import com.zgkj.fazhichun.fragments.dialog.phone.CallPhoneDialogFragment;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.LoadingView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Callback;
import okhttp3.FormBody;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/7.
 * Descr:   显示具体的理发类型项目的详细信息界面
 */

public class TypeActivity extends ToolbarActivity{

    /**
     * UI
     */
    private TextView mTitleView;
    private SmartRefreshLayout refresh_layout;
    private RecyclerView mRecyclerView;

    /**
     * DATA
     */
    private LoadManager mLoadManager;

    private TypeAdapter mTypeAdapter;

    private String typeId,typeName;

    public static void show(Context context,String type,String typeName) {
        Intent intent=new Intent(context, TypeActivity.class);
        intent.putExtra("ID",type);
        intent.putExtra("NAME",typeName);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        typeId=bundle.getString("ID");
        typeName=bundle.getString("NAME");
        return !TextUtils.isEmpty(typeId) && !TextUtils.isEmpty(typeName);
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_type;
    }


    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        mTitleView = findViewById(R.id.title);
        refresh_layout=findViewById(R.id.refresh_layout);
        mRecyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        mTitleView.setText(typeName);
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

        mTypeAdapter = new TypeAdapter(new RecyclerViewAdapter.AdapterListenerImpl<Goods>() {
            @Override
            public void onItemClick(RecyclerViewAdapter.ViewHolder<Goods> holder, Goods data) {
                super.onItemClick(holder, data);
                // 跳转到服务详情页面
                CommodityActivity.show(mContext,data.getHairdresser_id());
            }
        });

        mRecyclerView.setAdapter(mTypeAdapter);
        setRefresh();
        onGetList(page);
    }

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
                onGetList(page);
                refreshlayout.finishRefresh();
            }
        });

        // 滑到底部加载更多数据的监听接口
        refresh_layout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refresh_layout.finishLoadmore();
                page++;
                onGetList(page);
            }
        });
    }

    private int page = 1;

    /**
     * h获取列表信息
     * @param page
     */
    private void onGetList(final int page) {
        AsyncOkHttpClient okHttpClient=new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData=new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("page", String.valueOf(page));
        AsyncHttpPostFormData.addFormData("lng", SPUtil.get(Common.Constant.LONGITUDE_ID, "0"));
        AsyncHttpPostFormData.addFormData("lat", SPUtil.get(Common.Constant.LATITUDE_ID, "0"));
        AsyncHttpPostFormData.addFormData("category_id", typeId);
        okHttpClient.post("/v1/hairdresser/type-hairdresser-list", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onGetList"+response.toString());
                Type type = new TypeToken<RspModel<List<Goods>>>() {
                }.getType();
                List<Goods> goodsList=getAnalysis(response,type,"onGetList");
                if(goodsList!=null && !"[]".equals(goodsList.toString())){
                    mTypeAdapter.add(goodsList);
                }else {
                    if(page==1){
                        mLoadManager.showStateView(EmptyView.class);
                    }
                }
            }
        });
    }

    /**
     * 数据解析
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
                            Log.i(TAG, log + ": " + rspModel.getData());
                            return rspModel.getData();
                        default:
                            App.showMessage("data:code" + rspModel.getCode());
                            mLoadManager.showStateView(EmptyView.class);
                            break;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
            default:
                mLoadManager.showStateView(EmptyView.class);
                break;
        }
        return null;
    }

    private void showPhoneDialog(String phone) {
        // 弹出一个dialog
        CallPhoneDialogFragment phoneDialogFragment = CallPhoneDialogFragment.newInstance(phone);
        if (!phoneDialogFragment.isAdded()) {
            phoneDialogFragment.show(getSupportFragmentManager(), phoneDialogFragment.getClass().getName());
        }
    }


}
