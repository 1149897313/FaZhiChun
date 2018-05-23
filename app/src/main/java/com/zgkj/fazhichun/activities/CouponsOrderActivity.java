package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

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
import com.zgkj.common.widgets.recycler.decoration.SpaceItemDecoration;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.coupons.CouponsOrderAdapter;
import com.zgkj.fazhichun.entity.collection.Collection;
import com.zgkj.fazhichun.entity.coupon.Coupon;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.LoadingView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 订单优惠券
 */
public class CouponsOrderActivity extends ToolbarActivity implements CompoundButton.OnCheckedChangeListener {

    /**
     * UI
     */
    private TextView mTitleView;
    private CheckBox not_to_use_select;
    private NestedScrollView mNestedScrollView;
    private RecyclerView mRecyclerView;

    /**
     * DATA
     */
    private LoadManager mLoadManager;

    private CouponsOrderAdapter couponsAdapter;

    private String hId;

    /**
     * 显示具体的理发类型项目的详细信息界面
     *
     * @param context
     */
    public static void show(Context context, String hId) {
        Intent intent = new Intent(context, CouponsOrderActivity.class);
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
        return R.layout.activity_coupons_order;
    }


    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        mTitleView = findViewById(R.id.title);
        not_to_use_select = findViewById(R.id.not_to_use_select);
        mNestedScrollView = findViewById(R.id.nested_scroll_view);
        mRecyclerView = findViewById(R.id.recycler_view);

        not_to_use_select.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        mLoadManager = LoadFactory.getInstance().register(mNestedScrollView, new AbsView.OnReloadListener() {
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

        couponsAdapter = new CouponsOrderAdapter(new RecyclerViewAdapter.AdapterListenerImpl<Coupon>() {
            @Override
            public void onItemClick(RecyclerViewAdapter.ViewHolder<Coupon> holder, Coupon data) {
                super.onItemClick(holder, data);
                boolean selected = data.isSelected();
                if (!selected) {
                    not_to_use_select.setChecked(false);
                    //全部设置为未选中状态
                    for (int i = 0; i < couponsAdapter.getDataList().size(); i++){
                        Coupon collect = couponsAdapter.getDataList().get(i);
                        collect.setSelected(false);
                    }
                    // 改变为选中状态
                    data.setSelected(true);
                } else {
                    // 改变为未选中状态
                    data.setSelected(false);
                }
                // 刷新数据的显示
                couponsAdapter.notifyItemChanged(holder.getAdapterPosition());
            }
        });
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(mContext, 15));
        mRecyclerView.setAdapter(couponsAdapter);
        onGetCouponOderList(hId);
    }

    /**
     * 暂不使用优惠券
     */
    public void selectNotToUse(){
        if (couponsAdapter.getDataList() != null && couponsAdapter.getDataList().size() > 0){
            for (int i = 0; i < couponsAdapter.getDataList().size(); i++){
                Coupon collect = couponsAdapter.getDataList().get(i);
                collect.setSelected(false);
            }
            couponsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 返回数据
     */
    private void backValue(){
        String copuonId=null,copuonPrice="0";
        if (couponsAdapter.getDataList() != null && couponsAdapter.getDataList().size() > 0){
            for (int i = 0; i < couponsAdapter.getDataList().size(); i++){
                Coupon collect = couponsAdapter.getDataList().get(i);
                if(collect.isSelected()){
                    copuonId=collect.getCoupon_id();
                    copuonPrice=collect.getCoupon_money();
                }
            }
        }
        Intent intent = new Intent();
        intent.putExtra("copuonPrice",copuonPrice);
        intent.putExtra("copuonId", copuonId);
        setResult(1001, intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            backValue();
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            backValue();
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * 获取商品优惠券
     */
    private void onGetCouponOderList(String hId) {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("hairdresser_id", hId);
        okHttpClient.post("/v1/checkout/order-use-coupon", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showStateView(NetErrorView.class);
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onGetCouponOderList " + response.toString());
                Type type = new TypeToken<RspModel<List<Coupon>>>() {
                }.getType();
                List<Coupon> list = getAnalysis(response, type, "onGetCouponOderList");
                couponsAdapter.replace(list);
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
        return null;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {
        if (isChecked) {
            selectNotToUse();
        } else {
        }
    }
}
