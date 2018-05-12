package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.zgkj.factory.model.api.home.Banner;
import com.zgkj.factory.net.NewOkHttpClient;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.type.TypeAdapter;
import com.zgkj.fazhichun.entity.shop.StoreListAndProductList;
import com.zgkj.fazhichun.fragments.dialog.phone.CallPhoneDialogFragment;
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
    private NestedScrollView mNestedScrollView;
    private RecyclerView mRecyclerView;

    /**
     * DATA
     */
    private LoadManager mLoadManager;

    private TypeAdapter mTypeAdapter;


    /**
     * 显示具体的理发类型项目的详细信息界面
     *
     * @param context
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, TypeActivity.class));
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLoadManager.showSuccessView();
                    }
                }, 3000);

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadManager.showSuccessView();
            }
        }, 2000);



        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL,
                R.drawable.shape_divider_line,
                16, false));

        mTypeAdapter = new TypeAdapter(new RecyclerViewAdapter.AdapterListenerImpl<String>() {
            @Override
            public void onItemClick(RecyclerViewAdapter.ViewHolder<String> holder, String data) {
                super.onItemClick(holder, data);
                // 跳转到服务详情页面
                CommodityActivity.show(mContext,null);
            }
        });

        mRecyclerView.setAdapter(mTypeAdapter);

        loadData();

        onGet(0);
    }

    public void loadData(){
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++){
            list.add("a");
        }

        mTypeAdapter.replace(list);
    }


    /**
     * h获取列表信息
     * @param type
     */
    private void onGet(int type) {

        AsyncOkHttpClient okHttpClient=new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData=new AsyncHttpPostFormData();
        okHttpClient.post("/v1/banner/show", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onGetBanner"+response.toString());
                if(response.getCode()==200){
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<RspModel<List<Banner>>>() {
                        }.getType();
                        RspModel<List<Banner>> rspModel=gson.fromJson(response.getBody(),type);


                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void showPhoneDialog(String phone) {
        // 弹出一个dialog
        CallPhoneDialogFragment phoneDialogFragment = CallPhoneDialogFragment.newInstance(phone);
        if (!phoneDialogFragment.isAdded()) {
            phoneDialogFragment.show(getSupportFragmentManager(), phoneDialogFragment.getClass().getName());
        }
    }


}
