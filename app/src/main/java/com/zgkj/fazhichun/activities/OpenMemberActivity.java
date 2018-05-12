package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.type.CouponsAdapter;
import com.zgkj.fazhichun.entity.coupon.Coupon;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.LoadingView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;


/***
 * 开通会员
 */
public class OpenMemberActivity extends ToolbarActivity implements View.OnClickListener{

    /**
     * UI
     */
    private NestedScrollView mNestedScrollView;
    private LinearLayout open_button;

    /**
     * DATA
     */
    private LoadManager mLoadManager;

    private CouponsAdapter couponsAdapter;


    /**
     * 显示具体的理发类型项目的详细信息界面
     *
     * @param context
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, OpenMemberActivity.class));
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_open_member;
    }


    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        mNestedScrollView = findViewById(R.id.nested_scroll_view);
        open_button=findViewById(R.id.open_button);

        open_button.setOnClickListener(this);
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
        mLoadManager.showSuccessView();
    }

    /**
     * 获取我的优惠券
     */
    private void onGetCouponList() {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        okHttpClient.post("/v1/voucher/list", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showStateView(NetErrorView.class);
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onGetCouponList " + response.toString());
                if (response.getCode() == 200) {
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<RspModel<List<Coupon>>>() {
                        }.getType();
                        RspModel<List<Coupon>> rspModel = gson.fromJson(response.getBody(), type);
                        Log.i(TAG, "onBackValue:onGetCouponList " + rspModel.toString());
                        System.out.println("------:"+rspModel.getData());

                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
