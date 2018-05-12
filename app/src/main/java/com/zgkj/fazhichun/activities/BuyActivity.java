package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.zgkj.common.utils.ToastUtil;
import com.zgkj.common.widgets.QuantityView;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.order.Oder;
import com.zgkj.fazhichun.entity.order.ReadyOrder;
import com.zgkj.fazhichun.entity.shop.Hairdresser;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/13.
 * Descr:   购买提交订单的显示界面
 */

public class BuyActivity extends ToolbarActivity implements View.OnClickListener, QuantityView.OnQuantityChangeListener {

    /**
     * UI
     */
    private LinearLayout mContentLayout;
    private TextView mShopNameView;     // 理发店名
    private TextView mPriceView;        // 单价
    private TextView mSubtotalView;     // 小计
    private TextView coupon_number;//可用优惠券
    private TextView mTotalView;        // 总计
    private QuantityView mQuantityView; // 数量
    private TextView general_total_money;
    private RelativeLayout mBuyVoucherLayout;  // 抵用券
    private TextView mVoucherBalanceView;   // 代金券余额
    private TextView mDeductionMoneyView;   // 折扣金额
    private ImageView mDeductionMoneyImageView;
    private TextView open_vip;
    private RelativeLayout user_type;//普通用户
    private TextView total_money;
    private TextView mSubmitView; // 提交订单
    private String hId;


    /**
     * DATA
     */
    private LoadManager mLoadManager;


    /**
     * 显示购买提交订单的界面
     *
     * @param context
     */
    public static void show(Context context, String hId) {
        Intent intent = new Intent(context, BuyActivity.class);
        intent.putExtra("ID", hId);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        hId = bundle.getString("ID");
        return "".equals(hId) || hId == null ? false : true;
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_buy;
    }


    private BigDecimal bigDecimal;

    //控件赋值
    private void showView(ReadyOrder readyOrder) {
        mShopNameView.setText(readyOrder.getHairdresser_name());
        mPriceView.setText("￥"+String.valueOf(readyOrder.getFavorable_Price()));
        bigDecimal = readyOrder.getFavorable_Price();
        if (TextUtils.isEmpty(readyOrder.getUser_coupon_count()) || "0".equals(readyOrder.getUser_coupon_count())) {
            coupon_number.setText("暂无可用");
            coupon_number.setTextColor(getResources().getColor(R.color.textColorThird));
        } else {
            coupon_number.setText(readyOrder.getUser_coupon_count() + "个优惠券可用");
            coupon_number.setTextColor(getResources().getColor(R.color.textColorAccent));
        }
        if ("2".equals(readyOrder.getMember_level())) {
            user_type.setVisibility(View.GONE);
        } else {
            user_type.setVisibility(View.VISIBLE);
        }
        onCompute(1);
    }

    //商品价格
    BigDecimal money;
    /**
     * 费用计算
     *
     * @param quantity 数量
     */
    private void onCompute(int quantity) {
       money = bigDecimal.multiply(new BigDecimal(String.valueOf(quantity)));//原价
        mSubtotalView.setText("￥" + money);
        general_total_money.setText("￥" + money);
        total_money.setText("￥" + money);
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        mContentLayout = findViewById(R.id.content_layout);
        mShopNameView = findViewById(R.id.shop_name);
        mPriceView = findViewById(R.id.price);
        mSubtotalView = findViewById(R.id.subtotal);
        mQuantityView = findViewById(R.id.quantity_view);
        coupon_number = findViewById(R.id.coupon_number);
        user_type = findViewById(R.id.user_type);
        open_vip=findViewById(R.id.open_vip);
        general_total_money = findViewById(R.id.general_total_money);
        total_money = findViewById(R.id.total_money);
        mSubmitView = findViewById(R.id.submit);

        // 注册用户选择数量的回调监听
        mQuantityView.setOnQuantityChangeListener(this);
        //开通会员
        open_vip.setOnClickListener(this);
        // 为提交订单按钮注册点击事件
        mSubmitView.setOnClickListener(this);


        mLoadManager = LoadFactory.getInstance().register(mContentLayout, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {

            }
        });
    }


    @Override
    protected void initDatas() {
        super.initDatas();
        mQuantityView.setInventory(12);

        getReadyOrder(hId);
    }


    /**
     * 准备提交订单
     *
     * @param hId
     */
    private void getReadyOrder(String hId) {

        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("hairdresser_id", hId);
        okHttpClient.post("/v1/checkout/ready-order", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showStateView(NetErrorView.class);
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:getReadyOrder" + response.toString());
                Type type = new TypeToken<RspModel<ReadyOrder>>() {
                }.getType();
                ReadyOrder readyOrder=getAnalysis(response,type,"getReadyOrder");
                showView(readyOrder);
            }
        });
    }


    /**
     * 获取订单
     * @param hId
     * @param pay_money
     * @param number
     * @param copuonId
     */
    private void geOrder(String hId,String pay_money,String number,String copuonId) {

        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("hairdresser_id", hId);//hairdresser_id	int	是		商品的唯一标识	商品id
        AsyncHttpPostFormData.addFormData("pay_money", pay_money);//pay_money	int	是		商品的唯一标识	商品
        AsyncHttpPostFormData.addFormData("number", number);//number	int	是		数量	商品数量
        AsyncHttpPostFormData.addFormData("coupon_list_id", copuonId);//coupon_list_id	int	否		无	用户代金券id
        okHttpClient.post("/v1/checkout/sure-pay", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showStateView(NetErrorView.class);
                e.printStackTrace();
            }
            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:geOrder" + response.toString());
                Type type = new TypeToken<RspModel<Oder>>() {
                }.getType();
                Oder oder=getAnalysis(response,type,"geOrder");
            }
        });
    }

    /**
     * 数据解析
     *
     * @param response
     */
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
                            Log.i(TAG, log+": " + rspModel.getData());
                            return rspModel.getData();
                        default:
                            App.showMessage("data:code"+rspModel.getCode());
                            break;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }

    /**
     * 显示控件点击事件的回调方法
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.buy_voucher_layout:       // 购买代金券
//                // 跳转到购买代金券界面
//                RechargeActivity.show(mContext);
//                break;
            case R.id.open_vip:
                OpenMemberActivity.show(mContext);
                break;
            case R.id.submit:       // 提交订单
                // 跳转到支付订单界面
//                PaymentActivity.show(mContext);
                geOrder(hId,money.toString(),String.valueOf(mQuantityView.getValue()),"");
                break;
            default:
                break;
        }
    }



    @Override
    public void onQuantityChanged(int quantity) {
        ToastUtil.getInstance()
                .setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent))
                .setTextColor(mContext.getResources().getColor(R.color.textColorLight))
                .show("你选择的数量为：  " + quantity, Toast.LENGTH_SHORT);
        onCompute(quantity);
    }
}
