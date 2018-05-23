package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zgkj.common.Common;
import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.common.http.AsyncHttpPostFormData;
import com.zgkj.common.http.AsyncHttpResponse;
import com.zgkj.common.http.AsyncOkHttpClient;
import com.zgkj.common.http.AsyncResponseHandler;
import com.zgkj.common.utils.SPUtil;
import com.zgkj.common.utils.ToastUtil;
import com.zgkj.common.widgets.StarBarView;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.common.widgets.recycler.decoration.DividerItemDecoration;
import com.zgkj.common.widgets.recycler.decoration.SpaceItemDecoration;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.barbershop.ShopBarberAdapter;
import com.zgkj.fazhichun.adapter.barbershop.ShopCommodityAdapter;
import com.zgkj.fazhichun.adapter.barbershop.ShopImageAdapter;
import com.zgkj.fazhichun.adapter.barbershop.ShopServiceAdapter;
import com.zgkj.fazhichun.adapter.comment.CommentAdapter;
import com.zgkj.fazhichun.entity.collection.CollectionBack;
import com.zgkj.fazhichun.entity.shop.BarberInfo;
import com.zgkj.fazhichun.entity.shop.Hairdresser;
import com.zgkj.fazhichun.entity.shop.ShopB;
import com.zgkj.fazhichun.fragments.dialog.phone.CallPhoneDialogFragment;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.ErrorView;
import com.zgkj.fazhichun.view.LoadingView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/2/27.
 * Descr:   理发店详情显示界面
 */

public class BarberShopActivity extends ToolbarActivity implements View.OnClickListener {

    /**
     * UI
     */
    private TextView mTitleView;
    private NestedScrollView mNestedScrollView;
    private TextView mShopNameView;
    private RecyclerView mShopRecyclerView;
    private StarBarView mShopStarBarView;
    private TextView mShopScoreView;
    private TextView mBusinessHoursView;
    private TextView mSubscribeView;
    private TextView mAddressView;
    private TextView mDistanceView;
    private ImageView mPhoneView;
    private TextView mCommodityView;        // 店内服务
    private RecyclerView mCommodityRecyclerView;
    private FrameLayout mOtherCommodityLayout;
    private TextView mOtherCommodityView;
    private TextView mOtherBarberView;
    private RecyclerView mBarberRecyclerView;       // 发型师
    private RecyclerView mServiceRecyclerView;      // 商家服务
    private StarBarView mCommentStarBarView;
    private TextView mCommentScoreView;
    private TextView mCommentView;
    private RecyclerView mCommentRecyclerView;
    private FrameLayout mCommentLayout;

    private static String shopId;//商铺ID

    /**
     * DATA
     */
    private LoadManager mLoadManager;

    // 创建适配器对象
    private ShopImageAdapter mShopImageAdapter;
    private ShopCommodityAdapter mShopCommodityAdapter;
    private ShopBarberAdapter mShopBarberAdapter;
    private ShopServiceAdapter mShopServiceAdapter;
    private CommentAdapter mCommentAdapter;

    /**
     * 显示理发店界面
     *
     * @param context
     */
    public static void show(Context context, String id) {
        shopId = id;
        context.startActivity(new Intent(context, BarberShopActivity.class));
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_barbershop;
    }

    /**
     * 绑定控件赋值
     *
     * @param shop
     */
    private void valueShow(ShopB shop) {

        if (shop.getIs_love() == 0) {
            menuItem.setIcon(R.drawable.ic_menu_collect_normal);
        } else {
            menuItem.setIcon(R.drawable.ic_menu_collect_pressed);
        }
        mShopNameView.setText(shop.getShop().getShop_name());
        mShopStarBarView.setStarMark(shop.getShop().getShop_service_score());
        mShopScoreView.setText(String.valueOf(shop.getShop().getShop_service_score() + "分"));
        mBusinessHoursView.setText(shop.getShop().getOpening_hour());

        mAddressView.setText(shop.getShop().getAddress());
        phone = String.valueOf(shop.getShop().getShop_telphone());
        mDistanceView.setText("距您约" + shop.getShop().getDistance() + "米");//距离
        mShopImageAdapter.replace(shop.getShop().getShop_banner());

        mShopServiceAdapter.replace(shop.getShop_service());

        mCommentStarBarView.setStarMark(shop.getShop().getShop_service_score());
        mCommentScoreView.setText(String.valueOf(shop.getShop().getShop_service_score() + "分"));

        mCommentView.setText(shop.getTalk_num() + "条评论");

        mCommentAdapter.replace(shop.getTalk_list());

    }

    private void valueShowShopService(List<Hairdresser> list) {
        mShopCommodityAdapter.replace(list);
        mCommodityView.setText("店内服务（" + list.size() + ")");
        if (list.size() > 2) {
            mOtherCommodityLayout.setVisibility(View.VISIBLE);
            mOtherCommodityView.setText(String.format(getResources().getString(R.string.label_other_service), String.valueOf(list.size() - 2)));
        } else {
            mOtherCommodityLayout.setVisibility(View.GONE);
        }
    }

    private void valueShowBarberService(List<BarberInfo> list) {
        mShopBarberAdapter.replace(list);
        mOtherBarberView.setText("其他" + list.size() + "位发型师");
    }


    private String phone;

    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        mTitleView = findViewById(R.id.title);
        mNestedScrollView = findViewById(R.id.nested_scroll_view);
        mShopNameView = findViewById(R.id.shop_name);
        mShopRecyclerView = findViewById(R.id.shop_recycler_view);
        mShopStarBarView = findViewById(R.id.shop_star_bar_view);
        mShopScoreView = findViewById(R.id.shop_score);
        mBusinessHoursView = findViewById(R.id.business_hours);
        mSubscribeView = findViewById(R.id.subscribe);
        mAddressView = findViewById(R.id.address);
        mDistanceView = findViewById(R.id.distance);
        mPhoneView = findViewById(R.id.phone);
        mCommodityView = findViewById(R.id.commodity);
        mCommodityRecyclerView = findViewById(R.id.commodity_recycler_view);
        mOtherCommodityLayout = findViewById(R.id.other_commodity_layout);
        mOtherCommodityView = findViewById(R.id.other_commodity);
        mOtherBarberView = findViewById(R.id.other_barber);
        mBarberRecyclerView = findViewById(R.id.barber_recycler_view);       // 发型师
        mServiceRecyclerView = findViewById(R.id.service_recycler_view);      // 商家服务
        mCommentStarBarView = findViewById(R.id.comment_star_bar_view);
        mCommentScoreView = findViewById(R.id.comment_score);
        mCommentView = findViewById(R.id.comment);
        mCommentRecyclerView = findViewById(R.id.comment_recycler_view);
        mCommentLayout = findViewById(R.id.comment_layout);

        // 为立即预约注册点击监听事件
        mSubscribeView.setOnClickListener(this);
        // 为拨打商家电话注册点击事件
        mPhoneView.setOnClickListener(this);
        // 其他理发师
        mOtherBarberView.setOnClickListener(this);
        // 查看所有评论
        mCommentLayout.setOnClickListener(this);

    }

    /**
     * 初始化数据
     */
    @Override
    protected void initDatas() {
        super.initDatas();

        // 理发店图片
        mShopRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mShopRecyclerView.addItemDecoration(new SpaceItemDecoration(mContext, 10));
        mShopImageAdapter = new ShopImageAdapter(new RecyclerViewAdapter.AdapterListenerImpl<String>() {
            @Override
            public void onItemClick(RecyclerViewAdapter.ViewHolder<String> holder, String data) {
                super.onItemClick(holder, data);
            }
        });
        mShopRecyclerView.setAdapter(mShopImageAdapter);

        // 店内服务
        mCommodityRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mCommodityRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL, R.drawable.shape_divider_line, 16, true));
        mShopCommodityAdapter = new ShopCommodityAdapter(new RecyclerViewAdapter.AdapterListenerImpl<Hairdresser>() {
            @Override
            public void onItemClick(RecyclerViewAdapter.ViewHolder<Hairdresser> holder, Hairdresser data) {
                super.onItemClick(holder, data);
                // 跳转到服务详情界面
                CommodityActivity.show(mContext, data.getHairdresser_id());

            }
        });
        mCommodityRecyclerView.setAdapter(mShopCommodityAdapter);

        // 理发师信息
        mBarberRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mBarberRecyclerView.addItemDecoration(new SpaceItemDecoration(mContext, 10));
        mShopBarberAdapter = new ShopBarberAdapter(new RecyclerViewAdapter.AdapterListenerImpl<BarberInfo>() {
            @Override
            public void onItemClick(RecyclerViewAdapter.ViewHolder<BarberInfo> holder, BarberInfo data) {
                super.onItemClick(holder, data);
                // 跳转到发型师介绍界面
                IntroduceActivity.show(mContext, data.getBarber_id());
            }
        });
        mBarberRecyclerView.setAdapter(mShopBarberAdapter);


        // 商家服务
        mServiceRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        mShopServiceAdapter = new ShopServiceAdapter();
        mServiceRecyclerView.setAdapter(mShopServiceAdapter);


        // 评论列表
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mCommentRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL, R.drawable.shape_divider_line, 16, true));
        mCommentAdapter = new CommentAdapter();
        mCommentRecyclerView.setAdapter(mCommentAdapter);

        // 初始化界面加载管理器
        mLoadManager = LoadFactory.getInstance().register(mNestedScrollView, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                // 重新加载
                mLoadManager.showStateView(LoadingView.class);
            }
        });

        mTitleView.setText("理发店详情");

        onGetCollection(shopId);

        onGetBaberShop(shopId,SPUtil.getLatLng());

        onGetShopService(shopId, "10");

        onGetBarberList(shopId, "0");

        mLoadManager.showSuccessView();

    }



    /**
     * 获取商品是否收藏
     *
     * @param id
     */
    private void onGetCollection(String id) {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("id", id);
        AsyncHttpPostFormData.addFormData("type", "shop");
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
        AsyncHttpPostFormData.addFormData("type", "shop");
        okHttpClient.post("/v1/collection/add-collection", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onAddCollection " + response.toString());

                Type type = new TypeToken<RspModel<CollectionBack>>() {
                }.getType();
                CollectionBack list = getAnalysis(response, type, "onAddCollection");
                if (list != null) {
                    if ("1".equals(list.getIs_walk())) {
                        menuItem.setIcon(R.drawable.ic_menu_collect_pressed);
                    } else {
                        menuItem.setIcon(R.drawable.ic_menu_collect_normal);
                    }
                    ToastUtil.getInstance()
                            .setBackgroundColor(getResources().getColor(R.color.colorAccent))
                            .setTextColor(getResources().getColor(R.color.textColorLight))
                            .show(list.getMsg(), Toast.LENGTH_SHORT);
                }
            }
        });
    }

    /**
     * 获取商家信息
     *
     * @param id
     */
    private void onGetBaberShop(String id,LatLng latLng) {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("shop_id", id);
        AsyncHttpPostFormData.addFormData("lat", latLng.latitude);
        AsyncHttpPostFormData.addFormData("lng", latLng.longitude);
        okHttpClient.post("/v1/shop/shop-info", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:商家信息 " + response.toString());
                Type type = new TypeToken<RspModel<ShopB>>() {
                }.getType();
                ShopB list = getAnalysis(response, type, "商家信息");
                if (list != null) {
                    valueShow(list);
                }
            }
        });
    }

    /**
     * 获取店铺商品列表
     *
     * @param id
     */
    private void onGetShopService(String id, String number) {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("shop_id", id);
        AsyncHttpPostFormData.addFormData("number", number);
        okHttpClient.post("/v1/hairdresser/hairdresser-list", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onGetShopService " + response.toString());
                Type type = new TypeToken<RspModel<List<Hairdresser>>>() {
                }.getType();
                List<Hairdresser> list = getAnalysis(response, type, "onGetShopService");
                if (list != null) {
                    valueShowShopService(list);
                }
            }
        });
    }

    /**
     * 获取发型师列表
     *
     * @param id
     * @param number
     */
    private void onGetBarberList(String id, String number) {
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
                Log.i(TAG, "onSuccess:onGetBarberList " + response.toString());
                Type type = new TypeToken<RspModel<List<BarberInfo>>>() {
                }.getType();
                List<BarberInfo> list = getAnalysis(response, type, "onGetBarberList");
                if (list != null) {
                    valueShowBarberService(list);
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
                            return rspModel.getData();
                        default:
                            App.showMessage("错误码：" + rspModel.getCode());
                            mLoadManager.showStateView(EmptyView.class);
                            break;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
        }
        mLoadManager.showStateView(ErrorView.class);
        return null;
    }

    private MenuItem menuItem;

    /**
     * 创建菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_barbershop, menu);
        menuItem = menu.findItem(R.id.action_collect);
        return true;
    }


    /**
     * 监听菜单选项
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_collect) {
            onAddCollection(shopId);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示控件的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.subscribe:        //立即预约
                // 跳转到发型师列表界面
                BarberActivity.show(mContext, shopId);
                break;
            case R.id.phone:
                showCallPhoneDialogFragment();
                break;
            case R.id.other_barber:     // 其他发型师
                BarberActivity.show(mContext, shopId);
                break;
            case R.id.comment_layout:   // 查看所有评论
                CommentActivity.show(mContext, CommentActivity.TYPE_ENTIRE_COMMENT,shopId);
                break;
            default:
                break;
        }
    }


    /**
     * 显示拨打电话的dialog
     */
    private void showCallPhoneDialogFragment() {
        CallPhoneDialogFragment callPhoneDialogFragment = CallPhoneDialogFragment.newInstance(phone);
        if (!callPhoneDialogFragment.isAdded()) {
            callPhoneDialogFragment.show(getSupportFragmentManager(), callPhoneDialogFragment.getClass().getName());
        }
    }


}
