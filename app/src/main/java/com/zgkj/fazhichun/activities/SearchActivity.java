package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zgkj.common.Common;
import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.common.http.AsyncHttpPostFormData;
import com.zgkj.common.http.AsyncHttpResponse;
import com.zgkj.common.http.AsyncOkHttpClient;
import com.zgkj.common.utils.SPUtil;
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
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.home.HomeAdapter;
import com.zgkj.fazhichun.adapter.search.SearchAdapter;
import com.zgkj.fazhichun.entity.ShopList;
import com.zgkj.fazhichun.entity.shop.StoreListAndProductList;
import com.zgkj.fazhichun.view.EmptyView;
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
public class SearchActivity extends ToolbarActivity implements View.OnClickListener {

    /**
     * UI
     */
    private EditText mSearchView;
    private ImageView search_button;
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
        mSearchView = findViewById(R.id.search_edit);
        search_button=findViewById(R.id.search_button);
        mNestedScrollView = findViewById(R.id.nested_scroll_view);
        mRecyclerView = findViewById(R.id.recycler_view);
        search_button.setOnClickListener(this);

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


    /**
     * 搜索
     */
    private void onSearch(int page) {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("page", String.valueOf(page));
        AsyncHttpPostFormData.addFormData("lng", SPUtil.get(Common.Constant.LONGITUDE_ID, ""));
        AsyncHttpPostFormData.addFormData("lat", SPUtil.get(Common.Constant.LATITUDE_ID, ""));
        AsyncHttpPostFormData.addFormData("shop_name", mSearchView.getText().toString());
        okHttpClient.post("/v1/shop/shop-list", AsyncHttpPostFormData, new com.zgkj.common.http.AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onHot " + response.toString());

                Type type = new TypeToken<RspModel<ShopList>>() {
                }.getType();
                ShopList list=getAnalysis(response,type,"搜索");

                if(list!=null && !"[]".equals(list.toString())){
                    //商家列表
                    mAdapter.replace(list.getShop_list());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_button:
                hideKeyBoard();
                mLoadManager = LoadFactory.getInstance().register(mNestedScrollView);
                mAdapter.clear();
                onSearch(1);
                break;
        }
    }

    // 隐藏键盘
    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
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
