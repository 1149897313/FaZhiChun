package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.zgkj.common.widgets.recycler.decoration.GridSpaceItemDecoration;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.barber.BarberAdapter;
import com.zgkj.fazhichun.entity.shop.BarberInfo;
import com.zgkj.fazhichun.entity.shop.HairdresserInfo;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/2/28.
 * Descr:   发型师列表界面
 */

public class BarberActivity extends ToolbarActivity{

    /**
     * UI
     */
    private RecyclerView mRecyclerView;


    /**
     * DATA
     */
    private LoadManager mLoadManager;
    private BarberAdapter mAdapter;
    private String hairdresserId;

    /**
     * 显示理发师列表界面
     *
     * @param context
     */
    public static void show(Context context,String hairdresserId){
        Intent intent=new Intent(context, BarberActivity.class);
        intent.putExtra("ID",hairdresserId);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        hairdresserId=bundle.getString("ID");
        return !TextUtils.isEmpty(hairdresserId);
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_barber;
    }


    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        mRecyclerView = findViewById(R.id.recycler_view);

    }

    /**
     * 获取理发师列表
     * @param id
     */
    private void onGetBarberAllList(String id,String number) {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("shop_id", id);
        AsyncHttpPostFormData.addFormData("number", number);
        okHttpClient.post("/v1/barber/barber-list", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onGetBarberAllList " + response.toString());
                if (response.getCode() == 200) {
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<RspModel<List<BarberInfo>>>() {
                        }.getType();
                        Log.i(TAG, "onGetBarberAllList: " + response.getBody());
                        RspModel<List<BarberInfo>> rspModel = gson.fromJson(response.getBody(), type);
                        Log.i(TAG, "onGetBarberAllList: " + rspModel.toString());
                        mAdapter.replace(rspModel.getData());
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    protected void initDatas() {
        super.initDatas();
        mLoadManager = LoadFactory.getInstance().register(mRecyclerView, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadManager.showSuccessView();
            }
        }, 500);



        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new GridSpaceItemDecoration(mContext, 2, 10, true));
        mAdapter = new BarberAdapter(new RecyclerViewAdapter.AdapterListenerImpl<BarberInfo>() {
            @Override
            public void onItemClick(RecyclerViewAdapter.ViewHolder<BarberInfo> holder, BarberInfo data) {
                super.onItemClick(holder, data);
                // 跳转到发型师介绍界面
                IntroduceActivity.show(mContext,data.getBarber_id());
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        onGetBarberAllList(hairdresserId,"0");
    }
}
