package com.zgkj.fazhichun.activities.mywallet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.activities.OrderDetailActivity;
import com.zgkj.fazhichun.adapter.order.AllOrderAdapter;
import com.zgkj.fazhichun.adapter.wallet.GeneralizeAdapter;
import com.zgkj.fazhichun.entity.order.OrderInfo;
import com.zgkj.fazhichun.entity.wallet.Brokerage;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.ErrorView;
import com.zgkj.fazhichun.view.LoadingView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 推广规则
 */

public class GeneralizeActivity extends ToolbarActivity {

    /**
     * UI
     */
    private LinearLayout content;
    private RecyclerView recyclerView;
    private LoadManager mLoadManager;

    /**
     * DATA
     */
    private GeneralizeAdapter generalizeAdapter;

    public static void show(Context context) {
        // 跳转并传递值
        Intent intent = new Intent(context, GeneralizeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_wellet_generalize;
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        content = findViewById(R.id.content);
        recyclerView = findViewById(R.id.recycler_view);
        mLoadManager = LoadFactory.getInstance().register(content, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                mLoadManager.showStateView(LoadingView.class);
            }
        });
        mLoadManager.showSuccessView();
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL,
                R.drawable.shape_divider_line,
                16,
                true));
        generalizeAdapter = new GeneralizeAdapter();
        recyclerView.setAdapter(generalizeAdapter);

        List<Brokerage> list=new ArrayList<>();
        for(int i=0;i<3;i++){
            list.add(new Brokerage());
        }
        generalizeAdapter.replace(list);
    }

    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.open_wellet://开通钱包

                break;
        }
    }

    private void geOrderList(final int page, String condition) {

        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("page", page);
        AsyncHttpPostFormData.addFormData("condition", condition);
        okHttpClient.post("/v1/order/list", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showStateView(NetErrorView.class);
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:最近订单" + response.toString());
                Type type = new TypeToken<RspModel<List<OrderInfo>>>() {
                }.getType();
                List<OrderInfo> order = getAnalysis(response, type, "最近订单");
                if (order != null && !"[]".equals(order.toString())) {
                }else{
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
