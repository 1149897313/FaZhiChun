package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.zgkj.common.Common;
import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.common.http.AsyncHttpPostFormData;
import com.zgkj.common.http.AsyncHttpResponse;
import com.zgkj.common.http.AsyncOkHttpClient;
import com.zgkj.common.http.AsyncResponseHandler;
import com.zgkj.common.utils.SPUtil;
import com.zgkj.common.utils.ToastUtil;
import com.zgkj.common.widgets.LabelsView;
import com.zgkj.common.widgets.StarBarView;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.common.widgets.recycler.decoration.SpaceItemDecoration;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.barbershop.ShopServiceAdapter;
import com.zgkj.fazhichun.adapter.commodity.CommoditySpecificationAdapter;
import com.zgkj.fazhichun.entity.collection.CollectionBack;
import com.zgkj.fazhichun.entity.shop.HairdresserInfo;
import com.zgkj.fazhichun.entity.tag.Lable;
import com.zgkj.fazhichun.fragments.dialog.phone.CallPhoneDialogFragment;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.LoadingView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/2/28.
 * Descr:   理发店商品项目服务详情界面
 */

public class CommodityActivity extends ToolbarActivity implements View.OnClickListener {

    /**
     * UI
     */
    private FrameLayout mContentLayout;
    private ImageView mShopImageView;
    private TextView mTypeView;
    private TextView mShopNameView;
    private TextView mPriceView;
    private TextView mOriginalPriceView;
    private TextView mSellNumberView;
    private StarBarView mStarBarView;
    private TextView mScoreView;
    private TextView mCommentView;
    private LabelsView mLabelsView;
    private TextView mAddressView;
    private TextView mDistanceView;
    private ImageView mPhoneView;
    private RecyclerView mServiceRecyclerView;
    private RecyclerView mSpecificationRecyclerView;
    private LinearLayout mShareLayout;
    private TextView mBuyView;
    private MenuItem menuItem;

    private TextView service_explain;
    private TextView buy_know;


    /**
     * DATA
     */
    // 创建界面加载状态的管理对象
    private LoadManager mLoadManager;

    // 商家服务的适配器对象
    private ShopServiceAdapter mShopServiceAdapter;


    // 服务说明适配器对象
    private CommoditySpecificationAdapter mCommoditySpecificationAdapter;

    //商品ID
    private static String hId;

    //商家电话
    private String phone;

    /**
     * 理发店商品项目服务详情界面
     *
     * @param context 上下文对象
     */
    public static void show(Context context, String id) {
        context.startActivity(new Intent(context, CommodityActivity.class));
        hId = id;
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_commodity;
    }


    /**
     * 值绑定
     *
     * @param info
     */
    private void valueShow(HairdresserInfo info) {
        if (info.getIs_love() == 0) {
            menuItem.setIcon(R.drawable.ic_menu_collect_normal);
        } else {
            menuItem.setIcon(R.drawable.ic_menu_collect_pressed);
        }
        Picasso.get().load(info.getHairdresser().getPic_url()).placeholder(R.drawable.none_img)
                .into(mShopImageView);
        mShopNameView.setText(info.getHairdresser().getHairdresser_name());
        mPriceView.setText(String.valueOf("￥" + info.getHairdresser().getFavorable_Price()));
        mOriginalPriceView.setText(String.valueOf("￥" + info.getHairdresser().getSale_price()));
        mSellNumberView.setText(String.valueOf("已售:" + info.getHairdresser().getQuantity() + "份"));
        mStarBarView.setStarMark(info.getHairdresser().getShop_service_score());
        mScoreView.setText(String.valueOf(info.getHairdresser().getShop_service_score() + "分"));
        mCommentView.setText(info.getHairdresser().getTalk_num() + "条评价");
        List<String> list = new ArrayList<>();
        List<Lable> lables = info.getHairdresser_label();
        for (int i = 0; i < lables.size(); i++) {
            list.add(lables.get(i).getLabel_name() + "\t\t" + lables.get(i).getLabel_count());
        }
        mLabelsView.setLabels(list);
        mAddressView.setText(info.getHairdresser().getAddress());
        phone = String.valueOf(info.getHairdresser().getShop_telphone());
        float distance = AMapUtils.calculateLineDistance(new LatLng(info.getHairdresser().getShop_lat(), info.getHairdresser().getShop_lng()), SPUtil.getLatLng());
        mDistanceView.setText("距您约" + Math.round(distance) + "米");//距离
        mServiceRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));

        mShopServiceAdapter = new ShopServiceAdapter();
        mServiceRecyclerView.setAdapter(mShopServiceAdapter);


        mSpecificationRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mSpecificationRecyclerView.addItemDecoration(new SpaceItemDecoration(mContext, 4));
        mCommoditySpecificationAdapter = new CommoditySpecificationAdapter();
        mSpecificationRecyclerView.setAdapter(mCommoditySpecificationAdapter);


        mShopServiceAdapter.replace(info.getShop_service());
        buy_know.setText(info.getHairdresser().getBuy_know());
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        mContentLayout = findViewById(R.id.content_layout);
        mShopImageView = findViewById(R.id.shop_image);
        mTypeView = findViewById(R.id.type);
        mShopNameView = findViewById(R.id.shop_name);
        mPriceView = findViewById(R.id.price);
        mOriginalPriceView = findViewById(R.id.original_price);
        mSellNumberView = findViewById(R.id.sell_number);
        mStarBarView = findViewById(R.id.star_bar_view);
        mScoreView = findViewById(R.id.score);
        mCommentView = findViewById(R.id.comment);
        mLabelsView = findViewById(R.id.labels_view);
        mAddressView = findViewById(R.id.address);
        mDistanceView = findViewById(R.id.distance);
        mPhoneView = findViewById(R.id.phone);
        mServiceRecyclerView = findViewById(R.id.service_recycler_view);
        mSpecificationRecyclerView = findViewById(R.id.specification_recycler_view);
        service_explain = findViewById(R.id.service_explain);
        mShareLayout = findViewById(R.id.share_layout);
        mBuyView = findViewById(R.id.buy);
        buy_know = findViewById(R.id.buy_know);

        // 为拨打商家电话控件注册点击事件
        mPhoneView.setOnClickListener(this);

        // 为分享显示控件注册点击事件
        mShareLayout.setOnClickListener(this);

        // 为立即抢购按钮注册点击事件
        mBuyView.setOnClickListener(this);
        onGetCollection(hId);
        onGetInfo(hId);
    }

    private void onGetCollection(String id) {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("id", id);
        AsyncHttpPostFormData.addFormData("type", "hairdresser");
        okHttpClient.post("/v1/collection/user-collection", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onGetCollection " + response.toString());
                if (response.getCode() == 200) {
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<RspModel<String>>() {
                        }.getType();
                        RspModel<String> rspModel = gson.fromJson(response.getBody(), type);
                        Log.i(TAG, "onGetCollection: " + rspModel.toString());
                        if ("1".equals(rspModel.getData())) {//1或0---1为收藏和0为没收藏
                            menuItem.setIcon(R.drawable.ic_menu_collect_pressed);
                        } else {
                            menuItem.setIcon(R.drawable.ic_menu_collect_normal);
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 添加收藏
     *
     * @param id
     */
    private void onAddCollection(String id) {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("add_id", id);
        AsyncHttpPostFormData.addFormData("type", "hairdresser");
        okHttpClient.post("/v1/collection/add-collection", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onAddCollection " + response.toString());
                if (response.getCode() == 200) {
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<RspModel<CollectionBack>>() {
                        }.getType();
                        RspModel<CollectionBack> rspModel = gson.fromJson(response.getBody(), type);
                        Log.i(TAG, "onAddCollection: " + rspModel.toString());
                        if ("1".equals(rspModel.getData().getIs_walk())) {
                            menuItem.setIcon(R.drawable.ic_menu_collect_pressed);
                        } else {
                            menuItem.setIcon(R.drawable.ic_menu_collect_normal);
                        }
                        ToastUtil.getInstance()
                                .setBackgroundColor(getResources().getColor(R.color.colorAccent))
                                .setTextColor(getResources().getColor(R.color.textColorLight))
                                .show(rspModel.getData().getMsg(), Toast.LENGTH_SHORT);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 获取商品详情
     *
     * @param id 商品id
     */
    private void onGetInfo(String id) {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("hairdresser_id", id);
        okHttpClient.post("/v1/hairdresser/hairdresser-detail", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:商品详情 " + response.toString());
                Type type = new TypeToken<RspModel<HairdresserInfo>>() {
                }.getType();
                HairdresserInfo hairdresserInfo = getAnalysis(response, type, "商品详情");
                if (hairdresserInfo != null) {
                    valueShow(hairdresserInfo);
                }
            }
        });
    }

    /**
     * 数据解析
     *
     * @param response
     * @param type
     * @param log
     * @param <T>
     * @return
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


    @Override
    protected void initDatas() {
        super.initDatas();
        // 初始化界面加载管理对象
        mLoadManager = LoadFactory.getInstance().register(mContentLayout, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                mLoadManager.showStateView(LoadingView.class);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLoadManager.showSuccessView();
                    }
                }, 500);
            }
        });

        // 模拟数据请求
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadManager.showSuccessView();
            }
        }, 500);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_commodity, menu);
        menuItem = menu.findItem(R.id.action_collect);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // 如果点击了收藏按钮
        if (menuItem.getItemId() == R.id.action_collect) {
            onAddCollection(hId);
        }

        return super.onOptionsItemSelected(menuItem);
    }

    /**
     * 控件点击事件的回调方法
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address:      // 地址
                break;
            case R.id.phone:
                showCallPhoneDialogFragment();
                break;
            case R.id.share_layout:     // 分享
                share();
                break;
            case R.id.buy:          // 立即抢购
                // 跳转到购买界面
                BuyActivity.show(mContext, hId);
                break;
            default:
                break;
        }

    }

    /**
     * 分享
     */
    private void share() {


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
