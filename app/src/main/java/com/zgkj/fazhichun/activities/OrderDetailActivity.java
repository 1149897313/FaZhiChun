package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.common.widgets.recycler.decoration.SpaceItemDecoration;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.CropSquareTransformation;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.order.PPayNoAdapter;
import com.zgkj.fazhichun.entity.order.OrderDetail;
import com.zgkj.fazhichun.entity.order.ValidateCode;
import com.zgkj.fazhichun.fragments.dialog.phone.CallPhoneDialogFragment;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.LoadingView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/15.
 * Descr:   订单详情页
 */

public class OrderDetailActivity extends ToolbarActivity implements View.OnClickListener {

    /**
     * UI
     */
    private FrameLayout mContentLayout;
    private RelativeLayout shop_info;
    private ImageView mShopImageView;
    private TextView mTypeView;
    private TextView mShopNameView;
    private TextView number;
    private TextView mPriceView;
    private TextView order_detail_title;
    private TextView order_detail;
    private TextView mAddressView;
    private TextView mDistanceView;
    private ImageView mPhoneView;
    private TextView service_explain;
    private LinearLayout pay_no_linear;
    private RecyclerView mPPayNoRecyclerView;

    private TextView leftText, rightText;
    /**
     * DATA
     */
    private String hId;
    // 创建界面加载状态的管理对象
    private LoadManager mLoadManager;


    // 消费码适配器对象
    private PPayNoAdapter payNoAdapter;

    //订单ID
    private String oderId;

    //商家电话
    private String phone;

    public static void show(Context context, String oderId) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("ID", oderId);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        oderId = bundle.getString("ID");
        return !TextUtils.isEmpty(oderId);
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_order_detail;
    }


    //订单总价
    private BigDecimal orderAmount;

    private void showView(OrderDetail detail) {
        orderAmount = detail.getOrder_amount();
        detail.getHairdresser_id();
        Picasso.get().load("".equals(detail.getShop_image()) ? mContext.getResources().getString(R.string.none_image_url) : detail.getShop_image()).placeholder(R.drawable.none_img).transform(new CropSquareTransformation())
                .into(mShopImageView);
        mShopNameView.setText(detail.getShop_name());
        mTypeView.setText("[" + detail.getHairdresser_name() + "]");
        number.setText("数量：" + detail.getNum());
        mPriceView.setText("价格：" + detail.getOrder_amount());
        if (!TextUtils.isEmpty(detail.getPay_time())) {
            order_detail.setText(detail.getOrder_sn() + "\n" + detail.getPay_time() + "\n" + detail.getNum() + "\n￥" + detail.getOrder_amount());
        } else {
            order_detail.setText(detail.getOrder_sn() + "\n" + detail.getNum() + "\n￥" + detail.getOrder_amount());
            order_detail_title.setText("订单号\n数量\n总价");
        }
        mAddressView.setText(detail.getAddress());
        mDistanceView.setText("距您" + 0 + "KM");
        phone = detail.getShop_telphone();
        service_explain.setText(detail.getBuy_know());
        hId = detail.getHairdresser_id();
        if (detail.getValidate_code() != null && !"[]".equals(detail.getValidate_code().toString())) {
            payNoAdapter.replace(detail.getValidate_code());
        } else {
            pay_no_linear.setVisibility(View.GONE);
        }
        //1. 待付款  2. 待使用 4.5.6.已完成 8.退款
        switch (detail.getOrder_status()) {
            case 1:
                leftText.setVisibility(View.VISIBLE);
                rightText.setVisibility(View.VISIBLE);
                break;
            case 4:
                if(detail.getShop_service_score()==null || TextUtils.isEmpty(detail.getShop_service_score())){
                    leftText.setText("去评价");
                    leftText.setTextColor(getResources().getColor(R.color.textColorAccent));
                    leftText.setVisibility(View.VISIBLE);
                }else {
                    leftText.setVisibility(View.GONE);
                }
                break;
            case 5:
            case 6:
                leftText.setVisibility(View.VISIBLE);
                leftText.setText("去评价");
                leftText.setTextColor(getResources().getColor(R.color.textColorAccent));
                break;
            default:
                leftText.setVisibility(View.GONE);
                rightText.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        mContentLayout = findViewById(R.id.content_layout);
        shop_info = findViewById(R.id.shop_info);
        mShopImageView = findViewById(R.id.shop_image);
        mTypeView = findViewById(R.id.type);
        mShopNameView = findViewById(R.id.shop_name);
        number = findViewById(R.id.number);
        mPriceView = findViewById(R.id.price);
        order_detail_title = findViewById(R.id.order_detail_title);
        order_detail = findViewById(R.id.order_detail);
        mAddressView = findViewById(R.id.address);
        mDistanceView = findViewById(R.id.distance);
        mPhoneView = findViewById(R.id.phone);
        service_explain = findViewById(R.id.service_explain);
        pay_no_linear = findViewById(R.id.pay_no_linear);
        mPPayNoRecyclerView = findViewById(R.id.pay_no_recycler_view);
        leftText = findViewById(R.id.left_text);
        rightText = findViewById(R.id.right_text);
        //跳转商家
        shop_info.setOnClickListener(this);
        // 为拨打商家电话控件注册点击事件
        mPhoneView.setOnClickListener(this);
        leftText.setOnClickListener(this);
        rightText.setOnClickListener(this);
    }

    /**
     * 订单详情
     *
     * @param id
     */
    private void onGetOrderDetail(String id) {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("order_id", id);
        okHttpClient.post("/v1/order/order-info", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:订单详情 " + response.toString());

                Type type = new TypeToken<RspModel<OrderDetail>>() {
                }.getType();
                OrderDetail detail = getAnalysis(response, type, "订单详情");
                if (detail != null) {
                    showView(detail);
                }
            }
        });
    }

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
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
        }
        mLoadManager.showStateView(EmptyView.class);
        return null;
    }


    @Override
    protected void initDatas() {
        super.initDatas();
        // 初始化界面加载管理对象
        mLoadManager = LoadFactory.getInstance().register(mContentLayout, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                mLoadManager.showStateView(LoadingView.class);
            }
        });
        mPPayNoRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mPPayNoRecyclerView.addItemDecoration(new SpaceItemDecoration(mContext, 10));


        payNoAdapter = new PPayNoAdapter(new RecyclerViewAdapter.AdapterListenerImpl<ValidateCode>() {
            @Override
            public void onItemClick(RecyclerViewAdapter.ViewHolder<ValidateCode> holder, ValidateCode data) {
                super.onItemClick(holder, data);

            }
        });

        mPPayNoRecyclerView.setAdapter(payNoAdapter);

        onGetOrderDetail(oderId);
    }

    /**
     * 控件点击事件的回调方法
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shop_info://跳转商家
                // 跳转到理发店详情界面
                BarberShopActivity.show(mContext, hId);
            case R.id.address:// 地址
                break;
            case R.id.phone:
                showCallPhoneDialogFragment();
                break;
            case R.id.left_text:
                CommentWriteActivity.show(mContext, oderId);
                break;
            case R.id.right_text:
                // 跳转到支付订单界面
                PaymentActivity.show(mContext, oderId, orderAmount.toString());
                break;
            default:
                break;
        }
    }

    /**
     * 显示拨打商家电话的dialog
     */
    private void showCallPhoneDialogFragment() {
        CallPhoneDialogFragment callPhoneDialogFragment = CallPhoneDialogFragment.newInstance(phone);
        if (!callPhoneDialogFragment.isAdded()) {
            callPhoneDialogFragment.show(getSupportFragmentManager(), callPhoneDialogFragment.getClass().getName());
        }
    }
}
