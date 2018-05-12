package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.common.http.AsyncHttpPostFormData;
import com.zgkj.common.http.AsyncHttpResponse;
import com.zgkj.common.http.AsyncOkHttpClient;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.common.widgets.recycler.decoration.SpaceItemDecoration;
import com.zgkj.common.widgets.text.ClearTextIconEditText;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.factory.model.api.home.HomeRspModel;
import com.zgkj.factory.net.AsyncResponseHandler;
import com.zgkj.factory.net.Network;
import com.zgkj.factory.net.RemoteService;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.home.HomeAdapter;
import com.zgkj.fazhichun.adapter.search.SearchAdapter;
import com.zgkj.fazhichun.entity.ShopList;
import com.zgkj.fazhichun.entity.shop.StoreListAndProductList;
import com.zgkj.fazhichun.view.ErrorView;
import com.zgkj.fazhichun.view.LoadingView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/*
 * Author:  bozaixing.
 * Date:    2018-03-04.
 * Email:   654152983@qq.com.
 * Descr:   门店搜索界面
 */
public class SearchActivity extends ToolbarActivity {

    /**
     * UI
     */
    private ClearTextIconEditText mSearchView;
    private NestedScrollView mNestedScrollView;
    private RecyclerView mRecyclerView;

    /**
     * DATA
     */
    private LoadManager mLoadManager;
    private HomeAdapter mAdapter;


    /**
     * 显示搜索附近门店
     *
     * @param context
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        mSearchView = findViewById(R.id.search);
        mNestedScrollView = findViewById(R.id.nested_scroll_view);
        mRecyclerView = findViewById(R.id.recycler_view);

        mLoadManager = LoadFactory.getInstance().register(mNestedScrollView,
                new AbsView.OnReloadListener() {
                    @Override
                    public void onReload(View view) {
                        mLoadManager.showStateView(LoadingView.class);
                        requestServer();
                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadManager.showSuccessView();

            }
        }, 1000);


        // 为RecyclerView显示控件配置数据
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(mContext, 10));
        mAdapter = new HomeAdapter(new RecyclerViewAdapter.AdapterListenerImpl<StoreListAndProductList>() {
            @Override
            public void onItemClick(RecyclerViewAdapter.ViewHolder<StoreListAndProductList> holder, StoreListAndProductList data) {
                super.onItemClick(holder, data);
                // 跳转到理发店详情界面
                BarberShopActivity.show(mContext,data.getShop_id());
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        onSearch();

    }

    /**
     * 搜索
     */
    private void onSearch() {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("page", "1");
        AsyncHttpPostFormData.addFormData("lng", "0");
        AsyncHttpPostFormData.addFormData("lat", "0");
        AsyncHttpPostFormData.addFormData("shop_name", "测试");
        okHttpClient.post("/shops/1", AsyncHttpPostFormData, new com.zgkj.common.http.AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onHot " + response.toString());
                if (response.getCode() == 200) {
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<RspModel<ShopList>>() {
                        }.getType();
                        RspModel<ShopList> rspModel = gson.fromJson(response.getBody(), type);
                        Log.i(TAG, "onBackValue: " + rspModel.toString());
                        //商家列表
                        mAdapter.replace(rspModel.getData().getShop_list());

                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void requestServer() {
        final RemoteService remote = Network.remote();

        Call<RspModel<HomeRspModel>> call = remote.getData(-1);


        call.enqueue(new AsyncResponseHandler<RspModel<HomeRspModel>>() {
            @Override
            public void onSuccess(Response<RspModel<HomeRspModel>> response) {

                int errorCode = response.body().getCode();
                mLoadManager.showSuccessView();
            }

            @Override
            public void onError(int code, String msg) {
                mLoadManager.showStateView(ErrorView.class);
            }

            @Override
            public void onFailure(String errorMessage) {
                mLoadManager.showStateView(NetErrorView.class);
            }
        });
    }


}
