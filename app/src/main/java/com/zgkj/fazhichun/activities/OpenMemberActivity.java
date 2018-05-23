package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.coupons.CouponsAdapter;
import com.zgkj.fazhichun.adapter.member.MemberPrivilegeAdapter;
import com.zgkj.fazhichun.entity.coupon.Coupon;
import com.zgkj.fazhichun.entity.member.MemberPrivilege;
import com.zgkj.fazhichun.view.LoadingView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;


/***
 * 开通会员
 */
public class OpenMemberActivity extends ToolbarActivity implements View.OnClickListener {

    /**
     * UI
     */
    private NestedScrollView mNestedScrollView;
    private RecyclerView mRecyclerView;
    private LinearLayout open_button;
    private TextView reckon_title,reckon_content;
    /**
     * DATA
     */
    private LoadManager mLoadManager;
    private MemberPrivilegeAdapter mAdapter;
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
        mRecyclerView = findViewById(R.id.recycler_view);
        reckon_title=findViewById(R.id.reckon_title);
        reckon_content=findViewById(R.id.reckon_content);
        open_button = findViewById(R.id.open_button);

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
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mAdapter = new MemberPrivilegeAdapter(new RecyclerViewAdapter.AdapterListenerImpl<MemberPrivilege>() {
            @Override
            public void onItemClick(RecyclerViewAdapter.ViewHolder<MemberPrivilege> holder, MemberPrivilege data) {
                super.onItemClick(holder, data);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        List<MemberPrivilege> list = new java.util.ArrayList<>();
        for (int i = 0; i < 6; i++) {
            MemberPrivilege memberPrivilege = new MemberPrivilege();
            memberPrivilege.setName(name[i]);
            memberPrivilege.setContent(content[i]);
            memberPrivilege.setImageUrl(imageUrl[i]);
            memberPrivilege.setState(state[i]);
            list.add(memberPrivilege);
        }
        mAdapter.replace(list);
        //设置颜色
        SpannableString spannableString = new SpannableString("根据《美业大数据报告》估算");
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.textColorOrange)), 2, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        reckon_title.setText(spannableString);
        spannableString=new SpannableString("开通会员可为您每年节约1100元");
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.textColorOrange)), 11, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        reckon_content.setText(spannableString);
        mLoadManager.showSuccessView();
    }

    private String[] name = {"会员折扣", "会员专场", "每月送券", "10倍积分", "24小时客服", "免费体验"};
    private String[] content = {"全平台所有商品折扣购买", "享受会员专场超低折扣", "每月赠送百元优惠券", "全平台所有商品折扣购买", "随时随地专属客服服务", "平台返利会员和机会免费体验商"};
    private int[] imageUrl = {R.drawable.ico_24hour, R.drawable.ico_huiyuan, R.drawable.ico_priviledge_youhuiquan, R.drawable.ico_jifen, R.drawable.ico_24hour, R.drawable.ico_tiyan};
    private boolean[] state = {false, true, true, true, false, true};

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
                        System.out.println("------:" + rspModel.getData());

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
