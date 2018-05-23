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
import com.zgkj.fazhichun.entity.order.Order;
import com.zgkj.fazhichun.entity.order.ReadyOrder;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.LoadingView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;

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
    private TextView cut_down;
    private RelativeLayout vip_show;//会员折扣
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
    private TextView reduce_money;//
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
        return !TextUtils.isEmpty(hId);
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_buy;
    }

    //控件赋值
    private void showView(ReadyOrder readyOrder) {
        mShopNameView.setText(readyOrder.getHairdresser_name());
        mPriceView.setText("￥" + String.valueOf(readyOrder.getFavorable_Price()));
        bigDecimal = readyOrder.getFavorable_Price();
        if (TextUtils.isEmpty(readyOrder.getUser_coupon_count()) || "0".equals(readyOrder.getUser_coupon_count())) {
            coupon_number.setText("暂无可用");
            coupon_number.setTextColor(getResources().getColor(R.color.textColorThird));
        } else {
            coupon_number.setText(readyOrder.getUser_coupon_count() + "个优惠券可用");
            coupon_number.setTextColor(getResources().getColor(R.color.textColorAccent));
            coupon_number.setOnClickListener(this);
        }
        if ("2".equals(readyOrder.getMember_level())) {
            vip_show.setVisibility(View.VISIBLE);
            user_type.setVisibility(View.GONE);
        } else {
            vip_show.setVisibility(View.GONE);
            user_type.setVisibility(View.VISIBLE);
            reduce_money.setText(String.format(getResources().getString(R.string.label_buy_general_vip_reduce_money),"20"));
        }
        userCommission = readyOrder.getUser_commission();
        onCompute(1);
    }

    private BigDecimal bigDecimal;//单价
    private BigDecimal userCommission;//折扣
    private String copuonPrice = "0";//优惠券金额
    private String copuonId;//优惠券ID
    private BigDecimal money;//订单总价-未减去优惠券
    private BigDecimal toTalmoney;//订单总价


    DecimalFormat df= new DecimalFormat("0.00");
    /**
     * 费用计算
     *
     * @param quantity 数量
     */
    private void onCompute(int quantity) {
        money = bigDecimal.multiply(new BigDecimal(String.valueOf(quantity)));//原价
        mSubtotalView.setText("￥" + money);//小计
        //会员折扣
        BigDecimal decimal=new BigDecimal("1").subtract(userCommission);
        cut_down.setText("-￥" +df.format(money.multiply(decimal)));
        money = money.multiply(userCommission);// 总价X折扣
        toTalMoney();
    }

    /**
     * 订单总价显示
     */
    private void toTalMoney() {
        toTalmoney = money.subtract(new BigDecimal(copuonPrice));//减去优惠券
        general_total_money.setText("￥" + df.format(toTalmoney));//订单总价
        total_money.setText("￥" + df.format(toTalmoney));//订单总价
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
        cut_down = findViewById(R.id.cut_down);
        coupon_number = findViewById(R.id.coupon_number);
        vip_show = findViewById(R.id.vip_show);
        user_type = findViewById(R.id.user_type);
        reduce_money=findViewById(R.id.reduce_money);
        open_vip = findViewById(R.id.open_vip);
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
                mLoadManager.showStateView(LoadingView.class);
            }
        });
    }


    @Override
    protected void initDatas() {
        super.initDatas();
        mQuantityView.setInventory(9999);
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
                Log.i(TAG, "onSuccess:提交订单" + response.toString());
                Type type = new TypeToken<RspModel<ReadyOrder>>() {
                }.getType();
                ReadyOrder readyOrder = getAnalysis(response, type, "提交订单");
                if (readyOrder != null) {
                    showView(readyOrder);
                } else {
                    mLoadManager.showStateView(EmptyView.class);
                }
            }
        });
    }


    /**
     * 获取订单
     *
     * @param hId
     * @param pay_money
     * @param number
     * @param copuonId
     */
    private void geOrder(String hId,final String pay_money, String number,String copuonId) {

        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("hairdresser_id", hId);//hairdresser_id	int	是		商品的唯一标识	商品id
        AsyncHttpPostFormData.addFormData("pay_money", pay_money);//pay_money	int	是		商品的唯一标识	商品
        AsyncHttpPostFormData.addFormData("number", number);//number	int	是		数量	商品数量
        AsyncHttpPostFormData.addFormData("coupon_list_id", copuonId);//coupon_list_id	int	否		无	用户代金券id
        AsyncHttpPostFormData.addFormData("pay_type","2");//pay_type	int	是		1代金券--2微信(可加优惠券)
        okHttpClient.post("/v1/checkout/sure-pay", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showStateView(NetErrorView.class);
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:geOrder" + response.toString());
                Type type = new TypeToken<RspModel<Order>>() {
                }.getType();
                Order order = getAnalysis(response, type, "geOrder");
                if (order != null) {
                    // 跳转到支付订单界面
                    PaymentActivity.show(mContext, order.getOrder_id(),pay_money);
                } else {
                    mLoadManager.showStateView(EmptyView.class);
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
                }
                break;
            default:
                mLoadManager.showStateView(EmptyView.class);
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
            case R.id.open_vip://开通VIP
                OpenMemberActivity.show(mContext);
                break;
            case R.id.coupon_number://使用优惠券
                Intent intent = new Intent(mContext, CouponsOrderActivity.class);
                intent.putExtra("ID", hId);
                startActivityForResult(intent, 1000);
                break;
            case R.id.submit:// 提交订单
                geOrder(hId, money.toString(), String.valueOf(mQuantityView.getValue()), "");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000 && resultCode == 1001 && data != null) {
            copuonId = data.getStringExtra("copuonId");
            copuonPrice = data.getStringExtra("copuonPrice");
            coupon_number.setText("使用优惠券立减" + copuonPrice + "元");
            toTalMoney();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
