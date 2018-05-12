package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.common.http.AsyncHttpPostFormData;
import com.zgkj.common.http.AsyncHttpResponse;
import com.zgkj.common.http.AsyncOkHttpClient;
import com.zgkj.common.http.AsyncResponseHandler;
import com.zgkj.common.widgets.CircleImageView;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.introduce.IntroduceAdapter;
import com.zgkj.fazhichun.entity.shop.BarberDetail;
import com.zgkj.fazhichun.entity.shop.BarberInfo;
import com.zgkj.fazhichun.fragments.dialog.phone.CallPhoneDialogFragment;
import com.zgkj.fazhichun.view.LoadingView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/19.
 * Descr:   单个发型师介绍界面
 */
public class IntroduceActivity extends ToolbarActivity implements View.OnClickListener {

    /**
     * UI
     */
    private FrameLayout mContentLayout;
    private CircleImageView mBarberImageView;
    private TextView mBarberNameView;
    private TextView mbarberJobView;
    private TextView mAdvantageView;        // 擅长技能
    private TextView mIntroduceView;        // 简介
    private TextView mShopNameView;
    private TextView mAddressView;      // 地址
    private TextView mDistanceView;     // 距离
    private ImageView mPhoneView;        // 商家电话
    private TextView mSubscribeView;    // 电话预约
    private RecyclerView mRecyclerView;

    private static String barBerId;//理发师ID
    /**
     * DATA
     */
    // 创建界面加载状态管理对象
    private LoadManager mLoadManager;
    // 创建适配器对象
    private IntroduceAdapter mIntroduceAdapter;

    /**
     * 显示发型师介绍的界面
     *
     * @param context
     */
    public static void show(Context context, String id) {
        Intent intent=new Intent(context, IntroduceActivity.class);
        intent.putExtra("ID",id);
        context.startActivity(intent);
    }


    @Override
    protected boolean initArgs(Bundle bundle) {
        barBerId=bundle.getString("ID");
        return "".equals(barBerId) || barBerId==null?false:true;
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_introduce;
    }


    private String phone;
    /**
     * 数据绑定
     * @param detail
     */
    private void loadData(BarberDetail detail) {
        Picasso.get().load(detail.getBarber_msg().getPic_url()).placeholder(R.drawable.none_img)
                .into(mBarberImageView);
        mBarberNameView.setText(detail.getBarber_msg().getBarber_name());
        mbarberJobView.setText(detail.getBarber_msg().getPosition());
        mAdvantageView.setText(detail.getBarber_msg().getKeywords());
        mIntroduceView.setText(detail.getBarber_msg().getBarber_detail());
        mShopNameView.setText(detail.getBarber_msg().getShop_name());
        mAddressView.setText(detail.getBarber_msg().getAddress());
        mDistanceView.setText(detail.getDistance());
        phone=detail.getBarber_msg().getShop_telphone();

        mIntroduceAdapter.replace(detail.getBarber_msg_node());

    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        mContentLayout = findViewById(R.id.content_layout);
        mBarberImageView = findViewById(R.id.barber_image);
        mBarberNameView = findViewById(R.id.barber_name);
        mbarberJobView = findViewById(R.id.barber_job);
        mAdvantageView = findViewById(R.id.advantage);
        mIntroduceView = findViewById(R.id.introduce);
        mShopNameView=findViewById(R.id.shop_name);
        mAddressView = findViewById(R.id.address);
        mDistanceView = findViewById(R.id.distance);
        mPhoneView = findViewById(R.id.phone);
        mRecyclerView = findViewById(R.id.recycler_view);
        mSubscribeView = findViewById(R.id.subscribe);

        mLoadManager = LoadFactory.getInstance().register(mContentLayout, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                mLoadManager.showStateView(LoadingView.class);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadManager.showSuccessView();
            }
        }, 1000);

        mPhoneView.setOnClickListener(this);
        mSubscribeView.setOnClickListener(this);


    }


    @Override
    protected void initDatas() {
        super.initDatas();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mIntroduceAdapter = new IntroduceAdapter();
        mRecyclerView.setAdapter(mIntroduceAdapter);

        onGetBarberInfo(barBerId,"0","0");
    }


    /**
     * 获取发型师详情
     * @param id
     * @param lat
     * @param lng
     */
    private void onGetBarberInfo(String id,String lat,String lng) {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("barber_id", id);
        AsyncHttpPostFormData.addFormData("lat", lat);
        AsyncHttpPostFormData.addFormData("lng", lng);
        okHttpClient.post("/v1/barber/barber-info", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onGetBarberInfo " + response.toString());
                if (response.getCode() == 200) {
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<RspModel<BarberDetail>>() {
                        }.getType();
                        Log.i(TAG, "onGetBarberInfo: " + response.getBody());
                        RspModel<BarberDetail> rspModel = gson.fromJson(response.getBody(), type);
                        Log.i(TAG, "onGetBarberInfo: " + rspModel.toString());
                        loadData(rspModel.getData());
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 显示控件点击事件的回调方法
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.phone:        // 拨打电话
            case R.id.subscribe:
                showCallPhoneDialogFragment();
                break;
            default:
                break;
        }
    }


    /**
     * 显示拨打电话的dialog对象
     */
    private void showCallPhoneDialogFragment() {
        CallPhoneDialogFragment fragment = CallPhoneDialogFragment.newInstance(phone);
        if (!fragment.isAdded()) {
            fragment.show(getSupportFragmentManager(), fragment.getClass().getName());
        }
    }

}
